package com.example.bataillenavale_graphique;

import Utils.GameUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.*;

/**
 * Classe principale du jeu de bataille navale
 *
 * @since 08/02/2023
 * @author Romain Veydarier
 */
public class Bataille {

    /**
     * Grille qui contient la carte de l'ordinateur
     */
    public int[][] grilleOrdi = new int[10][10];
    private final int[][] grilleMouvementOrdi = new int[10][10];

    /**
     * Grille qui contient la carte du joueur
     */
    public int[][] grilleJeu = new int[10][10];

    public List<Bateau> leftBateau = new ArrayList<>();
    public List<Bateau> rightBateau = new ArrayList<>();

    /**
     * Variable aléatoire pour pouvoir générer des nombres aléatoires
     */
    public static Random rand = new Random();

    private GameSceneController gsc;
    public boolean yourTurn = false;

    /**
     * Fonction principale qui permet de lancer la bataille
     *
     * @since 07/02/2023
     */
    public Bataille() {
        // Initialisation des 2 grilles
        initGrilleOrdi();
        //initGrilleJeu();
        AfficherGrille(grilleJeu);
    }

    public void play(GameSceneController gsc) {
        this.gsc = gsc;

        // On dit que c'est le tour du joueur
        gsc.AddConsoleLine("A votre tour");
        yourTurn = true;
    }

    /**
     * Fonction pour faire jouer l'ordinateur
     */
    public void tourOrdinateur() {
        gsc.AddConsoleLine("Au tour de votre adversaire");
        yourTurn = false;

        int[] position = tirOrdinateur();
        mouvement(grilleJeu, position[0], position[1], false);

        if(vainqueur(grilleJeu)) {
            System.out.println("Victoire de l'ordinateur !");
            return;
        }

        gsc.AddConsoleLine("A votre tour");
        yourTurn = true;
    }

    /**
     * Fonction pour générer un nombre aléatoire par rapport à 2 bornes a et b
     *
     * @since 06/02/2023
     *
     * @param a Première borne inclus
     * @param b Deuxième borne exclu
     * @return Un nombre aléatoire entre a et b-1
     */
    public static int randRange(int a, int b) {
        return rand.nextInt(b-a) + a;
    }


    /**
     * Fonction pour ajouter un bateau à une grille
     *
     * @since 06/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param l Un numéro de ligne (compris entre 0 et 9)
     * @param c Un numéro de colonne (compris entre 0 et 9)
     * @param d Une direction (1 => Horizontal | 2 => Vertical)
     * @param t Nombre de cases que prend le bateau
     * @param idBateauGrille Identifiant du bateau à afficher sur la grille
     */
    public void ajouterBateau(int[][] grille, int l, int c, int d, int t, int idBateauGrille) {
        // Ajout du bateau sur le plateau de l'ordinateur
        if(d == 1) {
            for (int i = c; i > (c - t); i--) {
                grille[l][i] = idBateauGrille;
            }
        } else {
            for (int i = l; i < (l + t); i++) {
                grille[i][c] = idBateauGrille;
            }
        }
    }

    /**
     * Fonction pour réinitialiser une grille à une grille vide
     */
    public void resetGrille() {
        grilleJeu = new int[10][10];
    }

    /**
     * Fonction pour initialiser la grille de l'ordinateur avec la mise en place des 5 bateauxTaille sur sa grille
     *
     * @since 06/02/2023
     */
    private void initGrilleOrdi() {
        RdmInitGrid(grilleOrdi);
    }

    public void RdmInitGrid(int[][] grille) {
        // Numéro de ligne ( 0 - 9 )
        int l = randRange(0, 10);
        // Numéro de colonne ( 0 - 9 )
        int c = randRange(0, 10);
        // Numéro de direction
        int d = randRange(1, 3);

        int idBateau = 0;
        int t;

        while(idBateau < GameUtils.bateauxTaille.length) {
            t = GameUtils.bateauxTaille[idBateau];

            // Si on peut placer le bateau
            if(GameUtils.posOk(grille, l, c, d, t)) {
                ajouterBateau(grille, l, c, d, t, (idBateau+1));
                idBateau++;

            } else {
                l = randRange(0, 10);
                c = randRange(0, 10);
                d = randRange(1, 3);
            }
        }
    }

    /**
     * Fonction qui nous permet de savoir si un bateau est coulé
     *
     * @since 07/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param idBateau Identifiant d'un bateau
     *
     * @return Retourne vraie (true) si le bateau est coulé (qu'il n'est plus présent dans la grille)
     */
    public static boolean couler(int[][] grille, int idBateau) {
        if(idBateau < 1 || idBateau > 5) {
            System.out.println("Attention, la variable idBateau n'est pas comprise entre 1 et 5");
            return true;
        }

        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[y].length; x++) {
                if(grille[y][x] == idBateau) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction qui permet de tirer
     *
     * @since 07/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param l Un numéro de ligne
     * @param c Un numéro de colonne
     */
    public void mouvement(int[][] grille, int l, int c, boolean right) {
        Pane pane;

        System.out.println("Ligne " + l);
        System.out.println("Colonne " + c);

        System.out.println(c * gsc.getRightGrid().getColumnCount() + l);

        if(right)
            pane = (Pane)gsc.getRightGrid().getChildren().get(l * gsc.getRightGrid().getColumnCount() + c);
        else
            pane = (Pane)gsc.getLeftGrid().getChildren().get(c * gsc.getLeftGrid().getColumnCount() + l);

        Bounds bounds = pane.getBoundsInLocal();
        double centerX = bounds.getMinX() + bounds.getWidth() / 2.0;
        double centerY = bounds.getMinY() + bounds.getHeight() / 2.0;
        Point2D centerInScene = pane.localToScene(centerX, centerY);

        new Bullet(0, 0, (int)centerInScene.getX(), (int)centerInScene.getY(), gsc.getRoot());

        // Vérification que la position touche de l'eau ou un bateau déjà touché
        if(grille[l][c] <= 0 || grille[l][c] >= 6) {
            gsc.AddConsoleLine("[" + GameUtils.colonne[c] + " " + (l + 1) + "] À l’eau ");
            gsc.AddConsoleLine("");
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] À l’eau ");
            // Vérification qu'il n'y est déjà pas une autre image dessus
            if(grille[l][c] < 6) {
                File fileCercle = new File("src/main/resources/Images/cercle.png");

                Image cercleImg = new Image(fileCercle.toURI().toString(), 56, 56, false, false);
                ImageView cercleImgView = new ImageView(cercleImg);

                pane.getChildren().add(cercleImgView);
            }
            return;
        }

        // Récupération de l'id du bateau
        int idBateau = grille[l][c];
        grille[l][c] = 6;

        File fileCroix = new File("src/main/resources/Images/croix.png");

        Image croixImg = new Image(fileCroix.toURI().toString(), 56, 56, false, false);
        ImageView croixImgView = new ImageView(croixImg);

        pane.getChildren().add(croixImgView);

        // Si le bateau est coulé alors on affiche Coulé sinon Touché
        if(couler(grille, idBateau)) {
            gsc.AddConsoleLine("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Coulé, il s'agissait d'un " + GameUtils.bateauxNom[idBateau-1]);
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Coulé, il s'agissait d'un " + GameUtils.bateauxNom[idBateau-1]);
        } else {
            gsc.AddConsoleLine("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Touché, il s'agit d'un " + GameUtils.bateauxNom[idBateau-1]);
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Touché, il s'agit d'un " + GameUtils.bateauxNom[idBateau-1]);
        }

        gsc.AddConsoleLine("");
    }

    /**
     * Fonction qui génère une position aléatoire
     *
     * @since 07/02/2023
     *
     * @return Un tableau d'entier composé de 2 valeurs (0 => numéro de ligne | 1 => numéro de colonne)
     */
    public int[] tirOrdinateur() {
        int[] position;

        do {
            int l = randRange(0, 10);
            int c = randRange(0, 10);

            position = new int[]{l, c};
        } while(grilleMouvementOrdi[position[0]][position[1]] == 1);

        grilleMouvementOrdi[position[0]][position[1]] = 1;

        return position;
    }

    /**
     * Fonction qui nous permet de savoir s'il reste des bateaux sur la grille
     *
     * @since 07/02/2023
     *
     * @param grille Une grille de 10 x 10
     *
     * @return Retourne vraie (true) si tous les bateaux de la grille ont été coulés
     */
    public boolean vainqueur(int[][] grille) {
        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[y].length; x++) {
                if(grille[y][x] > 0 && grille[y][x] < 6) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction qui permet d'afficher une grille de jeu mis en paramètre
     *
     * @since 06/02/2023
     *
     * @param grille Une grille de 10 x 10
     */
    public void AfficherGrille(int[][] grille) {
        System.out.print("  ");
        // Affichage des lettres
        for (int i = 0; i < GameUtils.colonne.length; i++) {
            System.out.print(" " + GameUtils.colonne[i]);
        }

        System.out.println();
        for (int i = 0; i < grille.length; i++) {
            System.out.printf("%2d ", (i+1));
            for (int j = 0; j < grille[i].length; j++) {
                System.out.print(grille[i][j] + " ");
            }

            System.out.println();
        }

        System.out.println();
    }
}
