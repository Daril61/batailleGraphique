package com.example.bataillenavale_graphique;

import Utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Classe qui permet de gérer le placement des bateaux
 *
 * @author Romain Veydarier
 * @since 15/03/2023
 */
public class BoatPlacement implements Initializable {

    /**
     * Grille où le joueur doit placer les bateaux
     */
    @FXML
    private GridPane UIGrille;

    /**
     * Variable qui contient l'élément le plus haut dans la hiérarchie (ROOT)
     */
    @FXML
    private Pane root;
    /**
     * Variable qui contient le parent des bateaux qui ne sont pas placé
     */
    @FXML
    private Pane parentBateau;

    /**
     * Variable qui contient le bouton pour réinitialiser les bateaux
     */
    @FXML
    private Button restartButton;
    /**
     * Variable qui contient le bouton pour commencer la partie
     */
    @FXML
    private Button readyButton;

    /**
     * Variable qui contient le texte pour indiquer au joueur l'état de la rotation
     */
    @FXML
    private Label rotationInfo;

    /**
     * Variable qui donne l'état de la rotation
     */
    private RotateType rotation = RotateType.HORIZONTAL;
    /**
     * Variable qui indique le nombre de bateaux placés
     */
    private int nbPlacedBoat = 0;

    /**
     * Référence vers la classe Bataille
     *
     * @see Bataille
     */
    private Bataille bataille;

    /**
     * Fonction execute au démarrage de la scène
     *
     * @param url Variable URL donné par la fonction initialize
     * @param resourceBundle Variable resourceBundle donné par la fonction initialize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readyButton.setVisible(false);
        restartButton.setVisible(false);

        for (int y = 0 ; y < 10 ; y++) {
            for (int x = 0; x < 10; x++) {
                addPane(x, y);
            }
        }

        // Récupération de la bataille
        bataille = GameApplication.getInstance().getBataille();
        bataille.reset();

        Platform.runLater(this::instantiateBoat);
    }

    /**
     * Fonction qui permet d'ajouter un pane à la grille
     * @param colIndex Numéro de colonne
     * @param rowIndex Numéro de ligne
     */
    private void addPane(int colIndex, int rowIndex) {
        Pane pane = new Pane();
        pane.setMinSize(60, 60);

        // Ajout du background d'eau
        pane.setBackground(new Background(GameUtils.waterBackground));
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        pane.setOnMouseEntered(e -> {
            pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        });

        pane.setOnMouseExited(e -> {
            pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        });

        pane.setOnDragExited(dragEvent -> OnDragExited(dragEvent, pane));

        // Event execute quand le joueur passe sa souris sur une case avec un bateau sélectionné
        pane.setOnDragOver(dragEvent -> OnDragOver(dragEvent, pane));

        // Event execute quand le joueur lâche son clique sur une case
        pane.setOnDragDropped(dragEvent -> OnDragDropped(dragEvent, rowIndex, colIndex));

        GridPane.setConstraints(pane, colIndex, rowIndex);
        UIGrille.add(pane, colIndex, rowIndex);
    }

    /**
     * Fonction qui permet de faire apparaitre les différents bateaux présents dans le jeu
     */
    private void instantiateBoat() {
        bataille.leftBateau.add(new Bateau(BateauType.PorteAvion));
        bataille.leftBateau.add(new Bateau(BateauType.Croiseur));
        bataille.leftBateau.add(new Bateau(BateauType.ContreTorpilleurs));
        bataille.leftBateau.add(new Bateau(BateauType.SousMarin));
        bataille.leftBateau.add(new Bateau(BateauType.Torpilleur));

        for (int i = 0; i < bataille.leftBateau.size(); i++) {
            Bateau bateau = bataille.leftBateau.get(i);
            bateau.changeParent(parentBateau);

            bateau.getImage().setOnDragDetected(this::OnDragDetected);
            bateau.getImage().setOnDragDone(this::OnDragDone);
        }
    }

    /**
     * Fonction exécutée quand on commence un drag and drop
     * @param event Evenement de drag and drop
     */
    private void OnDragDetected(MouseEvent event) {
        System.out.println(event.getScreenX() + " - " + event.getScreenY());

        // Récupération de l'image
        ImageView img = (ImageView)event.getSource();
        // Permet de rendre l'image "Invisible pour la souris lors du placement"
        img.setMouseTransparent(true);

        // Création du drag and drop
        Dragboard db = img.startDragAndDrop(TransferMode.ANY);

        // On donne la taille du bateau
        ClipboardContent content = new ClipboardContent();

        // Transformation de la taille du bateau sélectionné en un string
        String taille = String.valueOf((int)(img.getFitHeight()/40));

        content.putString(taille);
        db.setContent(content);

        event.consume();
    }

    /**
     * Fonction exécutée quand on passe par-dessus une case de la grille de drag and drop
     * @param event Evenement de drag and drop
     */
    private void OnDragOver(DragEvent event, Pane pane) {
        Dragboard db = event.getDragboard();

        if (event.getGestureSource() != pane &&
                db.hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        //System.out.println("onDragOver");
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        event.consume();
    }

    /**
     * Fonction exécutée quand on sort de la grille de drag and drop
     * @param event Evenement de drag and drop
     */
    private void OnDragExited(DragEvent event, Pane pane) {
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        event.consume();
    }

    /**
     * Fonction exécutée quand on termine le drag and drop sur l'objet ou l'on a posé l'objet
     * @param event Evenement de drag and drop
     */
    private void OnDragDropped(DragEvent event, int l, int c) {
        // Récupération de l'évent de drag and drop
        Dragboard db = event.getDragboard();

        // Récupération de l'image
        ImageView img = (ImageView)event.getGestureSource();

        boolean success = false;
        // Si il y a un texte à l'intérieur
        if (db.hasString()) {
            // On récupère la taille du bateau à l'aide du texte à l'intérieur de l'évent de drag and drop
            int tailleBateau = Integer.parseInt(db.getString());
            // Récupération de l'instance de la classe bataille
            Bataille bataille = GameApplication.getInstance().getBataille();

            // Vérification que le bateau rentre dans la case sélectionnée
            if(GameUtils.posOk(bataille.grilleJeu, l, c, rotation.getRotate(), tailleBateau)) {
                // Récupération du bateau posé avec l'image que le joueur a sélectionné
                Bateau bateau = ImageToBateau(img);

                // Si il n'y a pas de bateau
                assert bateau != null;

                // On indique au bateau qu'il est posé
                bateau.place(true);

                // Ajout du bateau dans la grille (BACKEND)
                bataille.ajouterBateau(bataille.grilleJeu, l, c, rotation.getRotate(), bateau.size(), bateau.id());

                // Modification de la rotation
                bateau.changeRotate(rotation);

                parentBateau.getChildren().remove(img);
                root.getChildren().add(bateau.getImage());

                // Ajout du bateau dans la grille (FRONTEND)
                if(rotation == RotateType.VERTICAL)
                    bateau.placer(l, c-bateau.size()+1, UIGrille);
                else
                    bateau.placer(l, c, UIGrille);

                nbPlacedBoat++;
                CheckAllBoatsPlace();
            }

            success = true;
        }

        event.setDropCompleted(success);

        event.consume();
    }

    /**
     * Fonction exécutée quand on termine le drag and drop sur l'objet que l'on a drag and drop
     * @param event Evenement de drag and drop
     */
    private void OnDragDone(DragEvent event) {
        // Récupération de l'image
        ImageView img = (ImageView)event.getGestureSource();
        img.toFront();
        Bateau bateau = ImageToBateau(img);
        assert bateau != null;

        if (bateau.getPlace()) {
            // On rend l'image impossible à cliquer, car elle est placée sur la grille
            img.setMouseTransparent(true);
        } else {
            // On rend l'image possible d'être cliqué, car elle n'est pas placée
            img.setMouseTransparent(false);
        }

        img.setOnKeyPressed(null);

        event.consume();
    }

    /**
     * Fonction pour vérifier si tous les bateaux sont placés
     */
    private void CheckAllBoatsPlace() {
        if(nbPlacedBoat >= bataille.leftBateau.size()) {
            readyButton.setVisible(true);
        }

        if(nbPlacedBoat > 0) {
            restartButton.setVisible(true);
        }
    }

    /**
     * Fonction liée au bouton prêt présent sur la scène pour quand le joueur est prêt
     * @param event Evenement de clique sur le bouton
     */
    @FXML
    private void OnReadyButton(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        GameUtils.ChangeScene(stage, FxmlType.GameScene, "Scène de jeu");
    }

    /**
     * Fonction liée au bouton de redémarrage de partie présent sur la scène
     * @param event Evenement de clique sur le bouton
     */
    @FXML
    private void OnRestartButton(ActionEvent event) throws IOException {
        // On supprime les bateaux
        bataille.leftBateau.clear();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        GameUtils.ChangeScene(stage, FxmlType.BoatPlacement, "Placement des bateaux");
    }

    /**
     * Fonction liée au bouton d'aléatoire présent sur la scène
     * @param event Evenement de clique sur le bouton
     */
    @FXML
    private void OnRandomButton(ActionEvent event) {
        bataille.resetGrille();
        bataille.rdmInitGrid(bataille.grilleJeu, bataille.leftBateau, parentBateau, root, UIGrille, false);

        nbPlacedBoat = bataille.leftBateau.size();

        CheckAllBoatsPlace();
    }

    /**
     * Fonction pour changer la rotation pour les prochains bateaux à poser
     */
    @FXML
    private void DoRotation() {
        rotation = (rotation.getRotate() + 1) > 2 ? RotateType.VERTICAL : RotateType.HORIZONTAL;

        // Changement du texte de la rotation
        switch (rotation) {
            case HORIZONTAL -> rotationInfo.setText("Horizontal");
            case VERTICAL -> rotationInfo.setText("Vertical");
        }
    }

    /**
     * Fonction pour transformer une image que l'on sélectionne durant le drag and drop en bateau
     * @param img Une image de bateau
     * @return Une variable de type Bateau
     *
     * @see Bateau
     */
    private Bateau ImageToBateau(ImageView img) {
        for(Bateau bateau : bataille.leftBateau) {
            if(img == bateau.getImage() || img == bateau.getLastImg())
            {
                return bateau;
            }
        }

        return null;
    }

    /**
     * Fonction pour quitter, (Retour au menu principal)
     * @throws IOException S'il y a un problème lors du chargement d'une scène
     */
    @FXML
    private void Quitter() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();

        GameUtils.ChangeScene(stage, FxmlType.Lobby, "Menu Principal");
    }
}
