package Sim;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

public class BoidSimulationEnvironment extends JPanel {
	public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final int NUM_BOIDS = 50;

    private static final double ALIGNMENT_WEIGHT = 1;
    private static final double COHESION_WEIGHT = 1;
    private static final double SEPARATION_WEIGHT = 2;
    private static final double FOV_FILTER_ANGLE = 135.0;
	
    private List<Boid> boids;
    private List<IRule> rules;
    private List<IFilter> searchFilters;

    public BoidSimulationEnvironment() {
        boids = new ArrayList<>();
        
        Random random = new Random();
        Boolean test = random.nextBoolean();
        random = new Random(1);
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
        
        searchFilters = new ArrayList<>();
        searchFilters.add(new NeighborhoodFilter());
        searchFilters.add(new FovFilter(FOV_FILTER_ANGLE));
        searchFilters.add(new ObstructedVisionFilter());
        
    }

    public void update() {
        for (Boid boid : boids) {
            boid.update(boids, rules, searchFilters);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Boid boid : boids) {
            boid.draw(g);
        }
    }
}