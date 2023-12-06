package Sim.Rules;

import java.util.List;
import Sim.Vector;
import Sim.Boid.Boid;

public class CohesionRule implements IRule {
	
	public CohesionRule(Double Weight) {
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
        int count = boids.size();

        if (count > 0) {
	        for (Boid other : boids) {
	        	Vector local = boid.FindLocalCoordinates(other);
	            steering.add(local.x, local.y);
	        }

            steering.divide(count);
            steering.subtract(boid.x, boid.y);
            steering.normalize();
            steering.multiply(Boid.MAX_SPEED);
            steering.subtract(boid.velocityX, boid.velocityY);
            steering.limit(Boid.MAX_FORCE);
        }

        return steering;
	}

}
