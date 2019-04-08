/**
 * 
 */
package main.java.codelets.system1.behavioral.motivational;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.meca.system1.codelets.MotivationalBehavioralCodelet;

/**
 * @author andre
 *
 */
public class ConserveEnergy extends MotivationalBehavioralCodelet {
	
	private ArrayList<Memory> drivesMO;
	
	private Memory motionCommandMemory;

	public ConserveEnergy(String id, String motorCodeletId, ArrayList<String> motivationalCodeletsIds,
			String soarCodeletId) {
		
		super(id, motorCodeletId, motivationalCodeletsIds, soarCodeletId);
		
	}

	@Override
	public void accessMemoryObjects() {
		
		int index=0;
		
		if(motionCommandMemory==null && motorCodeletId!=null)
			motionCommandMemory = this.getOutput(motorCodeletId, index);
		
		if(drivesMO==null||drivesMO.size()==0)
		{
			drivesMO = new ArrayList<>();

			if(getMotivationalCodeletsIds()!=null){

				for(String motivationalCodeletsId : getMotivationalCodeletsIds())
				{
					Memory inputDrive = this.getInput(motivationalCodeletsId + "_DRIVE_MO");
					drivesMO.add(inputDrive);
				}
			}
		}

	}


	@Override
	public void calculateActivation() {
		
		double activation = 0;
		
		if (drivesMO!=null && drivesMO.size() > 0){

			for (Memory driveMO: drivesMO) {
				activation += driveMO.getEvaluation();
			}

			activation /= drivesMO.size();

		}
		
		try {

			if(activation<0.0d)
				activation=0.0d;

			if(activation>1.0d)
				activation=1.0d;

			setActivation(activation);

		} catch (CodeletActivationBoundsException e) {
			e.printStackTrace();
		}

	}

	
	@Override
	public void proc() {
		
		List<Float> velocitiesAxis = new ArrayList<>();
		velocitiesAxis.add(0.0f);
		velocitiesAxis.add(0.0f);
		velocitiesAxis.add(-0.2f);
		
		((MemoryContainer) motionCommandMemory).setI(velocitiesAxis,getActivation(),id);

	}

}
