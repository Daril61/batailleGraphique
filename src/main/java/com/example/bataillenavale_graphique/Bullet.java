package com.example.bataillenavale_graphique;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bullet {

    private int startX;
    private int startY;

    private int endX;
    private int endY;

    private static String bulletImage = "../Images/bullet.png";

    public Bullet(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        // Création du missile
        Image image = new Image(bulletImage);
        ImageView imgView = new ImageView(image);

        imgView.setX(startX);
        imgView.setY(startY);

        startAnimation();
    }

    private void startAnimation() {

    }

    public static float Lerp(float v0, float v1, float t)
    {
        return (1-t)*v0 + t*v1;
    }
}
