module com.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.pokemon to javafx.graphics, javafx.fxml;
    
    opens com.pokemon.controllers to javafx.fxml;

    exports com.pokemon;

    exports com.pokemon.controllers; 
}