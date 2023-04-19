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
        Profile profile = loadProfile(profileName);
        ArrayList<String> prettyOperations = new ArrayList<>();
    
        if (profile != null) {
            for (Operation<?> operation : profile.getOperations()) {
                String operationString = operation.getType().toString() + ": ";
                Map<String, ?> params = operation.getParameters();
                
                for (Map.Entry<String, ?> entry : params.entrySet()) {
                    operationString += entry.getKey() + " = " + entry.getValue().toString() + ", ";
                }
                
                // Remove the trailing comma and space
                operationString = operationString.substring(0, operationString.length() - 2);
                prettyOperations.add(operationString);
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