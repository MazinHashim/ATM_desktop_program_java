package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class atmBuilderController implements Initializable{

    @FXML
    private JFXButton home;
    @FXML
    private JFXButton close; 
    @FXML
    private JFXButton admin;
    @FXML
    private JFXButton user;
    @FXML
    private JFXButton exit;
    @FXML
    private JFXDrawer slider;
    @FXML
    private AnchorPane ancorPane;
    private static atmBuilderController instance;
    public atmBuilderController() {
    	instance = this;
    }
    public static atmBuilderController getInstance() {
    	return instance;
    }
    
    private double x = 0;
    private double y = 0;
//    private VBox sidePane;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadPane("home.fxml");
	}
	
	public void dragged(MouseEvent e) {
		Node node = (Node) e.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.setX(e.getScreenX()-x);
		stage.setY(e.getScreenY()-y);
	}
	public void pressed(MouseEvent e) {
		x = e.getSceneX();
		y = e.getSceneY();
	}
	
	public void loadPane(String fxml) {
		FadeTransition trans = new FadeTransition();
		
		try {
			AnchorPane pane = FXMLLoader.load(getClass().getResource(fxml));
			ancorPane.getChildren().clear();
			ancorPane.getChildren().add(pane);
			trans.setNode(pane);
			trans.setDuration(Duration.seconds(2));
			trans.setFromValue(0.0);
			trans.setToValue(2.0);
			trans.play();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
    @FXML
    public void mainMenu(ActionEvent event) {
    	if(event.getSource() == home) {
    		loadPane("home.fxml");
    	}
    	if(event.getSource() == admin) {
    		loadPane("home.fxml");
    		Stage primaryStage = new Stage();
    		try {
				Parent pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
				Scene scene = new Scene(pane);
				scene.setFill(Color.TRANSPARENT);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.TRANSPARENT);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}    		
    	}
    	if(event.getSource() == user) {
    		loadPane("home.fxml");
    		Stage primaryStage = new Stage();
    		try {
				Parent pane = FXMLLoader.load(getClass().getResource("userLogin.fxml"));
				Scene scene = new Scene(pane);
				scene.setFill(Color.TRANSPARENT);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.TRANSPARENT);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	if(event.getSource() == exit) {
    		Platform.exit();
    		System.exit(0);
    	}
    	if(event.getSource() == close) {
    		Platform.exit();
    		System.exit(0);
    	}
    }
    
}
