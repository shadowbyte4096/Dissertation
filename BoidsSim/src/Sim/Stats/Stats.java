package Sim.Stats;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
	public Vector position = new Vector();
	public double neighbourhoodUniformity;
	
	public void globalise(List<Stats> stats) {
		double mean = 0;
		double sumSqDiff  = 0;
		double counter = 0;
		for (Stats other : stats){
			position.add(other.position);
    		velocity.add(other.velocity);
    		
    		counter++;
            double delta = other.neighbourhoodSize - mean;
            mean += delta / counter;
            double delta2 = other.neighbourhoodSize - mean;
            sumSqDiff += delta * delta2;
    	}
		velocity.divide(stats.size());
		position.divide(stats.size());
		velocity.normalize();
		
		double stdev = Math.sqrt(sumSqDiff / (counter - 1));
		neighbourhoodUniformity = 1 - (stdev/mean);
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
	
	public void print(int frame, String filename) {
		Boolean append = frame == 1 ? false : true;
        String csvFile = filename + ".csv";
        try (FileWriter fileWriter = new FileWriter(csvFile, append);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
        	if (frame == 1) {
                printWriter.println("Frame,alignment,cohesion,separation,collisionCount,visibleSize,neighbourhoodUniformity,polerization");
        	}
            printWriter.println(String.format("%d,%f,%f,%f,%f,%f,%f,%f", frame,alignment,cohesion,separation,collisionCount,visibleSize,neighbourhoodUniformity,polerization));
        } catch (IOException e) {
            System.out.println("An error occurred while creating the CSV file.");
            e.printStackTrace();
        }
	}
}
