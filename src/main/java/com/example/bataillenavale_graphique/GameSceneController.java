package com.example.bataillenavale_graphique;

import Utils.FxmlType;
import Utils.GameUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe permettant de gérer la scène de jeu
 *
 * @author Romain Veydarier
 * @since 20/04/2023
 */
public class GameSceneController implements Initializable {

    /**
     * Variable qui contient la grille gauche
     */
    @FXML
    private GridPane leftGrid;
    /**
     * Fonction pour récupérer la grille de gauche
     * @return La grille gauche
     */
    public GridPane getLeftGrid() {
        return leftGrid;
    }

    /**
     * Variable qui contient la grille droite
     */
    @FXML
    private GridPane rightGrid;
    /**
     * Fonction pour récupérer la grille de droite
     * @return La grille droite
     */
    public GridPane getRightGrid() {
        return rightGrid;
    }

    /**
     * Variable qui contient l'élément le plus haut dans la hiérarchie (ROOT)
     */
    @FXML
    private Pane root;
    /**
     * Fonction pour récupérer la variable root
     * @return Une référence vers la variable root
     */
    public Pane getRoot() { return root; }

    /**
     * Variable qui contient la barre pour dérouler la console
     */
    @FXML
    private ScrollPane scrollPane;
    /**
     * Variable qui contient chaque ligne de texte pour les mettre en vertical
     */
    @FXML
    private VBox scrollPaneTextContainer;

    /**
     * Référence vers la classe Bataille
     *
     * @see Bataille
     */
    private Bataille bataille;

    /**
     * Variable pour savoir si la triche est activé ou non
     */
    private boolean hasTricheEnabled = false;

    /**
     * Fonction execute au démarrage de la scène
     *
     * @param url Variable URL donné par la fonction initialize
     * @param resourceBundle Variable resourceBundle donné par la fonction initialize
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
            b.placer(-1, -1, leftGrid);
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
            AddConsoleLine("Victoire du joueur !");
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

        for(Bateau b : bataille.rightBateau) {
            b.getImage().setVisible(hasTricheEnabled);
        }
    }
    /**
     * Fonction qui permet de forcer le changement de l'état de la triche
     */
    public void Tricher(boolean state) {
        hasTricheEnabled = state;

        for(Bateau b : bataille.rightBateau) {
            b.getImage().setVisible(hasTricheEnabled);
        }
    }

    /**
     * Fonction pour redémarrer la partie (Retour à la scène de placement de bateau)
     * @throws IOException S'il y a un problème lors du chargement d'une scène
     */
    @FXML
    private void Redemarrer() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();

        GameUtils.ChangeScene(stage, FxmlType.BoatPlacement, "Placement des bateaux");
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
