/**
 * 
 */
package main.java.codelets.system1.action;

import java.util.ArrayList;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.meca.system1.codelets.ActionFromPlanningCodelet;

/**
 * @author andre
 *
 */
public class Land extends ActionFromPlanningCodelet {

	public Land(String id, ArrayList<String> perceptualCodeletsIds, String motorCodeletId, String soarCodeletId) {
		super(id, perceptualCodeletsIds, motorCodeletId, soarCodeletId);
	}

	@Override
	public void proc(ArrayList<Memory> perceptualMemories, Memory broadcastMemory, Memory motorMemory) {
		
		((MemoryContainer) motorMemory).setI(id,getActivation(),id);	
	}
}
