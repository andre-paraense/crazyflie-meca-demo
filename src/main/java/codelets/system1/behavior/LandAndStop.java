/**
 * 
 */
package codelets.system1.behavior;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.meca.models.ActionSequencePlan;
import br.unicamp.meca.system1.codelets.BehaviorCodelet;

/**
 * @author andre
 *
 */
public class LandAndStop extends BehaviorCodelet {	
	
	private static float SAFE_LANDING_DISTANCE = 0.9f;

	public LandAndStop(String id, ArrayList<String> perceptualCodeletsIds, ArrayList<String> motivationalCodeletsIds,
			String soarCodeletId, ActionSequencePlan actionSequencePlan) {
		
		super(id, perceptualCodeletsIds, motivationalCodeletsIds, soarCodeletId,actionSequencePlan);		
	}

	@Override
	public void trackActionSequencePlan(ArrayList<Memory> perceptualMemories, ActionSequencePlan actionSequencePlan) {
		
		if(actionSequencePlan != null && actionSequencePlan.getActionIdSequence() != null) {
			
			actionSequencePlan.setCurrentActionIdIndex(0);
			
			if(perceptualMemories != null && perceptualMemories.size() > 0) {
				
				Memory worldSituation = perceptualMemories.get(0);
				
				if(worldSituation!=null && worldSituation.getI()!=null && worldSituation.getI() instanceof ArrayList){
					
					List<Number> bodyPerceptions = (List<Number>) worldSituation.getI();
					
					float downRange = (float) bodyPerceptions.get(6);
					
					if(downRange >= SAFE_LANDING_DISTANCE) {
						
						actionSequencePlan.setCurrentActionIdIndex(1);
					}					
				}				
			}
		}		
	}
}
