package Utils;

import java.io.File;

/**
 * Enumeration des types de bateau disponible dans le jeu
 */
public enum BateauType {
    PorteAvion("src/main/resources/Images/Croiseur.png", "src/main/resources/Images/Croiseur_VERTICAL.png", 5, (short)1, 25),
    Croiseur("src/main/resources/Images/PorteAvions.png", "src/main/resources/Images/PorteAvions_VERTICAL.png", 4, (short)2, 10),
    ContreTorpilleurs("src/main/resources/Images/ContreTorpilleurs.png", "src/main/resources/Images/ContreTorpilleurs_VERTICAL.png", 3, (short)3, 0),
    SousMarin("src/main/resources/Images/SousMarin.png", "src/main/resources/Images/SousMarin_VERTICAL.png", 3, (short)4, 0),
    Torpilleur("src/main/resources/Images/Torpilleur.png", "src/main/resources/Images/Torpilleur_VERTICAL.png", 2, (short)5, 0);

    /**
     * Fichier vers l'image horizontale du bateau
     */
    private final File fileHorizontal;
    /**
     * Fichier vers l'image verticale du bateau
     */
    private final File fileVertical;
    /**
     * Taille du bateau
     */
    private final int tailleBateau;
    /**
     * Identifiant du bateau
     */
    private final short id;
    /**
     * Offset du bateau sur l'axe de x quand il est posé à la vertical
     */
    private final int xVerticalOffset;

     BateauType(String urlHorizontal, String urlVertical, int taille, short id, int xVerticalOffset) {
        this.tailleBateau = taille;

        this.fileHorizontal = new File(urlHorizontal);
        this.fileVertical = new File(urlVertical);

        this.id = id;
        this.xVerticalOffset = xVerticalOffset;
    }

    /**
     * Fonction pour récupérer le lien de l'image horizontale
     * @return Lien de l'image horizontale
     */
    public String getImageURLHorizontal() {
        return fileHorizontal.toURI().toString();
    }
    /**
     * Fonction pour récupérer le lien de l'image verticale
     * @return Lien de l'image verticale
     */
    public String getImageURLVertical() {
        return fileVertical.toURI().toString();
    }

    /**
     * Fonction pour récupérer la taille du bateau
     * @return La taille du bateau
     */
    public int getTailleBateau() {
        return tailleBateau;
    }

    /**
     * Fonction pour récupérer l'identifiant du bateau
     * @return L'identifiant du bateau
     */
    public int getIdBateau() {
        return id;
    }

    /**
     * Fonction pour récupérer l'offset sur l'axe des x quand l'image est verticale
     * @return L'offset sur x
     */
    public int getXVerticalOffset() {
        return xVerticalOffset;
    }
}
