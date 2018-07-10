package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.list_medicine();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("RedCross.png")));
        primaryStage.setTitle("Pharmacy billing Application");
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        if(!Datasource.getInstance().open()){
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        Datasource.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
