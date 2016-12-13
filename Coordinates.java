package Project;

import java.io.Serializable;

/**
 * Represent the coordinates our agent & non-agent can have.
 * Ex: Position and Direction (Velocity).
 * **/
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

    public void times(double x, double y) {
        this.x *= x;
        this.y *= y;
    }

    /* Calculates the angle v between our coordinates */
    public double getV() {
        return Math.atan2(y,x);
    }

    public boolean isZero() {return (x == 0 && y == 0) ? true : false;}

    public double xSquared(){return x*x;}
    public double ySquared(){return y*y;}
    public double xySquared(){return xSquared()+ySquared();}

    public void normalize() {
        double length = Math.sqrt(xySquared());
        if(length > 0) {
            this.x = this.x / length;
            this.y = this.y / length;
        }
    }

}
