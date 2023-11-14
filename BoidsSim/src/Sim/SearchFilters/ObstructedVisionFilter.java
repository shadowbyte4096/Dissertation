package Sim.SearchFilters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Sim.Boid.Boid;
import Sim.Boid.BoidMeta;

public class ObstructedVisionFilter implements IFilter{

	@Override
	public List<Boid> filter(Boid boid, List<Boid> others) {
		Map<Boid, BoidMeta> map = createBoidMetaMap(boid, others);
		List<Boid> visibleBoids = getVisibleBoids(boid, map);
		
        return visibleBoids;
	}
	
	private List<Boid> getVisibleBoids(Boid boid, Map<Boid, BoidMeta> boidMetaMap) {
	    List<Boid> visibleBoids = new ArrayList<>();

	    for (Map.Entry<Boid, BoidMeta> entry : boidMetaMap.entrySet()) {
	        Boid other = entry.getKey();
	        BoidMeta boidMeta = entry.getValue();

	        boolean isVisible = true;
	        List<double[]> coveredRanges = new ArrayList<>();
	        for (Map.Entry<Boid, BoidMeta> anotherEntry : boidMetaMap.entrySet()) {
	            Boid another = anotherEntry.getKey();
	            BoidMeta anotherBoidMeta = anotherEntry.getValue();

	            if (another != other && anotherBoidMeta.distance < boidMeta.distance) {
	                coveredRanges.add(new double[]{anotherBoidMeta.angle1, anotherBoidMeta.angle2});
	            }
	        }

	        coveredRanges.sort(Comparator.comparingDouble(a -> a[0]));

	        double start = boidMeta.angle1;
	        for (double[] range : coveredRanges) {
	            if (range[0] <= start) {
	                start = Math.max(start, range[1]);
	            } else {
	                break;
	            }
	        }
	        if (start < boidMeta.angle2) {
	            isVisible = false;
	        }

	        if (isVisible) {
	            visibleBoids.add(other);
	        }
	    }

	    return visibleBoids;
	}
	
	private Map<Boid, BoidMeta> createBoidMetaMap(Boid boid, List<Boid> others) {
	    Map<Boid, BoidMeta> boidMetaMap = new HashMap<>();

	    for (Boid other : others) {
	        double distance = boid.distanceTo(other);
	        double angleToOther = Math.atan2(other.y - boid.y, other.x - boid.x);

	        double angleDifference = Math.asin(Boid.SIZE / distance);

	        double angle1 = angleToOther - angleDifference;
	        double angle2 = angleToOther + angleDifference;

	        BoidMeta boidMeta = new BoidMeta(distance, angle1, angle2);
	        boidMetaMap.put(other, boidMeta);
	    }

	    return boidMetaMap;
	}

	
}
