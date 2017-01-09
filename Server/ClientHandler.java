package Project.Server;

import Project.Global.Agent;
import Project.Global.Data;
import Project.Server.SimulationServer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * The ClientHandler
 *
 * @author Markus Dybeck
 * @since 2016-12-02
 * @version 1.0
 */

public class ClientHandler extends Thread {

    private static int numberOfClients = 0;

    public final int clientNumber = ++numberOfClients;

    /* Socket/Stream members */
    private final Socket socket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    /* Save agent for this socket */
    private Agent thisAgent = null;



    public ClientHandler(Socket s) throws IOException, ClassNotFoundException {

        this.socket = s;
        oos = new ObjectOutputStream(socket.getOutputStream());
        sendData(new Data("Welcome", ""));

        /* Initialize input object stream from SimulationServer*/
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Created streams");
        System.out.println("ClientThread " + clientNumber + " created.");

        Data data = (Data)ois.readObject();
        if(data.getAction().equals("Connect")) {
            System.out.println("Incoming connection");
            thisAgent = new Agent();
            thisAgent.setSight(75);
            SimulationServer.simulation.addClient(this,thisAgent);
        } else {
            System.out.println("Incoming object: " + data.getAction());
        }


        // Run thread.
        start();
    }


    public void sendData(Data obj){
        try {
            oos.writeObject(obj);
            oos.reset();
        } catch (IOException e) {
            System.out.println("Exception, socket probably closed");
        }

    }

    @Override
    public void run() {
        try{
            while(true){

                Thread.sleep(100);

                 /* Wait for message */
                Data data = (Data)ois.readObject();

                /* Check object type */
                if(data.getAction().equals("Data")) {
                    /* update agent */
                    Agent a = (Agent)data.getObject();
                    updateAgentValues(a);

                    //System.out.println("Data incoming");

                    SimulationServer.simulation.addToQueue(this,data.getAction());


                } else {
                    System.out.println("Incoming object: " + data.getAction());
                }

            }
        }
        catch (InterruptedException e) {
            /* Move on */
        } catch (IOException e) {
            /* Move on */
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            try{
                socket.close();
                System.out.println("Closed socket for client: " + clientNumber);
                SimulationServer.simulation.clientSize--;
            }
            catch(IOException e){
                System.out.println("Client thread " + clientNumber + ": Socket not closed!");
            }

            // Remove client from list.
            SimulationServer.simulation.getClientHashMap().remove(this,thisAgent);
            numberOfClients--;
            //4SimulationServer.simulation.setClientSize(numberOfClients);
            System.out.println("Connected client after closing one: " + SimulationServer.simulation.getClientHashMap().size());
        }

    }

    /** Update our agent with the new values **/
    private void updateAgentValues(Agent a) {
        thisAgent.pos.add(a.pos.x,a.pos.y);
        thisAgent.velocity.set(a.velocity.x,a.velocity.y);
    }

    /** Calculate all neighbours for one agent
     ** I use circles as agent shapes, so lets calculate if the neighbours
     *  are inside/on the given radius, instead of given points.
     *  Circle equation: x^2 + y^2 <= r^2
     * **/
    public ArrayList<Agent> checkNeighbors() {
        ArrayList<Agent> neighbourList = new ArrayList<Agent>();
        double unit = 75.0;
        double radius = unit * unit;

        Iterator it = SimulationServer.simulation.getClientHashMap().entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry) it.next();
            Agent neighbor = ((Agent)pair.getValue());

            if(neighbor != thisAgent) {
                /* Check if neighbor is in reasonable distance */
                double xPos = neighbor.pos.x-thisAgent.pos.x;
                double yPos = neighbor.pos.y-thisAgent.pos.y;
                double xPosSquared = xPos * xPos;
                double yPosSquared = yPos * yPos;

                if((xPosSquared + yPosSquared) <= radius)
                {
                    neighbourList.add(neighbor);
                }
            }

        }
        System.out.println("Neighbours: " + neighbourList.size());
        return neighbourList;
    }

}
