package Sim.Rules;

import java.util.List;
import Sim.Vector;
import Sim.Boid.Boid;

public interface IRule {
	
	public Double GetWeight();
	
	public Vector Calculate(Boid boid, List<Boid> boids);
}
