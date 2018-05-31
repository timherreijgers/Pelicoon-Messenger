package nl.avans.pelicoonmessenger.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.avans.pelicoonmessenger.base.logging.Logger;

public class ServerApplication extends Application {

    private Server server = Server.getInstance();

    public static void main(String[] args) {
        ServerApplication.launch(ServerApplication.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("layouts/server_console.fxml"));

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Server Console");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            server.stop();
            Platform.exit();
            event.consume();
        });

        Logger logger = new Logger("ServerApplication");
        logger.info("Testing logger");

        server.start();
    }
}
