package Project;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Agent implements Serializable {

    public Coordinates pos = new Coordinates();
    public Coordinates velocity = new Coordinates();
    private int sight = 0; // How far the agent can see in all directions

    public ArrayList<Agent> agents;

    public Agent() {
        Random rnd = new Random();
        this.pos.set(rnd.nextInt(500+1),rnd.nextInt(500+1));
        this.velocity.set(((double)(rnd.nextInt(200)-100))/100.0, ((double)(rnd.nextInt(200)-100))/100);
    }


    public void setSight(int s){this.sight = s;}
    public int getSight(){return this.sight;}


    /** Balance up our agent so its the center of the environment, relative to its neighbours **/
    private void balance() {

        Agent a = this;

        if(a.agents != null) {
            for (Agent neighbour : a.agents
                    ) {
                neighbour.pos.add(-a.pos.x,-a.pos.y);
//                neighbour.velocity.add(-a.velocity.x,-a.velocity.y);
            }
        }

        /* Set default values */
        a.pos.set(0,0);
//        a.velocity.set(0,0);
    }




    /** Calculates the movement of our agent, by BOIDs algorithm.
     ** this should only be done with the agents in sight.
     **/
    public void calculateMovement() {
        Agent a = this;

        Coordinates oldVelocity = new Coordinates(a.velocity.x,a.velocity.y);

        balance();

        // Default orientations
        Coordinates v1,v2,v3;


        /* Integers setting the weight of a function, number between -1 -> 1 */
        double f1, f2, f3, speed;
        f1 = 0.5;
        f2 = 1.2;
        f3 = 1;
        speed = 2;

        /* Calculate the different rules */
        if(a.agents != null && a.agents.size() > 0) {
            v1 = cohesion(a);
            v2 = separation(a);
            v3 = alignment(a);
            a.velocity.add((v1.x * f1 + v2.x * f2 + v3.x * f3), (v1.y * f1 + v2.y * f2 + v3.y * f3));
            a.velocity.set(a.velocity.x*speed, a.velocity.y*speed);
        }

        a.velocity.normalize();

        a.pos.add(a.velocity.x, a.velocity.y);


        if(a.velocity.isZero()) {
            a.pos.add(oldVelocity.x,oldVelocity.y);
        }
//        System.out.println("Vel: " + a.velocity.x + " :: " + a.velocity.y);

    }

    /** Calculate center of the mass of neighbour agents, not including self
     **
     * **/
    private Coordinates cohesion(Agent self) {
        Coordinates coord = new Coordinates();

        for (Agent a : self.agents) {
            if(a != self) {
                coord.add(a.pos.x,a.pos.y);
            }
        }

        coord.x = coord.x / (self.agents.size());
        coord.y = coord.y / (self.agents.size());

        coord.add(-self.pos.x, -self.pos.y);


        double maxDistanceToNeighbour = 75;
        //coord.set(coord.x/maxDistanceToNeighbour,coord.y/maxDistanceToNeighbour);

        System.out.println("Cohesion: " + coord.x + " :: " + coord.y);

        coord.normalize();
        return coord;
    }

    /** Avoid agents to collide **/
    private Coordinates separation(Agent self) {
        Coordinates coord = new Coordinates();

        double standardShape = 5.0;
        double unit = standardShape + 5.0; // This should be the same as our shape radius plus minimum space
        double shapeRed = standardShape*standardShape;
        double radius = unit * unit;

        for (Agent a : self.agents) {
            if(a != self) {

                if(a.pos.xySquared()-shapeRed <= radius) {
                    coord.add(a.pos.x-self.pos.x,a.pos.y-a.pos.y);
                }
            }
        }

        coord.set(coord.x/agents.size(), coord.y/agents.size());
        coord.set(coord.x*-1, coord.y*-1);
        coord.normalize();
        System.out.println("Separation: " + coord.x + " :: " + coord.y);
        return coord;
    }

    /** Match velocity with near agents, not including self **/
    private Coordinates alignment(Agent self) {
        Coordinates coord = new Coordinates();

        for (Agent a : self.agents) {
            if(a != self) {
                coord.add(a.velocity.x,a.velocity.y);
            }
        }

        coord.set(coord.x/self.agents.size(),coord.y/self.agents.size());
        coord.normalize();
        //coord.set((coord.x - self.velocity.x) / 20, (coord.y - self.velocity.y) / 20);


        System.out.println("Alignment: " + coord.x + " :: " + coord.y);
        return coord;
    }

}
