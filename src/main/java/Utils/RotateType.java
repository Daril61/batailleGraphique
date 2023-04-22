package Utils;

/**
 * Enumeration des types de rotation disponible dans le jeu
 */
public enum RotateType {

    VERTICAL(1),
    HORIZONTAL(2);

    /**
     * Une rotation
     */
    private final int rotate;

    RotateType(int rotate) {
        this.rotate = rotate;
    }

    /**
     * Fonction pour récupérer la rotation
     * @return Une rotation
     */
    public int getRotate() {
        return rotate;
    }
}

