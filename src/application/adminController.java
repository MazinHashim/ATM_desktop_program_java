package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;

public class adminController implements Initializable{
	
    @FXML
    private JFXTextField idtext,frist,last,user,balance,phone,city,
    idupd,fristUp,lastUp,userUp,phoneUp,balanceUp,cityUp,iddel;
    @FXML
    private JFXPasswordField pass;
    @FXML
    private JFXButton addButton,updButton,delButton;
    @FXML
    private Label lab,message,messageUp,delmessage;
    
	private int id;
	private String year = Integer.toString(LocalDate.now().getYear());
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id = LoginController.id;
		if(id < 10)
			idtext.setText(year+"0"+id);
		else
			idtext.setText(year+id);
		lab.setText(year+"+");
	}
    @FXML
    public void AdminAction(ActionEvent event) {
    	Connection connection = DBConnection.getConnection();
    	TranslateTransition trn = null;
    	if(event.getSource() == addButton) {
        	try {
        		if(!allNotEmpty()){
        			String sql = "insert into ATMacc values(?,?,?,?,?,?,?,?)";
        			PreparedStatement statement = connection.prepareStatement(sql);
        			statement.setInt(1, Integer.parseInt(idtext.getText()));
        			statement.setString(2, frist.getText());
        			statement.setString(3, last.getText());
        			statement.setString(4, user.getText());
        			statement.setString(5, pass.getText());
        			statement.setDouble(6, Double.parseDouble(balance.getText()));
        			statement.setString(7, phone.getText());
        			statement.setString(8, city.getText());
        			statement.executeUpdate();
        			message.setText("Thank you for Registeration");
        			trn =TranslateAnimation(message);
        			id++;
        			if(id < 10)
        				idtext.setText(year+"0"+id);
        			else
        				idtext.setText(year+id);    		
        			frist.clear();
        			last.clear();
        			user.clear();
        			pass.clear();
        			balance.clear();
        			phone.clear();
        			city.clear();
        		}else {
    				Alert alert = new Alert(AlertType.ERROR);
    				alert.setHeaderText(null);
    				alert.setContentText("We Need All Data");
    				alert.show();        			
        		}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	if(event.getSource() == updButton) {
    		String sql = "update ATMacc set fname=?,lname=?,uname=?,balance=?,phone=?,city=? where id=?";
    		try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, fristUp.getText());
				statement.setString(2, lastUp.getText());
				statement.setString(3, userUp.getText());
				statement.setDouble(4, Double.parseDouble(balanceUp.getText()));
				statement.setString(5, phoneUp.getText());
				statement.setString(6, cityUp.getText());
				statement.setInt(7, Integer.parseInt(idupd.getText()));
				statement.executeUpdate();
				clearUpd();
				updButton.setDisable(true);
				messageUp.setText("Update is Completed Successufull");
				TranslateAnimation(messageUp);
			} catch (SQLException e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Account is not found");
				alert.show();
			}catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Enter Some Data To Be Changed");
				alert.show();
			}
    	}
    	if(event.getSource() == delButton) {
    		String sql = "delete from ATMacc where id=?";
    		String sql2 = "select * from ATMacc where id=?";
    		try {
				PreparedStatement statement2 = connection.prepareStatement(sql2);
				statement2.setInt(1, Integer.parseInt(iddel.getText()));
				ResultSet set2 = statement2.executeQuery();
				int count = 0;
				String fname="",lname="";
				while(set2.next()) {
					fname = set2.getString("fname");
					lname = set2.getString("lname");
					count++;
				}
				if(count == 1) {
					PreparedStatement statement = connection.prepareStatement(sql);
					statement.setInt(1, Integer.parseInt(iddel.getText()));
					statement.executeUpdate();
					delmessage.setText(fname+" "+lname);
					FadeTransition fade = new FadeTransition();
					fade.setNode(delmessage);
					fade.setDuration(Duration.seconds(1));
					fade.setFromValue(0.0);
					fade.setToValue(5.0);
					fade.play();
					iddel.clear();
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("Account is not found");
					alert.show();
				}
			} catch (SQLException | NumberFormatException e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Please Enter The Id Number");
				alert.show();
			}
    	}
    	if(idtext.isFocused()||frist.isFocused()||last.isFocused()||balance.isFocused()||
    	   user.isFocused()||pass.isFocused()|phone.isFocused()||city.isFocused()) {
    		if(trn != null) {
    			trn.setAutoReverse(true);
    			trn.play();
    		}
    	}
    }
	private boolean allNotEmpty() {
		if(frist.getText().equals("")||last.getText().equals("")||user.getText().equals("")||pass.getText().equals("")||balance.getText().equals("")||city.getText().equals("")||phone.getText().equals(""))
			return true;
		return false; 
	}
	private void clearUpd() {
		fristUp.clear();
		lastUp.clear();
		phoneUp.clear();
		cityUp.clear();
		userUp.clear();
		balanceUp.clear();
	}
	public void getAccont(ActionEvent event) {
		Connection connection = DBConnection.getConnection();
		try {
			String sql = "select * from ATMacc where id="+idupd.getText();
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(sql);
			clearUpd();
			int count = 0;
			String fname="",lname="",uname="",phone="",city="";
			double balance=0.0;
			while(set.next()) {
				fname = set.getString("fname");
				lname = set.getString("lname");
				uname = set.getString("uname");
				balance = set.getDouble("balance");
				phone = set.getString("phone");
				city = set.getString("city");
				count++;
			}
			if(count == 1) {
				fristUp.setText(fname);
				lastUp.setText(lname);
				userUp.setText(uname);
				balanceUp.setText(Double.toString(balance));
				phoneUp.setText(phone);
				cityUp.setText(city);
				updButton.setDisable(false);
			}
			else {
				updButton.setDisable(true);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Account is not found");
				alert.show();
			}
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter Id Number");
			alert.show();
		}
		
	}
	private TranslateTransition TranslateAnimation(Node node) {
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(node);
		transition.setFromX(0.0);
		transition.setToX(-240);
		transition.setDuration(Duration.seconds(2));
		transition.play();
		return transition;
	}
}
