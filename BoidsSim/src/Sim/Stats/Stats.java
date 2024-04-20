package Sim.Stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import Sim.Vector;
import Sim.Boid.Boid;
import Sim.SearchFilters.IFilter;

public class Stats {
	public double visibleSize;
	public double neighbourhoodSize;
	public double collisionCount;
	public double cohesion;
	public double alignment;
	public double separation;
	
	public double polerization;
	public Vector velocity = new Vector();
	
	public void globalise(List<Stats> stats) {
		for (Stats other : stats){
    		velocity.add(other.velocity);
    	}
		velocity.divide(stats.size());
		velocity.normalize();
	}
	
	public void addStats(List<Stats> stats) {
		for (Stats other : stats){
			alignment += other.alignment;
			cohesion += other.cohesion;
			collisionCount += other.collisionCount;
			neighbourhoodSize += other.neighbourhoodSize;
			separation += other.separation;
			visibleSize += other.visibleSize;
			polerization += velocity.dotProduct(other.velocity);
		}
	}
	
	public void divideBy(int amount) {
		alignment /= amount;
		cohesion /= amount;
		//collisionCount /= amount;
		neighbourhoodSize /= amount;
		separation /= amount;
		visibleSize /= amount;
		polerization /= amount;
	}
	
	public void print(int frame) {
		Boolean append = frame == 1 ? false : true;
        String csvFile = "stats.csv";
        try (FileWriter fileWriter = new FileWriter(csvFile, append);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
        	if (frame == 1) {
                printWriter.println("Frame,alignment,cohesion,collisionCount,neighbourhoodSize,separation,visibleSize,polerization");
        	}
            printWriter.println(String.format("%d,%f,%f,%f,%f,%f,%f,%f", frame,alignment,cohesion,collisionCount,neighbourhoodSize,separation,visibleSize,polerization));
        } catch (IOException e) {
            System.out.println("An error occurred while creating the CSV file.");
            e.printStackTrace();
        }
	}
}
