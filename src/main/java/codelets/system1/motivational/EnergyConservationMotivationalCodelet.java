/**
 * 
 */
package main.java.codelets.system1.motivational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.cst.motivational.Drive;
import br.unicamp.meca.system1.codelets.MotivationalCodelet;

/**
 * @author andre
 *
 */
public class EnergyConservationMotivationalCodelet extends MotivationalCodelet {


	public EnergyConservationMotivationalCodelet(String id, double level, double priority, double urgencyThreshold,
			ArrayList<String> sensoryCodeletsIds, HashMap<String, Double> motivationalCodeletsIds)
			throws CodeletActivationBoundsException {
		super(id, level, priority, urgencyThreshold, sensoryCodeletsIds, motivationalCodeletsIds);

	}

	@Override
	public double calculateSimpleActivation(List<Memory> sensors) {
		
		double activation = 0.0d;
		
		if (sensors != null && sensors.size() > 0) {
			
			List<Number> bodyMeasures = null;
			
			for (Memory sensoryMemory : sensors) {
				if (sensoryMemory != null && sensoryMemory.getName() != null && sensoryMemory.getName().contains("BodySensor") && sensoryMemory.getI() instanceof ArrayList) {
					bodyMeasures = (ArrayList<Number>) sensoryMemory.getI();
	            }
			}
			
			if(bodyMeasures != null && bodyMeasures.size() > 0) {
				
				short batteryState = (short) bodyMeasures.get(0);
				
				if(batteryState == 3) {
					activation = 1.0d;
				}else {
					activation = 0.0d;
				}	
			}else {
				activation = 0.0d;
			}			
		}else {
			activation = 0.0d;
		}	
		
//		System.out.println("activation: "+activation);
		
		return activation;
	}

	@Override
	public double calculateSecundaryDriveActivation(List<Memory> sensors, List<Drive> listOfDrives) {
		return 0;
	}
}
