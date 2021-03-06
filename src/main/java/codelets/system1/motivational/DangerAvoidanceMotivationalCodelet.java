/**
 * 
 */
package codelets.system1.motivational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.cst.motivational.Drive;
import br.unicamp.meca.system1.codelets.MotivationalCodelet;
import codelets.system1.perceptual.SituationPerceptualCodelet;

/**
 * @author andre
 *
 */
public class DangerAvoidanceMotivationalCodelet extends MotivationalCodelet {


	public DangerAvoidanceMotivationalCodelet(String id, double level, double priority, double urgencyThreshold,
			ArrayList<String> sensoryCodeletsIds, HashMap<String, Double> motivationalCodeletsIds)
			throws CodeletActivationBoundsException {
		super(id, level, priority, urgencyThreshold, sensoryCodeletsIds, motivationalCodeletsIds);
	}

	@Override
	public double calculateSecundaryDriveActivation(List<Memory> arg0, List<Drive> arg1) {
		return 0;
	}

	@Override
	public double calculateSimpleActivation(List<Memory> sensoryMemories) {
		
		double activation = 0.1d;
		
		if (sensoryMemories != null && sensoryMemories.size() > 0) {
			
			List<Number> multirangerMeasures = null;
			
			for (Memory sensoryMemory : sensoryMemories) {
				if (sensoryMemory != null && sensoryMemory.getName() != null && sensoryMemory.getName().contains("MultirangerSensor") && sensoryMemory.getI() instanceof ArrayList) {
					multirangerMeasures = (ArrayList<Number>) sensoryMemory.getI();
                }
			}
			
			if(multirangerMeasures != null && multirangerMeasures.size() > 0) {
				
				for(int i =0; i < multirangerMeasures.size(); i++) {
					
					float range = (float) multirangerMeasures.get(i);
					float rangeActivation = 0.0f;
					if(i == 2 || i== 3) {
						rangeActivation = SituationPerceptualCodelet.SAFE_RANGE_SIDES / range;
					} else if (i < 4) {
						rangeActivation = SituationPerceptualCodelet.SAFE_RANGE_UP_FRONT_BACK / range;
					} else {
						rangeActivation = SituationPerceptualCodelet.SAFE_RANGE_DOWN / range;
					}
					
					if(rangeActivation > 0.99f && rangeActivation > activation) {
						
						activation = rangeActivation;
					}				
				}	
			}	
		}
		
		if(activation<0.1d)
			activation=0.1d;

		if(activation>1.0d)
			activation=1.0d;

		
		return activation;
	}

}
