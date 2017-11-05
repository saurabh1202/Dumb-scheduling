package trafficSimulation2;

public class EventPerformer {
	
	public EventScheduler es = new EventScheduler();
	public void performArrival(Event e) {
		
		CarState c = new CarState(Simulator.current_cars,e.getTime());
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		System.out.println("Car " + c.carID + " has arrived in the grid at Simulation clock = " + Simulator.clock);
		Simulator.current_cars = Simulator.current_cars + 1;
		es.scheduleNextArrival(last_event_time);
		return;
	}

}
