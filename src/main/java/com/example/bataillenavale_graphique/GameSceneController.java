package com.example.bataillenavale_graphique;

import Utils.GameUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
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
    public GridPane getRightGrid() {
        return rightGrid;
    }

    @FXML
    private Pane root;
    public Pane getRoot() { return root; }

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox scrollPaneTextContainer;

    private Bataille bataille;

    private boolean hasTricheEnabled = false;

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
                AddPane(leftGrid, i, j, false);
                AddPane(rightGrid, i, j, true);
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
                    if (bataille.grilleJeu[x][y] > 0) {
                        Pane pane = (Pane) leftGrid.getChildren().get(y * leftGrid.getColumnCount() + x);

                        pane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
            }
        }


        bataille.play(this);
    }

    /**
     * Fonction pour ajouter un pane à la grille du joueur
     * @param grille grille sur laquelle on doit ajouter le pane
     * @param colIndex Le numéro de colonne
     * @param rowIndex Le numéro de ligne
     * @param clickable Permet de savoir si on peut cliquer sur la cellule
     */
    private void AddPane(GridPane grille, int colIndex, int rowIndex, boolean clickable) {
        Pane pane = new Pane();
        pane.setMinSize(56, 56);

        pane.setBackground(new Background(GameUtils.waterBackground));
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

            pane.setOnMouseClicked(e -> OnSelectNode(e, grille));
        }

        GridPane.setConstraints(pane, colIndex, rowIndex);
        grille.add(pane, colIndex, rowIndex);
    }

    /**
     * Fonction appelée quand on clique sur une cellule de la grille adverse
     * @param e l'event de l'appuie de la souris
     * @param grille La grille sur laquelle le clique a été fait
     */
    @FXML
    private void OnSelectNode(MouseEvent e, GridPane grille) {
        // Si ce n'est pas le tour du joueur
        if(!bataille.yourTurn) return;

        Node source = e.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);

        // Si on ne peut pax récupérer une ligne ou une colonne par rapport au clique
        if(colIndex == null || rowIndex == null) return;

        System.out.println(grille.getCellBounds(colIndex, rowIndex));

        System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
        bataille.mouvement(bataille.grilleOrdi, colIndex, rowIndex, true);

        if(bataille.vainqueur(bataille.grilleOrdi)) {
            System.out.println("Victoire du joueur !");
            return;
        }

        bataille.tourOrdinateur();
    }

    /**
     * Fonction qui permet d'afficher les bateaux de l'adversaire
     */
    @FXML
    private void Tricher() {
        hasTricheEnabled = !hasTricheEnabled;

        for (int x = 0; x < bataille.grilleOrdi.length; x++) {
            for (int y = 0; y < bataille.grilleOrdi[x].length; y++) {
                if (bataille.grilleOrdi[x][y] > 0) {
                    Pane pane = (Pane) rightGrid.getChildren().get(x * rightGrid.getColumnCount() + y);

                    if(hasTricheEnabled)
                        pane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
                    else
                        pane.setBackground(new Background(GameUtils.waterBackground));
                }
            }
        }
    }

    /**
     * Fonction pour ajouter un message dans la console du jeu
     *
     * @param msg Message à faire apparaître
     */
    public void AddConsoleLine(String msg) {
        Text text = new Text(msg);

        scrollPaneTextContainer.getChildren().add(text);

        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
