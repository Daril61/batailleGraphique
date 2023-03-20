module com.example.bataillenavale_graphique {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bataillenavale_graphique to javafx.fxml;
    exports com.example.bataillenavale_graphique;
}