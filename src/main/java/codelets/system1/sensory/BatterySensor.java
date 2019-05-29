/**
 * 
 */
package codelets.system1.sensory;

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
public class BatterySensor extends SensoryCodelet {
	
	private Crazyflie crazyflie;
	
	private LogConfig lc;

	public BatterySensor(String id, Crazyflie crazyflie) {
		super(id);
		this.crazyflie = crazyflie;
		
		int periodInMs = 10;
		lc = new LogConfig("battery", periodInMs);
		lc.addVariable("pm.state", VariableType.INT16_T);// [BATTERY, CHARGING, CHARGED, LOW_POWER] = list(range(4))
        lc.addVariable("pm.vbat", VariableType.FLOAT);//value in volts       
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
//		                	if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//			                    String msg = "";
//			                    if(logConfig.isAdded()) {
//			                        msg = "' added";
//			                    } else {
//			                        msg = "' deleted";
//			                    }
//			                    System.out.println("LogConfig '" + logConfig.getName() + msg);
//							}
		                }

		                public void logConfigError(LogConfig logConfig) {
//		                	if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//		                		System.err.println("Error when logging '" + logConfig.getName() + "': " + logConfig.getErrNo());
//							}		                    
		                }

		                public void logConfigStarted(LogConfig logConfig) {
//		                	if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//			                    String msg = "";
//			                    if(logConfig.isStarted()) {
//			                        msg = "' started";
//			                    } else {
//			                        msg = "' stopped";
//			                    }
//			                    System.out.println("LogConfig '" + logConfig.getName() + msg);
//							}
		                }

		                public void logDataReceived(LogConfig logConfig, Map<String, Number> data, int timestamp) {
		                	if(logConfig.getName().equalsIgnoreCase(lc.getName())){
//		                		System.out.println("timestamp: " + timestamp);
			                    List<Number> batteryMeasures = new ArrayList<>();
			                    batteryMeasures.add(data.get("pm.state"));
			                    batteryMeasures.add(data.get("pm.vbat"));
			                    sensoryMemory.setI(batteryMeasures);                 
//			                    System.out.println("Battery state: "+batteryMeasures.get(0)+", battery value (V): "+batteryMeasures.get(1));
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
