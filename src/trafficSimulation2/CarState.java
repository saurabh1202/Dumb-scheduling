package trafficSimulation2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.Iterator;
import gridConfiguration.GridConfig;

public class CarState extends RoadConstructor{
	public int carID ;
	public int carLength = 1;
	public static EventPerformer ep = new EventPerformer();
	public static EventScheduler es = new EventScheduler();
	public static final double speed = 4.0;
	public static final double acceleration = 2.0;
	public static final double deceleration = 2.0;
	public double start_time;
	//public double last_movement_time;
	//public double last_movement_end_time;
	public double end_time;
	private int entry_point[] = new int[2];
	private int exit_point[] = new int[2];
	public static final int block_size = 100; // Size of the block
	public int distance_covered = 0;
	public double time_in_system;
	public int total_distance;
	public double total_time;
	public double total_waiting_time;
	public int dist;
	public double time;	
	public int no_of_turns ;
	public List<Integer> path = new ArrayList<Integer>();
	public int current_start_street ;
	public int current_start_avenue ;
	public int current_end_street;
	public int current_end_avenue;
	public int current_lane;
	public int total_roads;
	public int completed_roads;
	public int current_road[] = new int[4];
	public ArrayList<Integer> lane_id = new ArrayList<Integer>();
	public Integer lane_identity[]; 
	public Integer ints[] ;
	public int ints_updater;
	public Iterator<Integer> itr;
	public static final double time_acc = (speed - 0)/acceleration;// time spent while accelerating to a constant speed
	public static final double dist_acc = 0.5 * acceleration * time_acc * time_acc; // Distance covered accelerating.
	public static final double time_dec = (speed - 0)/deceleration;// time spent covered while decelerating to zero.
	public static final double dist_dec = (speed * time_dec) - (0.5 * deceleration * time_dec * time_dec);// Distance covered decelerating
	//TotalTime to cover a distance = 0.5 * ((speed/acceleration) + (speed/deceleration)) + (distance/speed); // This equation should be only considered if
																											 // dist_acc + dist_dec <= Distance to be covered
	
	
	public CarState(int _carID,double _start_time) {
		super();
		carID = _carID;
		start_time = _start_time;
		end_time = start_time;
		Random r = new Random();
		int High = 2*(GridConfig.grid_size-1);
		int Result1 = r.nextInt(High) ;
		int Result2 = r.nextInt(High) ;
		entry_point[0]=0;//GridConfig.entry_points[Result1][0];
		entry_point[1]=1;//GridConfig.entry_points[Result1][1];
		
		exit_point[0]=4;//GridConfig.exit_points[Result2][0];
		exit_point[1]=1;//GridConfig.exit_points[Result2][1];
		System.out.println("the entry is "+ entry_point[0] + " , "+entry_point[1]);
		System.out.println("the exit is "+ exit_point[0] + " , "+exit_point[1]);		
		path = generatePath(entry_point,exit_point);
		path.add(0,entry_point[0]);
		path.add(1,entry_point[1]);
		path.add(exit_point[0]);
		path.add(exit_point[1]);
		System.out.println("The car is going to travel the following path " + path);
		itr = path.iterator();
		int k = 0;
		for (int i = 0; i < path.size(); i++) {
			//System.out.println(path.get(i));
			if (path.get(i)/10 > 0) {
				lane_id.add(k, path.get(i));
				path.remove(i);
				k++;
			}
		}
		lane_identity= new Integer[lane_id.size()];
		lane_identity = lane_id.toArray(lane_identity);
		//System.out.println(Arrays.toString(lane_identity));
		if(lane_identity.length == 1) {
			no_of_turns = 0;
		}
		else if (lane_identity.length == 2) {
			no_of_turns = 1;
		}
		else {
			no_of_turns = 2;
		}
		ints = new Integer[path.size()];
		ints = path.toArray(ints);
		//System.out.println(Arrays.toString(ints));
		total_roads = (ints.length/2) - 1; 
		total_distance = block_size*(total_roads);
		total_time = 0.5 * ((speed/acceleration) + (speed/deceleration)) + (total_distance/speed);
		ints_updater = 0;
		//System.out.println("yes working");
		//roadGenerator(path);
	}
	public void setCurrentLane(int _current_lane) {
		current_lane = _current_lane;
	}
	public int getCurrentLane() {
		return current_lane;
	}
	public void setCurrentRoad() {
		if(ints_updater <= (ints.length - 4)) {
			current_start_street = ints[ints_updater];
			current_start_avenue = ints[ints_updater + 1];
			current_end_street = ints[ints_updater + 2];
			current_end_avenue = ints[ints_updater + 3];
			//System.out.println(current_start_street + " , " + current_start_avenue + " , " + current_end_street + " , " + current_end_avenue);
		}
		else {
			System.out.println("Car " + this.carID + " has exited the grid at exit point " + exit_point[0] + exit_point[1]);
			System.out.println(" It had a total time in system of " + time_in_system + " with a total waiting time of " + total_waiting_time);
		}
	}
	
	/*public void roadGenerator() {
		Iterator<Integer> itr = path.iterator();
		List<Integer> turn = new ArrayList<Integer>();
		while(itr.hasNext()) {
			int x = itr.next();
			if (x/10 == 1 || x/10 == 2 || x/10 == 3) {
				turn.add(x);
				itr.remove();
			}
		}
		Integer intersection[] = new Integer[path.size()];
		intersection = path.toArray(intersection);
		Simulator.rc[entry_point[0]][entry_point[1]][intersection[0]][intersection[1]].setLane(turn.get(0));
		Simulator.rc[entry_point[0]][entry_point[1]][intersection[0]][intersection[1]].addCarToLane(this);
		//moveCar(entry_point[0],entry_point[1],intersection[0],intersection[1]);
		for (int i = 0; i < intersection.length ; i=i+2) {
			
		}
		//System.out.println("the turns " + turn);
		//System.out.println("intersections " + path);
	}
	public static void moveCar(int i , int j , int x , int y) {
		int road_type ;
		if (i == x) {
			road_type = 0;
		}
		else if (j == y) {
			road_type = 1;
		}
		int dist = Simulator.rc[i][j][x][y].getCurrentCapacity();
		if (dist >= (dist_acc + dist_dec)) {
			double time =  0.5 * ((speed/acceleration) + (speed/deceleration)) + (dist/speed);
			Event e = new Event(4,time);
			EventList.queue(e);
		}
		else if () {
			
		}
	}*/
	
		//List<Integer> turn = new ArrayList<Integer>();
		/*while(itr.hasNext()) {
			int x = itr.next();
			if (x/10 == 1 || x/10 == 2 || x/10 == 3) {
				turn.add(x);
				itr.remove();
			}
		}
		*/
		
		//Integer turning[] = new Integer[turn.size()];
		//turning = path.toArray(turning);
		//boolean change_in_street = false;
		//boolean change_in_avenue = false;
	
		
		/*if (entry_point[0] != intersection[0]) {
			int else_part = 0;
			Simulator.rc[entry_point[0]][entry_point[1]][intersection[0]][intersection[1]].setLane(turning[0]);
			for (int i = 0 ; i < intersection.length ; i=i+2) {
				if(intersection[i] != intersection[i+2]) {
					if(else_part == 0) {
						Simulator.rc[intersection[i]][intersection[i+1]][intersection[i+2]][intersection[i+3]].setLane(turning[0]);
					}
					else if (else_part > 0) {
						Simulator.rc[intersection[i]][intersection[i+1]][intersection[i+2]][intersection[i+3]].setLane(turning[2]);
					}
				}
				else {
					else_part += 1;
					Simulator.rc[intersection[i]][intersection[i+1]][intersection[i+2]][intersection[i+3]].setLane(turning[1]);
				}
			}
		}
		else if*/
		
		
	

	
		
			/*
			for (int i = 2 ; i < path.size() - 1; i=i+2) {
				Simulator.rc[path.get(i-2)][path.get(i-1)][path.get(i)][path.get(i+1)].setLane(turning);
				int dist = Simulator.rc[path.get(i-2)][path.get(i-1)][path.get(i)][path.get(i+1)].getCurrentCapacity(); 
				while(distance_covered != block_size) {
					if(dist >= (dist_acc + dist_dec)) {
						double time = 0.5 * ((CarState.speed/CarState.acceleration) + (CarState.speed/CarState.deceleration)) + (dist/CarState.speed);
						distance_covered = distance_covered + dist;
						end_time = end_time + time;
						Simulator.clock = Simulator.clock + time;
						System.out.println("Car " + carID + " is nearing the intersection at " + path.get(i) + " , "+path.get(i+1)  + " and has travelled the distance " + distance_covered + " in time " + time + " at " + Simulator.clock);
						Event e = new Event(4,Simulator.clock + time);
						EventList.queue(e);
						dist = Simulator.rc[path.get(i-2)][path.get(i-1)][path.get(i)][path.get(i+1)].getCurrentCapacity() - dist;						
					}
				}
				Simulator.rc[path.get(i-2)][path.get(i-1)][path.get(i)][path.get(i+1)].addCarToLane(this);
				System.out.println("Car has reached the intersection " + path.get(i) + " , " + path.get(i+1) + " at Simulation clock " + Simulator.clock);
				if(path.get(i-2) != path.get(i)) {
					// Its a change in street no. hence check avenue light
					if(Simulator.tl[path.get(i)][path.get(i+1)][1].get_current_light() == "green") {
						CarState c1 =Simulator.rc[path.get(i-2)][path.get(i-1)][path.get(i)][path.get(i+1)].removeCarFromLane();
						System.out.println("Car " + c1.carID + " found a green light at " + path.get(i) + " , " + path.get(i+1) + " and has left the intersection at " + Simulator.clock);
					}
				}
				else if (path.get(i-1) != path.get(i+1)) {
					//Its a change in avenue no. hence check street light 
					if(Simulator.tl[path.get(i)][path.get(i+1)][0].get_current_light() == "green") {
						//
					}
				}
				
			}
			
		*/
@SuppressWarnings("unused")
public static List<Integer> generatePath(int entry_point[] , int exit_point[]){
		
		int i = entry_point[0];
		int j = entry_point[1];
		int x = exit_point[0];
		int y = exit_point[1];
		int left = 10;
		int middle = 20;
		int right = 30;
		List<Integer> intersections = new ArrayList<Integer>();
		if (i == 0) {
			// Its a NS avenue entry
			
			// Now since we have discovered that it is a NS avenue entry , it is time to discover whether it is a
			// NS avenue exit, EW street exit , WE street exit , SN Avenue exit
			if(x == GridConfig.NO_OF_STREETS-1) {
				// Its a NS avenue exit
				// Now its time to figure out whether it the car should be put in right lane or left lane
				if (j < y) {
					// Put the car in the left lane queue
					
					intersections.add(left);// This is to notify that in which the lane the car will be put to. Left,middle,right.
					intersections.add(++i);
					intersections.add(j);	
					
					
					// Now , since we know its a NS avenue entry , the next intersection joins with the EW street
					// so car cannot make a left turn , hence we move to the next intersection in the 
					// NS direction which will be a WE street and there we can make our left turn.
					// hence the next step
					intersections.add(++i);
					intersections.add(j);
					intersections.add(right);
					// here since we have to make a right turn since we are going down the NS avenue we put the vehicle in the right lane
					
					while(j != y) {
						// here we make our left turn and go up to the avenue no. in which we have to exit.
						
						intersections.add(i);
						intersections.add(++j);
					}
					intersections.add(middle);
					// Now we have reached the avenue in which we have to exit but still not reached the row where 
					// we want to exit .
					// Now we will go to the row no. - 1 where we have to exit
					while(i != x - 1) {
						// Here we will only go to the intersection before the exit intersection and the journey from
						//that intersection to the exit intersection will be handled separately
						intersections.add(++i);
						intersections.add(j);
					}
				}
				else if (j > y) {
					// Put the car in the right lane queue
					// Since the next intersection is a EW street and we want to take a right we don't need to go to 
					// the next intersection, we can take a right from there itself.
					intersections.add(right);//putting the car in the right lane. 
					intersections.add(++i);
					intersections.add(j);
					intersections.add(left);// Since it is going to take a left turn next put it in the left lane
					while(j != y) {
						intersections.add(i);
						intersections.add(--j);						
					}
					intersections.add(middle);
					while(i != x - 1) {
						intersections.add(++i);
						intersections.add(j);
					}
					
				}
				else {
					// Put the car in the middle lane queue
					intersections.add(middle);
					intersections.add(++i);
					intersections.add(j);
					while(i != x - 1) {
						intersections.add(++i);
						intersections.add(j);						
					}
				}
				
			}
			else if(x==0) {
				// Its a SN avenue exit ; a kind of a U exit.
				if (j < y) {
					// Put the car in the left lane
					intersections.add(left);
					intersections.add(++i);
					intersections.add(j);
					
					//since here we are on the EW street the traffic is in the opposite direction of where we want to go , Hence move down one
					//more block
					intersections.add(++i);
					intersections.add(j);
					intersections.add(left);
					while(j != y) {
						intersections.add(i);
						intersections.add(++j);
					}
					intersections.add(middle);
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}					
				}
				else if (j > y) {
					// Put the car in the right lane
					intersections.add(right);
					intersections.add(++i);
					intersections.add(j);
					intersections.add(right);
					
					while(j != y) {
						
						intersections.add(i);
						intersections.add(--j);
					}
					intersections.add(middle);
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}
				}
			}
			else if (y == 0) {
				//Its a EW street exit
				// Put the car in the right lane
				intersections.add(right);
				intersections.add(++i);
				intersections.add(j);
				
				while(i != x) {
					intersections.add(++i);
					intersections.add(j);
				}
				intersections.add(middle);
				while(j != y + 1) {
					intersections.add(i);
					intersections.add(--j);
				}
			}
			else {
				// Its a WE street exit
				intersections.add(left);//putting the car in the left lane
				intersections.add(++i);
				intersections.add(j);
				while(i != x) {
					intersections.add(++i);
					intersections.add(j);
				}
				intersections.add(middle);
				while(j != y - 1) {
					intersections.add(i);
					intersections.add(++j);
				}
			}
		}
		else if (i == GridConfig.NO_OF_STREETS - 1) {
			// Its a SN avenue entry
			
			// Now since we have discovered that it is a SN avenue entry , it is time to discover whether it is a
			// SN avenue exit, EW street exit , WE street exit , NS avenue exit
			if(x == 0) {
				// Its a SN avenue exit
				if (j < y) {
					// Put the car in the right lane
					intersections.add(right);//putting the car in the right lane
					intersections.add(--i);
					intersections.add(j);
					
					
					if ((GridConfig.NO_OF_STREETS - 2) % 2 != 0) {
						// the street that meets at first intersection is a EW street hence the car needs to travel
						//upwards to the next intersection where the street would be a WE street
						intersections.add(--i);
						intersections.add(j);
					}
					intersections.add(left);
					while(j != y) {
						intersections.add(i);
						intersections.add(++j);
					}
					intersections.add(middle);
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}
				}
				else if (j > y) {
					// put the car in the left lane
					intersections.add(left);
					intersections.add(--i);
					intersections.add(j);
					
					if((GridConfig.NO_OF_STREETS - 2) % 2 == 0){
						// the street that meets at first intersection is a WE street hence the car needs to travel
						//upwards to the next intersection where the street would be a EW street
						intersections.add(--i);
						intersections.add(j);
					}
					intersections.add(right);
					while(j != y) {
						intersections.add(i);
						intersections.add(--j);
					}
					intersections.add(middle);
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}
				}
				else {
					// put the car in the middle lane
					intersections.add(middle);
					intersections.add(--i);
					intersections.add(j);
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}
				}
			}
			else if(x == GridConfig.NO_OF_STREETS - 1) {
				// Its a NS Avenue exit
				if (j < y) {
					// Put the car in the right lane
					intersections.add(right);
					intersections.add(--i);
					intersections.add(j);
					
					
					if ((GridConfig.NO_OF_STREETS - 2) % 2 != 0 ) {
						// Traffic in opposite direction hence move up one block
						intersections.add(--i);
						intersections.add(j);
					}
					intersections.add(right);
					while(j != y) {
						intersections.add(i);
						intersections.add(++j);
					}
					intersections.add(middle);
					while(i != x - 1) {
						intersections.add(++i);
						intersections.add(j);
					}
				}
				if (j > y) {
					// Put the car in the left lane
					intersections.add(left);
					intersections.add(--i);
					intersections.add(j);
					
					
					if((GridConfig.NO_OF_STREETS - 2) % 2 == 0) {
						// Traffic in opposite direction hence move up one block
						intersections.add(--i);
						intersections.add(j);
					}
					intersections.add(left);
					while(j != y) {
						intersections.add(i);
						intersections.add(--j);
					}
					intersections.add(middle);
					while(i != x - 1) {
						intersections.add(++i);
						intersections.add(j);
					}
				}
			}
			else if(y == 0) {
				// Its a EW street exit
				// put car in the left lane
				intersections.add(left);
				intersections.add(--i);
				intersections.add(j);
				
				if ((GridConfig.NO_OF_STREETS - 2) % 2 == 0 ) {
					intersections.add(--i);
					intersections.add(j);
				}
				while(i != x ) {
					intersections.add(--i);
					intersections.add(j);
				}
				intersections.add(middle);
				while( j != y + 1) {
					intersections.add(i);
					intersections.add(--j);
				}
			}
			else {
				// Its a WE street exit
				//put car in the right lane
				intersections.add(right);
				intersections.add(--i);
				intersections.add(j);

				
				if ((GridConfig.NO_OF_STREETS - 2) % 2 != 0) {
					intersections.add(--i);
					intersections.add(j);
				}
				while(i != x) {
					intersections.add(--i);
					intersections.add(j);
				}
				intersections.add(middle);
				while(j != y - 1) {
					intersections.add(i);
					intersections.add(++j);
				}
			}
		}
		else if (j == GridConfig.NO_OF_AVENUES - 1) {
			// Its a EW street entry
			
			// Now it can be a EW exit, NS exit ,SN exit , WE exit
			if (y == 0) {
				// Its a EW street exit
				if(i < x) {
					//put the car in the left lane queue
					intersections.add(left);
					intersections.add(i);
					intersections.add(--j);
					
					
					if((GridConfig.NO_OF_AVENUES - 2) % 2 == 0) {
						intersections.add(i);
						intersections.add(--j);
					}
					intersections.add(right);
					while(i != x) {
						intersections.add(++i);
						intersections.add(j);
					}
					intersections.add(middle);
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
				else if (i > x) {
					// Put the car in the right lane queue
					intersections.add(right);
					intersections.add(i);
					intersections.add(--j);
					
					
					if ((GridConfig.NO_OF_AVENUES - 2) % 2 != 0) {
						intersections.add(i);
						intersections.add(--j);
					}
					intersections.add(left);
					while(i != x) {
						intersections.add(--i);
						intersections.add(j);
					}
					intersections.add(middle);
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
				else {
					// put the car in the middle lane queue
					intersections.add(middle);
					intersections.add(i);
					intersections.add(--j);
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
			}
			else if(y == GridConfig.NO_OF_AVENUES - 1){
				// Its a WE street exit
				if(i < x) {
					//Put the car in the left lane
					intersections.add(left);
					intersections.add(i);
					intersections.add(--j);
					
					
					if((GridConfig.NO_OF_AVENUES - 2) % 2 == 0) {
						// traffic in opposite direction hence move towards West for one block
						intersections.add(i);
						intersections.add(--j);
					}
					intersections.add(left);
					while(i != x) {
						intersections.add(++i);
						intersections.add(j);
					}
					intersections.add(middle);
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}
				}
				else if(i > x) {
					//Put the car in the right lane 
					intersections.add(right);
					intersections.add(i);
					intersections.add(--j);
					
					
					if ((GridConfig.NO_OF_AVENUES - 2) % 2 != 0) {
						// Traffic in opposite direction hence move towards West for one block
						intersections.add(i);
						intersections.add(--j);
					}
					intersections.add(right);
					while(i != x) {
						intersections.add(--i);
						intersections.add(j);						
					}
					intersections.add(middle);
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}
				}				
			}
			else if(x == GridConfig.NO_OF_STREETS - 1) {
				// Its a NS avenue exit
				// Put the car in the left lane
				intersections.add(left);
				intersections.add(i);
				intersections.add(--j);
				
				while(j != y) {
					intersections.add(i);
					intersections.add(--j);
				}
				intersections.add(middle);
				while(i != x - 1) {
					intersections.add(++i);
					intersections.add(j);
				}
			}
			else { 
				// Its a SN avenue exit
				intersections.add(right);//putting the car in the right lane
				intersections.add(i);
				intersections.add(--j);
				
				while(j != y) {
					intersections.add(i);
					intersections.add(--j);
				}
				intersections.add(middle);
				while(i != x + 1) {
					intersections.add(--i);
					intersections.add(j);
				}
			}
		}
		else if (j == 0) {
			//Its a WE street entry
			
			// Now decide whether its a WE street exit, NS avenue exit ,SN avenue exit , EW street exit
			if(y == GridConfig.NO_OF_AVENUES - 1) {
				// Its a WE street exit
				if (i < x) {
					//Putting the car in the right lane
					intersections.add(right);
					intersections.add(i);
					intersections.add(++j);
					intersections.add(left);
					
					while(i != x) {
						intersections.add(++i);
						intersections.add(j);
					}
					intersections.add(middle);
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}				
				}
				else if (i > x) {
					// Put the car in the left lane queue from the start
					intersections.add(left);
					intersections.add(i);
					intersections.add(++j);
					
					
					// Since the first intersection is a NS avenue the car cannot turn left hence it should travel
					// one more intersection towards East
					intersections.add(i);
					intersections.add(++j);
					intersections.add(right);
					while(i != x) {
						intersections.add(--i);
						intersections.add(j);
					}
					intersections.add(middle);
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}
				}
				else {
					// Its a straight exit in the WE street
					// put the car in the middle lane 
					intersections.add(middle);
					intersections.add(i);
					intersections.add(++j);
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}
				}
			}
			else if(y == 0) {
				// Its a EW street exit
				if(i < x) {
					//put the car in the right lane
					intersections.add(right);
					intersections.add(i);
					intersections.add(++j);
					intersections.add(right);
					
					while(i != x) {
						intersections.add(++i);
						intersections.add(j);
					}
					intersections.add(middle);
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
				else if (i > x) {
					// Put the car in the left lane
					intersections.add(left);
					intersections.add(i);
					intersections.add(++j);
					
					
					// Since the traffic is in the opposite direction in this avenue we move towards East for one block
					intersections.add(i);
					intersections.add(++j);
					intersections.add(left);
					while(i != x) {
						intersections.add(--i);
						intersections.add(j);
					}
					intersections.add(middle);
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
			}
			else if (x == 0) {
				// Its a SN avenue exit
				// Put the car in the left lane queue
				intersections.add(left);
				intersections.add(i);
				intersections.add(++j);
				
				while(j != y) {
					intersections.add(i);
					intersections.add(++j);
				}
				intersections.add(middle);
				while(i != x + 1) {
					intersections.add(--i);
					intersections.add(j);
				}
			}
			else {
				// Its a NS avenue exit
				// Put the car in the right lane queue
				intersections.add(right);
				intersections.add(i);
				intersections.add(++j);
				
				while(j != y) {
					intersections.add(i);
					intersections.add(++j);
				}
				intersections.add(middle);
				while(i != x - 1) {
					intersections.add(++i);
					intersections.add(j);
				}
			}
		}		
		return intersections;
	}
	
}
