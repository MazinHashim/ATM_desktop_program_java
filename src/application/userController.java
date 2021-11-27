package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class userController implements Initializable{

    @FXML
    private Label checkBalan,desmess,withmess,tranmess,changmess;
    @FXML
    private JFXPasswordField repass,newpass,confirm;
    @FXML
    private JFXTextField destext,withtext,recev,amountran,fullname,username,
    						password,balance,phone,city,oldpass;
    @FXML
    private Pane settpane;
    @FXML
    private JFXButton ref,resh,desposit,withdraw,transfer,load,change,setting,logout;
    
    private boolean vis = false;
	public static Account account;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		account = LoginController.acc;
		setting.setTooltip(new Tooltip("Setting"));
		resh.setTooltip(new Tooltip("Refresh"));
		logout.setTooltip(new Tooltip("Logout"));
		System.out.println("Wellcome "+account.getUserName());
		checkBalan.setText(Double.toString(account.getBalance()));
	}
	public void UserAction(ActionEvent e) {
		if(e.getSource() == desposit) {
			System.out.println("Desposit Maoney");
			DBConnection.despositAmount(destext,desmess);
		}
		if(e.getSource() == withdraw) {
			System.out.println("Withdraw Maoney");
			DBConnection.withdrawAmount(withtext,withmess);
		}
		if(e.getSource() == transfer) {
			System.out.println("Balance Transfer");
			DBConnection.transferAmount(recev,amountran,repass,tranmess);
		}
		if(e.getSource() == load) {
			DBConnection.infromation(fullname,username,password,balance,phone,city);
			System.out.println("Infomation");
			
		}
		if(e.getSource() == change) {
			DBConnection.changePassword(oldpass,newpass,confirm,changmess);
			System.out.println("Change Password");
		}
		if(e.getSource() == setting) {
			FadeTransition trans = new FadeTransition();
			trans.setNode(settpane);
			trans.setDuration(Duration.seconds(1));
			if(vis) {
				settpane.setVisible(false);
				trans.setFromValue(1.0);
				trans.setToValue(0.0);
				vis = false;
			}else {
				trans.setFromValue(0.0);
				trans.setToValue(1.0);
				settpane.setVisible(true);
				vis = true;
			}
			trans.play();
		}
		if(e.getSource() == logout) {
			atmBuilderController.getInstance().loadPane("home.fxml");
		}
		if(e.getSource() == resh) {
			atmBuilderController.getInstance().loadPane("user.fxml");
		}
		if(e.getSource() == ref) {
			Connection connection = DBConnection.getConnection();
			try {
				String sql = "select * from ATMacc where id=?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, account.getIdnum());
				ResultSet set = statement.executeQuery();
				double bal=0.0;
				while(set.next()) {
					bal = set.getDouble("balance");
					checkBalan.setText(Double.toString(bal));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}finally {
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
