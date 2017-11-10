package trafficSimulation2;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class RoadConstructor extends CarState{
	public int start_street ;
	public int start_avenue ; 
	public int end_street ; 
	public int end_avenue ;
	public String lane;
	public ArrayBlockingQueue<CarState> leftLane = new ArrayBlockingQueue<CarState>(100);
	public ArrayBlockingQueue<CarState> middleLane = new ArrayBlockingQueue<CarState>(100);
	public ArrayBlockingQueue<CarState> rightLane = new ArrayBlockingQueue<CarState>(100);
	public RoadConstructor(int i , int j , int x , int y){
		super(i,j,x,y);
		
		start_street = i;
		start_avenue = j ;
		end_street = x;
		end_avenue = y;
		//System.out.println("A road for " + start_street + " , " + start_avenue + " to " + end_street + " , " + end_avenue + " has been created");
		
		
	}
	public void setLane(int _lane) {
	
		int x = _lane ;
		if(x == 10) {
			lane = "left";
		}
		else if(x == 20) {
			lane = "middle";
		}
		else if (x == 30) {
			lane = "right";
		}
	}
	public int getCurrentCapacity() {
		if(lane == "left") {
			return leftLane.remainingCapacity();
		}
		else if (lane == "right") {
			return rightLane.remainingCapacity();
		}
		else if (lane == "middle") {
			return middleLane.remainingCapacity();
		}
		return 0;
	}
	
	public void addCarToLane(CarState c) {
		if (lane == "left") {
			leftLane.add(c);
		}
		else if (lane == "right") {
			rightLane.add(c);
		}
		else if (lane == "middle") {
			middleLane.add(c);
		}
	}
	public CarState removeCarFromLane() {
		if (lane == "left") {
			CarState c = leftLane.remove();
			return c;
		}
		else if (lane == "right") {
			CarState c = rightLane.remove();
			return c;
		}
		else if (lane == "middle") {
			CarState c = middleLane.remove();
			return c;
		}
		return null;
	}
}
