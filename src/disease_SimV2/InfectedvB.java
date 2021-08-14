package disease_SimV2;
/**
 * @author Scott Brunton
 *
 */
import java.util.ArrayList;

import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

public class InfectedvB extends Infected{
	
	//init and retrieves parameters set by user 
	Parameters params = RunEnvironment.getInstance().getParameters();
	private int maxDaysvB = (Integer)params.getValue("maxDaysvB");
	private float chanceDeathvB = (Float)params.getValue("chanceDeathvB");
	private float chanceSymptomsvB = (Float)params.getValue("chanceSymptomsvB");
	private float chanceRecoveringvB = (Float)params.getValue("chanceRecoveringvB");
	private float chanceSevereSymptomsvB = (Float)params.getValue("chanceSevereSymptomsvB");
	private float infectionChancevB = (Float)params.getValue("infectionChancevB");
	private float maskEfficacy = (Float)params.getValue("maskEfficacy");
	private boolean masks = (Boolean)params.getValue("masks");
	private boolean qSymptomatic = (Boolean)params.getValue("qSymptomatic");
	private float chanceDeathHospitalisedvB = (Float)params.getValue("chanceDeathHospitalisedvB");

	public InfectedvB(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space,grid);
		
	}
	
	//method executes every tick to move, infect() and checkSymptoms() if agent is not dead, recovered or hospitalised
	//checks if agent has recovered, died, developed symptoms or been hospitalised
	//a link is created between infected and infectee via the network
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		this.daysInfected++;
		boolean isRecovered = checkRecover();
		boolean isDead = checkDead();
			
			if(!isDead && !isRecovered && !hospitalised) {
				GridPoint pt = grid.getLocation(this);
				
				GridCellNgh<Object> nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, 1, 1);
				List<GridCell<Object>> gridCells = nghCreator.getNeighborhood(true);
				SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
				
				GridCell<Object> cell = gridCells.get(0);
				
				GridPoint ptNew = cell.getPoint();
				moveTowards(ptNew);
				
				infect();
				checkSymptoms();
			}
	
		if(daysInfected >= maxDaysvB) {
			recover();
		}
		if(severeSymptoms) {
			goHospital();
		}
	}
		
	//method creates list of all susceptible agents in adjacent cells
	//susceptible agents have a chance to be infected if the random r variable is less than the chance to infect
	public void infect() {
		GridPoint pt = grid.getLocation(this);
		List<Object> susceptible = new ArrayList<Object>();
		
		for(Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Susceptible) {
				susceptible.add(obj);
			}
		}
		double r = Math.random();
		if(masks != true) {
			if(infectionChancevB >= r) {
				if(susceptible.size() > 0) {
					for(Object obj : susceptible) {
						NdPoint newPt = space.getLocation(obj);
						Context<Object> context = ContextUtils.getContext(obj);
						context.remove(obj);
						if(context != null) {
							InfectedvB infected = new InfectedvB(space, grid);
							context.add(infected);
							space.moveTo(infected, newPt.getX(), pt.getY());
							grid.moveTo(infected, pt.getX(), pt.getY());
			
							Network<Object> net = (Network<Object>) context.getProjection("infection network");
							net.addEdge(this, infected);
						}
					}
				}
			}
		}
		else {
			if(infectionChancevB*maskEfficacy  >= r ) {
				if(susceptible.size() > 0) {
					for(Object obj : susceptible) {
						NdPoint newPt = space.getLocation(obj);
						Context<Object> context = ContextUtils.getContext(obj);
						context.remove(obj);
						if(context != null) {
							InfectedvB infected = new InfectedvB(space, grid);
							context.add(infected);
							space.moveTo(infected, newPt.getX(), pt.getY());
							grid.moveTo(infected, pt.getX(), pt.getY());
								
							Network<Object> net = (Network<Object>) context.getProjection("infection network");
							net.addEdge(this, infected);
						}
					}
				}
			}
		}
	}
	
	//checks whether agent will die
	public boolean checkDead() {
		double r = Math.random();
		if(hospitalised && r <= chanceDeathHospitalisedvB) {
			if (hospitalised && this.hospital != null) {
				this.hospital.currentCapacity++;
			}
			die();
			return true;
		}
		else if(severeSymptoms && !hospitalised && r <= chanceDeathvB) {
			if (hospitalised && this.hospital != null) {
				this.hospital.currentCapacity++;
			}
			die();
			return true;
		}
		return false;
	}
	
	//checks whether agent will recover
	public boolean checkRecover() {
		double r = Math.random();
		if(r <= chanceRecoveringvB) {
			if (hospitalised && this.hospital != null) {
				this.hospital.currentCapacity++;
			}
			recover();
			return true;
		}
		return false;
	}
	
	//checks whether agent will develop symptoms 
	public void checkSymptoms() {
		double r = Math.random();
		if(r <= chanceSymptomsvB) {
			symptoms = true;
			double a = Math.random();
			if(a <= chanceSevereSymptomsvB) {
				severeSymptoms = true;
			}
		}
	}
}
	

