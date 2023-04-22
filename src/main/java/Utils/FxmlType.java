package Utils;

/**
 * Enumeration des types de scène disponible dans le jeu
 */
public enum FxmlType {
    Lobby("lobby.fxml"),
    BoatPlacement("boatPlacement.fxml"),
    GameScene("gameScene.fxml");

    /**
     * Nom du fichier FXML
     */
    private final String fxmlName;

    FxmlType(String fxml) {
        this.fxmlName = fxml;
    }

    /**
     * Fonction pour récupérer le nom du fichier FXML
     * @return Le nom du fichier
     */
    public String getFxmlName() {
        return fxmlName;
    }
}
