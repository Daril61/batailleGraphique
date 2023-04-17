package com.example.bataillenavale_graphique;

import Utils.FxmlType;
import Utils.GameType;
import Utils.GameUtils;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Classe principal de l'application
 *
 * @author Romain Veydarier
 * @since 16/03/2023
 */
public class GameApplication extends Application {

    private static GameApplication instance;

    public static GameApplication getInstance() {
        return instance;
    }

    private Bataille bataille = null;

    public Bataille getBataille() {
        return bataille;
    }

    /**
     * Fonction appelée au démarrage de l'application
     *
     * @param args Paramètre java
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Au démarrage de l'application graphique
     *
     * @param stage Fenêtre d'affichage de l'application
     */
    @Override
    public void start(Stage stage) throws IOException {
        instance = this;

        /*FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("lobby.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.setMaximized(true);

        stage.show();*/

        // Récupération de l'image d'eau
        File file = new File(GameUtils.waterURL);
        GameUtils.waterBackground = new BackgroundImage(new Image(file.toURI().toString(),32,32,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        GameUtils.ChangeScene(stage, FxmlType.Lobby, "Lobby");
    }

    /**
     * Fonction pour lancer une partie gérée par l'application
     *
     * @param type Mode que l'utilisateur a choisi
     */
    public void lancerPartie(Stage stage, GameType type) throws IOException {
        switch (type) {
            // Lancement du jeu en mode seul contre l'ordinateur
            case Soloplayer -> {
                bataille = new Bataille();

                GameUtils.ChangeScene(stage, FxmlType.BoatPlacement, "Placement des bateaux");
            }

            // Lancement du jeu en mode multijoueur contre un autre joueur
            case Multiplayer -> {

            }
        }
    }
}