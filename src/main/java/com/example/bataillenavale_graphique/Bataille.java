package com.example.bataillenavale_graphique;

import Utils.BateauType;
import Utils.GameUtils;
import Utils.RotateType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
    /**
     * Grille de tous les mouvements que l'ordinateur à fait
     */
    private final int[][] grilleMouvementOrdi = new int[10][10];

    /**
     * Grille qui contient la carte du joueur
     */
    public int[][] grilleJeu = new int[10][10];

    /**
     * Liste des bateaux de la grille de gauche
     */
    public List<Bateau> leftBateau = new ArrayList<>();
    /**
     * Liste des bateaux de la grille de droite
     */
    public List<Bateau> rightBateau = new ArrayList<>();

    /**
     * Variable aléatoire pour pouvoir générer des nombres aléatoires
     */
    public static Random rand = new Random();

    /**
     * Variable pour faire le lien avec le controlleur de la scène
     *
     * @see GameSceneController
     */
    private GameSceneController gsc;
    /**
     * Variable qui permet de savoir si c'est le tour du joueur ou non
     */
    public boolean yourTurn = false;

    /**
     * Fonction pour initialiser la bataille et commencer à joueur
     * @param gsc Référence vers le controller de la scène de jeu
     */
    public void play(GameSceneController gsc) {
        this.gsc = gsc;

        // Génération de la grille de l'ia
        initGrilleOrdi();

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
            gsc.AddConsoleLine("Victoire de l'ordinateur !");
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
     * Fonction pour réinitialiser la classe bataille
     */
    public void reset() {
        grilleJeu = new int[10][10];
        grilleOrdi = new int[10][10];

        leftBateau = new ArrayList<>();
        rightBateau = new ArrayList<>();
    }

    /**
     * Fonction pour initialiser la grille de l'ordinateur avec la mise en place des 5 bateauxTaille sur sa grille
     *
     * @since 06/02/2023
     */
    private void initGrilleOrdi() {
        rightBateau.add(new Bateau(BateauType.PorteAvion));
        rightBateau.add(new Bateau(BateauType.Croiseur));
        rightBateau.add(new Bateau(BateauType.ContreTorpilleurs));
        rightBateau.add(new Bateau(BateauType.SousMarin));
        rightBateau.add(new Bateau(BateauType.Torpilleur));

        for (Bateau bateau : rightBateau) {
            bateau.changeParent(gsc.getRoot());
        }

        rdmInitGrid(grilleOrdi, rightBateau, gsc.getRoot(), gsc.getRoot(), gsc.getRightGrid(), true);

        gsc.Tricher(false);

        AfficherGrille(grilleOrdi);
    }

    /**
     * Fonction pour générer aléatoirement une nouvelle grille
     * @param grille La grille où l'on place les bateaux
     * @param bateaux La liste des bateaux à poser
     * @param parentBateau Le parent des bateaux
     * @param root Le nouveau parent des bateux
     * @param gridPane La grille UI
     * @param invertCL Inverse les colonnes et ligne
     */
    public void rdmInitGrid(int[][] grille, final List<Bateau> bateaux, Pane parentBateau, Pane root, GridPane gridPane, boolean invertCL) {
        // Numéro de ligne ( 0 - 9 )
        int l = randRange(0, 10);
        // Numéro de colonne ( 0 - 9 )
        int c = randRange(0, 10);
        // Numéro de direction
        int d = randRange(1, 3);

        int idBateau = 0;
        Bateau b;

        while(idBateau < bateaux.size()) {
            // Récupération du bateau que l'on veut placer
            b = bateaux.get(idBateau);

            // Si on peut placer le bateau
            if(GameUtils.posOk(grille, l, c, d, b.size())) {
                // Changement du parent du bateau pour pouvoir le placer librement dans la scène
                if(parentBateau != root) {
                    parentBateau.getChildren().remove(b.getImage());
                }

                // On indique au bateau qu'il est posé
                b.place(true);

                // Ajout du bateau dans la grille (BACKEND)
                ajouterBateau(grille, l, c, d, b.size(), b.id());

                RotateType rotation = d == 1 ? RotateType.VERTICAL : RotateType.HORIZONTAL;

                // Modification de la rotation
                b.changeRotate(rotation);

                // Changement de parent
                root.getChildren().add(b.getImage());

                // Ajout du bateau dans la grille (FRONTEND)
                if(invertCL) {
                    if (rotation == RotateType.VERTICAL)
                        b.placer(c, l - b.size() + 1, gridPane);
                    else
                        b.placer(c, l, gridPane);
                } else {
                    if (rotation == RotateType.VERTICAL)
                        b.placer(l, c - b.size() + 1, gridPane);
                    else
                        b.placer(l, c, gridPane);
                }

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
    public boolean couler(int[][] grille, int idBateau) {
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

                cercleImgView.toFront();

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
        croixImgView.toFront();

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
