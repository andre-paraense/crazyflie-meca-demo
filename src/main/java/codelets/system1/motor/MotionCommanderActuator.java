/**
 * 
 */
package main.java.codelets.system1.motor;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.meca.system1.codelets.MotorCodelet;
import se.bitcraze.crazyflie.lib.crazyflie.Crazyflie;
import se.bitcraze.crazyflie.lib.positioning.MotionCommander;

/**
 * @author andre
 *
 */
public class MotionCommanderActuator extends MotorCodelet {
	
	private MotionCommander motionCommander;

	public MotionCommanderActuator(String id, Crazyflie crazyflie) {
		super(id);
		
		motionCommander = new MotionCommander(crazyflie);
		try {
			motionCommander.takeOff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void proc(Memory motorMemory) {
		
		if(motorMemory!=null && motorMemory.getI()!=null && motorMemory.getI() instanceof ArrayList){
			
			List<Float> velocitiesAxis = (ArrayList<Float>) motorMemory.getI();
			
			if(velocitiesAxis != null && velocitiesAxis.size() > 0 ) {	
				try {
					motionCommander.startLinearMotion(velocitiesAxis.get(0), velocitiesAxis.get(1), velocitiesAxis.get(2));					
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		}
	}
}
