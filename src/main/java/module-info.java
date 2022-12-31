module java1_2022_opa0023 {
    requires transitive javafx.controls;
    requires javafx.fxml;
	requires transitive java.desktop;
    requires java.sql;
    requires javafx.base;
    requires java.base;
    requires javafx.graphics;
    opens presentation_layer to javafx.fxml;
    exports presentation_layer;
    exports domain_layer;
    exports data_layer;
}