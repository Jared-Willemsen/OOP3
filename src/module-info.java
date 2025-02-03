module oop3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.naming;
    requires java.desktop;

    opens project.controller to javafx.fxml;  // Allows FXML to access the controller package
    opens project.view to javafx.fxml;
    opens project.main to javafx.fxml;               // Exports the controller package
    exports project.model;
    exports project.main;// Exports the view package
}
