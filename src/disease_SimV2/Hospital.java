package disease_SimV2;
/**
 * @author Scott Brunton
 *
 */
import repast.simphony.parameter.Parameters;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Hospital {

	//init and retrieves parameters set by user 
	Parameters params = RunEnvironment.getInstance().getParameters();
	private int numBeds = (Integer)params.getValue("numBeds");

	public int currentCapacity;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	public Hospital(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
		this.currentCapacity = numBeds;
	}
	
	//boolean method checks if space is available for agent to go hospital
	public boolean checkFreeBed() {
		if (currentCapacity > 0) {
		return true;
		}
		return false;
	}
}
