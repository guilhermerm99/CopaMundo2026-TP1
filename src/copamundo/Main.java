package copamundo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/copamundo/principal/visao/TelaPrincipal.fxml"));
        primaryStage.setTitle("Sistema de Gestão — Copa do Mundo 2026");
        primaryStage.setScene(new Scene(root, 1100, 680));
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
