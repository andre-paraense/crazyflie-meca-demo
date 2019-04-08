/**
 * 
 */
package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.exceptions.CodeletActivationBoundsException;
import br.unicamp.cst.util.MindViewer;
import br.unicamp.meca.mind.MecaMind;
import br.unicamp.meca.system1.codelets.MotivationalBehavioralCodelet;
import br.unicamp.meca.system1.codelets.MotivationalCodelet;
import br.unicamp.meca.system1.codelets.MotorCodelet;
import br.unicamp.meca.system1.codelets.PerceptualCodelet;
import br.unicamp.meca.system1.codelets.RandomBehavioralCodelet;
import br.unicamp.meca.system1.codelets.ReactiveBehavioralCodelet;
import br.unicamp.meca.system1.codelets.SensoryCodelet;
import main.java.codelets.system1.behavioral.random.RandomMove;
import main.java.codelets.system1.behavioral.reactive.ReactToRange;
import main.java.codelets.system1.motivational.EnergyConservationMotivationalCodelet;
import main.java.codelets.system1.motor.MotionCommanderActuator;
import main.java.codelets.system1.perceptual.SituationPerceptualCodelet;
import main.java.codelets.system1.sensory.WholeBodySensor;
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

			instantiateCrazyflieMECAMind();
		}
	};

	private void instantiateCrazyflieMECAMind() {

		MecaMind mecaMind = new MecaMind("Mind of the Crazyflie");

		/* Sensory codelets we are about to create for this Crazyflie*/
		List<SensoryCodelet> sensoryCodelets = new ArrayList<>();	
		/* Lists that will hold the codelets ids. This is important 
		 * for the MECA mind mounting algorithm be able to glue the 
		 * codelets according to the reference architecture
		 * */
		ArrayList<String> sensoryCodeletsIds = new ArrayList<>();

		WholeBodySensor bodySensor = new WholeBodySensor("BodySensor", crazyflie);
		sensoryCodelets.add(bodySensor);
		sensoryCodeletsIds.add(bodySensor.getId());
		
		/*
		 * Now it is a good time to create the motor codelets, before the Behavioral ones,
		 * because the behavioral will need the motor ids to be positioned between them
		 * and the rest of the architecture.
		 */
		List<MotorCodelet> motorCodelets = new ArrayList<>();	
		
		MotionCommanderActuator motionCommanderActuator = new MotionCommanderActuator("MotionCommanderActuator", crazyflie);
		motorCodelets.add(motionCommanderActuator);
		
		/*
		 * Then, we create the Situation Perceptual codelet. 
		 * This codelet must receive the ids of the sensory codelets,
		 * in order to be glued to them, receiving  their inputs.
		 */
		List<PerceptualCodelet> perceptualCodelets = new ArrayList<>();
		ArrayList<String> perceptualCodeletsIds = new ArrayList<>();
		
		SituationPerceptualCodelet situationPerceptualCodelet = new SituationPerceptualCodelet("SituationPerceptualCodelet", sensoryCodeletsIds);
		perceptualCodeletsIds.add(situationPerceptualCodelet.getId());
		perceptualCodelets.add(situationPerceptualCodelet);
		
		/*
		 * Next step is to create the motivational codelets.
		 * This codelets must receive the ids of the sensory codelets,
		 * in order to be glued to them, receiving  their inputs.
		 */
		List<MotivationalCodelet> motivationalCodelets = new ArrayList<>();
		ArrayList<String> energyConservationMotivationalCodeletIds = new ArrayList<>();
		
		EnergyConservationMotivationalCodelet energyConservationMotivationalCodelet;
		
		try {
			energyConservationMotivationalCodelet = new EnergyConservationMotivationalCodelet("EnergyConservationMotivationalCodelet", 0, 0.5, 0.9677, sensoryCodeletsIds, new HashMap<String, Double>());
			energyConservationMotivationalCodeletIds.add(energyConservationMotivationalCodelet.getId());
			motivationalCodelets.add(energyConservationMotivationalCodelet);
			
		} catch (CodeletActivationBoundsException e) {
			e.printStackTrace();
		}
		
		/*
		 * Last step is to create the behavioral codelets,
		 * all three random, reactive and motivational types.
		 * They receive the ids of the perceptual codelets and
		 * motor codelets, in order to be glued to them, according
		 * to the reference architecture.		
		 */
		List<RandomBehavioralCodelet> randomBehavioralCodelets = new ArrayList<>();
		
		RandomMove randomMove = new RandomMove("RandomMove", motionCommanderActuator.getId(), null);
		randomBehavioralCodelets.add(randomMove);
		
		List<ReactiveBehavioralCodelet> reactiveBehavioralCodelets = new ArrayList<>();
		
		ReactToRange reactToRange = new ReactToRange("ReactToRange", perceptualCodeletsIds,  motionCommanderActuator.getId(), null);
		reactiveBehavioralCodelets.add(reactToRange);
		
		List<MotivationalBehavioralCodelet> motivationalBehavioralCodelets = new ArrayList<>();
		

		/*
		 * Inserting the System 1 codelets inside MECA mind
		 */
		mecaMind.setSensoryCodelets(sensoryCodelets);
		mecaMind.setMotorCodelets(motorCodelets);
		mecaMind.setPerceptualCodelets(perceptualCodelets);
		mecaMind.setMotivationalCodelets(motivationalCodelets);
		mecaMind.setRandomBehavioralCodelets(randomBehavioralCodelets);
		mecaMind.setReactiveBehavioralCodelets(reactiveBehavioralCodelets);
		mecaMind.setMotivationalBehavioralCodelets(motivationalBehavioralCodelets);

		/*
		 * After passing references to the codelets, we call the method 'MecaMind.mountMecaMind()', which
		 * is responsible for wiring the MecaMind altogether according to the reference architecture, including
		 * the creation of memory objects and containers which glue them together. This method is of pivotal
		 * importance and inside it resides all the value from the reference architecture created - the idea is 
		 * that the user only has to create the codelets, put them inside lists of differente types and call
		 * this method, which transparently glue the codelets together accordingly to the MECA reference 
		 * architecture.
		 */			
		mecaMind.mountMecaMind();

		/*
		 * Starting the mind
		 */
		mecaMind.start();
		
		/*
		 * Instead of inserting the sensory codelets in the
		 * CST visualization tool, let's insert the behaviroal
		 * codelets, which activation has a pivotal role.
		 */
		List<Codelet> listOfCodelets = new ArrayList<>();
		listOfCodelets.addAll(mecaMind.getRandomBehavioralCodelets());
		listOfCodelets.addAll(mecaMind.getReactiveBehavioralCodelets());
		listOfCodelets.addAll(mecaMind.getMotivationalBehavioralCodelets());

		MindViewer mv = new MindViewer(mecaMind, "MECA Mind Inspection - "+mecaMind.getId(), listOfCodelets);
		mv.setVisible(true);

	}

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
