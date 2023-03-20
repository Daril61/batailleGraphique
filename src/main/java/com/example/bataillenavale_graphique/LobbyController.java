package com.example.bataillenavale_graphique;

import Utils.FxmlType;
import Utils.GameUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

        GameUtils.ChangeScene(stage, FxmlType.BoatPlacement, "Placement des bateaux");
    }

    /**
     * Fonction qui permet de lancer le jeu en mode multijoueur (Socket)
     * @param event Variable événement à l'appuie du bouton
     *
     * @since 15/03/2023
     */
    @FXML
    protected void MultiplayerButton(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

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
