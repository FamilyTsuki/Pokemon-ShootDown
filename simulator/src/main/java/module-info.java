module com.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.base;
    


    opens com.pokemon to javafx.graphics, javafx.fxml;
    
    opens com.pokemon.controllers to javafx.fxml;

    exports com.pokemon;

    opens com.pokemon.models to javafx.base;
    exports com.pokemon.models;
    exports com.pokemon.effect;
    

    exports com.pokemon.controllers; 
}