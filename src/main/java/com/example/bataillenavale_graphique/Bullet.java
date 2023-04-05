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

public class Bullet {

    private int startX;
    private int startY;

    private int endX;
    private int endY;

    private ImageView img;
    private Pane parent;

    public Bullet(int startX, int startY, int endX, int endY, Pane pane) {
        this.parent = pane;
        this.startX = startX;
        this.startY = startY;
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
