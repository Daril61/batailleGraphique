package com.example.bataillenavale_graphique;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.net.URL;
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

    private static ImageView[] bateaux = new ImageView[5];

    private boolean dragStart = false;


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
    }

    @FXML
    private void OnBoatDragStart(MouseEvent event) {
        System.out.println("OnBoatDragStart()");
        System.out.println(event.getScreenX() + " - " + event.getScreenY());
        dragStart = true;
    }
    @FXML
    private void OnBoatDragStop() {
        System.out.println("OnBoatDragStop()");
        dragStart = false;
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

        UIGrille.add(pane, colIndex, rowIndex);
    }
}
