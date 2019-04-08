/**
 * 
 */
package main.java.codelets.system1.behavioral.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.meca.system1.codelets.RandomBehavioralCodelet;

/**
 * @author andre
 *
 */
public class RandomMove extends RandomBehavioralCodelet {

	private Memory motionCommandMemory;

	public RandomMove(String id, String motorCodeletId, String soarCodeletId) {
		super(id, motorCodeletId, soarCodeletId);
	}

	@Override
	public void accessMemoryObjects() {

		int index=0;

		if(motionCommandMemory==null && motorCodeletId!=null)
			motionCommandMemory = this.getOutput(motorCodeletId, index);

	}

	@Override
	public void calculateActivation() {

		double activation=0.0d;	

		try{

			double rangeMin = 0.0d;
			double rangeMax = 1.0d;

			Random random = new Random(System.currentTimeMillis());

			activation = rangeMin + (rangeMax - rangeMin) * random.nextDouble();

			if(activation >= 0.999d) // Very hard for the random phase codelet to prevail
				activation = 1.0d;
			else
				activation = 0.0d;	

		}catch(Exception e){

			e.printStackTrace();

		}

		try 
		{			
			this.setActivation(activation);

		} catch (CodeletActivationBoundsException e) 
		{			
			e.printStackTrace();
		}		
	}

	@Override
	public void proc() {

		Random random = new Random(System.currentTimeMillis());

		List<Float> velocitiesAxis = new ArrayList<>();

		float velocity = 0.3f;
		
		if(random.nextDouble() > 0.5d) {
			velocity *= -1.0f;
		}
		
		if(random.nextDouble() > 0.7d) {
			velocitiesAxis.add(velocity);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
		} else if(random.nextDouble() > 0.3d) {
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(velocity);
			velocitiesAxis.add(0.0f);
		}else {
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(0.0f);
			velocitiesAxis.add(velocity);
		}

		((MemoryContainer) motionCommandMemory).setI(velocitiesAxis,getActivation(),id);
	}
}
