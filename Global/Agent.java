package Project.Global;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;


/**
 * The Agent class creates an agent, e.g a bird in a flock.
 * Agent class got methods to calculate the agents position and velocity.
 *
 * @author Markus Dybeck
 * @since 2016-12-06
 * @version 1.0
 */

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

    /** Balance up our agent so its the center of the environment, relative to its neighbors **/
    private void balance() {

        Agent a = this;

        if(a.agents != null) {
            for (Agent neighbor : a.agents
                    ) {
                neighbor.pos.add(-a.pos.x,-a.pos.y);
            }
        }
        /* Set default values */
        a.pos.set(0,0);
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


        /* Integers setting the weight of a function */
        double f1, f2, f3, max_speed;
        f1 = 1;
        f2 = 2;
        f3 = 1;
        max_speed = 0.15;

        /* Calculate the different rules */
        if(a.agents != null && a.agents.size() > 0) {
            v1 = cohesion(a, max_speed);
            v2 = separation(a);
            v3 = alignment(a, max_speed);
            a.velocity.add((v1.x * f1 + v2.x * f2 + v3.x * f3), (v1.y * f1 + v2.y * f2 + v3.y * f3));
        }

        a.velocity.normalize();

        a.pos.add(a.velocity.x, a.velocity.y);

        if(a.velocity.isZero()) {
            a.pos.add(oldVelocity.x,oldVelocity.y);
        }
    }

    /** Calculate center of the mass of neighbor agents, not including self **/
    private Coordinates cohesion(Agent self, double max_speed) {
        Coordinates coord = new Coordinates();

        for (Agent a : self.agents) {
            if(a != self) {
                coord.add(a.pos.x,a.pos.y);
            }
        }

        coord.divideAll(self.agents.size());

        /*Ease it out*/
        if(coord.length() > 0 ) {
            coord.normalize();

            coord.multiplyXY(max_speed);
            coord.add(-self.velocity.x, -self.velocity.y);
            if(coord.length() > max_speed) {
                coord.normalize();
                coord.multiplyXY(max_speed);
            }

        }


        System.out.println("Cohesion: " + coord.x + " :: " + coord.y);
        return coord;
    }

    /** Avoid agents to collide **/
    private Coordinates separation(Agent self) {
        Coordinates coord = new Coordinates();

        double standardShape = 5.0;

        for (Agent a : self.agents) {
            if(a != self) {
                if(a.pos.length() <= 15) {
                   System.out.println("Length: " + a.pos.length());
                    a.pos.normalize();
                    a.pos.divideAll(a.pos.length());
                    coord.add(-(a.pos.x),-(a.pos.y));
                }
            }
        }
        coord.divideAll(agents.size());
        System.out.println("Separation: " + coord.x + " :: " + coord.y);
        return coord;
    }

    /** Match velocity with near agents, not including self **/
    private Coordinates alignment(Agent self, double max_speed) {
        Coordinates coord = new Coordinates();

        for (Agent a : self.agents) {
            if(a != self) {
                coord.add(a.velocity.x,a.velocity.y);
            }
        }

        coord.set(coord.x/self.agents.size(),coord.y/self.agents.size());

        if(coord.length() > max_speed) {
            coord.normalize();
            coord.multiplyXY(max_speed);
        }
        System.out.println("Alignment: " + coord.x + " :: " + coord.y);
        return coord;
    }

}
