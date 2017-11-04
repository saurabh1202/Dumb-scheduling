package trafficSimulation2;

public class Event {
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
}
