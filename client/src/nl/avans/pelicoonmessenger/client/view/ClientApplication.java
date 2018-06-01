package nl.avans.pelicoonmessenger.client.view;

import com.sun.istack.internal.Nullable;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.avans.pelicoonmessenger.base.BaseApplication;
import nl.avans.pelicoonmessenger.client.controller.Controller;

import java.util.ResourceBundle;

public class ClientApplication extends BaseApplication {

    private static ClientApplication instance;
    private Stage stage;
    private Controller controller;

    public static ClientApplication getInstance(){
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        this.stage = stage;
        loadLayout("layouts/client_login.fxml", "Login", 300, 275, null);
    }

    public void loadLayout(String name, String title, int width, int height, @Nullable Controller controller) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(name));
        this.controller = controller;
        if(controller != null)
            loader.setController(controller);
        Parent root = loader.load();
        stage.setTitle(title);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            if(controller != null)
                controller.onClose();
            event.consume();
        });
    }
}
