package trafficSimulation2;

import gridConfiguration.GridConfig;
import java.util.*;

public class EventPerformer {
	
	public EventScheduler es = new EventScheduler();
	public void performArrival(Event e) {
		
		CarState c = new CarState(Simulator.current_cars,e.getTime());
		//System.exit(0);
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		System.out.println("Car " + c.carID + " has arrived in the grid at Simulation clock = " + Simulator.clock);
		//List<Integer> path = c.roadGenerator();
		c.moveCar();
		Simulator.current_cars = Simulator.current_cars + 1;
		es.scheduleNextArrival(last_event_time);
		//c.moveCar();
		return;
	}
	public void greenToYellow(Event e) {
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
					if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
						continue;
					}
					if(Simulator.tl[i][j][0].get_current_light() == "green") {
						Simulator.tl[i][j][0].set_new_light("yellow");
						System.out.println("The light at " + i + " , " + j + " Street just turned " + Simulator.tl[i][j][0].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
					else if(Simulator.tl[i][j][1].get_current_light() == "green") {
						Simulator.tl[i][j][1].set_new_light("yellow");
						System.out.println("The light at " + i + " , " + j + " Avenue just turned " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
			}
		}
		es.scheduleNextGreenToYellow(last_event_time);
		return;
	}
	public void yellowToRed(Event e) {
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
					if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
						continue;
					}
					if(Simulator.tl[i][j][0].get_current_light() == "yellow") {
						Simulator.tl[i][j][0].set_new_light("red");
						System.out.println("The light at " + i + " , " + j + " Street just turned " + Simulator.tl[i][j][0].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
					else if(Simulator.tl[i][j][0].get_current_light() == "red") {
						Simulator.tl[i][j][0].set_new_light("green");
						System.out.println("The light at " + i + " , " + j + " Street just turned " + Simulator.tl[i][j][0].get_current_light() + " at Simulation clock = " + Simulator.clock);
						//if (i % 2 !=0) {
							//Simulator.rc[i][j+1][i][j].
						//}
					}
					if(Simulator.tl[i][j][1].get_current_light() == "yellow") {
						Simulator.tl[i][j][1].set_new_light("red");
						System.out.println("The light at " + i + " , " + j + " Avenue just turned " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
					else if(Simulator.tl[i][j][1].get_current_light() == "red") {
						Simulator.tl[i][j][1].set_new_light("green");
						System.out.println("The light at " + i + " , " + j + " Avenue just turned " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
			}
		}
		es.scheduleNextYellowToRed(last_event_time);
		return;	
	}
	
}
