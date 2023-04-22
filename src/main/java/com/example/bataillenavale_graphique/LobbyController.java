package com.example.bataillenavale_graphique;

import Utils.FxmlType;
import Utils.GameType;
import Utils.GameUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe qui permet de gérer l'interface du lobby au lancement du jeu
 *
 * @author Romain Veydarier
 * @since 15/03/2023
 */
public class LobbyController {

    /**
     * Fonction qui permet de lancer le jeu en mode solo
     * * @param event Variable événement à l'appuie du bouton
     *
     * @since 15/03/2023
     */
    @FXML
    protected void SoloButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        GameApplication.getInstance().lancerPartie(stage, GameType.Soloplayer);
    }

    /**
     * Fonction qui permet de lancer le jeu en mode multijoueur (Socket) NON FONCTIONNELLE
     * @param event Variable événement à l'appuie du bouton
     *
     * @since 15/03/2023
     */
    @FXML
    protected void MultiplayerButton(ActionEvent event) {
        Button button = (Button)event.getSource();
        Stage stage = (Stage)button.getScene().getWindow();

        button.setDisable(true);
    }

    /**
     * Fonction qui permet de quitter l'application
     *
     * @since 15/03/2023
     */
    @FXML
    protected void QuitButton() {
        // Permet d'arrêter l'application
        Platform.exit();
    }
}
