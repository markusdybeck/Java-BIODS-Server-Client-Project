package Project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Markus on 2016-12-11.
 */
public class ServerGUI extends Application {

    private static Group root;
    private static Stage primaryStage;
    private static int windowWidth = 500;
    private static int windowHeight = 500;



    public static void startServerGUI(String[] args) {
        launch(args);
    }
    public static void startServerGUI(String[] args, int wW, int wH) {
        windowWidth = wW;
        windowHeight = wH;
        launch(args);
    }

    public void clearScreen(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                /* Clear the root*/
                root.getChildren().clear();
            }
        });


        }
    public void addMembers(Agent a) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
            }
        });
    }


    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        ServerGUI.primaryStage = primaryStage;

        primaryStage.setTitle("BOIDS Server");

        ServerGUI.root = new Group();

        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.show();
    }
}
