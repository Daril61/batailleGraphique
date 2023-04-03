package com.example.bataillenavale_graphique;

import Utils.BateauType;
import Utils.GameUtils;
import Utils.RotateType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Classe qui permet de créer un bateau
 *
 * @author Romain Veydarier
 * @since 22/03/2023
 */
public class Bateau {

    private final ImageView img;

    public int x() { return (int)img.getX(); }
    public int y() { return (int)img.getY(); }

    private final BateauType type;
    public int size() { return type.getTailleBateau(); }
    public int id() { return type.getIdBateau(); }

    public boolean place = false;

    // 1 => Horizontal || 2 => Vertical
    private RotateType rotate = RotateType.HORIZONTAL;

    public Bateau(BateauType type, int x, int y) {
        this.type = type;

        Image image = new Image(type.getImageURL());
        System.out.println("URL : " + type.getImageURL() + " | taille image : " + image.getWidth() + "x" + image.getHeight());
        img = new ImageView(image);

        img.setFitHeight(GameUtils.BOAT_SIZE_FOR_ONE_UNIT * this.type.getTailleBateau());

        placer(x, y);
    }

    public void placer(int x, int y) {
        img.setLayoutX(x);
        img.setLayoutY(y);
    }
    public void place(boolean isPlace) {
        place = isPlace;
    }
    public boolean getPlace() { return place; }

    public void changeRotate(RotateType rotate) {
        this.rotate = rotate;

        switch (rotate) {
            case HORIZONTAL:
            {
                img.setRotate(0);
            }
            break;
            case VERTICAL:
            {
                img.setRotate(90);
            }
            break;
        }
    }

    public void changeParent(Pane parent) {
        parent.getChildren().add(img);
    }

    public ImageView getImage() {
        return img;
    }
}
