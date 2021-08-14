package disease_SimV2;
/**
 * @author Scott Brunton
 *
 */
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Infected extends Person{
	
	public Infected(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space,grid);
		this.daysInfected = 0;
		this.symptoms = false;
		this.hospital = null;
		this.severeSymptoms = false;
		this.hospitalised = false;
	}
	
	//method finds agent on grid, removes it and replaces with a dead agent
	public void die() {
		GridPoint pt = grid.getLocation(this);
		NdPoint spacePt = space.getLocation(this);
		Context<Object>  context = ContextUtils.getContext(this);
		if(context !=null) {
			context.remove(this);
			Dead dead = new Dead(space,grid);
			context.add(dead);
			space.moveTo(dead, spacePt.getX(), spacePt.getY());
			grid.moveTo(dead, pt.getX(), pt.getY());
		}
	}
		
	//method finds agent on grid, removes it and replaces with a recovered agent
	public void recover() {
		GridPoint pt = grid.getLocation(this);
		NdPoint spacePt = space.getLocation(this);
		Context<Object> context = ContextUtils.getContext(this);
		if(context !=null) {
			context.remove(this);
			Recovered recovered= new Recovered(space, grid);
			context.add(recovered);
			space.moveTo(recovered, spacePt.getX(), spacePt.getY());
			grid.moveTo(recovered, pt.getX(), pt.getY());
		}
	}
		
	//method finds hospital agent and checks if it has space for infected agent to move to it
	public void goHospital() {
		Context context = ContextUtils.getContext(this);
		if(context !=null) {
			for(Object agent : context) {
				if(agent instanceof Hospital) {
					 hospital = (Hospital) agent;
					if(hospital.currentCapacity > 0) {
						NdPoint target = space.getLocation(hospital);
						space.moveTo(this, (double)target.getX(), (double)target.getY());
						grid.moveTo(this, (int)target.getX(), (int)target.getY());
						hospital.currentCapacity--;
						hospitalised = true;
					}
				}
			}
		}
	}
		

}
