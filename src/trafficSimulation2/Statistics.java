package trafficSimulation2;

import java.util.*;

public class Statistics {
	public static double total_transition_time = 0;
	public static double total_waiting_time = 0;
	public static ArrayList<CarState> car_record = new ArrayList<CarState>();
	public static Iterator<CarState> itr;
	public static double average_transition_time;
	public static double average_waiting_time;
	public static double calAverageTimeinSystem() {
		itr = car_record.iterator();
		int i = 0;
		while(itr.hasNext()) {
			CarState c = itr.next();
			total_transition_time = total_transition_time + c.time_in_system;
			i++;
		}	
		//System.out.println(i);
		average_transition_time = total_transition_time/i ;
		return average_transition_time;
	}
	public static double calAverageWaitingTime() {
		itr = car_record.iterator();
		int i = 0;
		while(itr.hasNext()) {
			CarState c = itr.next();
			total_waiting_time = total_waiting_time + c.total_waiting_time;
			i++;
		}
		//System.out.println(i);
		average_waiting_time = total_waiting_time/i ;
		return average_waiting_time;

	}
	
}
