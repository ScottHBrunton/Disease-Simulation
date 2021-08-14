
package disease_SimV2.chart;

import java.util.Map;
import java.util.HashMap;

import repast.simphony.statecharts.*;
import repast.simphony.statecharts.generator.GeneratedFor;

import disease_SimV2.*;

@GeneratedFor("_4TXEUPpaEeuESsVKgGs85g")
public class Statechart extends DefaultStateChart<disease_SimV2.Person> {

	public static Statechart createStateChart(disease_SimV2.Person agent, double begin) {
		Statechart result = createStateChart(agent);
		StateChartScheduler.INSTANCE.scheduleBeginTime(begin, result);
		return result;
	}

	public static Statechart createStateChart(disease_SimV2.Person agent) {
		StatechartGenerator generator = new StatechartGenerator();
		return generator.build(agent);
	}

	private Statechart(disease_SimV2.Person agent) {
		super(agent);
	}

	private static class MyStateChartBuilder extends StateChartBuilder<disease_SimV2.Person> {

		public MyStateChartBuilder(disease_SimV2.Person agent, AbstractState<disease_SimV2.Person> entryState,
				String entryStateUuid) {
			super(agent, entryState, entryStateUuid);
			setPriority(0.0);
		}

		@Override
		public Statechart build() {
			Statechart result = new Statechart(getAgent());
			setStateChartProperties(result);
			return result;
		}
	}

	private static class StatechartGenerator {

		private Map<String, AbstractState<Person>> stateMap = new HashMap<String, AbstractState<Person>>();

		public Statechart build(Person agent) {
			throw new UnsupportedOperationException("Statechart has not been defined.");

		}

		private void createTransitions(MyStateChartBuilder mscb) {

		}

	}
}
