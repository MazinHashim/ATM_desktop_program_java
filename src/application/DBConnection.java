package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

public class DBConnection {
	
	private static Account user = null;
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","mazindb");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}

	public static int createTable() {
		Connection connection = getConnection();
		int id = 1;
		try {
			String sql = "create table ATMacc(id number,fname varchar2(25),lname varchar2(25),uname varchar2(25),"
					+ "password varchar2(20),balance number,phone varchar2(20),city varchar2(20))";
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			System.out.println("Table Created Successfull");
		} catch (SQLException e) {
			try {
				Statement statement = connection.createStatement();
				ResultSet set = statement.executeQuery("select * from ATMacc");
				while(set.next()) {
					id++;
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	public static Account getAccount(String usr) {
		Connection connection = null;
		Account acc = null;
		try {
			connection = getConnection();
			String sql = "select * from ATMacc where uname=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, usr);
			ResultSet set = statement.executeQuery();
			int count=0,idnum=0;
			String fname="",lname="",uname="",phone="",pass="",city="";
			double balance=0.0;
			while(set.next()) {
				idnum = set.getInt("id");
				fname = set.getString("fname");
				lname = set.getString("lname");
				uname = set.getString("uname");
				pass = set.getString("password");
				balance = set.getDouble("balance");
				phone = set.getString("phone");
				city = set.getString("city");
				count++;
			}
			if(count == 1) {
				acc = new Account(idnum,fname,lname,uname,pass,balance,phone,city);
				return acc;
			}else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Account is not found");
				alert.show();
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return acc;
	}

	public static void despositAmount(JFXTextField destext, Label desmess) {
		user = LoginController.acc;
		Connection connection = null;
		try {
			double amount = Double.parseDouble(destext.getText()); 
			connection = getConnection();
			String sql = "update ATMacc set balance=balance+? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, amount);
			statement.setInt(2, user.getIdnum());
			statement.executeUpdate();
			destext.clear();
			desmess.setVisible(true);
			desmess.setText("Cash Has Been Despoit");
		} catch (SQLException | NumberFormatException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter Some Amount");
			alert.show();
		}
	}

	public static void withdrawAmount(JFXTextField withtext, Label withmess) {
		user = LoginController.acc;
		Connection connection = null;
		try {
			double amount = Double.parseDouble(withtext.getText()); 
			connection = getConnection();
			String sql = "update ATMacc set balance=balance-? where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, amount);
			statement.setInt(2, user.getIdnum());
			statement.executeUpdate();
			withtext.clear();
			withmess.setVisible(true);
			withmess.setText("Cash Has Been Withdrawed");
		} catch (SQLException | NumberFormatException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter Some Amount");
			alert.show();
		}		
	}

	public static void transferAmount(JFXTextField recev, JFXTextField amountran, JFXPasswordField repass,
			Label tranmess) {
		user = LoginController.acc;
		Connection connection = null;
		try {
			double amount = Double.parseDouble(amountran.getText());
			int reid = Integer.parseInt(recev.getText());
			String pass = repass.getText();
			connection = getConnection();
			String sql1 = "update ATMacc set balance=balance+? where id=?";
			String sql2 = "update ATMacc set balance=balance-? where id=? and password=?";
			PreparedStatement statement2 = connection.prepareStatement(sql2);
			statement2.setDouble(1, amount);
			statement2.setInt(2, user.getIdnum());
			statement2.setString(3, pass);
			boolean found = statement2.execute();
			System.out.println(found);
			if(!found) {
				System.out.println("tranfer Successfully");
				PreparedStatement statement = connection.prepareStatement(sql1);
				statement.setDouble(1, amount);
				statement.setInt(2, reid);
				statement.executeUpdate();
				tranmess.setText("Cash Has Been Transfered");
				tranmess.setVisible(true);
				recev.clear();
				amountran.clear();
				repass.clear();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			repass.clear();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Password is wrong");
			alert.show();
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter Data");
			alert.show();
		}
	}

	public static void infromation(JFXTextField fullname, JFXTextField username, JFXTextField password,
			JFXTextField balance, JFXTextField phone, JFXTextField city) {
		Connection connection = DBConnection.getConnection();
		user = LoginController.acc;
		try {
			String sql = "select * from ATMacc where id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, user.getIdnum());
			ResultSet set = statement.executeQuery();
			int count = 0;
			String fname="",pass="",uname="",ph="",cit="";
			double bal=0.0;
			while(set.next()) {
				fname = set.getString("fname");
				fname = fname.concat(" "+set.getString("lname"));
				pass = set.getString("password");
				uname = set.getString("uname");
				bal = set.getDouble("balance");
				ph = set.getString("phone");
				cit = set.getString("city");
				count++;
			}
			if(count == 1) {
				fullname.setText(fname);
				username.setText(uname);
				password.setText(pass);
				balance.setText(Double.toString(bal));
				phone.setText(ph);
				city.setText(cit);
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Account is not found");
				alert.show();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void changePassword(JFXTextField oldpass, JFXPasswordField newpass, JFXPasswordField confirm,
			Label changmess) {
		user = LoginController.acc;
		Connection connection = null;
		try {
			String old = oldpass.getText();
			String nw = newpass.getText();
			String conf = confirm.getText();
			String sql = "update ATMacc set password=? where id=? and password=?";
			connection = getConnection();
			if(nw.equals(conf)) {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, nw);
				statement.setInt(2, user.getIdnum());
				statement.setString(3, old);
				statement.executeUpdate();
				changmess.setText("Password is Changed Successfull");
				changmess.setVisible(true);
				oldpass.clear();
				newpass.clear();
				confirm.clear();
			}else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Password Not Confirm Matches");
				alert.show();
				
			}
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Passwrod is wrong");
			alert.show();

		}catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter Some Data");
			alert.show();
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
