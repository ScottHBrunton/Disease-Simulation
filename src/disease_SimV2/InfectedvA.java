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

public class InfectedvA extends Infected{

	//init and retrieves parameters set by user 
	Parameters params = RunEnvironment.getInstance().getParameters();
	private int maxDaysvA = (Integer)params.getValue("maxDaysvA");
	private float chanceDeathvA = (Float)params.getValue("chanceDeathvA");
	private float chanceRecoveringvA = (Float)params.getValue("chanceRecoveringvA");
	private float chanceSevereSymptomsvA = (Float)params.getValue("chanceSevereSymptomsvA");
	private float infectionChancevA = (Float)params.getValue("infectionChancevA");
	private boolean masks = (Boolean)params.getValue("masks");
	private float maskEfficacy = (Float)params.getValue("maskEfficacy");
	private float chanceSymptomsvA = (Float)params.getValue("chanceSymptomsvA");
	private boolean qSymptomatic = (Boolean)params.getValue("qSymptomatic");
	private float chanceDeathHospitalisedvA = (Float)params.getValue("chanceDeathHospitalisedvA");
	
	public InfectedvA(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
	}
	
	
	//method executes every tick to move, infect() and checkSymptoms() if agent is not dead, recovered or hospitalised
	//checks if agent has recovered, died, developed symptoms or been hospitalised 
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
		if(daysInfected >= maxDaysvA) {
			recover();
		}
		if(severeSymptoms) {
			goHospital();
		}

	}
		
	//method creates list of all susceptible agents in adjacent cells
	//susceptible agents have a chance to be infected if the random r variable is less than the chance to infect
	//a link is created between infected and infectee via the network
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
			if(infectionChancevA >= r) {
				if(susceptible.size() > 0) {
					for(Object obj : susceptible) {
						NdPoint newPt = space.getLocation(obj);
						Context<Object> context = ContextUtils.getContext(obj);
						context.remove(obj);
						if(context != null) {
							InfectedvA infected = new InfectedvA(space, grid);
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
			if(infectionChancevA*maskEfficacy  >= r) {
				if(susceptible.size() > 0) {
					for(Object obj : susceptible) {
						NdPoint newPt = space.getLocation(obj);
						Context<Object> context = ContextUtils.getContext(obj);
						context.remove(obj);
						if(context != null) {
							InfectedvA infected = new InfectedvA(space, grid);
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
		if(hospitalised && r <= chanceDeathHospitalisedvA) {
			if (hospitalised && this.hospital != null) {
				this.hospital.currentCapacity++;
			}
			die();
			return true;
		}
		else if(severeSymptoms && !hospitalised && r <= chanceDeathvA) {
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
		if(r <= chanceRecoveringvA) {
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
		if(r <= chanceSymptomsvA) {
			symptoms = true;
			double a = Math.random();
			if(a <= chanceSevereSymptomsvA) {
				severeSymptoms = true;
			}
		}
	}
}
	

