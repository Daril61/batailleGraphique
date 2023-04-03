package Utils;

import java.io.File;

public enum BateauType {
    PorteAvion("src/main/resources/Images/PorteAvions.png", 5, (short)1),
    Croiseur("src/main/resources/Images/Croiseur.png", 4, (short)2),
    ContreTorpilleurs("src/main/resources/Images/ContreTorpilleurs.png", 3, (short)3),
    SousMarin("src/main/resources/Images/SousMarin.png", 3, (short)4),
    Torpilleur("src/main/resources/Images/Torpilleur.png", 2, (short)5);

    private final File file;
    private final int tailleBateau;
    private final short id;

    private BateauType(String url, int taille, short id) {
        this.tailleBateau = taille;

        this.file = new File(url);

        this.id = id;
    }

    public String getImageURL() {
        return file.toURI().toString();
    }

    public int getTailleBateau() {
        return tailleBateau;
    }
    public int getIdBateau() {
        return id;
    }
}
