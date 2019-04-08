/**
 * 
 */
package main.java.codelets.system1.perceptual;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.meca.system1.codelets.PerceptualCodelet;

/**
 * @author andre
 *
 */
public class SituationPerceptualCodelet extends PerceptualCodelet {
	
	public static final float SAFE_RANGE = 200.0f;
	
    private ArrayList<Memory> sensoryMemories = new ArrayList<Memory>();

    private Memory worldSituation;

	public SituationPerceptualCodelet(String id, ArrayList<String> sensoryCodeletsIds) {
		super(id, sensoryCodeletsIds);
	}

	@Override
	public void accessMemoryObjects() {
		
        int index = 0;

        if (worldSituation == null)
        	worldSituation = this.getOutput(id, index);

        if (sensoryMemories == null || sensoryMemories.size() == 0) {
            if (sensoryCodeletsIds != null) {

                for (String sensoryCodeletsId : sensoryCodeletsIds) {
                    sensoryMemories.add(this.getInput(sensoryCodeletsId, index));
                }
            }
        }
	}

	@Override
	public void calculateActivation() {
		try{

			setActivation(0.0d);

		} catch (CodeletActivationBoundsException e) {

			e.printStackTrace();
		}	
	}

	@Override
	public void proc() {
		
		if (sensoryMemories != null && sensoryMemories.size() > 0) {
			
			List<Number> bodyMeasures = null;
			
			for (Memory sensoryMemory : sensoryMemories) {
				if (sensoryMemory != null && sensoryMemory.getName() != null && sensoryMemory.getName().contains("BodySensor") && sensoryMemory.getI() instanceof ArrayList) {
					bodyMeasures = (ArrayList<Number>) sensoryMemory.getI();
                }
			}
			
			if(bodyMeasures != null && bodyMeasures.size() > 0) {
				List<Number> bodyPerceptions = new ArrayList<>();
				
				/*
				 * Battery sense is already a perception given.
				 */
				bodyPerceptions.add(bodyMeasures.get(0));
				
				/*
				 * Range senses must be interpreted as perceptions of how close we are
				 */
				for(int i =1; i < bodyMeasures.size(); i++) {
					
					float range = (float) bodyMeasures.get(i);
					float activation = SAFE_RANGE / range;
					if (activation < 0.0f)
		                activation = 0.0f;

		            if (activation > 1.0f)
		                activation = 1.0f;
		            
		            bodyPerceptions.add(activation);
					
				}
				
				worldSituation.setI(bodyPerceptions);
				
//				System.out.println("Battery state: "+bodyPerceptions.get(0));
//				System.out.println("Front: "+bodyPerceptions.get(1));
//				System.out.println("Back: "+bodyPerceptions.get(2));
//				System.out.println("Left: "+bodyPerceptions.get(3));
//				System.out.println("Right: "+bodyPerceptions.get(4));
//				System.out.println("Up: "+bodyPerceptions.get(5));
//				System.out.println("Down: "+bodyPerceptions.get(6));
				
			} else {
				worldSituation.setI(null);
			}
		} else {
			worldSituation.setI(null);
		}
	}
}
