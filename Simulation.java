package Project;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Markus on 2016-12-09.
 */
public class Simulation extends Thread {

    private ConcurrentHashMap<ClientHandler, Agent> clients = new ConcurrentHashMap<ClientHandler,Agent>();
    private ConcurrentHashMap<ClientHandler, String> queue = new ConcurrentHashMap<>();
    private int clientSize;

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

    public void setClientSize(int i) {this.clientSize = i;}

    @Override
    public void run() {
        try{
            while(true){

                Thread.sleep(100);

               this.clientSize = clients.size();

                /* Simple print to see our iterations */
                System.out.println("##################");

                /* Tell our clients to send data */
                Iterator it = clients.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry pair = (Map.Entry) it.next();
                    Agent agent = ((Agent)pair.getValue());
                    System.out.println("Agent hashmap " + agent.pos.x + " :: " + agent.pos.y);
                    agent.agents = ((Agent)pair.getValue()).agents;
                    ((ClientHandler)pair.getKey()).sendData(new Data("getData",agent));
                    System.out.println("getData Sent for clientThread" +   ((ClientHandler)pair.getKey()).clientNumber );
                }

                /* Wait for all clients to send their data */
                while (queue.size() != clientSize) {}

                /* Clear ServerGUI */
                SimulationServer.serverGUI.clearScreen();

                /* Check neighbours for clients */
                it = clients.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry pair = (Map.Entry) it.next();
                   ((Agent)pair.getValue()).agents = ((ClientHandler)pair.getKey()).checkNeighbours();
                    System.out.println("neighbours checked for clientThread" +   ((ClientHandler)pair.getKey()).clientNumber + " size: " +  ((Agent)pair.getValue()).agents.size() );

                    /* Check screen coordinates */
                    if(SimulationServer.serverGUI.primaryStage.getWidth() < ((Agent)pair.getValue()).pos.x) {
                        ((Agent)pair.getValue()).pos.x = 0;
                    } else if(0 > ((Agent)pair.getValue()).pos.x) {
                        ((Agent)pair.getValue()).pos.x = SimulationServer.serverGUI.primaryStage.getWidth();
                    }

                    if(SimulationServer.serverGUI.primaryStage.getHeight() < ((Agent)pair.getValue()).pos.y) {
                        ((Agent)pair.getValue()).pos.y = 0;
                    } else if(0 > ((Agent)pair.getValue()).pos.y) {
                        ((Agent)pair.getValue()).pos.y = SimulationServer.serverGUI.primaryStage.getHeight();
                    }


                    /* Update ServerGUI */
                    SimulationServer.serverGUI.addMembers( ((Agent)pair.getValue()));
                }

                /* Reset our queue */
                queue.clear();

            }
        }
        catch (InterruptedException e) {
            /* Move on */
        } catch (IOException e) {
            /* Move on */
        }

    }

}
