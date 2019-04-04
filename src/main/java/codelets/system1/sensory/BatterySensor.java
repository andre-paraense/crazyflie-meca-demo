/**
 * 
 */
package main.java.codelets.system1.sensory;

import java.util.Map;
import java.util.Map.Entry;

import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
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
	
	private Memory batteryStateMO;
	
	private Memory batteryValueMO;
	
	private LogConfig lc;

	public BatterySensor(String id, Crazyflie crazyflie) {
		super(id);
		this.crazyflie = crazyflie;
		
		int periodInMs = 500;
		lc = new LogConfig("battery", periodInMs);
		lc.addVariable("pm.state", VariableType.INT16_T);// [BATTERY, CHARGING, CHARGED, LOW_POWER] = list(range(4))
        lc.addVariable("pm.vbat", VariableType.FLOAT);//value in volts
	}

	@Override
	public void accessMemoryObjects() {

		if(batteryStateMO == null)
			batteryStateMO = this.getOutput(id, 0);	
		
		if(batteryValueMO == null)
			batteryValueMO = this.getOutput(id, 1);
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
		
		if(crazyflie != null && crazyflie.isConnected()) {
	        
	        /**
	         *  Adding the configuration cannot be done until a Crazyflie is connected and
	         *  the setup is finished, since we need to check that the variables we
	         *  would like to log are in the TOC.
	         */

	        final Logg logg = crazyflie.getLogg();

	        if (logg != null) {
	        	
	        	if(!logg.getLogConfigs().contains(lc)) {
	        		
	        		logg.addConfig(lc);

		            logg.addLogListener(new LogListener() {

		                public void logConfigAdded(LogConfig logConfig) {
		                    String msg = "";
		                    if(logConfig.isAdded()) {
		                        msg = "' added";
		                    } else {
		                        msg = "' deleted";
		                    }
		                    System.out.println("LogConfig '" + logConfig.getName() + msg);
		                }

		                public void logConfigError(LogConfig logConfig) {
		                    System.err.println("Error when logging '" + logConfig.getName() + "': " + logConfig.getErrNo());
		                }

		                public void logConfigStarted(LogConfig logConfig) {
		                    String msg = "";
		                    if(logConfig.isStarted()) {
		                        msg = "' started";
		                    } else {
		                        msg = "' stopped";
		                    }
		                    System.out.println("LogConfig '" + logConfig.getName() + msg);
		                }

		                public void logDataReceived(LogConfig logConfig, Map<String, Number> data, int timestamp) {
		                    System.out.println("timestamp: " + timestamp);
		                    batteryStateMO.setI(data.get("pm.state"));
		                    batteryValueMO.setI(data.get("pm.vbat"));	                  
		                    for (Entry<String, Number> entry : data.entrySet()) {
		                        System.out.print("\t" + entry.getKey() + ": " + entry.getValue());
		                    }
		                    System.out.println();
		                }

		            });

		            // Start the logging
		            logg.start(lc);
	        	}
	        } else {
	        	batteryStateMO.setI(null);
	        	batteryValueMO.setI(null);
	        }
		}else {
			batteryStateMO.setI(null);
			batteryValueMO.setI(null);
		}
	}

}
