package Project;

import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Markus on 2016-12-06.
 */
public class CreateClients {

        public static void main (String[] args) throws IOException, ClassNotFoundException, InterruptedException {
            for (int i = 0; i < 20; i++) {
                Client c = new Client();
            }

            for (int i = 0; i < 1; i++) {
                Client c = new Client(true);
            }

        }
}

