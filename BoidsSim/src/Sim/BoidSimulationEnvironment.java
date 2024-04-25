package Sim;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import Sim.Boid.Boid;
import Sim.Rules.AlignmentRule;
import Sim.Rules.CohesionRule;
import Sim.Rules.IRule;
import Sim.Rules.SeperationRule;
import Sim.SearchFilters.FovFilter;
import Sim.SearchFilters.IFilter;
import Sim.SearchFilters.NeighborhoodFilter;
import Sim.SearchFilters.ObstructedVisionFilter;
import Sim.Stats.Stats;

public class BoidSimulationEnvironment extends JPanel {
	public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final int NUM_BOIDS = 50;

    private static final double ALIGNMENT_WEIGHT = 1;
    private static final double COHESION_WEIGHT = 1;
    private static final double SEPARATION_WEIGHT = 1.5;
    //private static final double FOV_FILTER_ANGLE_BEHIND = 40/2;
    //private static final double FOV_FILTER_ANGLE_INFRONT = 5;
	
    private List<Boid> boids;
    private List<IRule> rules;
    private List<IFilter> searchFilters;
    
    private int frame = 7001;
    
    private List<List<IFilter>> configs;
    private List<String> configNames;
    private String statsFileName = "";

    public BoidSimulationEnvironment() {
    	
    	configs = new ArrayList<>();
    	configNames = new ArrayList<>();
    	
    	for (double angle=0; angle < 142/2; angle += 20/2) {
    		for (double i=1; i <= 5; i++) {
    			List<IFilter> filters = new ArrayList<>();
            	filters.add(new NeighborhoodFilter());
            	filters.add(new FovFilter(angle, false));
            	filters.add(new ObstructedVisionFilter());
            	configs.add(filters);
            	configNames.add("Stats-" + angle*2 + "-" + i);
    		}
    		
    	}
    }
    
    private void newRun(List<IFilter> filters) {
    	boids = new ArrayList<>();
        
        Random random = new Random();
        Boolean test = random.nextBoolean();
        //random = new Random(1);
        for (int i = 0; i < NUM_BOIDS; i++) {
            double x = random.nextDouble() * WIDTH;
            double y = random.nextDouble() * HEIGHT;
            double velocityX = random.nextDouble() * 2 - 1;
            double velocityY = random.nextDouble() * 2 - 1;
            boids.add(new Boid(x, y, velocityX, velocityY, test));
        }
        
        rules = new ArrayList<>();
        rules.add(new AlignmentRule(ALIGNMENT_WEIGHT));
        rules.add(new CohesionRule(COHESION_WEIGHT));
        rules.add(new SeperationRule(SEPARATION_WEIGHT));
        
        searchFilters = filters;
    }
    
    private boolean checkForNewConfig() {
    	if (frame > 7000) {
    		if (!configs.isEmpty()) {
    			newRun(configs.remove(0));
    			statsFileName = configNames.remove(0);
        		frame = 0;
        		return false;
    		}
    		else {
    			return true;
    		}
    	}
    	return false;
    	
    }

    public void update() {
    	if (!checkForNewConfig()) {
    		for (Boid boid : boids) {
                boid.update(boids, rules, searchFilters);
            }
            collectStats();
            frame += 1;
    	}
    }
    
    private void collectStats() {
    	Stats stats = new Stats();
    	
    	List<Stats> boidStats = boids.stream()
                .map(boid -> boid.stats)
                .collect(Collectors.toList());
    	
    	stats.globalise(boidStats);
    	stats.addStats(boidStats);
    	stats.divideBy(boidStats.size());
    	stats.print(frame, statsFileName);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Boid boid : boids) {
            boid.draw(g);
        }
    }
}