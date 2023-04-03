package com.example.bataillenavale_graphique;

import Utils.BateauType;
import Utils.EventKeyPressed;
import Utils.GameUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import Utils.RotateType;

import java.net.URL;
import java.util.*;

/**
 * Classe qui permet de gérer le placement des bateaux
 *
 * @author Romain Veydarier
 * @since 15/03/2023
 */
public class BoatPlacement implements Initializable, EventKeyPressed {

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

    @FXML
    private Label rotationInfo;

    private final List<Bateau> bateaux = new ArrayList<>();
    private boolean isKeyEventInitialize = false;
    private ImageView selectedBoat = null;

    private RotateType rotation = RotateType.HORIZONTAL;

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
        GameApplication.getInstance().addListeners(this);

        for (int i = 9 ; i >= 0 ; i--) {
            for (int j = 9; j >= 0; j--) {
                addPane(i, j);
            }
        }
        instantiateBoat();
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

        GridPane.setConstraints(pane, colIndex, rowIndex);
        UIGrille.add(pane, colIndex, rowIndex);
    }

    @Override
    public void OnKeyPressed(String key) {
        System.out.println("test" + key);
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

            b.getImage().setOnDragDetected(this::OnDragDetected);
            b.getImage().setOnDragDone(this::OnDragDone);
        }
    }

    private void OnDragDetected(MouseEvent event) {
        System.out.println(event.getScreenX() + " - " + event.getScreenY());

        // Récupération de l'image
        ImageView img = (ImageView)event.getSource();
        // Permet de rendre l'image "Invisible pour la souris lors du placement"
        img.setMouseTransparent(true);

        img.setOnKeyPressed(this::OnKeyPressed);

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

        boolean success = false;
        if (db.hasString()) {
            int tailleBateau = Integer.parseInt(db.getString());
            Bataille bataille = GameApplication.getInstance().getBataille();

            // Vérification que le bateau rentre dans la case sélectionnée
            if(GameUtils.posOk(bataille.grilleJeu, l, c, rotation.getRotate(), tailleBateau)) {
                System.out.println("test");

                Bateau bateau = ImageToBateau(img);

                assert bateau != null;
                bateau.place(true);
                System.out.println(rotation.getRotate());
                bataille.ajouterBateau(bataille.grilleJeu, l, c, rotation.getRotate(), bateau.size(), bateau.id());
                bataille.AfficherGrille(bataille.grilleJeu);

                // Changement du parent du bateau pour pouvoir le placer librement dans la carte
                parentBateau.getChildren().remove(img);
                root.getChildren().add(img);

                /*Pane pane = (Pane)(UIGrille.getChildren().get(7 * l + 11 * c));
                System.out.println(c * 9 + l);
                pane.getChildren().add(img);*/

                // Modification de la rotation
                bateau.changeRotate(rotation);


                Bounds b = GetBounds(event);

                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());
                //bateau.placer(0, 0);
                bateau.placer((int)event.getSceneX(), (int)event.getSceneY());
            }

            success = true;
        }

        event.setDropCompleted(success);

        event.consume();
    }

    private void OnDragDone(DragEvent event) {
        // Récupération de l'image
        ImageView img = (ImageView)event.getGestureSource();
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

    private void OnKeyPressed(KeyEvent event) {
        System.out.println("testzadzadzadazdzadza");

        if (event.getCode() == KeyCode.R) {
            System.out.println("RRRRRR");
            //rotation = rotation == 2 ? 1 : 2;
        }
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

    @FXML
    private void DoRotation() {
        rotation = (rotation.getRotate() + 1) > 2 ? RotateType.VERTICAL : RotateType.HORIZONTAL;

        // Changement du texte de la rotation
        switch (rotation) {
            case HORIZONTAL -> rotationInfo.setText("Horizontal");
            case VERTICAL -> rotationInfo.setText("Vertical");
        }
    }

    private Bateau ImageToBateau(ImageView img) {
        for(Bateau bateau : bateaux) {
            if(img == bateau.getImage())
            {
                return bateau;
            }
        }

        return null;
    }
}
