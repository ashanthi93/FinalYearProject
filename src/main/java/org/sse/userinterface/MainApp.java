package org.sse.userinterface;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sse.knowedgemodel.prolog.AssociationLoader;
import org.sse.knowedgemodel.prolog.KbBuilder;
import org.sse.knowedgemodel.prolog.PrologConverter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class MainApp extends Application {

    public static Scene welcomeWindow;
    
    @Override
    public void start(Stage stage) throws Exception {
        //new KbBuilder().writeSimilarity();

        //KbBuilder.write();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScene.fxml"));

        welcomeWindow = new Scene(root);
        welcomeWindow.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Welcome To Conexus");
        stage.setResizable(false);
        stage.setScene(welcomeWindow);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        launch(args);
    }
}
