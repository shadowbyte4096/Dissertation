package Sim;

import javax.swing.JFrame;

public class BoidSim {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Boid Simulation");
        BoidSimulationEnvironment environment = new BoidSimulationEnvironment();
        frame.add(environment);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {
            environment.update();
            environment.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}