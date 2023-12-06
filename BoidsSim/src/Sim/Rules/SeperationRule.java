package Sim.Rules;

import java.util.List;

import Sim.Boid.Boid;
import Sim.BoidSimulationEnvironment;
import Sim.Vector;

public class SeperationRule implements IRule {
	
	public SeperationRule(Double Weight) {
		this.Weight = Weight;
	}
	
	private Double Weight;
	
	@Override
	public Double GetWeight() {
		return Weight;
	}
	
	@Override
	public Vector Calculate(Boid boid, List<Boid> boids) {
		Vector steering = new Vector();
		int count = 0;

        for (Boid other : boids) {
            double distance = boid.distanceTo(other);
            if (distance < Boid.SEPARATION_DISTANCE) {
            	Vector local = boid.FindLocalCoordinates(other);
                double diffX = boid.x - local.x;
                double diffY = boid.y - local.y;
                diffX /= distance;
                diffY /= distance;
                steering.add(diffX, diffY);
                count++;
            }
        }

        if (count > 0) {
            steering.divide(count);
            steering.normalize();
            steering.multiply(Boid.MAX_SPEED);
            steering.subtract(boid.velocityX, boid.velocityY);
            steering.limit(Boid.MAX_FORCE);
        }

        return steering;
	}

}
