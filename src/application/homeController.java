package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class homeController implements Initializable{

    @FXML
    private ImageView cash;

    @FXML
    private ImageView cenimg;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TranslateTransition transition1 = new TranslateTransition();
		transition1.setNode(cash);
		transition1.setDuration(Duration.seconds(1));
		transition1.setAutoReverse(true);
		transition1.setCycleCount(TranslateTransition.INDEFINITE);
		transition1.setFromX(0.0);
		transition1.setToY(20.0);
		transition1.play();
		
		TranslateTransition transition2 = new TranslateTransition();
		transition2.setNode(cenimg);
		transition2.setDuration(Duration.seconds(2));
		transition2.setFromX(0.0);
		transition2.setToY(150.0);
		transition2.play();
	}

}
