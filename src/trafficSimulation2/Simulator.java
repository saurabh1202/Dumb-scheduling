package trafficSimulation2;
import java.util.*;

public class Simulator {
	
	public static double clock = 0.0;
	public static EventPerformer ep = new EventPerformer();
	public static int no_of_cars = 100;
	public static int current_cars = 1;
	public static double max_simulation_time = 1000;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		initializeSimulation();
	}
	public static void initializeSimulation(){
		
		Event e = new Event(1,3);             	
		EventList.queue(e);	//Schedule first car arrival
		System.out.println("Simulation started");
		startSimulation();
	}
	public static void startSimulation() {
	
		while(clock < max_simulation_time && current_cars <= no_of_cars) {
			
			Event e = EventList.getEvent();
			EventList.dequeue();
			if(e.getType()== 1) {
				ep.performArrival(e);
			}
		}
	}
}
