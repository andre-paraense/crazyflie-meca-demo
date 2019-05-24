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
public class ExploreMotivationalCodelet extends MotivationalCodelet {


	public ExploreMotivationalCodelet(String id, double level, double priority, double urgencyThreshold,
			ArrayList<String> sensoryCodeletsIds, HashMap<String, Double> motivationalCodeletsIds)
			throws CodeletActivationBoundsException {
		super(id, level, priority, urgencyThreshold, sensoryCodeletsIds, motivationalCodeletsIds);
	}

	@Override
	public double calculateSecundaryDriveActivation(List<Memory> sensoryMemories, List<Drive> listOfDrives) {
		return 0;
	}

	@Override
	public double calculateSimpleActivation(List<Memory> sensoryMemories) {
		
		double activation = 0.1d;
		
		if (sensoryMemories != null && sensoryMemories.size() > 0) {
			
			List<Number> bodyMeasures = null;
			
			for (Memory sensoryMemory : sensoryMemories) {
				if (sensoryMemory != null && sensoryMemory.getName() != null && sensoryMemory.getName().contains("BodySensor") && sensoryMemory.getI() instanceof ArrayList) {
					bodyMeasures = (ArrayList<Number>) sensoryMemory.getI();
                }
			}
			
			if(bodyMeasures != null && bodyMeasures.size() > 0) {
				
				for(int i =0; i < bodyMeasures.size(); i++) {
					
					float range = (float) bodyMeasures.get(i);					
					float rangeActivation = 0.0f;
					if(i < 4) {
						rangeActivation = SituationPerceptualCodelet.SAFE_RANGE_SIDES / range;
					} else if (i == 4) {
						rangeActivation = SituationPerceptualCodelet.SAFE_RANGE_UP / range;
					} else {
						rangeActivation = SituationPerceptualCodelet.SAFE_RANGE_DOWN / range;
					}
					
					if(rangeActivation > 0.99f && rangeActivation > activation) {
						
						activation = rangeActivation;
					}				
				}	
			}	
		}
		
		activation = 1.0d - activation;
		
		if(activation<0.0d)
			activation=0.0d;

		if(activation>1.0d)
			activation=1.0d;

		
		return activation;
	}

}
