package Project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Markus Dybeck on 2016-11-29.
 **/
public class SimulationServer {

    public static Simulation simulation;
    public static ServerGUI serverGUI;

    public static final int PORT = 2000;
    public static void main(String[] args) throws IOException {

        simulation = new Simulation();
        Thread one = new Thread() {
            public void run() {
        serverGUI = new ServerGUI();
        serverGUI.startServerGUI(args,700,700);
            }
        };
        one.start();




        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server-socket:" + s);
        System.out.println("Server listening...");
        try {
            while(true){
                Socket socket = s.accept();
                System.out.println("Connection accepted.");
                System.out.println("The new socket: " + socket);
                ClientHandler sh = new ClientHandler(socket);
                System.out.println("New thread started.");
                System.out.println("The new thread: " + sh);
            }
        }
        catch (IOException e) {
            s.close();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            s.close();
        }
    }
}
