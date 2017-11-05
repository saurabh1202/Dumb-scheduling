package trafficSimulation2;
import java.util.ArrayList;
import java.util.List;
import gridConfiguration.GridConfig;

public class CarState {
	public int carID ;
	public int carLength = 1;
	public double speed = 4.0;
	public double acceleration = 2.0;
	public double deceleration = 2.0;
	public double start_time;

	public CarState(int _carID,double _start_time) {
		carID = _carID;
		start_time = _start_time;
	}
	
@SuppressWarnings("unused")
public List<Integer> generatePath(int entry_point[] , int exit_point[]){
		
		int i = entry_point[0];
		int j = entry_point[1];
		int x = exit_point[0];
		int y = exit_point[1];
		int left = 10;
		int middle = 11;
		int right = 12;
		List<Integer> intersections = new ArrayList<Integer>();
		if (i == 0) {
			// Its a NS avenue entry
			intersections.add(++i);
			intersections.add(j);
			// Now since we have discovered that it is a NS avenue entry , it is time to discover whether it is a
			// NS avenue exit, EW street exit , WE street exit
			if(x == GridConfig.NO_OF_STREETS-1) {
				// Its a NS avenue exit
				// Now its time to figure out whether it the car should be put in right lane or left lane
				if (j < y) {
					// Put the car in the left lane queue
					// Now , since we know its a NS avenue entry , the next intersection joins with the EW street
					// so car cannot make a left turn , hence we move to the next intersection in the 
					// NS direction which will be a WE street and there we can make our left turn.
					// hence the next step
					intersections.add(10);// This is to notify that in which the lane the car will be put to. Left,middle,right.
					intersections.add(++i);
					intersections.add(j);
					while(j != y) {
						// here we make our left turn and go up to the avenue no. in which we have to exit.
						intersections.add(i);
						intersections.add(++j);
					}
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
					intersections.add(12); //putting the car in the right lane. 
					while(j != y) {
						intersections.add(i);
						intersections.add(--j);						
					}
					while(i != x - 1) {
						intersections.add(++i);
						intersections.add(j);
					}
					
				}
				else {
					// Put the car in the middle lane queue
					intersections.add(11);
					while(i != x - 1) {
						intersections.add(++i);
						intersections.add(j);						
					}
				}
				
			}
			else if (y == 0) {
				//Its a EW street exit
				// Put the car in the right lane
				intersections.add(12);
				while(i != x) {
					intersections.add(++i);
					intersections.add(j);
				}
				while(j != y + 1) {
					intersections.add(i);
					intersections.add(--j);
				}
			}
			else {
				// Its a WE street exit
				intersections.add(10);//putting the car in the left lane
				while(i != x) {
					intersections.add(++i);
					intersections.add(j);
				}
				while(j != y - 1) {
					intersections.add(i);
					intersections.add(++j);
				}
			}
		}
		else if (i == GridConfig.NO_OF_STREETS - 1) {
			// Its a SN avenue entry
			intersections.add(--i);
			intersections.add(j);
			// Now since we have discovered that it is a SN avenue entry , it is time to discover whether it is a
			// SN avenue exit, EW street exit , WE street exit
			if(x == 0) {
				// Its a SN avenue exit
				if (j < y) {
					// Put the car in the right lane
					intersections.add(12);//putting the car in the right lane
					if ((GridConfig.NO_OF_STREETS - 2) % 2 != 0) {
						// the street that meets at first intersection is a EW street hence the car needs to travel
						//upwards to the next intersection where the street would be a WE street
						intersections.add(--i);
						intersections.add(j);
					}
					while(j != y) {
						intersections.add(i);
						intersections.add(++j);
					}
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}
				}
				else if (j > y) {
					// put the car in the left lane
					intersections.add(10);//putting the car in the left lane
					if((GridConfig.NO_OF_STREETS - 2) % 2 == 0){
						// the street that meets at first intersection is a WE street hence the car needs to travel
						//upwards to the next intersection where the street would be a EW street
						intersections.add(--i);
						intersections.add(j);
					}
					while(j != y) {
						intersections.add(i);
						intersections.add(--j);
					}
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}
				}
				else {
					// put the car in the middle lane
					intersections.add(11);
					while(i != x + 1) {
						intersections.add(--i);
						intersections.add(j);
					}
				}
			}
			else if(y == 0) {
				// Its a EW street exit
				// put car in the left lane
				intersections.add(10);
				if ((GridConfig.NO_OF_STREETS - 2) % 2 == 0 ) {
					intersections.add(--i);
					intersections.add(j);
				}
				while(i != x ) {
					intersections.add(--i);
					intersections.add(j);
				}
				while( j != y + 1) {
					intersections.add(i);
					intersections.add(--j);
				}
			}
			else {
				// Its a WE street exit
				//put car in the right lane
				intersections.add(12);
				if ((GridConfig.NO_OF_STREETS - 2) % 2 != 0) {
					intersections.add(--i);
					intersections.add(j);
				}
				while(i != x) {
					intersections.add(--i);
					intersections.add(j);
				}
				while(j != y - 1) {
					intersections.add(i);
					intersections.add(++j);
				}
			}
		}
		else if (j == GridConfig.NO_OF_AVENUES - 1) {
			// Its a EW street entry
			intersections.add(i);
			intersections.add(--j);
			// Now it can be a EW exit, NS exit or SN exit
			if (y == 0) {
				// Its a EW street exit
				if(i < x) {
					//put the car in the left lane queue
					intersections.add(10);
					if((GridConfig.NO_OF_AVENUES - 2) % 2 == 0) {
						intersections.add(i);
						intersections.add(--j);
					}
					while(i != x) {
						intersections.add(++i);
						intersections.add(j);
					}
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
				else if (i > x) {
					// Put the car in the right lane queue
					intersections.add(12);
					if ((GridConfig.NO_OF_AVENUES - 2) % 2 != 0) {
						intersections.add(i);
						intersections.add(--j);
					}
					while(i != x) {
						intersections.add(--i);
						intersections.add(j);
					}
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
				else {
					// put the car in the middle lane queue
					intersections.add(11);
					while(j != y + 1) {
						intersections.add(i);
						intersections.add(--j);
					}
				}
			}
			else if(x == GridConfig.NO_OF_STREETS - 1) {
				// Its a NS avenue exit
				// Put the car in the left lane
				intersections.add(10);
				while(j != y) {
					intersections.add(i);
					intersections.add(--j);
				}
				while(i != x - 1) {
					intersections.add(++i);
					intersections.add(j);
				}
			}
			else { 
				// Its a SN avenue exit
				intersections.add(12);//putting the car in the right lane
				while(j != y) {
					intersections.add(i);
					intersections.add(--j);
				}
				while(i != x + 1) {
					intersections.add(--i);
					intersections.add(j);
				}
			}
		}
		else if (j == 0) {
			//Its a WE street entry
			intersections.add(i);
			intersections.add(++j);
			// Now decide whether its a WE street exit, NS avenue exit or SN avenue exit
			if(y == GridConfig.NO_OF_AVENUES - 1) {
				// Its a WE street exit
				if (i < x) {
					//Putting the car in the right lane
					intersections.add(12);
					while(i != x) {
						intersections.add(++i);
						intersections.add(j);
					}
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}				
				}
				else if (i > x) {
					// Put the car in the left lane queue from the start
					intersections.add(10);
					// Since the first intersection is a NS avenue the car cannot turn left hence it should travel
					// one more intersection towards East
					intersections.add(i);
					intersections.add(++j);
					while(i != x) {
						intersections.add(--i);
						intersections.add(j);
					}
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}
				}
				else {
					// Its a straight exit in the WE street
					// put the car in the middle lane 
					intersections.add(11);
					while(j != y - 1) {
						intersections.add(i);
						intersections.add(++j);
					}
				}
			}
			else if (x == 0) {
				// Its a SN avenue exit
				// Put the car in the left lane queue
				intersections.add(10);
				while(j != y) {
					intersections.add(i);
					intersections.add(++j);
				}
				while(i != x + 1) {
					intersections.add(--i);
					intersections.add(j);
				}
			}
			else {
				// Its a NS avenue exit
				// Put the car in the right lane queue
				intersections.add(12);
				while(j != y) {
					intersections.add(i);
					intersections.add(++j);
				}
				while(i != x - 1) {
					intersections.add(++i);
					intersections.add(j);
				}
			}
		}		
		return intersections;
	}
	
}
