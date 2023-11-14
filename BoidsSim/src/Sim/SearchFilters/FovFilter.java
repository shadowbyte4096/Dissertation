package Sim.SearchFilters;

import java.util.ArrayList;
import java.util.List;

import Sim.Boid.Boid;
import Sim.BoidSimulationEnvironment;

public class FovFilter implements IFilter{

	private double searchAngle;
	
	public FovFilter() {
		this(135); //default search angle
	}
	
	public FovFilter(double angle) {
		searchAngle = angle;
	}
	
	@Override
	public List<Boid> filter(Boid boid, List<Boid> others) {
		List<Boid> visibleBoids = new ArrayList<>();
        double boidDirection = Math.atan2(boid.velocityY, boid.velocityX);

        for (Boid other : others) { 
            double dx = other.x - boid.x;
            double dy = other.y - boid.y;

            // Adjust the difference in coordinates for wrap-around
            if (Math.abs(dx) > BoidSimulationEnvironment.WIDTH / 2) {
                dx = dx < 0 ? dx - BoidSimulationEnvironment.WIDTH : dx + BoidSimulationEnvironment.WIDTH;
            }
            if (Math.abs(dy) > BoidSimulationEnvironment.HEIGHT / 2) {
                dy = dy < 0 ? dy - BoidSimulationEnvironment.HEIGHT : dy + BoidSimulationEnvironment.HEIGHT;
            }

            double angleToOther = Math.atan2(dy, dx);
            double angleDifference = angleToOther - boidDirection;

            angleDifference = ((angleDifference + Math.PI) % (2 * Math.PI));
            if (angleDifference < 0)
                angleDifference += 2 * Math.PI;
            angleDifference -= Math.PI;
            
            if (Math.abs(angleDifference) > searchAngle * (Math.PI/180)) {
            	continue;
            }

            visibleBoids.add(other);
        }

        return visibleBoids;
	}

	
}
