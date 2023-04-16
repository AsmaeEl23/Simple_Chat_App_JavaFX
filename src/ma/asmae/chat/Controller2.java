package ma.asmae.chat;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller2 implements Initializable {

    @FXML
    private TextField userName;
    @FXML
    private FontAwesomeIcon icon;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    void connect(ActionEvent event) throws Exception {

        String name=userName.getText();
        if(!name.isEmpty()){
            FXMLLoader loader = new FXMLLoader ();
            loader.setLocation(getClass().getResource("interface2.fxml"));
            loader.load();
            Controller controller= loader.getController();
            controller.setUp(name);

            BorderPane borderPane= loader.getRoot();
            anchorPane.getChildren().removeAll();
            anchorPane.getChildren().setAll(borderPane);
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DropShadow dropShadow=new DropShadow(10, Color.web("#fce988"));
        icon.setEffect(dropShadow);

    }
}
