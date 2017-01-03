package Project.Client;

import java.io.IOException;
import java.net.Inet4Address;

/**
 * The CreateClients program simply create a few instances of the Client program.
 *
 * @author Markus Dybeck
 * @since 2016-12-06
 * @version 1.0
 *
 */
public class CreateClients {

        public static void main (String[] args) throws IOException, ClassNotFoundException, InterruptedException {
            for (int i = 0; i < 40; i++) {
                Client c = new Client(Inet4Address.getLocalHost().getHostAddress(),2000);
            }

            /* Create a agent instance with GUI representation */
            for (int i = 0; i < 1; i++) {
                Client c = new Client(Inet4Address.getLocalHost().getHostAddress(),2000,true);
            }

        }
}

