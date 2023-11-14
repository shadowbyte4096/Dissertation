package Sim.Boid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import Sim.BoidSimulationEnvironment;
import Sim.Vector;
import Sim.Rules.IRule;
import Sim.SearchFilters.IFilter;

public class Boid {
	public static final double MAX_SPEED = 2.0;
    public static final double MAX_FORCE = 0.03;
    public static final double NEIGHBORHOOD_RADIUS = 50.0;
    public static final double SEPARATION_DISTANCE = 20.0;
    public static final double SIZE = 10.0;
	
	public double x;
	public double y;
    public double velocityX;
    public double velocityY;
    
    public Boid(double x, double y, double velocityX, double velocityY, Boolean test) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void update(List<Boid> boids, List<IRule> rules, List<IFilter> filters) {
        
    	for (IFilter filter : filters){
    		boids = filter.filter(this, boids);
    	}
    	
    	List<Vector> changes = new ArrayList<>();
    	for (IRule rule : rules) {
    		Vector change = rule.Calculate(this, boids);
    		change.multiply(rule.GetWeight());
    		changes.add(change);
    	}
    	
    	for (Vector change : changes) {
    		velocityX += change.x;
            velocityY += change.y;
    	}
    	
        double speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
        if (speed > MAX_SPEED) {
            velocityX = (velocityX / speed) * MAX_SPEED;
            velocityY = (velocityY / speed) * MAX_SPEED;
        }

        x += velocityX;
        y += velocityY;

        //wrap around
        if (x < 0) {
            x = BoidSimulationEnvironment.WIDTH;
        } else if (x > BoidSimulationEnvironment.WIDTH) {
            x = 0;
        }
        if (y < 0) {
            y = BoidSimulationEnvironment.HEIGHT;
        } else if (y > BoidSimulationEnvironment.HEIGHT) {
            y = 0;
        }
    }
    
    public double distanceTo(Boid other) {
        double diffX = Math.abs(x - other.x);
        double diffY = Math.abs(y - other.y);

        if (diffX > BoidSimulationEnvironment.WIDTH / 2) {
            diffX = BoidSimulationEnvironment.WIDTH - diffX;
        }
        if (diffY > BoidSimulationEnvironment.HEIGHT / 2) {
            diffY = BoidSimulationEnvironment.HEIGHT - diffY;
        }

        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        return distance;
    }

    public void draw(Graphics g) {
        double angle = Math.atan2(velocityY, velocityX);

        int[] xPoints = {
        		(int) (x + Math.cos(angle) * SIZE),
        		(int) (x + Math.cos(angle + 2 * Math.PI / 3) * SIZE),
        		(int) (x + Math.cos(angle + 4 * Math.PI / 3) * SIZE)};
        int[] yPoints = {
        		(int) (y + Math.sin(angle) * SIZE),
        		(int) (y + Math.sin(angle + 2 * Math.PI / 3) * SIZE),
        		(int) (y + Math.sin(angle + 4 * Math.PI / 3) * SIZE)};

        // Draw the triangle
        g.setColor(Color.BLACK);
        g.fillPolygon(xPoints, yPoints, 3);
    }
}