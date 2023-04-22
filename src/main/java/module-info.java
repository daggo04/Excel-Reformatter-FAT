

module com.eyeshare.Dag.excelreformatter {
    requires java.base;
    requires java.logging;
    requires java.desktop;
    requires java.xml;

    requires org.apache.poi.ooxml;
    requires org.apache.commons.io;
    requires com.google.gson;

    exports com.eyeshare.Dag;
}