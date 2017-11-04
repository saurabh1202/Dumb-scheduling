package trafficSimulation2;

import java.util.*;

public class EventList  {
	
	private static PriorityQueue<Event> eq = new PriorityQueue<Event>(11,new Comparator<Event>() {
											public int compare(Event e1 , Event e2) {
												return e1.compareTo(e2);
											}
		
									});
	
	public static void schedule() {
		eq.queue()
	}
}
