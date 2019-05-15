/**
 * 
 */
package codelets.system1.action;

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
	
	private final static float EXPLORE_VELOCITY = 0.2f;
	
	private final static int FRONT = 1;	
	private final static int BACK = 2;
	private final static int LEFT = 3;
	private final static int RIGHT = 4;


	public MoveSomewhere(String id, ArrayList<String> perceptualCodeletsIds, ArrayList<String> motivationalCodeletsIds,
			String motorCodeletId, String soarCodeletId) {
		super(id, perceptualCodeletsIds, motivationalCodeletsIds, motorCodeletId, soarCodeletId);
	}

	@Override
	public void proc(ArrayList<Memory> perceptualMemories, Memory broadcastMemory, Memory motorMemory) {
		
		int directionIndex = -1;
		
		Random random = new Random(System.currentTimeMillis());
		
		double next = random.nextDouble();
		
		if(next > 0.5d) {
			directionIndex = FRONT;
		} else if(next > 0.3d) {
			directionIndex = RIGHT;
		} else if(next > 0.1d) {
			directionIndex = LEFT;
		} else {
			directionIndex = BACK;
		}
		
		List<Float> velocitiesAxis = new ArrayList<>();
		
		switch (directionIndex) {
		case BACK:
			velocitiesAxis.add(-1.0f*EXPLORE_VELOCITY);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			break;
		case FRONT:
			velocitiesAxis.add(EXPLORE_VELOCITY);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			break;
		case RIGHT:
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(EXPLORE_VELOCITY);
			velocitiesAxis.add(0.0f);
			break;
		case LEFT:
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(-1.0f*EXPLORE_VELOCITY);
			velocitiesAxis.add(0.0f);
			break;
		default:
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			break;
		}
		
		((MemoryContainer) motorMemory).setI(velocitiesAxis,getActivation(),id);

	}

}
