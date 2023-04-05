package Utils;

public enum FxmlType {
    Lobby("lobby.fxml"),
    BoatPlacement("boatPlacement.fxml"),
    GameScene("gameScene.fxml");

    private final String fxmlName;

    private FxmlType(String fxml) {
        this.fxmlName = fxml;
    }

    public String getFxmlName() {
        return fxmlName;
    }
}
