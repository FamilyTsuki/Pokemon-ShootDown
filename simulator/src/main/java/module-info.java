module com.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.pokemon to javafx.graphics, javafx.fxml;
    exports com.pokemon;
}