package trafficSimulation2;

public class Event implements Comparable<Event>{
	public double time;
	private int type;
	
	public Event(int _type, double _time) {
		type = _type;
		time = _time;
	}
	public int getType() {
		return type;
	}
	public double getTime() {
		return time;
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
