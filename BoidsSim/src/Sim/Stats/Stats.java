package Sim.Stats;

public class Stats {
	public double visibleSize;
	public double neighbourhoodSize;
	public double collisionCount;
	public double cohesion;
	public double alignment;
	public double separation;
	
	public void addStats(Stats other) {
		alignment += other.alignment;
		cohesion += other.cohesion;
		collisionCount += other.collisionCount;
		neighbourhoodSize += other.neighbourhoodSize;
		separation += other.separation;
		visibleSize += other.visibleSize;
	}
	
	public void divideBy(int amount) {
		alignment /= amount;
		cohesion /= amount;
		collisionCount /= amount;
		neighbourhoodSize /= amount;
		separation /= amount;
		visibleSize /= amount;
	}
}
