package disease_SimV2;
/**
 * @author Scott Brunton
 *
 */
import java.util.List;


import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.SimUtilities;

public class Vaccinated extends Person{

	public Vaccinated(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		//gets grid location of this agent and creates a list of all directly adjacent grid cells 
		//then randomly selects a cell from list and moves there
		GridPoint pt = grid.getLocation(this);
		
		GridCellNgh<Object> nghCreator = new GridCellNgh<Object>(grid, pt, Object.class, 1, 1);
		List<GridCell<Object>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
		
		GridCell<Object> cell = gridCells.get(0);
		
		GridPoint ptNew = cell.getPoint();
		moveTowards(ptNew);
		endRunCheck();
	}
}
