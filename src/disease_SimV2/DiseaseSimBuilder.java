package disease_SimV2;
/**
 * @author Scott Brunton
 *
 */
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class DiseaseSimBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {

		//builds network for infected infectee links
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("infection network", context, true);
		netBuilder.buildNetwork();
		
		context.setId("Disease SimV2");

		//builds map
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), 50, 50);
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new WrapAroundBorders(), new SimpleGridAdder<Object>(), true, 50, 50));
		
		//init and retrieves parameters set by user 
		Parameters params = RunEnvironment.getInstance().getParameters();
		int infectedvACount = (Integer)params.getValue("numInfectedvA");
		int infectedvBCount = (Integer)params.getValue("numInfectedvB");
		int numSusceptible = (Integer)params.getValue("numSusceptible");
		int numVaccinated = (Integer)params.getValue("numVaccinated");

		//creates infectedvA agents up to num set by user in params
		for (int i = 0; i < infectedvACount; i++) {
			context.add(new InfectedvA(space, grid));
		}
		
		//creates infectedvB agents up to num set by user in params
		for (int i = 0; i < infectedvBCount; i++) {
			context.add(new InfectedvB(space, grid));
		}
		
		//creates susceptible agents up to num set by user in params
		for (int i = 0; i < numSusceptible; i++) {
			context.add(new Susceptible(space, grid));
		}
		
		//creates Vaccinated agents up to num set by user in params
		for (int i = 0; i < numVaccinated; i++) {
			context.add(new Vaccinated(space, grid));
		}

		//creates hospital agent
		context.add(new Hospital(space, grid));
		
		
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		
		
		return context;
	}
	


}
