package trafficSimulation2;
import java.util.*;
import gridConfiguration.GridConfig;

public class TrafficLightsState {
	public static double green_time = 30.0;
	public static double yellow_time = 10.0;
	public static double red_time = 40.0;
	private int lightStreet;
	private int lightAvenue;
	private String roadType;
	private String currentLight;
	
	public TrafficLightsState(int _street,int _avenue , int _road_type , int _currentLight) {
		
		lightStreet = _street;
		lightAvenue = _avenue;
		if (_road_type == 0) {
			roadType = "street";
		}
		else {
			roadType = "avenue";
		}
		if (_currentLight == 0) {
			currentLight = "red";
		}
		else {
			currentLight = "green";
		}
	}
	public int getStreet() {
		return lightStreet;
	}
	public int getAvenue() {
		return lightAvenue;
	}
	public String getRoadType() {
		return roadType;
	}
	public void set_new_light(String s) {
		currentLight = s;
	}
	public String get_current_light() {
		return currentLight;
	}
}
