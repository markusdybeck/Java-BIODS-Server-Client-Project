package Project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.awt.*;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Created by Markus Dybeck on 2016-11-29.
 **/

public class ClientGUI extends Application {

    private static Group root;
    private static Stage primaryStage;

    public static void startClientGUI(String[] args) {
        launch(args);
    }


    public void updateScreen(Agent a) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                /* Clear the root*/
                clearScreen();
                /* Add all our shapes again.*/
                addMembers(a);
            }
        });
    }



    private void clearScreen(){root.getChildren().clear();}
    private void addMembers(Agent a) {

        Color primaryColor = Color.BLUEVIOLET;
        Color seoncdaryColor = Color.RED;

        double centerW = primaryStage.getWidth()/2;
        double centerH = primaryStage.getHeight()/2;

        double r = 75; // Neighbour radius
        double r5 = r-5;
        double cr = 5; // Circle Radius


        /* Create our direction line */
        double v = a.velocity.getV(); // Angle between velocity coordinates
        Line d = new Line(centerW,centerH,(centerW)+(r5*Math.cos(v)),(centerH)+(r5*Math.sin(v)));
        d.setStrokeWidth(1);
        d.setStroke(primaryColor);

        /* Add our agent with an outer bound circle and direction line */
        root.getChildren().add(new Circle(primaryStage.getWidth() / 2, primaryStage.getHeight() / 2, r, primaryColor));
        root.getChildren().add(new Circle(primaryStage.getWidth() / 2, primaryStage.getHeight() / 2, r5, Color.WHITE));
        root.getChildren().add(d);
        root.getChildren().add(new Circle(primaryStage.getWidth() / 2, primaryStage.getHeight() / 2, cr, primaryColor));

        /* Some debugging/information texts */
        Text t3 = new Text(10,20,"World position: " + new BigDecimal(a.pos.x).setScale(2, RoundingMode.HALF_UP) + " :: " + new BigDecimal(a.pos.y).setScale(2, RoundingMode.HALF_UP));
        Text t = new Text (10, 40, "Velocity: " + new BigDecimal(a.velocity.x).setScale(2, RoundingMode.HALF_UP) + " :: " + new BigDecimal(a.velocity.y).setScale(2, RoundingMode.HALF_UP));
        Text t4 = new Text(10,60, "Direction: " + v*180/3.14);
        Text t2 = new Text(10,80, "Neighbours: " + a.agents.size());
        root.getChildren().add(t);
        root.getChildren().add(t2);
        root.getChildren().add(t3);
        root.getChildren().add(t4);

        /* Show all neighbours */
        if(a.agents != null) {
            for (Agent neighbour : a.agents ) {

                /* Calculate the neighbour positions relative our centered host */
                double xPos = (neighbour.pos.x - a.pos.x) + primaryStage.getWidth() / 2;
                double yPos = (neighbour.pos.y - a.pos.y) + primaryStage.getHeight() / 2;
                Circle c = new Circle(xPos, yPos, cr, seoncdaryColor);

                /* Some debugging prints */
                System.out.println("Neighbour in ClientGUI: " + xPos + " :: " + yPos);
//                System.out.println("Neighbour distance: " + (neighbour.pos.x-a.pos.x) + " :: " + (neighbour.pos.y-a.pos.y) );
//                System.out.println("Neighbour : " + (neighbour.pos.x) + " :: " + (neighbour.pos.y) + " Self: " + a.pos.x + " :: " + a.pos.y );

                root.getChildren().add(c);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Hello World!");

        this.root = new Group();

        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();
    }
}
