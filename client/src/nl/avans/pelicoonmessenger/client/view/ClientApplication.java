package nl.avans.pelicoonmessenger.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.avans.pelicoonmessenger.base.BaseApplication;

public class ClientApplication extends BaseApplication {

    private static ClientApplication instance;
    private Stage stage;

    public static ClientApplication getInstance(){
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        this.stage = stage;
        loadLayout("layouts/client_login.fxml", "Login", 300, 275);
    }

    public void loadLayout(String name, String title, int width, int height) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(name));
        stage.setTitle(title);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }
}
