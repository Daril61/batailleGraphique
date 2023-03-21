package com.example.bataillenavale_graphique;

import Utils.GameUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

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
    private Parent parentBateau;

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



    private final Map<ImageView, int[]> positionBateau = new HashMap<>();


    /**
     * Variable qui permet d'avoir la rotation du bateau que l'on pose
     * (1 => Horizontal | 2 => Vertical)
     */
    private int rotation = 1;

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
        for (int i = 0 ; i < 10 ; i++) {
            for (int j = 0; j < 10; j++) {
                addPane(i, j);
            }
        }

        positionBateau.put(porteAvion, new int[] {(int) porteAvion.getX(), (int) porteAvion.getY()});
        positionBateau.put(croiseur, new int[] {(int) croiseur.getX(), (int) croiseur.getY()});
        positionBateau.put(contreTorpilleurs, new int[] {(int) contreTorpilleurs.getX(), (int) contreTorpilleurs.getY()});
        positionBateau.put(sousMarin, new int[] {(int) sousMarin.getX(), (int) sousMarin.getY()});
        positionBateau.put(torpilleur, new int[] {(int) torpilleur.getX(), (int) torpilleur.getY()});
    }

    @FXML
    private void OnBoatStartDrag(MouseEvent event) {
        System.out.println("OnBoatDragStart()");
        System.out.println(event.getScreenX() + " - " + event.getScreenY());

        // Récupération de l'image
        ImageView img = (ImageView)event.getSource();

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

    private void addPane(int colIndex, int rowIndex) {
        Pane pane = new Pane();
        Random rand = new Random();

        int red = rand.nextInt(255);
        int green = rand.nextInt(255);
        int blue = rand.nextInt(255);

        pane.setBackground(new Background(new BackgroundFill(Color.rgb(red, green, blue), CornerRadii.EMPTY, Insets.EMPTY)));

        pane.setOnMouseEntered(e -> {
            pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        });

        pane.setOnMouseExited(e -> {
            pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        });


        // Event executé quand le joueur passe sa souris sur une case avec un bateau sélectionné
        pane.setOnDragOver(event -> {
            System.out.println("onDragOver");

            Dragboard db = event.getDragboard();

            if (event.getGestureSource() != pane &&
                    db.hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

                int tailleBateau = Integer.parseInt(db.getString());
                ImageView img = (ImageView)event.getGestureSource();

                System.out.println(img.getFitHeight());

                System.out.println("X : " + img.getLayoutX() + " | Y : " + img.getLayoutY());
                System.out.println("X : " + event.getScreenX() + " | Y : " + event.getScreenY());
                // Horizontal
                if(rotation == 1) {

                    double x = (pane.getWidth()/2)-(pane.getWidth()/2);
                    double y = (pane.getHeight()/2)-(pane.getHeight()/2);

                    System.out.println("X : " + x + " || Y : " + y);

                    img.setLayoutX(x);
                    img.setLayoutY(y);

                // Vertical
                } else {

                }
            }


            pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            event.consume();
        });

        pane.setOnDragExited(event -> {
            pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            event.consume();
        });


        // Event executé quand le joueur lâche son clique sur une case
        pane.setOnDragDropped(event -> {
            System.out.println("onDragDropped");

            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                int tailleBateau = Integer.parseInt(db.getString());

                // Vérification que le bateau rentre dans la case sélectionnée
                boolean t = GameUtils.posOk(GameApplication.getInstance().getBataille().grilleJeu, rowIndex, colIndex, rotation, tailleBateau);

                System.out.println("Result : " + t);

                success = true;
            }

            event.setDropCompleted(success);

            event.consume();
        });

        UIGrille.add(pane, colIndex, rowIndex);
    }
}
