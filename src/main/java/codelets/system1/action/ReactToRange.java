/**
 * 
 */
package main.java.codelets.system1.action;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.meca.system1.codelets.ActionCodelet;

/**
 * @author andre
 *
 */
public class ReactToRange extends ActionCodelet {
	
	private int directionIndex = -1;
	
	private final static float ESCAPE_VELOCITY = 0.5f;
	
	private final static int FRONT = 1;	
	private final static int BACK = 2;
	private final static int LEFT = 3;
	private final static int RIGHT = 4;
	private final static int UP = 5;
	private final static int DOWN = 6;

	public ReactToRange(String id, ArrayList<String> perceptualCodeletsIds, String motorCodeletId,
			String soarCodeletId, boolean isPlanedAction) {
		
		super(id, perceptualCodeletsIds, motorCodeletId, soarCodeletId, isPlanedAction);
	}

	@Override
	public void calculateActivation(ArrayList<Memory> perceptualMemories, Memory broadcastMemory, Memory actionSequencePlanMemoryContainer) {
		
		double activation = 0.0d;
		directionIndex = -1;
		
		if(perceptualMemories != null && perceptualMemories.size() > 0) {
			
			Memory worldSituation = perceptualMemories.get(0);
			
			if(worldSituation!=null && worldSituation.getI()!=null && worldSituation.getI() instanceof ArrayList){
				
				List<Number> bodyPerceptions = (List<Number>) worldSituation.getI();
				
				for(int i =1; i < bodyPerceptions.size(); i++) {
					
					float rangeActivation = (float) bodyPerceptions.get(i);
					
					if(rangeActivation > 0.8f && rangeActivation > activation) {
						activation = rangeActivation;
						directionIndex = i;
					}				
				}
			}
			
		}
		
		try 
		{
			if(activation<0.0d)
				activation=0.0d;

			if(activation>0.5d)
				activation=0.5d;

			this.setActivation(activation);

		} catch (CodeletActivationBoundsException e) 
		{			
			e.printStackTrace();
		}
	}

	@Override
	public void proc(ArrayList<Memory> perceptualMemories, Memory broadcastMemory, Memory actionSequencePlanMemoryContainer, Memory motorMemory) {
		
		List<Float> velocitiesAxis = new ArrayList<>();
		
		switch (directionIndex) {
		case FRONT:
			velocitiesAxis.add(-1.0f*ESCAPE_VELOCITY);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			break;
		case BACK:
			velocitiesAxis.add(ESCAPE_VELOCITY);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			break;
		case LEFT:
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(ESCAPE_VELOCITY);
			velocitiesAxis.add(0.0f);
			break;
		case RIGHT:
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(-1.0f*ESCAPE_VELOCITY);
			velocitiesAxis.add(0.0f);
			break;
		case UP:
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(-1.0f*ESCAPE_VELOCITY);
			break;
		case DOWN:
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(ESCAPE_VELOCITY);
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
