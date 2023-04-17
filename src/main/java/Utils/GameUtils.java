package Utils;

import com.example.bataillenavale_graphique.GameApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Classe qui contient les variables et fonctions utiles au jeu
 *
 * @since 15/03/2023
 * @author Romain Veydarier
 */
public class GameUtils {

    public final static int BOAT_SIZE_FOR_ONE_UNIT = 40;

    /**
     * Liste des tailles des bateaux dans l'ordre par rapport à la variable bateauxNom
     */
    public static final int[] bateauxTaille = new int[]{
            5,
            4,
            3,
            3,
            2
    };

    /**
     * Liste des noms des bateaux
     */
    public static final String[] bateauxNom = new String[] {
            "Porte-avions",
            "Croiseur",
            "Contre-torpilleurs",
            "Sous-marin",
            "Torpilleur"
    };

    public final static String waterURL = "src/main/resources/Images/water.png";
    public static BackgroundImage waterBackground;

    /**
     * Liste des colonnes, c'est-à-dire les lettres de la grille
     */
    public static final char[] colonne = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    /**
     * Fonction pour changer de scène
     *
     * @param stage Gestionnaire de l'application JavaFX
     * @param fxml Type de la nouvelle fenêtre
     * @param windowName Nom de la nouvelle fenêtre
     * @throws IOException Envoyé en cas d'impossibilité de récupérer le fxml mis en paramètre
     *
     * @since 15/03/2023
     */
    public static void ChangeScene(Stage stage, FxmlType fxml, String windowName) throws IOException {
        // Récupération du fxml
        Parent root = FXMLLoader.load(
            Objects.requireNonNull(
                GameApplication.class.getResource(fxml.getFxmlName())
            )
        );

        // Création de la nouvelle scène
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle(windowName);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        //stage.setResizable(false);

        //stage.setMaximized(true);
        stage.show();
    }

    /**
     * Fonction pour vérifier que l'on puisse placer un bateau par rapport à plusieurs paramètres
     *
     * @since 06/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param l Un numéro de ligne (compris entre 0 et 9)
     * @param c Un numéro de colonne (compris entre 0 et 9)
     * @param d Une direction (1 => Horizontal | 2 => Vertical)
     * @param t Nombre de cases que prend le bateau
     *
     * @return Retourne vraie (true) si on peut mettre le bateau sur les cases correspondantes
     */
    public static boolean posOk(int[][] grille, int l, int c, int d, int t) {

        // Cas horizontal
        if (d == 1) {

            // Vérification que le bateau puisse rentrer
            if (c - t < -1) {
                return false;
            }

            // Vérification qu'il n'y ait aucun bateau sur les cases analysées
            for (int i = c; i > (c - t); i--) {
                if(grille[l][i] != 0) {
                    return false;
                }
            }

            // Cas vertical
        } else {
            // Vérification que le bateau puisse rentrer
            if(l + t > 10) {
                return false;
            }

            // Vérification qu'il n'y ait aucun bateau sur les cases analysées
            for (int i = l; i < (l + t); i++) {
                if(grille[i][c] != 0) {
                    return false;
                }
            }
        }

        return true;
    }
}
