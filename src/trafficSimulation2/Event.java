package trafficSimulation2;

public class Event implements Comparable<Event>{
	public double time;
	private int type;
	private CarState c;
	int i,j,x,y,road,left_lane_counter,middle_lane_counter,right_lane_counter;
	public Event(int _type , double _time , int _i , int _j , int _x , int _y , int _road , int _lc , int _mc , int _rc) {
		type = _type;
		time = _time;
		i = _i;
		j = _j;
		x = _x;
		y = _y;
		road = _road;
		left_lane_counter = _lc;
		middle_lane_counter = _mc;
		right_lane_counter = _rc;
	}
	public Event(int _type, double _time) {
		type = _type;
		time = _time;
	}
	public Event(int _type, double _time , CarState _c) {
		type = _type;
		time = _time;
		c = _c;
	}
	public int getType() {
		return type;
	}
	public double getTime() {
		return time;
	}
	public CarState getCarState() {
		return c;
	}
	public int compareTo(Event e) {
		if(this.getTime() < e.getTime()) {
			return -1;
		}
		else if (this.getTime() == e.getTime()) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
}
