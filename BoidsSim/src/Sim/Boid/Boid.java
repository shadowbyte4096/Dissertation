package Sim.Boid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Sim.BoidSimulationEnvironment;
import Sim.Vector;
import Sim.Rules.IRule;
import Sim.SearchFilters.IFilter;
import Sim.SearchFilters.NeighborhoodFilter;
import Sim.Stats.Stats;

public class Boid {
	public static final double MAX_SPEED = 2.0;
    public static final double MAX_FORCE = 0.03;
    public static final double NEIGHBORHOOD_RADIUS = 100.0;
    public static final double SEPARATION_DISTANCE = 20.0;
    public static final double SIZE = 10.0;
	
	public double x;
	public double y;
    public double velocityX;
    public double velocityY;
    public Stats stats;
    
    public Boid(double x, double y, double velocityX, double velocityY, Boolean test) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void update(List<Boid> boids, List<IRule> rules, List<IFilter> filters) {
        
    	
    	List<Boid> unfilteredBoids = new ArrayList<>();
    	unfilteredBoids = boids;
    	for (IFilter filter : filters){
    		boids = filter.filter(this, boids);
    	}
    	
    	List<Boid> neighborhoodBoids = (new NeighborhoodFilter()).filter(this, unfilteredBoids);
    	collectStats(unfilteredBoids, neighborhoodBoids, boids);
    	
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

        System.out.println("Start:" + x + "," + y);
        
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
        System.out.println("End:" + x + "," + y);
    }
    
    public Vector FindLocalCoordinates(Boid other) {
    	double otherX = other.x;
        double otherY = other.y;

        if (Math.abs(otherX - x) > BoidSimulationEnvironment.WIDTH / 2) {
            otherX = otherX < x ? otherX + BoidSimulationEnvironment.WIDTH : otherX - BoidSimulationEnvironment.WIDTH;
        }
        if (Math.abs(otherY - y) > BoidSimulationEnvironment.HEIGHT / 2) {
            otherY = otherY < y ? otherY + BoidSimulationEnvironment.HEIGHT : otherY - BoidSimulationEnvironment.HEIGHT;
        }
		return new Vector(otherX, otherY);
    }
    
    public double distanceTo(Boid other) {
    	Vector local = FindLocalCoordinates(other);
        double diffX = Math.abs(x - local.x);
        double diffY = Math.abs(y - local.y);

        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        return distance;
    }
    
    private void collectStats(List<Boid> allBoids, List<Boid> neighbourhoodBoids, List<Boid> visibleboids) {
		stats = new Stats();
		stats.separation = NEIGHBORHOOD_RADIUS;
		stats.visibleSize = visibleboids.size();
		stats.neighbourhoodSize = neighbourhoodBoids.size();
		stats.velocity = new Vector (velocityX, velocityY);
		stats.velocity.normalize();
		for (Boid other : allBoids) {
            double distance = distanceTo(other);
            if (distance < Boid.SIZE && other != this) {
                stats.collisionCount++;
            }
            if (distance < stats.separation && other != this) {
            	stats.separation = distance;
            }
            stats.cohesion += distance;
            stats.alignment += Math.abs(Math.atan2(velocityY, velocityX) - Math.atan2(other.velocityY, other.velocityX));
        }
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