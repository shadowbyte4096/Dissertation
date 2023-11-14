package Sim.Rules;

import java.util.List;

import Sim.Boid.Boid;
import Sim.Vector;

public class AlignmentRule implements IRule {
	
	public AlignmentRule(Double Weight) {
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
            	steering.add(other.velocityX, other.velocityY);
            }
        	
            steering.divide(count);
            steering.normalize();
            steering.multiply(Boid.MAX_SPEED);
            steering.subtract(boid.velocityX, boid.velocityY);
            steering.limit(Boid.MAX_FORCE);
        }

        return steering;
	}
}
