package trafficSimulation2;
import java.util.*;
public class EventScheduler {

	public static double interarrival_time;
	public static double lambda = 1;
	public static Random rand = new Random();
	public void scheduleNextArrival(double _last_event_time) {
		
		double last_event_time = _last_event_time;
		interarrival_time = Math.ceil(-Math.log(1-rand.nextFloat())/lambda);
		System.out.println("last event time " + last_event_time);
		System.out.println("interarrival time = " + interarrival_time);
		Event e = new Event(1,last_event_time + interarrival_time);
		EventList.queue(e);
		return;
	}
	
}
