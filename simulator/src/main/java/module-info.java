module com.pokemon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.graphics;
    requires transitive javafx.base;

    opens com.pokemon to javafx.fxml, javafx.graphics;
    opens com.pokemon.controllers to javafx.fxml;
    opens com.pokemon.models to javafx.base;

    exports com.pokemon;
    exports com.pokemon.models;
    exports com.pokemon.effect;
    exports com.pokemon.controllers;
    exports com.pokemon.items.items;
    exports com.pokemon.items.UseableItems;
}