package Utils;

import java.io.File;

public enum BateauType {
    PorteAvion("src/main/resources/Images/PorteAvions.png", 5),
    Croiseur("src/main/resources/Images/Croiseur.png", 4),
    ContreTorpilleurs("src/main/resources/Images/ContreTorpilleurs.png", 3),
    SousMarin("src/main/resources/Images/SousMarin.png", 3),
    Torpilleur("src/main/resources/Images/Torpilleur.png", 2);

    private final File file;
    private final int tailleBateau;

    private BateauType(String url, int taille) {
        this.tailleBateau = taille;

        this.file = new File(url);
    }

    public String getImageURL() {
        return file.toURI().toString();
    }

    public int getTailleBateau() {
        return tailleBateau;
    }
}
