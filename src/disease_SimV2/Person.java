package disease_SimV2;
/**
 * @author Scott Brunton
 *
 */
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Person {
	
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected boolean symptoms;	
	protected boolean severeSymptoms;
	protected boolean hospitalised;
	protected int daysInfected;
	protected Hospital hospital;

	//init and retrieves parameters set by user 
	Parameters params = RunEnvironment.getInstance().getParameters();
	private float mobility = (Float)params.getValue("mobility");
	
	public Person(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
	
	
		
	//method finds agents location and moves to new location checking that the new location is not equal to current location
	public void moveTowards(GridPoint pt) {
			if (!pt.equals(grid.getLocation(this))) {
				NdPoint myPoint = space.getLocation(this);
				NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
				double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
				space.moveByVector(this, mobility, angle, 0);
				myPoint = space.getLocation(this);
				grid.moveTo(this, (int) myPoint.getX(), (int) myPoint.getY());
			}
		}
		
	//method checks if the instances of infected agents is not equal to 0, if it is the run will end
	public void endRunCheck() {
		Context<Object> context = ContextUtils.getContext(this);
		if(context !=null) {
			if (context.getObjects(InfectedvA.class).size() == 0 && context.getObjects(InfectedvB.class).size() == 0) {
				RunEnvironment.getInstance().endRun();
			}
		}
	}
}
