package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LoginController implements Initializable{
	
    @FXML
    private JFXButton loginadm,useLogin;
    @FXML
    private JFXTextField useradm,userus;
    @FXML
    private JFXPasswordField passadm,passus;
    @FXML
    private JFXButton cls;
    @FXML
    private JFXSpinner progressadm,progressus;
    
    public static int id;
    public static Account acc;
    private static LoginController instance;
    
    public LoginController() {
		instance = this;
	}
    public static LoginController getInstance() {
    	return instance;
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id = DBConnection.createTable();
		
	}
    public void loginUser(ActionEvent e) {
    	String use = userus.getText();
    	String pass = passus.getText();
    	acc = DBConnection.getAccount(use);
    	progressus.setVisible(true);
    	
    	try {
    		if(use.equals(acc.getUserName())&&pass.equals(acc.getPassword())) {
    			atmBuilderController.getInstance().loadPane("user.fxml");
    			useLogin.getScene().getWindow().hide();
    		}else {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText(null);
    			alert.setContentText("Account is not found");
    			alert.show();
    		}
    	}catch (NullPointerException ex) {
		}
    }
    public void loginAdmin(ActionEvent e) {
    	String use = useradm.getText();
    	String pass = passadm.getText();
    	progressadm.setVisible(true);
    	
		if(use.equals("Admin")&&pass.equals("ATMacc")) {
			atmBuilderController.getInstance().loadPane("admin.fxml");
			loginadm.getScene().getWindow().hide();
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Account is not found");
			alert.show();
		}
    }
    public void closeAction(ActionEvent e) {
		cls.getScene().getWindow().hide();
	}

}
