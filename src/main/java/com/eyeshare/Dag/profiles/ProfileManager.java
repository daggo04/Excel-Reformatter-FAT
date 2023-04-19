package com.eyeshare.Dag.profiles;

import com.eyeshare.Dag.utils.OperationDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfileManager {
    private static final String PROFILES_DIR = "src/main/resources/profiles";
    private List<String> profileNames;
    private Gson gson;

    public ProfileManager() {
        this.profileNames = new ArrayList<>();
        
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Operation.class, new OperationDeserializer());
        this.gson = gsonBuilder.create();
        
        loadProfileNames();
    }
    public List<String> getProfileNames() {
        return profileNames;
    }


    public Profile loadProfile(String name) {
        try {
            FileReader reader = new FileReader(getProfilePath(name));
            Type profileType = new TypeToken<Profile>() {}.getType();
            Profile profile = gson.fromJson(reader, profileType);
            reader.close();
            return profile;
        } catch (FileNotFoundException e) {
            System.out.println("Profile file not found: " + name);
            return null;
        } catch (IOException e) {
            System.out.println("Error reading profile file: " + e.getMessage());
            return null;
        }
    }

    public void addProfile(Profile profile) {
        profileNames.add(profile.getName());
        saveProfile(profile);
    }

    public void removeProfile(String name) {
        profileNames.remove(name);
        try {
            Files.delete(Paths.get(getProfilePath(name)));
        } catch (IOException e) {
            System.out.println("Error deleting profile file: " + e.getMessage());
        }
    }

    public void updateProfile(Profile profile) {
        saveProfile(profile);
    }


    public ArrayList<String> operationsAsPrettyString(String profileName) {
        ArrayList<String> prettyOperations = new ArrayList<>();
        Profile profile = loadProfile(profileName);
        if (profile != null) {
            prettyOperations.add("Profile Name: " + profile.getName());
            prettyOperations.add("Template Path: " + profile.getTemplatePath());
            prettyOperations.add("Naming Convention : " + profile.getNamingConvention());
            for (Operation<?> operation : profile.getOperations()) {
                Map<String, ?> params = operation.getParameters();
                StringBuilder main_sb = new StringBuilder();
                main_sb.append(operation.getType().toString());
                main_sb.append(" - ");
                main_sb.append("Source Sheet: " +params.get("srcSheet"));
                main_sb.append(" - ");
                main_sb.append("Destination Sheet: " +params.get("dstSheet"));
                prettyOperations.add(main_sb.toString());
                if (operation.getType() == OpType.COPY_SPLIT_ROW) {
                    @SuppressWarnings("unchecked")
                    Map<Double, Object> colMap = (Map<Double, Object>) params.get("colMap");
                    for (Map.Entry<Double, Object> entry : colMap.entrySet()) {
                        String col_str = ("    Col:" + entry.getKey().toString() + " copy to Col:" + entry.getValue().toString());
                        prettyOperations.add(col_str);
                    }
                }else{
                    String col_str = ("    Col:" + params.get("srcCol") + " copy to Col:" + params.get("dstCol"));
                    prettyOperations.add(col_str);
                }
            }
        }
        return prettyOperations;
    }
    

    private void loadProfileNames() {
        try {
            Files.createDirectories(Paths.get(PROFILES_DIR));
            profileNames = Files.list(Paths.get(PROFILES_DIR))
                    .map(path -> path.getFileName().toString().replace(".json", ""))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error reading profiles directory: " + e.getMessage());
        }
    }

    private void saveProfile(Profile profile) {
        try {
            FileWriter writer = new FileWriter(getProfilePath(profile.getName()));
            gson.toJson(profile, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving profile: " + e.getMessage());
        }
    }

    private String getProfilePath(String name) {
        return PROFILES_DIR + File.separator + name + ".json";
    }
}