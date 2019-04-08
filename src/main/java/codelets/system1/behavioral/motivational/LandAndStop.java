/**
 * 
 */
package main.java.codelets.system1.behavioral.motivational;

import java.util.ArrayList;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.meca.models.ActionSequencePlan;
import br.unicamp.meca.system1.codelets.BehaviorCodelet;

/**
 * @author andre
 *
 */
public class LandAndStop extends BehaviorCodelet {			

	public LandAndStop(String id, ArrayList<String> perceptualCodeletsIds, ArrayList<String> motivationalCodeletsIds,
			String soarCodeletId, ActionSequencePlan actionSequencePlan) {
		
		super(id, perceptualCodeletsIds, motivationalCodeletsIds, soarCodeletId,actionSequencePlan);
		
	}

	@Override
	public void trackActionSequencePlan(Memory worldSituation, ActionSequencePlan actionSequencePlan) {
		// TODO Auto-generated method stub
		
	}

}
