package trafficSimulation2;

import gridConfiguration.GridConfig;
import java.util.*;

public class EventPerformer {
	
	public static int total_cars_exited = 0;
	public EventScheduler es = new EventScheduler();
	public void performArrival(Event e) throws Exception {
		
		CarState c = new CarState(Simulator.current_cars,e.getTime());
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		System.out.println("Car " + c.carID + " has arrived in the grid at Simulation clock = " + Simulator.clock);
		//es.scheduleNextArrival(last_event_time);
		es.scheduleCarQueuing(c);
		Simulator.current_cars = Simulator.current_cars + 1;
		return;
	}
	/*
	public void greenToYellow(Event e) {
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
					if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
						continue;
					}
					if(Simulator.tl[i][j][0].get_current_light() == "green") {
						//Simulator.tl[i][j][0].set_new_light("yellow");
						Event e1 = new Event(9,Simulator.clock,i,j,0);
						EventList.queue(e1);						
					}
					else if(Simulator.tl[i][j][1].get_current_light() == "green") {
						//Simulator.tl[i][j][1].set_new_light("yellow");
						Event e1 = new Event(9,Simulator.clock,i,j,1);
						EventList.queue(e1);						
					}
			}
		}
		es.scheduleNextGreenToYellow(last_event_time);
		return;
	}
	public void yellowToRed(Event e) throws Exception{
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
					if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
						continue;
					}
					if(Simulator.tl[i][j][0].get_current_light() == "yellow") {
						//Simulator.tl[i][j][0].set_new_light("red");
						Event e1 = new Event(10,Simulator.clock,i,j,0);
						EventList.queue(e1);
						
					}
					else if(Simulator.tl[i][j][0].get_current_light() == "red") {
						//Simulator.tl[i][j][0].set_new_light("green");
						Event e1 = new Event(11,Simulator.clock,i,j,0);
						EventList.queue(e1);
					}
					if(Simulator.tl[i][j][1].get_current_light() == "yellow") {
						Simulator.tl[i][j][1].set_new_light("red");
						System.out.println("The light at " + i + " , " + j + " Avenue is now " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
					else if(Simulator.tl[i][j][1].get_current_light() == "red") {
						Simulator.tl[i][j][1].set_new_light("green");						
						System.out.println("The light at " + i + " , " + j + " Avenue is now " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
						int green_time = 0;
						while(green_time != TrafficLightsState.green_time) {
							Event e1 = new Event (8,Simulator.clock + green_time,i,j,1);
							EventList.queue(e1);
							green_time++;
						}
					}			
			}
		}
		es.scheduleNextYellowToRed(last_event_time);
		return;	
	}*/
	
	public void individualGreenToYellow(Event e)throws Exception{
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		int i = e.i;
		int j = e.j;
		int road = e.road;
		Simulator.tl[i][j][road].set_new_light("yellow");
		if (road == 0) {
			System.out.println("The light at " + i + " , " + j + " Street just turned " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
		}
		else if (road == 1) {
			System.out.println("The light at " + i + " , " + j + " Avenue just turned " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
		}
		es.scheduleNextYellowToRed(last_event_time,e);
	}
	public void individualYellowToRed(Event e) throws Exception{
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		int i = e.i;
		int j = e.j;
		int road = e.road;
		Simulator.tl[i][j][road].set_new_light("red");
		if (road == 0) {
			System.out.println("The light at " + i + " , " + j + " Street is now " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
		}
		else if (road == 1) {
			System.out.println("The light at " + i + " , " + j + " Avenue is now " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
		}
		es.scheduleNextRedToGreen(last_event_time,e);
	}
	public void individualRedToGreen(Event e) throws Exception{
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		int i = e.i;
		int j = e.j;
		int road = e.road;
		Simulator.tl[i][j][road].set_new_light("green");
		if (road == 0) {
			System.out.println("The light at " + i + " , " + j + " Street is now " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
			int green_time = 0;
			while(green_time != TrafficLightsState.green_time) {
				Event e1 = new Event (8,Simulator.clock + green_time,i,j,0);
				EventList.queue(e1);
				green_time++;
			}
		}
		else if (road == 1) {
			System.out.println("The light at " + i + " , " + j + " Avenue is now " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
			int green_time = 0;
			while(green_time != TrafficLightsState.green_time) {
				Event e1 = new Event (8,Simulator.clock + green_time,i,j,1);
				EventList.queue(e1);
				green_time++;
			}
		}
		es.scheduleNextGreenToYellow(last_event_time,e);
	}
	public void individualGreenToGreen(Event e) throws Exception{
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		int i = e.i;
		int j = e.j;
		int road = e.road;
		Simulator.tl[i][j][road].set_new_light("green");
		if (road == 0) {
			System.out.println("The light at " + i + " , " + j + " Street is now " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
			int green_time = 0;
			while(green_time != TrafficLightsState.green_time) {
				Event e1 = new Event (8,Simulator.clock + green_time,i,j,0);
				EventList.queue(e1);
				green_time++;
			}
		}
		else if (road == 1) {
			System.out.println("The light at " + i + " , " + j + " Avenue is now " + Simulator.tl[i][j][road].get_current_light() + " at Simulation clock = " + Simulator.clock);
			int green_time = 0;
			while(green_time != TrafficLightsState.green_time) {
				Event e1 = new Event (8,Simulator.clock + green_time,i,j,1);
				EventList.queue(e1);
				green_time++;
			}
		}
		es.scheduleNextGreenToYellow(last_event_time,e);
	}
	public void stillGreen(Event e) throws Exception{
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		int i = e.i;
		int j = e.j;
		int road = e.road;
		if (road == 0) {
			if (i % 2 !=0) {
				es.scheduleCarDeque(last_event_time, i, j+1, i, j, 0);				
			}
			else {
				es.scheduleCarDeque(last_event_time, i, j-1, i ,j , 0);
			}
		}
		else if (road == 1){
			if(j % 2 != 0) {
				es.scheduleCarDeque(last_event_time,i-1,j,i,j , 1);
			}
			else {
				es.scheduleCarDeque(last_event_time,i+1,j,i,j , 1);
			}
		}
	}
	public void carAdvanced(Event e , CarState c) {
		Simulator.clock = e.getTime();
		//c.last_movement_time = Simulator.clock;
		Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].setLane(c.current_lane);
		if(!(Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].checkIfCarPresent(c))) {
			Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
			/*if (Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].getCurrentCapacity() < 90) {
				int road_type;
				if (c.current_start_street == c.current_end_street) {
					// Its a street
					road_type = 0; 
				}
				else {
					// Its an avenue;
					road_type = 1;
				}
				if (Simulator.tl[c.current_end_street][c.current_end_avenue][road_type].get_current_light() == "red") {
					System.out.println("the traffic at " + c.current_end_street + " , " + c.current_end_avenue + " and road type "+ road_type +" has crossed the threshold of queued cars");
					int _road_type;
					if(road_type == 0) {
						_road_type = 1;
					}
					else {
						_road_type = 0;
					}
					if(Simulator.tl[c.current_end_street][c.current_end_avenue][_road_type].get_current_light()== "yellow") {
						Simulator.tl[c.current_end_street][c.current_end_avenue][_road_type].set_new_light("red");
						Event e1 = new Event(9,Simulator.clock + TrafficLightsState.red_time, c.current_end_street,c.current_end_avenue,_road_type);
						EventList.queue(e1);
						Simulator.tl[c.current_end_street][c.current_end_avenue][road_type].set_new_light("green");
						Event e2 = new Event(10,Simulator.clock + TrafficLightsState.green_time, c.current_end_street,c.current_end_avenue,road_type);
						
					}
				}
			}*/
		}
		c.end_time = Simulator.clock;
		c.distance_covered = c.distance_covered + c.dist;		
		//Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
		System.out.println("Car " + c.carID + " has advanced and queued in the road " + c.current_start_street + c.current_start_avenue +"===========" +c.current_end_street + c.current_end_avenue + "  road at Simulation clock = " + Simulator.clock);
		return;
		//es.scheduleCarMovement(c);	
	}
	
	public void reachedIntersection(Event e, CarState c) {
		Simulator.clock = e.getTime();
		Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].setLane(c.current_lane);
		if(!(Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].checkIfCarPresent(c))) {
			Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
		}
		c.end_time = Simulator.clock;
		c.distance_covered = c.distance_covered + c.dist;		
		System.out.println("Car " + c.carID + " has reached the intersection " + c.current_end_street + " , " + c.current_end_avenue + " at Simulation clock = " + Simulator.clock);			
	}
	public void LeftIntersection(Event e, CarState c) throws Exception{
		Simulator.clock = e.getTime();
		try {
			c.end_time = Simulator.clock ;
			//c.distance_covered = c.distance_covered;
			System.out.println("Car " + c.carID + " has left the intersection " + c.current_end_street + " , " + c.current_end_avenue + " at Simulation clock = " + Simulator.clock);						
			c.ints_updater = c.ints_updater + 2;	
			//System.out.println("this is ints updater value currently " + c.ints_updater);
			if(c.ints_updater == c.ints.length - 4) {
				c.setCurrentRoad();
				es.scheduleExit(c);
				return;
			}
			es.scheduleCarQueuing(c);
		}catch(Exception e1) {
			
		}
	}
	/*public void carLeftIntersection(Event e) throws Exception{
		Simulator.clock = e.getTime();
		while(e.left_lane_counter != 0) {
			try {
				CarState c1 = Simulator.rc[e.i][e.j][e.x][e.y].removeCarFromLeftLane();
				es.scheduleCarQueuing(c1);
				System.out.println("Car " + c1.carID + " left the intersection from the left lane of  " + e.x + " , " + e.y + " at Simulation clock = " + Simulator.clock);					
				for(CarState c : Simulator.rc[e.i][e.j][e.x][e.y].leftLane) {
					System.out.println("Currently in this queue is " + c.carID);
				}
			}catch(Exception u) {
				//System.out.println("No Car was found queued at intersection in the left lane of " + e.x + " , " + e.y);
			}
			e.left_lane_counter--;
		}
		while(e.middle_lane_counter != 0) {
			try {				
				CarState c2 = Simulator.rc[e.i][e.j][e.x][e.y].removeCarFromMiddleLane();
				es.scheduleCarQueuing(c2);
				//System.out.println("Car " + c2.carID + " left the intersection from the middle lane of  " + e.x + " , " + e.y + " at Simulation clock = " + Simulator.clock);
				//Iterator<CarState> itr = Simulator.rc[e.i][e.j][e.x][e.y].middleLane.iterator();
				//int i = 0;
				/*while(itr.hasNext()) {
					CarState c = itr.next();
					System.out.println("Currently in this queue " + c.carID );
					i++;
				}
				System.out.println("total cars in the queue " + i);*/
			/*}catch(Exception u) {
				//System.out.println("No Car was found queued at intersection in the middle lane of " + e.x + " , " + e.y);
			}
			e.middle_lane_counter--;
		}
		while(e.right_lane_counter != 0) {
			try{
				CarState c3 = Simulator.rc[e.i][e.j][e.x][e.y].removeCarFromRightLane();
				es.scheduleCarQueuing(c3);
				System.out.println("Car " + c3.carID + "left the intersection from the right lane of " + e.x + " , " + e.y + " at Simulation clock = " + Simulator.clock);
				/*for(CarState c : Simulator.rc[e.i][e.j][e.x][e.y].rightLane) {
					System.out.println("Currently in this queue is " + c.carID);
				}*/
			/*}catch(Exception u) {
			//System.out.println("No Car was found queued at intersection in the right lane of " + e.x + " , " + e.y);
			}
			e.right_lane_counter--;
		}
	} */
	public void carGridExit(Event e , CarState c) {
		Simulator.clock = e.getTime();
		System.out.println("Car " + c.carID + " has exited the grid at intersection " + c.current_end_street + " , " +c.current_end_avenue + " at Simulation clock = " + Simulator.clock);
		System.out.println("Its time in system was " + (c.end_time - c.start_time) + " time units and total waiting time was " + (c.end_time - c.start_time- c.total_time));		
		c.time_in_system = c.end_time - c.start_time;
		c.total_waiting_time = c.time_in_system - c.total_time;
		Statistics.car_record.add(c);
		total_cars_exited ++;
		
		//System.out.println("time in system = " + c.time_in_system);
		//System.out.println("total waiting timem = " + c.total_waiting_time);
	}
}
