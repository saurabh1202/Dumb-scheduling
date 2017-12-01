package trafficSimulation2;
import java.util.*;

import gridConfiguration.GridConfig;

public class Simulator {

	public static double clock = 0.0;
	public static Random r = new Random();
	public static EventPerformer ep = new EventPerformer();
	public static EventScheduler es = new EventScheduler();
	public static int no_of_cars;
	public static int current_cars = 1; 
	public static double max_simulation_time;
	public static TrafficLightsState tl [][][] = new TrafficLightsState [GridConfig.NO_OF_STREETS][GridConfig.NO_OF_AVENUES][2];
	//public static Intersections intersections [][] = new Intersections [GridConfig.NO_OF_STREETS][GridConfig.NO_OF_AVENUES];
	public static RoadConstructor rc[][][][] = new RoadConstructor[GridConfig.NO_OF_STREETS + 1][GridConfig.NO_OF_AVENUES + 1][GridConfig.NO_OF_STREETS + 1][GridConfig.NO_OF_AVENUES + 1];
	public static int randomlights[] = {0,1};
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Please enter the max no. of cars : ");
		Scanner sc = new Scanner(System.in);
		no_of_cars = sc.nextInt();
		System.out.println("Please enter the max Simulation time : ");
		max_simulation_time = sc.nextDouble();
		/*System.out.println("Please enter the Grid size : ");
		/*int gridSize = sc.nextInt();
		GridConfig.grid_size = gridSize - 1;
		GridConfig.NO_OF_AVENUES = GridConfig.grid_size + 1;
		GridConfig.NO_OF_STREETS = GridConfig.grid_size + 1;		
		GridConfig.no_of_intersections = (GridConfig.NO_OF_AVENUES - 2) * (GridConfig.NO_OF_STREETS - 2);
		GridConfig.grid =  new int[GridConfig.NO_OF_AVENUES - 1 ][GridConfig.NO_OF_STREETS - 1];
		GridConfig.entry_points = new int[2*(GridConfig.grid_size-1)][2];
		GridConfig.exit_points = new int[2*(GridConfig.grid_size-1)][2];*/
		System.out.println("Please enter the arrival rate Lambda for Dumb Scheduling : ");
		EventScheduler.lambda = sc.nextInt();
		System.out.println("The arrival rate is set to " + EventScheduler.lambda);
		generateEntryExit();
		initializeTrafficLights();
		initializeRoads();
		//System.exit(0);
		initializeSimulation();
		
	}
	public static void initializeRoads() {
		
		for (int i = 0 ; i < GridConfig.NO_OF_STREETS ; i++) {
			for(int j = 0; j <= GridConfig.NO_OF_AVENUES - 1; j++) {
				
				if((j + 1) == GridConfig.NO_OF_AVENUES) {
					continue;
				}
				rc[i][j][i][j+1] = new RoadConstructor(i,j,i,j+1);
				rc[i][j+1][i][j] = new RoadConstructor(i,j+1,i,j);
				System.out.println(i+","+j + "==========" + i+","+(j+1) + " road is created");
				System.out.println(i+","+(j+1) + "==========" + i+","+j + " road is created");
			}
		}
		for (int j = 0 ; j < GridConfig.NO_OF_AVENUES ; j++) {
			for(int i = 0 ; i <= GridConfig.NO_OF_STREETS - 1; i++) {
				
				if((i + 1) == GridConfig.NO_OF_STREETS) {
					continue;
				}
				rc[i][j][i+1][j] = new RoadConstructor(i,j,i+1,j);
				rc[i+1][j][i][j] = new RoadConstructor(i+1,j,i,j);
				System.out.println(i+","+j + "==========" + (i+1)+","+ j + " road is created");
				System.out.println((i+1)+","+j + "==========" +i+","+ j + " road is created");
			}
		}	
		//System.exit(0);
	}
	
	/*public static void initializeIntersections() {
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
				if ((i == 0 && (j == 0 || j == GridConfig.NO_OF_AVENUES - 1)) || (i == GridConfig.NO_OF_STREETS - 1 && (j == 0 || j ==GridConfig.NO_OF_AVENUES - 1))) {
					intersections[i][j] = null;
					continue;
				}
				if ()
			}
		}
	}*/
	public static void initializeTrafficLights() {
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
					if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
						tl[i][j][0] = null;
						tl[i][j][1] = null;
						continue;
					}
					int index =  r.nextInt(randomlights.length);
					int light = randomlights[index];
					int light1 ;
					if (light == 0) {
						light1 = 1;
					}
					else {
						light1 = 0;
					}
					tl[i][j][0] = new TrafficLightsState(i,j,0,light);
					tl[i][j][1] = new TrafficLightsState(i,j,1,light1);
					System.out.println("the state of Traffic light at intersection " + i + " , " + j + " Street is " + tl[i][j][0].get_current_light());
					System.out.println("the state of Traffic light at intersection " + i + " , " + j + " Avenue is " + tl[i][j][1].get_current_light());
			}
		}
	}
	
	public static void generateEntryExit(){
		int k=0,l=0;
		
		
		for (int i=0; i<=GridConfig.grid_size; i++){
			for(int j=0; j<=GridConfig.grid_size ; j++){
			
				if(i==0){
					if((j!=0) &&(j!=GridConfig.grid_size)){
						if(j%2==0){
							GridConfig.exit_points[k][0]=0;
							GridConfig.exit_points[k][1]=j;
							k++;
							GridConfig.entry_points[l][0]=j;
							GridConfig.entry_points[l][1]=0;
							l++;
							
						}
						else{
							GridConfig.entry_points[l][0]=0;
							GridConfig.entry_points[l][1]=j;
							l++;
							
							GridConfig.exit_points[k][0]=j;
							GridConfig.exit_points[k][1]=0;
							k++;
						}
					}
				}
				
				if(i==GridConfig.grid_size){
					if((j!=0) &&(j!=GridConfig.grid_size)){
						if(j%2==0){
							GridConfig.entry_points[l][0]=GridConfig.grid_size;
							GridConfig.entry_points[l][1]=j;
							l++;
							
							GridConfig.exit_points[k][0]=j;
							GridConfig.exit_points[k][1]=GridConfig.grid_size;
							k++;
							
						}
						else{							
							GridConfig.exit_points[k][0]=GridConfig.grid_size;
							GridConfig.exit_points[k][1]=j;
							k++;
							GridConfig.entry_points[l][0]=j;
							GridConfig.entry_points[l][1]=GridConfig.grid_size;
							l++;
							
						}
					}
				}
				
			}
		}
		
	}
	public static void initializeSimulation() throws Exception{
		
		//Event e = new Event(1,1);  
		//EventList.queue(e);	//Schedule first car arrival
		es.scheduleNextArrival(0.0);
		for (int i = 0;i < GridConfig.NO_OF_STREETS; i++) {
			for (int j = 0;j < GridConfig.NO_OF_AVENUES; j++) {
				if(i == 0 || j == 0 || i == GridConfig.NO_OF_STREETS - 1 || j == GridConfig.NO_OF_AVENUES - 1) {
					continue;
				}
				else {
					if(tl[i][j][0].get_current_light()=="red") {
						Event e1 = new Event(11,clock + TrafficLightsState.red_time,i,j,0);
						EventList.queue(e1);
					}
					else if (tl[i][j][0].get_current_light()=="yellow"){
						Event e1 = new Event(10,clock + TrafficLightsState.yellow_time,i,j,0);
						EventList.queue(e1);
					}
					else if (tl[i][j][0].get_current_light()=="green") {
						Event e1 = new Event(12,clock,i,j,0);
						EventList.queue(e1);
					}
					if(tl[i][j][1].get_current_light()=="red") {
						Event e1 = new Event(11,clock + TrafficLightsState.red_time,i,j,1);
						EventList.queue(e1);
					}
					else if (tl[i][j][1].get_current_light()=="yellow"){
						Event e1 = new Event(10,clock + TrafficLightsState.yellow_time,i,j,1);
						EventList.queue(e1);
					}
					else if (tl[i][j][1].get_current_light()=="green") {
						Event e1 = new Event(12,clock,i,j,1);
						EventList.queue(e1);
					}					
				}
			}
		}
		//Event e1 = new Event(2,TrafficLightsState.green_time);
		//EventList.queue(e1);
		//Event e2 = new Event(3,TrafficLightsState.red_time);
		//EventList.queue(e2);
		//Event e3 = new Event(4,27);
		//EventList.queue(e3);
		System.out.println("Simulation started");
		startSimulation();
	}
	public static void startSimulation() throws Exception {
		
		int traffic_light_event_counter = 0;
		while(Simulator.clock != max_simulation_time) {
			
			Event e = EventList.getEvent();
			EventList.dequeue();
			if(e.getType() == 1) {
				ep.performArrival(e);
			}
			/*else if (e.getType() == 2) {
				ep.greenToYellow(e);
			}
			else if (e.getType() == 3) {
				ep.yellowToRed(e);
			}
			*/
			else if (e.getType() == 4) {
				CarState c = e.getCarState();
				ep.reachedIntersection(e, c);
			}
			else if (e.getType() == 5) {
				CarState c = e.getCarState();
				ep.carAdvanced(e,c);
			}
			else if (e.getType() == 6) {
				CarState c = e.getCarState();
				ep.LeftIntersection(e,c);
			}
			else if (e.getType() == 7) {
				CarState c = e.getCarState();
				ep.carGridExit(e, c);
			}
			else if (e.getType() == 8) {
				ep.stillGreen(e);
			}
			else if (e.getType() == 9) {
				traffic_light_event_counter++;
				ep.individualGreenToYellow(e);
			}
			else if (e.getType() == 10) {
				traffic_light_event_counter++;
				ep.individualYellowToRed(e);
			}
			else if (e.getType() == 11) {
				traffic_light_event_counter++;
				ep.individualRedToGreen(e);
			}
			else if (e.getType() == 12) {
				traffic_light_event_counter++;
				ep.individualGreenToGreen(e);
			}
		}
		System.out.println("total cars exited = " + EventPerformer.total_cars_exited);
		System.out.println("The average time in system for this simulation cycle was " + Statistics.calAverageTimeinSystem());
		System.out.println("The average waiting time for a car in this simulation cycle was " + Statistics.calAverageWaitingTime());
		System.out.println("the total traffic light events were : " + traffic_light_event_counter);
	}	

}
