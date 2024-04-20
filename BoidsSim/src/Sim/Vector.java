package Sim;

public class Vector {
    public double x;
    public double y;

    public Vector() {
        this(0, 0);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }
    
    public void add(Vector other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }
    
    public void subtract(Vector other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public void divide(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
    }
    
    public double dotProduct(Vector other) {
    	double dp =  this.x * other.x;
    	dp += this.y * other.y;
    	return dp;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double magnitude = magnitude();
        if (magnitude != 0) {
            x /= magnitude;
            y /= magnitude;
        }
    }

    public void limit(double max) {
        double magnitude = magnitude();
        if (magnitude > max) {
            x = (x / magnitude) * max;
            y = (y / magnitude) * max;
        }
    }
}
