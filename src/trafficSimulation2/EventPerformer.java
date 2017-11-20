package trafficSimulation2;

import gridConfiguration.GridConfig;
import java.util.*;

public class EventPerformer {
	
	public EventScheduler es = new EventScheduler();
	public void performArrival(Event e) {
		
		CarState c = new CarState(Simulator.current_cars,e.getTime());
		//System.exit(0);
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		System.out.println("Car " + c.carID + " has arrived in the grid at Simulation clock = " + Simulator.clock);
		//List<Integer> path = c.roadGenerator();
		es.scheduleNextArrival(last_event_time);
		es.scheduleCarQueuing(c);
		//System.out.println("yes");
		Simulator.current_cars = Simulator.current_cars + 1;
		
		//c.moveCar();
		return;
	}
	public void greenToYellow(Event e) {
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
					if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
						continue;
					}
					if(Simulator.tl[i][j][0].get_current_light() == "green") {
						Simulator.tl[i][j][0].set_new_light("yellow");
						System.out.println("The light at " + i + " , " + j + " Street just turned " + Simulator.tl[i][j][0].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
					else if(Simulator.tl[i][j][1].get_current_light() == "green") {
						Simulator.tl[i][j][1].set_new_light("yellow");
						System.out.println("The light at " + i + " , " + j + " Avenue just turned " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
			}
		}
		es.scheduleNextGreenToYellow(last_event_time);
		return;
	}
	public void yellowToRed(Event e) {
		Simulator.clock = e.getTime();
		double last_event_time = Simulator.clock;
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
					if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
						continue;
					}
					if(Simulator.tl[i][j][0].get_current_light() == "yellow") {
						Simulator.tl[i][j][0].set_new_light("red");
						System.out.println("The light at " + i + " , " + j + " Street just turned " + Simulator.tl[i][j][0].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
					else if(Simulator.tl[i][j][0].get_current_light() == "red") {
						Simulator.tl[i][j][0].set_new_light("green");
						System.out.println("The light at " + i + " , " + j + " Street just turned " + Simulator.tl[i][j][0].get_current_light() + " at Simulation clock = " + Simulator.clock);
						if (i % 2 !=0) {
							es.scheduleCarDeque(last_event_time, i, j+1, i, j, 0);
							//Simulator.rc[i][j+1][i][j].
						}
						else {
							es.scheduleCarDeque(last_event_time, i, j-1, i ,j , 0);
						}
					}
					if(Simulator.tl[i][j][1].get_current_light() == "yellow") {
						Simulator.tl[i][j][1].set_new_light("red");
						System.out.println("The light at " + i + " , " + j + " Avenue just turned " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
					}
					else if(Simulator.tl[i][j][1].get_current_light() == "red") {
						Simulator.tl[i][j][1].set_new_light("green");
						System.out.println("The light at " + i + " , " + j + " Avenue just turned " + Simulator.tl[i][j][1].get_current_light() + " at Simulation clock = " + Simulator.clock);
						if(j % 2 != 0) {
							es.scheduleCarDeque(last_event_time,i-1,j,i,j , 1);
						}
						else {
							es.scheduleCarDeque(last_event_time,i+1,j,i,j , 1);
						}
					}
			}
		}
		es.scheduleNextYellowToRed(last_event_time);
		return;	
	}
	public void carAdvanced(Event e , CarState c) {
		Simulator.clock = e.getTime();
		//c.last_movement_time = Simulator.clock;
		c.distance_covered = c.distance_covered + c.dist;
		c.end_time = c.end_time + c.time;
		//Simulator.rc[c.current_start_street][c.current_start_avenue][c.current_end_street][c.current_end_avenue].addCarToLane(c);
		System.out.println("Car " + c.carID + " has advanced and queued in the road " + c.current_start_street+ " , " + c.current_start_avenue + " , "+c.current_end_street + " , " + c.current_end_avenue + "  road at Simulation clock = " + Simulator.clock);
		return;
		//es.scheduleCarMovement(c);	
	}
	
	public void reachedIntersection(Event e, CarState c) {
		Simulator.clock = e.getTime();
		c.end_time = Simulator.clock;
		c.distance_covered = c.distance_covered + c.dist;
		c.ints_updater= c.ints_updater + 2;
		
		System.out.println("Car " + c.carID + " has reached the intersection " + c.current_end_street + " , " + c.current_end_avenue + " at Simulation clock = " + Simulator.clock);
		if(c.ints_updater == c.ints.length - 2) {
			es.scheduleExit(c);
		}
		return;
	}
	public void carLeftIntersection(Event e) throws Exception{
		Simulator.clock = e.getTime();
		while(e.left_lane_counter != 0) {
			try {
				CarState c1 = Simulator.rc[e.i][e.j][e.x][e.y].removeCarFromLeftLane();
				es.scheduleCarQueuing(c1);
				System.out.println("Car " + c1.carID + " left the intersection from the left lane of  " + e.x + " , " + e.y + " at Simulation clock = " + Simulator.clock);					
				for(CarState c : Simulator.rc[e.i][e.j][e.x][e.y].leftLane) {
					System.out.println("Currently in this queue is " + c.carID);
				}
			}
			catch(Exception u) {
				//System.out.println("No Car was found queued at intersection in the left lane of " + e.x + " , " + e.y);
			}
			e.left_lane_counter--;
		}
		while(e.middle_lane_counter != 0) {
			try {
				
				/*CarState c2 = Simulator.rc[e.i][e.j][e.x][e.y].removeCarFromMiddleLane();
				es.scheduleCarQueuing(c2);
				//System.out.println("Car " + c2.carID + " left the intersection from the middle lane of  " + e.x + " , " + e.y + " at Simulation clock = " + Simulator.clock);
				Iterator<CarState> itr = Simulator.rc[e.i][e.j][e.x][e.y].middleLane.iterator();
				int i = 0;
				while(itr.hasNext()) {
					CarState c = itr.next();
					System.out.println("Currently in this queue " + c.carID );
					i++;
				}
				System.out.println("total cars in the queue " + i);*/
			}
			catch(Exception u) {
				//System.out.println("No Car was found queued at intersection in the middle lane of " + e.x + " , " + e.y);
			}
			e.middle_lane_counter--;
		}
		while(e.right_lane_counter != 0) {
			try{
				CarState c3 = Simulator.rc[e.i][e.j][e.x][e.y].removeCarFromRightLane();
				es.scheduleCarQueuing(c3);
				System.out.println("Car " + c3.carID + "left the intersection from the right lane of " + e.x + " , " + e.y + " at Simulation clock = " + Simulator.clock);
				for(CarState c : Simulator.rc[e.i][e.j][e.x][e.y].rightLane) {
					System.out.println("Currently in this queue is " + c.carID);
				}
			}
			catch(Exception u) {
			//System.out.println("No Car was found queued at intersection in the right lane of " + e.x + " , " + e.y);
			}
			e.right_lane_counter--;
		}
	}	
	public void carGridExit(Event e , CarState c) {
		Simulator.clock = e.getTime();
		System.out.println("Car " + c.carID + " has exited the grid at intersection " + c.current_end_street + " , " +c.current_end_avenue + " at Simulation clock = " + Simulator.clock);
		System.out.println("Its time in system was " + (c.end_time - c.start_time) + " time units and total waiting time was " + (c.end_time - c.start_time - c.total_time));		
	}
}
