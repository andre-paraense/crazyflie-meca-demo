/**
 * 
 */
package main.java;

import java.util.List;

import se.bitcraze.crazyflie.lib.crazyflie.ConnectionAdapter;
import se.bitcraze.crazyflie.lib.crazyflie.Crazyflie;
import se.bitcraze.crazyflie.lib.crazyradio.ConnectionData;
import se.bitcraze.crazyflie.lib.crazyradio.RadioDriver;
import se.bitcraze.crazyflie.lib.usb.UsbLinkJava;

/**
 *
 */
public class Main {
	
	private Crazyflie crazyflie;

	public Main(ConnectionData connectionData) {

		crazyflie = new Crazyflie(new RadioDriver(new UsbLinkJava()));
		crazyflie.getDriver().addConnectionListener(connectionAdapter);
		System.out.println("Connecting to " + connectionData);
        // Try to connect to the Crazyflie
		crazyflie.setConnectionData(connectionData);
		crazyflie.connect();
	}
	
	private ConnectionAdapter connectionAdapter = new ConnectionAdapter() {
		
        public void connected() {
            System.out.println("CONNECTED");           
        }
        
        public void disconnected() {
            System.out.println("DISCONNECTED");
        }
        
        public void connectionFailed(String msg) {
            System.out.println("CONNECTION FAILED - Msg: " + msg);
        }
        
        public void connectionLost(String connectionInfo) {
            System.out.println("CONNECTION LOST: " +  connectionInfo);
        }
        
        @Override
        public void setupFinished() {
            System.out.println("SETUP FINISHED");
            
        }
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Scanning interfaces for Crazyflies...");
		RadioDriver radioDriver = new RadioDriver(new UsbLinkJava());
		List<ConnectionData> foundCrazyflies = radioDriver.scanInterface();
		radioDriver.disconnect();
		System.out.println("Crazyflies found:");
		for (ConnectionData connectionData : foundCrazyflies) {
			System.out.println(connectionData);
		}

		if (foundCrazyflies.size() > 0) {
			new Main(foundCrazyflies.get(0));
		} else {
			System.out.println("No Crazyflies found, cannot run example");
		}		
	}
}
