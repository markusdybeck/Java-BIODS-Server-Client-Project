package Project.Global;

import java.io.Serializable;

/**
 * The Coordinates class represent values and methods for an agent
 * e.g Velocity or Position.
 * Implements class Serializable so it can be sent with ObjectStreamer.
 *
 * @author Markus Dybeck
 * @since 2016-12-06
 * @version 1.0
 */

public final class Coordinates implements Serializable {
    public double x = 0; // Position x value in 2D map.
    public double y = 0; // Position y value in 2D map.

    // Default constructor
    public Coordinates(){}

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void multiplyXY(double m) {
        this.x *= m;
        this.y *= m;
    }

    /* Calculates the angle v between our coordinates */
    public double getV() {
        return Math.atan2(y,x);
    }

    public boolean isZero() {return (x == 0 && y == 0) ? true : false;}

    public double xSquared(){return x*x;}
    public double ySquared(){return y*y;}
    public double xySquared(){return xSquared()+ySquared();}
    public double length(){return Math.sqrt(xySquared());}
    public void divideAll(double d) {
        if(d > 0) {
            this.x/=d;
            this.y/=d;
        }
    }

    public void normalize() {
        double length = this.length();
        if(length > 0) {
            this.x = this.x / length;
            this.y = this.y / length;
        }
    }

}
