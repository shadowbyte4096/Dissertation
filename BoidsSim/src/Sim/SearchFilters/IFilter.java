package Sim.SearchFilters;

import java.util.List;

import Sim.Boid.Boid;

public interface IFilter {

	List<Boid> filter(Boid boid, List<Boid> others);
}
