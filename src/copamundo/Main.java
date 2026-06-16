package copamundo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/copamundo/usuarios/visao/TelaLogin.fxml")
                //getClass().getResource("/copamundo/principal/visao/TelaPrincipal.fxml")
                //getClass().getResource("/copamundo/partidas/visao/TelaConsultaPartidas.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
