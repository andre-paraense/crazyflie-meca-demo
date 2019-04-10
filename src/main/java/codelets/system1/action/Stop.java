/**
 * 
 */
package main.java.codelets.system1.action;

import java.util.ArrayList;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.meca.system1.codelets.ActionCodelet;

/**
 * @author andre
 *
 */
public class Stop extends ActionCodelet {

	public Stop(String id, ArrayList<String> perceptualCodeletsIds, String motorCodeletId, String soarCodeletId,
			boolean isPlanedAction) {
		super(id, perceptualCodeletsIds, motorCodeletId, soarCodeletId, isPlanedAction);
	}

	@Override
	public void calculateActivation(ArrayList<Memory> perceptualMemories, Memory broadcastMemory,
			Memory actionSequencePlanMemoryContainer) {
	}

	@Override
	public void proc(ArrayList<Memory> perceptualMemories, Memory broadcastMemory,
			Memory actionSequencePlanMemoryContainer, Memory motorMemory) {
		
		((MemoryContainer) motorMemory).setI(id,getActivation(),id);
	}

}
