package trafficSimulation2;
import java.util.*;
public class EventScheduler {

	public static double interarrival_time;
	public static double lambda = 1;
	public static Random rand = new Random();
	public static EventScheduler es = new EventScheduler();
	public void scheduleNextArrival(double _last_event_time) {
		
		double last_event_time = _last_event_time;
		interarrival_time = Math.ceil(-Math.log(1-rand.nextFloat())/lambda);
		//System.out.println("last event time " + last_event_time);
		//System.out.println("interarrival time = " + interarrival_time);
		Event e = new Event(1,last_event_time + interarrival_time);
		EventList.queue(e);
		return;
	}
	public void scheduleNextGreenToYellow(double _last_event_time) {
		double last_event_time = _last_event_time;
		Event e = new Event(2,last_event_time + TrafficLightsState.red_time);
		EventList.queue(e);
		return;
	}
	public void scheduleNextYellowToRed(double _last_event_time) {
		double last_event_time = _last_event_time;
		Event e = new Event(3,last_event_time + TrafficLightsState.green_time + TrafficLightsState.yellow_time);
		EventList.queue(e);
		return;
	}
	
	/*
	public void scheduleCarMovement(CarState c) {
		int turn = c.path.remove(2);
		while(c.distance_covered != c.total_distance) {
			for(int i = 0; i < c.path.size(); i=i+2) {
				Simulator.rc[c.path.get(i-2)][c.path.get(i-1)][c.path.get(i)][c.path.get(i+1)].setLane(turn);
				c.dist = Simulator.rc[c.path.get(i-2)][c.path.get(i-1)][c.path.get(i)][c.path.get(i+1)].getCurrentCapacity(); 
				 
				while(c.distance_covered!=CarState.block_size) {
					if(c.dist >= (CarState.dist_acc + CarState.dist_dec)) {
						c.time = 0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
						Event e = new Event(4, c.last_movement_time + c.time , c);
						EventList.queue(e);
					}
					else if (c.dist < (CarState.dist_acc + CarState.dist_dec)) {
						c.time = Math.sqrt(2*c.dist/CarState.acceleration) ; 
						Event e = new Event(4 , c.last_movement_time + c.time , c);
						EventList.queue(e);
					}
					c.dist = Simulator.rc[c.path.get(i-2)][c.path.get(i-1)][c.path.get(i)][c.path.get(i+1)].getCurrentCapacity() - c.dist;
				es.scheduleCarQueuing(c);
				Simulator.rc[c.path.get(i-2)][c.path.get(i-1)][c.path.get(i)][c.path.get(i+1)].addCarToLane(c);
				
				if(c.path.get(i-2) != c.path.get(i)) {
					// Its a change in street no. hence check avenue light
					if(Simulator.tl[c.path.get(i)][c.path.get(i+1)][1].get_current_light() == "green") {
						CarState c1 =Simulator.rc[c.path.get(i-2)][c.path.get(i-1)][c.path.get(i)][c.path.get(i+1)].removeCarFromLane();
						System.out.println("Car " + c1.carID + " found a green light at " + c.path.get(i) + " , " + c.path.get(i+1) + " and has left the intersection at " + Simulator.clock);
					}
				}
				else if (c.path.get(i-1) != c.path.get(i+1)) {
					//Its a change in avenue no. hence check street light 
					if(Simulator.tl[c.path.get(i)][c.path.get(i+1)][0].get_current_light() == "green") {
						CarState c1 =Simulator.rc[c.path.get(i-2)][c.path.get(i-1)][c.path.get(i)][c.path.get(i+1)].removeCarFromLane();
						System.out.println("Car " + c1.carID + " found a green light at " + c.path.get(i) + " , " + c.path.get(i+1) + " and has left the intersection at " + Simulator.clock);
					}
				}
			}
		}
	}
	*/
	public void scheduleCarQueuing(CarState c) {
		//System.out.println("Now queuing");
		if (c.no_of_turns == 0) {
			//System.out.println("no_of_turns " + c.no_of_turns);
			c.setCurrentRoad();
			c.setCurrentLane(c.lane_identity[0]);
			Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].setLane(c.current_lane);
			//System.out.println(c.lane_identity[0]);
			//System.exit(0);
			if(c.end_time == c.start_time) {
				//System.out.println("inside if");
				int capacity = Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].getCurrentCapacity();
				c.dist = capacity;	
				//System.out.println(c.dist);
				if(capacity == CarState.block_size) {
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
					double time_taken_for_movement = 0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					Event e = new Event(4,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement ,c);
					EventList.queue(e);
				}
				else if(capacity < CarState.block_size && capacity > (CarState.dist_acc + CarState.dist_dec)) {
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
					double time_taken_for_movement =  0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					// Now check here if c
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement , c);
					EventList.queue(e);
				}
				else if(capacity < CarState.block_size && capacity < (CarState.dist_acc + CarState.dist_dec)) {
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
					double time_taken_for_movement =  Math.sqrt(2 * c.dist/CarState.acceleration);
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time +time_taken_for_movement , c);
					EventList.queue(e);
				}
			}
			else if (c.end_time > c.start_time) {
				int capacity = Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].getCurrentCapacity();
				c.dist = capacity;	
				//System.out.println(c.dist);
				if(capacity == CarState.block_size) {
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
					double time_taken_for_movement = 0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					Event e = new Event(4,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement ,c);
					EventList.queue(e);
				}
				else if(capacity < CarState.block_size && capacity > (CarState.dist_acc + CarState.dist_dec)) {
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
					double time_taken_for_movement =  0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement , c);
					EventList.queue(e);
				}
				else if(capacity < (CarState.dist_acc + CarState.dist_dec)) {
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
					double time_taken_for_movement =  Math.sqrt(2 * c.dist/CarState.acceleration);
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement , c);
					EventList.queue(e);
				}
			}
		}
	}
	public void scheduleCarDeque(double _last_event_time,int _i, int _j, int _x, int _y , int _rt) {
		
		double last_event_time = _last_event_time;
		int i = _i;
		int j = _j;
		int x = _x;
		int y = _y;
		int road = _rt;
		Event e = new Event(6,last_event_time,i,j,x,y,road);
		EventList.queue(e);
		return;
	}
	public void scheduleExit(CarState c) {
		Event e = new Event(7,Simulator.clock,c);
		EventList.queue(e);
	}
}
