package Sim.SearchFilters;

import java.util.ArrayList;
import java.util.List;

import Sim.Boid.Boid;
import Sim.BoidSimulationEnvironment;

public class NeighborhoodFilter implements IFilter{
	
	@Override
	public List<Boid> filter(Boid boid, List<Boid> others) {
		List<Boid> visibleBoids = new ArrayList<>();

        for (Boid other : others) {
            double distance = boid.distanceTo(other);
            
            if (distance == 0 || distance > Boid.NEIGHBORHOOD_RADIUS) {
                continue;
            }

            visibleBoids.add(other);
        }

        return visibleBoids;
	}

	
}
