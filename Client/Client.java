package Project.Client;

import Project.Global.Agent;
import Project.Global.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * The Client class creates a socket and connect to a server.
 * The class can create instances of agents, and represent them in a GUI, clientGUI.
 * Handles incoming data from the server.
 *
 * @author Markus Dybeck
 * @since 2016-12-09
 * @version 1.0
 */

public class Client extends Thread {

    /* Server Members */
    private int PORT = 2000;
    private String IP = "192.18.1.73";
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private boolean GUI = false;
    private ClientGUI clientGUI = null;

    public void sendData(Data data) throws IOException {
        oos.writeObject(data);
        oos.reset();
    }

    public Client(String ip, int port ) {
        this.IP = ip;
        this.PORT = port;
        start();
    }

    /** Start a client with GUI representation **/
    public Client(String ip, int port, boolean startGUI) {
        this.IP = ip;
        this.PORT = port;
        if(startGUI) {
            this.GUI = true;
            String[] args = {""};
            Thread one = new Thread() {
                public void run() {
                    clientGUI = new ClientGUI();
                    clientGUI.startClientGUI(args);
                }
            };
            one.start();
        }
        start();
    }


    /**
     * The run method create the socket instance,
     * stands by and wait for incoming data and then
     * updates the clientGUI.
     */
    @Override
    public void run() {
        try {
            /* Set up socket/streams */
            InetAddress addr = InetAddress.getByName(IP);
            Socket socket = new Socket(addr, PORT);

             /* Initialize Streams */
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            /* Send connectionObject */
            sendData(new Data("Connect",""));

            /* While true, Parse messages */
            while(true) {

                /* Start with some sleep */
                Thread.sleep(200);

                /* Wait for message */
                Data data = (Data)ois.readObject();

                /* Check object type */
                if(data.getAction().equals("getData")) {

                    Agent agent = (Agent)data.getObject();

                    /* Change the GUI representation */
                    if(this.GUI && agent!= null) {

                        /* Make deep copy of agent positions and neighbors */
                        Agent agent1 = new Agent();
                        agent1.pos.set(agent.pos.x,agent.pos.y);
                        agent1.velocity.set(agent.velocity.x,agent.velocity.y);
                        agent1.agents = new ArrayList<Agent>();

                        if(agent.agents != null) {
                            for (Agent a : agent.agents
                                 ) {
                                Agent neighbour = new Agent();
                                neighbour.pos.set(a.pos.x,a.pos.y);
                                neighbour.velocity.set(a.velocity.x,a.velocity.y);
                                agent1.agents.add(neighbour);
                            }
                        }

                        System.out.println("ClientGUI neighbors: " + agent1.agents.size());

                        /* Update client screen */
                        clientGUI.updateScreen(agent1);
                    }

                    /* Calculate new data */
                    agent.calculateMovement();

                    /* Send our updated agent */
                    sendData(new Data("Data",agent));
                }

            } /* End while-loop */

        /* Catch some exceptions */
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
             e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  finally {
            try {
                oos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    } /* End Run Method */

    /** Simple main method to start one instance of Client class */
    public static void main (String[] args) throws UnknownHostException {
        new Client(Inet4Address.getLocalHost().getHostAddress(),2000, true);
    }

}
