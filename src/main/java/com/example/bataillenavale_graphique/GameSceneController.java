package com.example.bataillenavale_graphique;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable {

    @FXML
    private GridPane leftGrid;

    public GridPane getLeftGrid() {
        return leftGrid;
    }

    @FXML
    private GridPane rightGrid;

    @FXML
    private Pane root;
    public Pane getRoot() { return root; }
    private Bataille bataille;

    /**
     * Fonction execute au démarrage de la scène
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisation des panes pour les grilles
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                addPane(leftGrid, i, j, false);
                addPane(rightGrid, i, j, true);
            }
        }
        bataille = GameApplication.getInstance().getBataille();

        Platform.runLater(this::InitialisationBateauGrille);
    }

    /**
     * Fonction qui permet de faire apparaitre les différents bateaux présents dans le jeu
     */
    private void InitialisationBateauGrille() {
        for (int i = 0; i < bataille.leftBateau.size(); i++) {
            Bateau b = bataille.leftBateau.get(i);
            b.changeParent(root);

            for (int x = 0; x < bataille.grilleJeu.length; x++) {
                for (int y = 0; y < bataille.grilleJeu[x].length; y++) {
                    if(bataille.grilleJeu[x][y] > 0) {
                        Pane pane = (Pane) leftGrid.getChildren().get(y * leftGrid.getColumnCount() + x);
                        pane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            }
        }

        bataille.play(this);
    }

    private void addPane(GridPane grille, int colIndex, int rowIndex, boolean clickable) {
        Pane pane = new Pane();
        pane.setMinSize(56, 56);

        pane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        if(clickable) {
            pane.setOnMouseEntered(e -> {
                pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            });

            pane.setOnMouseExited(e -> {
                pane.setBorder(new Border(new BorderStroke(Color.BLACK,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            });

            pane.setOnMouseClicked(e -> OnMouseClickCase(e, grille));
        }

        GridPane.setConstraints(pane, colIndex, rowIndex);
        grille.add(pane, colIndex, rowIndex);
    }

    // Fonction quand on clique sur un pane
    private void OnMouseClickCase(MouseEvent e, GridPane grille) {
        Node source = e.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        if(colIndex == null || rowIndex == null) return;

        System.out.println(grille.getCellBounds(colIndex, rowIndex));

        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
    }
}
