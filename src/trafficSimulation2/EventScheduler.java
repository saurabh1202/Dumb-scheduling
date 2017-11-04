package trafficSimulation2;

public class EventScheduler {
	private int type ;
	public EventScheduler(Event e) {
		type = e.getType();
		if (type == 1) {
			scheduleCarArrival();
		}
	}
	public static double scheduleCarArrival() {
		
		for(int i = 1 ; i < Simulator.no_of_cars ; i ++) {
			
			CarState c = new CarState();
		}
	}
}
