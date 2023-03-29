package com.example.bataillenavale_graphique;

import Utils.BateauType;
import Utils.FxmlType;
import Utils.GameUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.*;

/**
 * Classe qui permet de gérer le placement des bateaux
 *
 * @author Romain Veydarier
 * @since 15/03/2023
 */
public class BoatPlacement implements Initializable {

    /**
     * Grille du jeu pour la partie interface
     */
    @FXML
    private GridPane UIGrille;

    @FXML
    private Pane root;
    @FXML
    private Pane parentBateau;

    @FXML
    private ImageView porteAvion;
    @FXML
    private ImageView croiseur;
    @FXML
    private ImageView contreTorpilleurs;
    @FXML
    private ImageView sousMarin;
    @FXML
    private ImageView torpilleur;

    private final List<Bateau> bateaux = new ArrayList<>();
    private boolean isKeyEventInitialize = false;
    private ImageView selectedBoat = null;

    /**
     * Variable qui permet d'avoir la rotation du bateau que l'on pose
     * (1 => Horizontal | 2 => Vertical)
     */
    private int rotation = 2;

    @FXML
    public void clickGrid(MouseEvent event) {
        Node source = event.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        if(colIndex == null || rowIndex == null) return;

        System.out.println(UIGrille.getCellBounds(colIndex, rowIndex));

        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 9 ; i >= 0 ; i--) {
            for (int j = 9; j >= 0; j--) {
                addPane(i, j);
            }
        }
        instantiateBoat();
    }

    private void OnKeyPressed(KeyEvent event) {
        System.out.println("tzrzerzesdd");
    }

    private void addPane(int colIndex, int rowIndex) {
        Pane pane = new Pane();
        pane.setMinSize(60, 60);
        Random rand = new Random();

        int red = rand.nextInt(255);
        int green = rand.nextInt(255);
        int blue = rand.nextInt(255);

        pane.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue, 0), CornerRadii.EMPTY, Insets.EMPTY)));
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

        UIGrille.add(pane, colIndex, rowIndex);
    }
    private void OnBoatSelect(MouseEvent event) {
        // Récupération de l'image
        selectedBoat = (ImageView)event.getSource();


    }

    /**
     * Fonction qui permet de faire apparaitre les différents bateaux présents dans le jeu
     */
    private void instantiateBoat() {
        bateaux.add(new Bateau(BateauType.PorteAvion, 0, 0));
        bateaux.add(new Bateau(BateauType.Croiseur, 0, 0));
        bateaux.add(new Bateau(BateauType.ContreTorpilleurs, 0, 0));
        bateaux.add(new Bateau(BateauType.SousMarin, 0, 0));
        bateaux.add(new Bateau(BateauType.Torpilleur, 0, 0));

        for (int i = 0; i < bateaux.size(); i++) {
            Bateau b = bateaux.get(i);
            b.changeParent(parentBateau);

            b.getImage().setOnDragDetected(this::OnDragStart);
        }
    }

    private void OnDragStart(MouseEvent event) {

        System.out.println("OnBoatDragStart()");
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

    private void OnDragOver(DragEvent event, Pane pane) {
        //System.out.println("onDragOver");

        // Récupération de la source
        ImageView source = (ImageView)event.getGestureSource();

        Dragboard db = event.getDragboard();

        if (event.getGestureSource() != pane &&
                db.hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

            // Vérification que la

            //System.out.println(source.getFitHeight());

            //System.out.println("X : " + source.getLayoutX() + " | Y : " + source.getLayoutY());
            //System.out.println("X : " + event.getScreenX() + " | Y : " + event.getScreenY());

            if(rotation == 1) {
                // Horizontal

                double x = (pane.getWidth()/2)-(pane.getWidth()/2);
                double y = (pane.getHeight()/2)-(pane.getHeight()/2);

                //System.out.println("X : " + x + " || Y : " + y + " || width : " + pane.getWidth());

                source.setLayoutX(x);
                source.setLayoutY(y);


            } else {
                // Vertical


            }
        }


        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        event.consume();
    }

    private void OnDragExited(DragEvent event, Pane pane) {
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        event.consume();
    }

    private void OnDragDropped(DragEvent event, int l, int c) {
        System.out.println("onDragDropped");

        Dragboard db = event.getDragboard();

        // Récupération de l'image
        ImageView img = (ImageView)event.getGestureSource();

        img.setMouseTransparent(false);

        boolean success = false;
        if (db.hasString()) {
            int tailleBateau = Integer.parseInt(db.getString());

            // Vérification que le bateau rentre dans la case sélectionnée
            if(GameUtils.posOk(GameApplication.getInstance().getBataille().grilleJeu, l, c, rotation, tailleBateau)) {
                System.out.println("test");

                // On retire le bateau de son parent
                parentBateau.getChildren().remove(img);

                // Modification de la rotation
                root.getChildren().add(img);

                Bounds b = GetBounds(event);

                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());

                img.setLayoutX(event.getSceneX()/*b.getMinX() + (b.getMaxX() - b.getMinX())*/);
                img.setLayoutY(event.getSceneY()/*b.getMinY() + (b.getMaxY() - b.getMinY())*/);
                //img.

                img.setMouseTransparent(true);
            }

            success = true;
        }

        event.setDropCompleted(success);

        event.consume();
    }

    /**
     * Fonction pour récupérer les contours d'une cellule
     *
     * @param event Event lors du drag and drop
     * @return Retourne les contours de la cellule
     */
    private Bounds GetBounds(DragEvent event) {
        Node source = event.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        if(colIndex == null || rowIndex == null) return null;

        return UIGrille.getCellBounds(colIndex, rowIndex);
    }
}
