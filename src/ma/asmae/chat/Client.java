package ma.asmae.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Client extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Interface1.fxml"));
        Scene scene=new Scene(root);
        primaryStage.setTitle("Chat App ");
        scene.getStylesheets().add(getClass().getResource("css.css").toExternalForm());
        Image icon=new Image("ma/asmae/chat/images/icon.jfif");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
