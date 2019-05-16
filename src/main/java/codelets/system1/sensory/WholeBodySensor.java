/**
 * 
 */
package main.java.codelets.system1.sensory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.meca.system1.codelets.SensoryCodelet;
import se.bitcraze.crazyflie.lib.crazyflie.Crazyflie;
import se.bitcraze.crazyflie.lib.log.LogConfig;
import se.bitcraze.crazyflie.lib.log.LogListener;
import se.bitcraze.crazyflie.lib.log.Logg;
import se.bitcraze.crazyflie.lib.toc.VariableType;

/**
 * @author andre
 *
 */
public class WholeBodySensor extends SensoryCodelet {

	private Crazyflie crazyflie;

	private LogConfig lc;

	public WholeBodySensor(String id, Crazyflie crazyflie) {
		super(id);
		this.crazyflie = crazyflie;
		
		int cycleMS = 10;

		lc = new LogConfig("BodySensors", cycleMS);
//		lc.addVariable("pm.vbat", VariableType.FLOAT);
		lc.addVariable("range.front", VariableType.FLOAT);
		lc.addVariable("range.back", VariableType.FLOAT);
		lc.addVariable("range.left", VariableType.FLOAT);
		lc.addVariable("range.right", VariableType.FLOAT);
		lc.addVariable("range.up", VariableType.FLOAT);
		lc.addVariable("range.zrange", VariableType.FLOAT);
	}

	@Override
	public void proc(Memory sensoryMemory) {
		if(crazyflie != null && crazyflie.isConnected()) {	       

			Logg logg = crazyflie.getLogg();

			if (logg != null) {

				if(!logg.getLogConfigs().contains(lc)) {

					logg.addConfig(lc);

					logg.addLogListener(new LogListener() {

						public void logConfigAdded(LogConfig logConfig) {
//							if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//								String msg = "";
//								if(logConfig.isAdded()) {
//									msg = "' added";
//								} else {
//									msg = "' deleted";
//								}
//								System.out.println("LogConfig '" + logConfig.getName() + msg);
//							}
						}

						public void logConfigError(LogConfig logConfig) {
//							if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//								System.err.println("Error when logging '" + logConfig.getName() + "': " + logConfig.getErrNo());
//							}		                    
						}

						public void logConfigStarted(LogConfig logConfig) {
//							if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//								String msg = "";
//								if(logConfig.isStarted()) {
//									msg = "' started";
//								} else {
//									msg = "' stopped";
//								}
//								System.out.println("LogConfig '" + logConfig.getName() + msg);
//							}
						}

						public void logDataReceived(LogConfig logConfig, Map<String, Number> data, int timestamp) {
							if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//								System.out.println("timestamp: " + timestamp);
								List<Number> bodyMeasures = new ArrayList<>();
//								bodyMeasures.add(data.get("pm.vbat"));
								bodyMeasures.add(data.get("range.front"));
								bodyMeasures.add(data.get("range.back"));
								bodyMeasures.add(data.get("range.left"));
								bodyMeasures.add(data.get("range.right"));
								bodyMeasures.add(data.get("range.up"));
								bodyMeasures.add(data.get("range.zrange"));
								sensoryMemory.setI(bodyMeasures);                 
//								System.out.println("Battery state: "+bodyMeasures.get(0));
//								System.out.println("Front: "+bodyMeasures.get(1));
//								System.out.println("Back: "+bodyMeasures.get(2));
//								System.out.println("Left: "+bodyMeasures.get(3));
//								System.out.println("Right: "+bodyMeasures.get(4));
//								System.out.println("Up: "+bodyMeasures.get(5));
//								System.out.println("Down: "+bodyMeasures.get(6));
							}		                  
						}
					});

					// Start the logging
					logg.start(lc);
				}
			} else {
				sensoryMemory.setI(null);
			}
		}else {
			sensoryMemory.setI(null);
		}		
	}
}
