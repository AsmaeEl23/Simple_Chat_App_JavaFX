package ma.asmae.chat;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private TextField message;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label title;
    @FXML
    private ComboBox<String> to;
    @FXML
    private VBox vbox_message;
    private PrintWriter pw;
    private ObservableList<String> users= FXCollections.observableArrayList();
    private String name;

    public void setUp(String name){
        this.name=name;
        title.setText("Welcome, "+name);
        connectToServer();
    }

    private void connectToServer(){
        String host="localhost";
        int port=1234;
        try {
            Socket socket=new Socket(host,port);
            InputStream is=socket.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            OutputStream os=socket.getOutputStream();
            pw=new PrintWriter(os,true);
            pw.println(name);
            System.out.println("Connection ....");

            new Thread(()->{
                while (true){
                    try {
                        String rqt= br.readLine();
                        String[] lst=rqt.split("=>");
                        String from=lst[0];
                        String response=lst[1];

                        Platform.runLater(()-> {
                        if(response.contains("-")){
                            String[] list= response.split("-");
                            users.clear();
                            users.add("All");
                            users.addAll(list);
                            users.removeAll("", name);
                        }else{
                            Platform.runLater(()->{
                                HBox hBox=new HBox();
                                hBox.setAlignment(Pos.CENTER_LEFT);
                                hBox.setPadding(new Insets(5,5,5,10));

                                Text text=new Text(response);
                                text.setFill(new Color(0,0,0,1));

                                TextFlow textFlow=new TextFlow(text);
                                textFlow.setStyle("-fx-background-color: #a6a4a4;-fx-background-radius: 20px;-fx-font-weight: bold ;");
                                textFlow.setPadding(new Insets(5,5,5,10));

                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                Label dateLabel=new Label(dateFormat.format(date));

                                FontAwesomeIcon user_icon=new FontAwesomeIcon();
                                user_icon.setGlyphName("USER");
                                user_icon.setSize("20");

                                Label labelName=new Label();

                                labelName.setStyle("-fx-font-size: 15px;" +
                                        "    -fx-font-family:Elephant;" +
                                        "    -fx-text-fill: #000000;");
                                labelName.setFont(new Font(20));
                                labelName.setText(from);
                                VBox vBox1=new VBox(user_icon,labelName);
                                vBox1.setAlignment(Pos.CENTER);
                                vBox1.setMinWidth(70);

                                VBox vBox2=new VBox(textFlow,dateLabel);
                                vBox2.setAlignment(Pos.CENTER_LEFT);

                                hBox.getChildren().addAll(vBox1,vBox2);
                                hBox.setSpacing(10);
                                vbox_message.getChildren().add(hBox);
                            });
                        }
                        });
                    }catch (IOException e){
                        throw new RuntimeException(e);
                    }
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void send(ActionEvent event){
        System.out.println("Send function ");
        String request=message.getText();
        String toStr=to.getValue();
        if(!request.isEmpty() && toStr!=null){
            HBox hBox=new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text text=new Text(request);
            text.setFill(new Color(0,0,0,1));

            TextFlow textFlow=new TextFlow(text);
            textFlow.setStyle("-fx-background-color: #fce988;-fx-background-radius: 20px;-fx-font-weight: bold ;");
            textFlow.setPadding(new Insets(5,5,5,10));

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            Label dateLabel=new Label(dateFormat.format(date));

            VBox vBox=new VBox(textFlow,dateLabel);
            vBox.setAlignment(Pos.CENTER_RIGHT);

            hBox.getChildren().add(vBox);
            vbox_message.getChildren().add(hBox);
            pw.println(toStr+"=>"+request);
            message.clear();
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You should write a message and shoos a receiver ");
            alert.show();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        vbox_message.setAlignment(Pos.TOP_CENTER);
        to.setItems(users);
        vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });
    }

}
