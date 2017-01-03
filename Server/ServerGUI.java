package Project.Server;

import Project.Global.Agent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The ServerGUI class extends JavaFX application and simply showing all agents in the environment.
 * Got methods and members so it can be administrated outside the class.
 *
 * @author Markus Dybeck
 * @since 2016-12-11
 * @version 1.0
 */

public class ServerGUI extends Application {

    private static Group root;
    public static Stage primaryStage;
    private static int windowWidth = 500;
    private static int windowHeight = 500;

    public static void startServerGUI(String[] args) {
        launch(args);
    }
    /** Create an instance with window Width & Height. */
    public static void startServerGUI(String[] args, int wW, int wH) {
        windowWidth = wW;
        windowHeight = wH;
        launch(args);
    }
    /** Clear the window, with a runLater lambda function */
    public void clearScreen(){
        Platform.runLater(() -> {
            /* Clear the root*/
            root.getChildren().clear();
        });
    }
    /** Add members and their direction with a runLater lamba function */
    public void addMembers(Agent a) {
        Platform.runLater(() -> {
            Color primaryColor = Color.RED;
            double cr = 5; // Circle Radius

            /* Create our direction line */
            double v = a.velocity.getV(); // Angle between velocity coordinates
            Line d = new Line(a.pos.x,a.pos.y,(a.pos.x)+(7*Math.cos(v)),(a.pos.y)+(7*Math.sin(v)));
            d.setStrokeWidth(1);
            d.setStroke(Color.BLACK);

            /* Add our agent with an outer bound circle and direction line */
            root.getChildren().add(d);
            root.getChildren().add(new Circle(a.pos.x, a.pos.y, cr, primaryColor));

            /* Some debugging/information texts */
//                Text t = new Text (10, 40, "Velocity: " + new BigDecimal(a.velocity.x).setScale(2, RoundingMode.HALF_UP) + " :: " + new BigDecimal(a.velocity.y).setScale(2, RoundingMode.HALF_UP));
//                root.getChildren().add(t);
        });
    }


    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        ServerGUI.primaryStage = primaryStage;

        primaryStage.setTitle("BOIDS Server");

        ServerGUI.root = new Group();

        /* Add scene, remove margins for borders */
        ServerGUI.primaryStage.setScene(new Scene(root,windowWidth, windowHeight));
        primaryStage.show();
    }
}
