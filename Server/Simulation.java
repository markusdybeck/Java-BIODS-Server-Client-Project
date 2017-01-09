package Project.Server;

import Project.Global.Agent;
import Project.Global.Data;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Simulation class is the main server class,
 * it coordinates all the connected clients with a thread safe ConcurrentHashMap.
 * The class contains the actual values for all Agents in the environment,
 * handles data and tell the clients what they see.
 *
 * @author Markus Dybeck
 * @since 2016-12-09
 * @version 1.0
 */
public class Simulation extends Thread {

    private ConcurrentHashMap<ClientHandler, Agent> clients = new ConcurrentHashMap<ClientHandler,Agent>();
    private ConcurrentHashMap<ClientHandler, String> queue = new ConcurrentHashMap<>();
    public static int clientSize;

    public Simulation() {
        start();
    }

    public void addClient(ClientHandler s, Agent a) {
        clients.put(s,a);
        System.out.println("Added ClientHandler and Agents");
    }

    public void addToQueue(ClientHandler s, String str) {
        queue.put(s,str);
    }

    public ConcurrentHashMap getClientHashMap() {return clients;}

    @Override
    public void run() {
        try{
            while(true){

                Thread.sleep(100);

                if(clients.size() > 0) {

                    clientSize = clients.size();

                    /* Simple print to see our iterations */
                    System.out.println("##################");

                    /* Tell our clients to send data */
                    Iterator it = clients.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        Agent agent = ((Agent) pair.getValue());
                        System.out.println("Agent hashmap " + agent.pos.x + " :: " + agent.pos.y);
                        // agent.agents = ((Agent)pair.getValue()).agents;
                        ((ClientHandler) pair.getKey()).sendData(new Data("getData", agent));
                        System.out.println("getData Sent for clientThread" + ((ClientHandler) pair.getKey()).clientNumber);
                    }

                    /* Wait for all clients to send their data */
                    while (queue.size() != clientSize) {
                    }

                    /* Clear ServerGUI */
                    SimulationServer.serverGUI.clearScreen();

                    /* Check neighbors for clients */
                    it = clients.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        ((Agent) pair.getValue()).agents = ((ClientHandler) pair.getKey()).checkNeighbors();
                        System.out.println("neighbors checked for clientThread" + ((ClientHandler) pair.getKey()).clientNumber + " size: " + ((Agent) pair.getValue()).agents.size());

                        /* Check screen coordinates */
                        System.out.println("Server W & H : " + SimulationServer.serverGUI.primaryStage.getWidth() + " :: " + SimulationServer.serverGUI.primaryStage.getHeight() );
                        if (SimulationServer.serverGUI.primaryStage.getWidth() < ((Agent) pair.getValue()).pos.x) {
                            ((Agent) pair.getValue()).pos.x = 0;
                        } else if (0 > ((Agent) pair.getValue()).pos.x) {
                            /* Set new position - window border */
                            ((Agent) pair.getValue()).pos.x = SimulationServer.serverGUI.primaryStage.getWidth()-16;
                        }

                        if (SimulationServer.serverGUI.primaryStage.getHeight() < ((Agent) pair.getValue()).pos.y) {
                            ((Agent) pair.getValue()).pos.y = 0;
                        } else if (0 > ((Agent) pair.getValue()).pos.y) {
                            /* Set new position - window border */
                            ((Agent) pair.getValue()).pos.y = SimulationServer.serverGUI.primaryStage.getHeight()-39;
                        }


                    /* Update ServerGUI */
                        SimulationServer.serverGUI.addMembers(((Agent) pair.getValue()));
                    }

                    /* Reset our queue */
                    queue.clear();
                }

            }
        }
        catch (InterruptedException e) {
            /* Move on */
            e.printStackTrace();
        }
//        } catch (IOException e) {
//            /* Move on */
//            e.printStackTrace();
//        }

    }

}
