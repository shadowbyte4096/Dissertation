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
		List<Boid> visibleBoids = getVisibleBoids(map);
		
        return visibleBoids;
	}
	
	private List<Boid> getVisibleBoids(Map<Boid, BoidMeta> boidMetaMap) {
	    List<Boid> visibleBoids = new ArrayList<>();

	    for (Map.Entry<Boid, BoidMeta> entry : boidMetaMap.entrySet()) {
	        Boid canditate = entry.getKey();
	        BoidMeta canditateMeta = entry.getValue();

	        boolean isVisible = true;
	        List<double[]> coveredRanges = new ArrayList<>();
	        for (Map.Entry<Boid, BoidMeta> blockerEntry : boidMetaMap.entrySet()) {
	            Boid blocker = blockerEntry.getKey();
	            BoidMeta blockerBoidMeta = blockerEntry.getValue();

	            if (blocker != canditate && blockerBoidMeta.distance < canditateMeta.distance) {
	                coveredRanges.add(new double[]{blockerBoidMeta.left, blockerBoidMeta.right});
	            }
	        }

	        coveredRanges.sort(Comparator.comparingDouble(a -> a[0]));

	        Boolean rearrange = false;
	        if (canditateMeta.right > 330 || canditateMeta.left < 30) {
	        	rearrange = true;
	        	canditateMeta.left = rearrange(canditateMeta.left);
	        	canditateMeta.right = rearrange(canditateMeta.right);
	        }
	        
	        for (double[] range : coveredRanges) {
	        	double blockingLeft = rearrange ? rearrange(range[0]) : range[0];
	        	double blockingRight = rearrange ? rearrange(range[1]) : range[1];
	        	
	        	if (blockingLeft < canditateMeta.left && blockingRight > canditateMeta.right) {
	        		isVisible = false;
	        		break;
	        	}
	        	
	        	else if (canditateMeta.left > blockingLeft && canditateMeta.left < blockingRight) {
	        		canditateMeta.left = blockingRight;
	        		continue;
	        	}
	        	
	        	else if (canditateMeta.right > blockingLeft && canditateMeta.right < blockingRight) {
	        		canditateMeta.right = blockingLeft;
	        		continue;
	        	}
	        }

	        if (isVisible) {
	            visibleBoids.add(canditate);
	        }
	    }

	    return visibleBoids;
	}
	
	private Map<Boid, BoidMeta> createBoidMetaMap(Boid boid, List<Boid> others) {
	    Map<Boid, BoidMeta> boidMetaMap = new HashMap<>();

	    for (Boid other : others) {
	        double distance = boid.distanceTo(other);
	        double angleToOther = Math.atan2(other.y - boid.y, other.x - boid.x);

	        double angleDifference = Math.asin(Boid.SIZE / distance) / 2;

	        double left = angleToOther - angleDifference;
	        double right = angleToOther + angleDifference;

	        BoidMeta boidMeta = new BoidMeta(distance, left, right);
	        boidMetaMap.put(other, boidMeta);
	    }

	    return boidMetaMap;
	}
	
	//rearrange so that 180 and 540 are the wrap around points rather than 0 and 360
	private double rearrange(double angle) {
		if (angle < 180) {
            return angle + 360;
        }
        return angle;
	}

	
}
