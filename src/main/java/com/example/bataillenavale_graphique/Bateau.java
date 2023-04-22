package com.example.bataillenavale_graphique;

import Utils.BateauType;
import Utils.GameUtils;
import Utils.RotateType;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Classe qui permet de créer un bateau
 *
 * @author Romain Veydarier
 * @since 22/03/2023
 */
public class Bateau {

    /**
     * Image actuelle du bateau
     */
    private ImageView img;
    /**
     * Ancienne image du bateau avant rotation
     */
    private ImageView lastImg;

    /**
     * Variable qui nous donne le type de bateau
     */
    private final BateauType type;

    /**
     * Fonction pour récupérer la taille du bateau
     * @return La taille du bateau
     */
    public int size() { return type.getTailleBateau(); }

    /**
     * Fonction qui retourne l'identifiant du bateau
     * @return L'identifiant du bateau
     */
    public int id() { return type.getIdBateau(); }

    /**
     * Variable qui nous indique si le bateau est placé
     */
    public boolean place = false;
    /**
     * Ligne sur laquelle le bateau est placé
     */
    private int ligneGrid;
    /**
     * Colonne sur laquelle le bateau est placé
     */
    private int colonneGrid;

    /**
     * Variable qui nous indique la rotation du bateau
     * 1 => Horizontal || 2 => Vertical
     *
     * @see RotateType
     */
    private RotateType rotate = RotateType.HORIZONTAL;

    /**
     * Constructeur de la classe bateau
     * @param type Type de bateau que l'on crée
     */
    public Bateau(BateauType type) {
        this.type = type;

        Image image = new Image(type.getImageURLHorizontal());
        System.out.println("URL : " + type.getImageURLHorizontal() + " | taille image : " + image.getWidth() + "x" + image.getHeight());
        img = new ImageView(image);

        img.setFitHeight(GameUtils.BOAT_SIZE_FOR_ONE_UNIT * this.type.getTailleBateau());
    }

    /**
     * Fonction qui permet de placer le bateau dans une grille
     * @param l Numéro de ligne de la grille
     * @param c Numéro de colonne de la grille
     * @param grille Grille sur laquelle on place le bateau
     */
    public void placer(int l, int c, GridPane grille) {
        Pane pane;

        if(l >= 0 && c >= 0) {
            ligneGrid = l;
            colonneGrid = c;
            pane = (Pane)grille.getChildren().get(ligneGrid * grille.getColumnCount() + colonneGrid);
        } else {
            pane = (Pane)grille.getChildren().get(colonneGrid * grille.getColumnCount() + ligneGrid);
        }

        Point2D panePosition = pane.localToScene(0, 0);
        Scene scene = pane.getScene();
        double paneX = panePosition.getX() + scene.getX() + (rotate == RotateType.VERTICAL ? type.getXVerticalOffset() : 0);
        double paneY = panePosition.getY() + scene.getY() - 20;

        img.setLayoutX(paneX);
        img.setLayoutY(paneY);
    }

    /**
     * Fonction qui permet de dire que l'on a placé le bateau
     * @param isPlace État du bateau
     */
    public void place(boolean isPlace) {
        place = isPlace;
    }

    /**
     * Fonction qui nous permet de savoir si on a placé le bateau
     * @return État du bateau
     */
    public boolean getPlace() { return place; }

    /**
     * Fonction qui permet de faire tourner le bateau
     * @param rotate Nouvelle rotation
     */
    public void changeRotate(RotateType rotate) {
        this.rotate = rotate;

        switch (rotate) {
            case HORIZONTAL:
            {
                lastImg = img;

                // Changement de l'image pour celle en horizontal
                Image image = new Image(type.getImageURLHorizontal());
                System.out.println("URL : " + type.getImageURLHorizontal() + " | taille image : " + image.getWidth() + "x" + image.getHeight());
                img = new ImageView(image);
            }
            break;
            case VERTICAL:
            {
                lastImg = img;
                // Changement de l'image pour celle en vertical
                Image image = new Image(type.getImageURLVertical());
                System.out.println("URL : " + type.getImageURLVertical() + " | taille image : " + image.getWidth() + "x" + image.getHeight());
                img = new ImageView(image);
            }
            break;
        }

        lastImg.setVisible(false);
        img.setVisible(true);
    }

    /**
     * Fonction qui permet de changer le parent du bateau
     * @param parent Nouveau parent
     */
    public void changeParent(Pane parent) {
        parent.getChildren().add(img);
    }

    /**
     * Fonction qui permet de récupérer l'image actuelle du bateau
     * @return L'image actuelle du bateau
     */
    public ImageView getImage() {
        return img;
    }

    /**
     * Fonction qui permet de récupérer l'ancienne image avant rotation du bateau
     * @return L'ancienne image avant rotation
     */
    public ImageView getLastImg() {
        return lastImg;
    }
}
