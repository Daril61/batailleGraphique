package com.example.bataillenavale_graphique;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;

/**
 * Classe qui nous permet d'avoir des boulets dans le jeu
 *
 * @author Romain Veydarier
 * @since 18/04/2023
 */
public class Bullet {

    /**
     * Position X vers laquelle le boulet doit aller
     */
    private final int endX;
    /**
     * Position Y vers laquelle le boulet doit aller
     */
    private final int endY;

    /**
     * Image du boulet
     */
    private final ImageView img;
    /**
     * Parent du boulet
     */
    private final Pane parent;

    /**
     * Constructeur de la classe
     * @param startX Position X de départ
     * @param startY Position Y de départ
     * @param endX Position X d'arrivée
     * @param endY Position Y d'arrivée
     * @param pane Parent du boulet
     */
    public Bullet(int startX, int startY, int endX, int endY, Pane pane) {
        this.parent = pane;

        this.endX = endX;
        this.endY = endY;

        // Création du missile
        String bulletImage = "src/main/resources/Images/bullet.png";
        File file = new File(bulletImage);
        Image image = new Image(file.toURI().toString());
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
        img = new ImageView(image);

        parent.getChildren().add(img);

        img.setLayoutX(startX);
        img.setLayoutY(startY);
        img.toFront();

        startAnimation();
    }

    /**
     * Fonction qui s'occupe de faire l'animation du boulet
     */
    private void startAnimation() {

        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(img.layoutXProperty(), endX);
        KeyValue keyValueY = new KeyValue(img.layoutYProperty(), endY);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), keyValueX, keyValueY);
        timeline.getKeyFrames().add(keyFrame);

        EventHandler<ActionEvent> onFinished = event -> parent.getChildren().remove(img);
        timeline.setOnFinished(onFinished);

        timeline.play();
    }
}
