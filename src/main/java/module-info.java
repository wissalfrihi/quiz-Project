module com.example.quiz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires itextpdf;

    opens com.example.quiz to javafx.fxml;
    exports com.example.quiz;
}