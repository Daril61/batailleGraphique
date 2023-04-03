package Utils;

import java.io.File;

public enum BateauType {
    PorteAvion("src/main/resources/Images/PorteAvions.png", "src/main/resources/Images/PorteAvions_VERTICAL.png", 5, (short)1),
    Croiseur("src/main/resources/Images/Croiseur.png", "src/main/resources/Images/Croiseur_VERTICAL.png", 4, (short)2),
    ContreTorpilleurs("src/main/resources/Images/ContreTorpilleurs.png", "src/main/resources/Images/ContreTorpilleurs_VERTICAL.png", 3, (short)3),
    SousMarin("src/main/resources/Images/SousMarin.png", "src/main/resources/Images/SousMarin_VERTICAL.png", 3, (short)4),
    Torpilleur("src/main/resources/Images/Torpilleur.png", "src/main/resources/Images/Torpilleur_VERTICAL.png", 2, (short)5);

    private final File fileHorizontal;
    private final File fileVertical;
    private final int tailleBateau;
    private final short id;

    private BateauType(String urlHorizontal, String urlVertical, int taille, short id) {
        this.tailleBateau = taille;

        this.fileHorizontal = new File(urlHorizontal);
        this.fileVertical = new File(urlVertical);

        this.id = id;
    }

    public String getImageURLHorizontal() {
        return fileHorizontal.toURI().toString();
    }
    public String getImageURLVertical() {
        return fileVertical.toURI().toString();
    }

    public int getTailleBateau() {
        return tailleBateau;
    }
    public int getIdBateau() {
        return id;
    }
}
