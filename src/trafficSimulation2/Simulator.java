package trafficSimulation2;
import java.util.*;

import gridConfiguration.GridConfig;

public class Simulator {
	
	public static double clock = 0.0;
	public static Random r = new Random();
	public static EventPerformer ep = new EventPerformer();
	public static int no_of_cars = 1000;
	public static int current_cars = 1;
	public static double max_simulation_time = 1000;
	public static TrafficLightsState tl [][][] = new TrafficLightsState [GridConfig.NO_OF_STREETS][GridConfig.NO_OF_AVENUES][2];
	//public static Intersections intersections [][] = new Intersections [GridConfig.NO_OF_STREETS][GridConfig.NO_OF_AVENUES];
	public static RoadConstructor rc[][][][] = new RoadConstructor[GridConfig.NO_OF_STREETS][GridConfig.NO_OF_AVENUES][GridConfig.NO_OF_STREETS][GridConfig.NO_OF_AVENUES];
	public static int randomlights[] = {0,1};
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		generateEntryExit();
		initializeTrafficLights();
		initializeRoads();
		//System.exit(0);
		initializeSimulation();
		
	}
	public static void initializeRoads() {
		
		for (int i = 0 ; i < GridConfig.NO_OF_STREETS ; i++) {
			for(int j = 0; j < GridConfig.NO_OF_AVENUES - 1; j++) {

				rc[i][j][i][j+1] = new RoadConstructor(i,j,i,j+1);
				
			}
		}
		for (int j = 0 ; j < GridConfig.NO_OF_AVENUES ; j++) {
			for(int i = 0 ; i < GridConfig.NO_OF_STREETS - 1; i++) {
				
				rc[i][j][i+1][j] = new RoadConstructor(i,j,i+1,j);
				
			}
		}
		
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
		
		Event e = new Event(1,2);  
		EventList.queue(e);	//Schedule first car arrival
		Event e1 = new Event(2,TrafficLightsState.green_time);
		EventList.queue(e1);
		Event e2 = new Event(3,TrafficLightsState.red_time);
		EventList.queue(e2);
		//Event e3 = new Event(4,27);
		//EventList.queue(e3);
		System.out.println("Simulation started");
		startSimulation();
	}
	public static void startSimulation() throws Exception {
	
		while(clock < max_simulation_time && current_cars <= no_of_cars) {
			
			Event e = EventList.getEvent();
			EventList.dequeue();
			if(e.getType() == 1) {
				ep.performArrival(e);
			}
			else if (e.getType() == 2) {
				ep.greenToYellow(e);
			}
			else if (e.getType() == 3) {
				ep.yellowToRed(e);
			}
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
		}
		System.out.println("total cars exited = " + EventPerformer.total_cars_exited);
		System.out.println("The average time in system for this simulation cycle was " + Statistics.calAverageTimeinSystem());
		System.out.println("The average waiting time for a car in this simulation cycle was " + Statistics.calAverageWaitingTime());
	}	
}
