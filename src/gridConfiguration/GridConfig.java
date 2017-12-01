package gridConfiguration;

public class GridConfig {	
		
	public static final int NO_OF_AVENUES = 10;
	public static final int NO_OF_STREETS = 10;
	public static int no_of_intersections = (NO_OF_AVENUES - 2) * (NO_OF_STREETS - 2);
	public static int grid_size = 9;
	public static int grid[][]= new int[NO_OF_AVENUES - 1 ][NO_OF_STREETS - 1];
	public static int entry_points[][]=new int[2*(grid_size-1)][2];
	public static int exit_points[][]=new int[2*(grid_size-1)][2];
	
}
