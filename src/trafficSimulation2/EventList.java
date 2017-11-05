package trafficSimulation2;

import java.util.*;

public class EventList  {
	
	private static PriorityQueue<Event> eq = new PriorityQueue<Event>(11,new Comparator<Event>() {
											public int compare(Event e1 , Event e2) {
												return e1.compareTo(e2);
											}
		
									});
	public static void queue(Event e) {
		eq.add(e);
	}
	public static Event getEvent() {
		
		Event e = eq.peek();
		return e;
	}
	public static void dequeue() {
		eq.remove();
	}
}
