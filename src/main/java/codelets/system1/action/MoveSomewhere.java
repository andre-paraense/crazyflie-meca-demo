/**
 * 
 */
package main.java.codelets.system1.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.meca.system1.codelets.ActionFromPerception;

/**
 * @author andre
 *
 */
public class MoveSomewhere extends ActionFromPerception {
	
	private final static float EXPLORE_VELOCITY = 0.1f;

	public MoveSomewhere(String id, ArrayList<String> perceptualCodeletsIds, ArrayList<String> motivationalCodeletsIds,
			String motorCodeletId, String soarCodeletId) {
		super(id, perceptualCodeletsIds, motivationalCodeletsIds, motorCodeletId, soarCodeletId);
	}

	@Override
	public void proc(ArrayList<Memory> perceptualMemories, Memory broadcastMemory, Memory motorMemory) {
		
		List<Float> velocitiesAxis = new ArrayList<>();

		velocitiesAxis.add(EXPLORE_VELOCITY);
		velocitiesAxis.add(0.0f);
		velocitiesAxis.add(0.0f);
		
		((MemoryContainer) motorMemory).setI(velocitiesAxis,getActivation(),id);

	}

}
