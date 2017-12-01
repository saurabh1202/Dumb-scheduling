package trafficSimulation2;
import java.util.*;

public class EventScheduler {

	public static double interarrival_time;
	public static double lambda ;
	public static Random rand = new Random();
	public static EventScheduler es = new EventScheduler();
	public void scheduleNextArrival(double _last_event_time) {
		
		double last_event_time = _last_event_time;
		//interarrival_time = Math.ceil(-Math.log(1-rand.nextFloat())/lambda);
		//interarrival_time = Math.round(a * 100)/100;
		//System.out.println("last event time " + last_event_time);
		//System.out.println("interarrival time = " + interarrival_time);
		//Event e = new Event(1,last_event_time + interarrival_time);
		//EventList.queue(e);
		//return;
		int j = 0;
		for (double i = 0.0; i != Simulator.max_simulation_time && j <= Simulator.no_of_cars ; i = i + last_event_time ) {
			
			interarrival_time = -1 * Math.log(1-rand.nextFloat())/lambda;
			System.out.println("interarrival time = " + interarrival_time);
			Event e = new Event(1,last_event_time + interarrival_time);
			EventList.queue(e);
			last_event_time = last_event_time + interarrival_time;
			System.out.println("last event time " + last_event_time);
			j++;
		}
		//System.out.println("total no. of cars scheduled in the grid is  " + (j - 1));
		//System.exit(0);
	}
	public void scheduleNextGreenToYellow(double _last_event_time , Event _e) {
		double last_event_time = _last_event_time;
		Event e = _e;
		Event e1 = new Event(9,last_event_time + TrafficLightsState.green_time,e.i,e.j,e.road);
		EventList.queue(e1);
		return;
	}
	public void scheduleNextYellowToRed(double _last_event_time, Event _e) {
		double last_event_time = _last_event_time;
		Event e = _e;
		Event e1 = new Event(10,last_event_time + TrafficLightsState.yellow_time,e.i,e.j,e.road);
		EventList.queue(e1);
		return;
	}
	public void scheduleNextRedToGreen(double _last_event_time, Event _e) {
		double last_event_time = _last_event_time;
		Event e = _e;
		Event e1 = new Event(11,last_event_time + TrafficLightsState.red_time,e.i,e.j,e.road);
		EventList.queue(e1);
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
	public void scheduleCarQueuing(CarState c) throws Exception{
		//System.out.println("Now scheduling Car " + c.carID);
		//if (c.no_of_turns == 0) {
			//System.out.println("no_of_turns " + c.no_of_turns);
		try {
			c.setCurrentRoad();
			//c.setCurrentLane(c.lane_identity[0]);
			//System.out.println("before setting lane");
			//System.out.println("now setting lane as " + c.current_lane);
			Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].setLane(c.current_lane);
			//System.out.println(Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].getLane());
			//System.out.println("after setting lane");
			//System.exit(0);
			if(c.end_time == c.start_time) {
				//System.out.println("inside if");
				//System.out.println("Now scheduling Car " + c.carID);
				int count = Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].getCount();
				int capacity = CarState.block_size - count;
				c.dist = capacity;	
				//System.out.println(c.dist);
				if(capacity == CarState.block_size) {					
					double time_taken_for_movement = 0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].incrementCount();
					Event e = new Event(4,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement ,c);
					EventList.queue(e);
				}
				else if(capacity < CarState.block_size && capacity > (CarState.dist_acc + CarState.dist_dec)) {					
					double time_taken_for_movement =  0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].incrementCount();
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement , c);
					EventList.queue(e);
				}
				else if(capacity < CarState.block_size && capacity < (CarState.dist_acc + CarState.dist_dec)) {
					double time_taken_for_movement =  Math.sqrt(2 * c.dist/CarState.acceleration);
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].incrementCount();
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time +time_taken_for_movement , c);
					EventList.queue(e);
				}
			}
			else if (c.end_time > c.start_time) {
				int count = Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].getCount();
				int capacity = CarState.block_size - count;
				c.dist = capacity;	
				//System.out.println(c.dist);
				if(capacity == CarState.block_size) {					
					double time_taken_for_movement = 0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].incrementCount();
					Event e = new Event(4,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement ,c);
					EventList.queue(e);
				}
				else if(capacity < CarState.block_size && capacity > (CarState.dist_acc + CarState.dist_dec)) {
					double time_taken_for_movement =  0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (c.dist/CarState.speed);
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].incrementCount();
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement , c);
					EventList.queue(e);
				}
				else if(capacity < (CarState.dist_acc + CarState.dist_dec)) {
					double time_taken_for_movement =  Math.sqrt(2 * c.dist/CarState.acceleration);
					Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].incrementCount();
					Event e = new Event(5,c.end_time + Simulator.clock - c.end_time + time_taken_for_movement , c);
					EventList.queue(e);
				}
			}
		}catch(Exception e) {
			
		}
	}
	public void scheduleCarDeque(double _last_event_time,int _i, int _j, int _x, int _y , int _rt) throws Exception{
		
		double last_event_time = _last_event_time;
		
		int i = _i;
		int j = _j;
		int x = _x;
		int y = _y;
		
		try{
			CarState c = Simulator.rc[i][j][x][y].removeCarFromLeftLane();
			if (c != null) {
				Simulator.rc[i][j][x][y].decrementLeftCount();
			}
			Event e = new Event(6,last_event_time,c);
			EventList.queue(e);
			for(CarState c1 : Simulator.rc[i][j][x][y].leftLane) {
				c1.distance_covered = c1.distance_covered + 1;
				c1.end_time = c1.end_time + Simulator.clock - c1.end_time + 1;
			}
		}catch(Exception e) {
			//System.out.println("No car found in the left lane");
		}
		try {
			CarState c = Simulator.rc[i][j][x][y].removeCarFromMiddleLane();
			if (c != null) {
				Simulator.rc[i][j][x][y].decrementMiddleCount();
			}
			Event e = new Event(6,last_event_time,c);
			EventList.queue(e);
			for(CarState c1 : Simulator.rc[i][j][x][y].middleLane) {
				c1.distance_covered = c1.distance_covered + 1;
				c1.end_time = c1.end_time + Simulator.clock - c1.end_time + 1;			
			}
		}catch(Exception e) {
			//System.out.println("No car found in the middle lane");
		}
		try {
			CarState c = Simulator.rc[i][j][x][y].removeCarFromRightLane();
			if (c != null) {
				Simulator.rc[i][j][x][y].decrementRightCount();
			}
			Event e = new Event(6,last_event_time,c);
			EventList.queue(e);
			for(CarState c1 : Simulator.rc[i][j][x][y].rightLane) {
				c1.distance_covered = c1.distance_covered + 1;
				c1.end_time = c1.end_time + Simulator.clock - c1.end_time + 1;
			}
		}catch(Exception e) {
			//System.out.println("No car found in the right lane");
		}
	}
	public void scheduleExit(CarState c) {
		double time_taken_for_movement = 0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (CarState.block_size/CarState.speed);
		c.end_time = c.end_time + time_taken_for_movement;
		Event e = new Event(7,Simulator.clock + time_taken_for_movement,c);		
		EventList.queue(e);
	}
}
