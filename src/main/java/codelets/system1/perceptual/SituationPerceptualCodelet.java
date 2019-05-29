/**
 * 
 */
package codelets.system1.perceptual;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.meca.system1.codelets.PerceptualCodelet;

/**
 * @author andre
 *
 */
public class SituationPerceptualCodelet extends PerceptualCodelet {
	
	public static final float SAFE_RANGE_SIDES = 10.0f;
	public static final float SAFE_RANGE_UP = 70.0f;
	public static final float SAFE_RANGE_DOWN =30.0f;
	public static final float VOLTAGE_THRESHOLD = 3.0f;

	public SituationPerceptualCodelet(String id, ArrayList<String> sensoryCodeletsIds) {
		super(id, sensoryCodeletsIds);
	}

	@Override
	public void proc(ArrayList<Memory> sensoryMemories, Memory perceptualMemory) {
		
		if (sensoryMemories != null && sensoryMemories.size() > 0) {
			
			List<Number> multiRangerMeasures = null;
			float batteryVoltage = -1.0f;
			
			for (Memory sensoryMemory : sensoryMemories) {
				if (sensoryMemory != null && sensoryMemory.getName() != null && sensoryMemory.getName().contains("MultirangerSensor") && sensoryMemory.getI() instanceof ArrayList) {
					multiRangerMeasures = (ArrayList<Number>) sensoryMemory.getI();
                } else if (sensoryMemory != null && sensoryMemory.getName() != null && sensoryMemory.getName().contains("BatterySensor") && sensoryMemory.getI() instanceof ArrayList) {
					List<Number> batteryMeasures = (ArrayList<Number>) sensoryMemory.getI();
					batteryVoltage = (float) batteryMeasures.get(1);
                }
			}
			
			if(multiRangerMeasures != null && multiRangerMeasures.size() > 0 && batteryVoltage != -1.0f) {
				List<Number> bodyPerceptions = new ArrayList<>();
				
				int batteryState = -1;
				if(batteryVoltage < VOLTAGE_THRESHOLD) {
					batteryState = 0;
				}else {
					batteryState = 1;
				}
				bodyPerceptions.add(batteryState);
				
				/*
				 * Range senses must be interpreted as perceptions of how close we are
				 */
				for(int i = 0; i < multiRangerMeasures.size(); i++) {
					
					float range = (float) multiRangerMeasures.get(i);
					float activation = 0.0f;
					if(i < 4) {
						activation = SAFE_RANGE_SIDES / range;
					} else if (i == 4) {
						activation = SAFE_RANGE_UP / range;
					} else {
						activation = SAFE_RANGE_DOWN / range;
					}
					
					if (activation < 0.0f)
		                activation = 0.0f;

		            if (activation > 1.0f)
		                activation = 1.0f;
		            
		            bodyPerceptions.add(activation);
					
				}
				
				perceptualMemory.setI(bodyPerceptions);
				
//				System.out.println("Battery state: "+bodyPerceptions.get(0));
//				System.out.println("Front: "+bodyPerceptions.get(1));
//				System.out.println("Back: "+bodyPerceptions.get(2));
//				System.out.println("Left: "+bodyPerceptions.get(3));
//				System.out.println("Right: "+bodyPerceptions.get(4));
//				System.out.println("Up: "+bodyPerceptions.get(5));
//				System.out.println("Down: "+bodyPerceptions.get(6));
				
			} else {
				perceptualMemory.setI(null);
			}
		} else {
			perceptualMemory.setI(null);
		}
	}
}
