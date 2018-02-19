/**
 * Class for managing the SmartDashboard data
 */
package org.usfirst.frc.team3316.robot.humanIO;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.auton.commands.DriveDistance;
import org.usfirst.frc.team3316.robot.auton.sequences.LeftPositionSwitch;
import org.usfirst.frc.team3316.robot.auton.sequences.RightPositionSwitch;
import org.usfirst.frc.team3316.robot.auton.sequences.SwitchType;
import org.usfirst.frc.team3316.robot.commands.emptyCommand;
import org.usfirst.frc.team3316.robot.commands.chassis.MoveChassis;
import org.usfirst.frc.team3316.robot.commands.chassis.ResetGyro;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorToLevel;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorToLevelBangbang;
import org.usfirst.frc.team3316.robot.commands.elevator.ShiftGear;
import org.usfirst.frc.team3316.robot.config.Config;
import org.usfirst.frc.team3316.robot.logger.DBugLogger;
import org.usfirst.frc.team3316.robot.subsystems.Elevator.Gear;
import org.usfirst.frc.team3316.robot.subsystems.Elevator.Level;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SDB {
	/*
	 * Runnable that periodically updates values from the robot into the
	 * SmartDashboard This is the place where all of the robot data should be
	 * displayed from
	 */
	private class UpdateSDBTask extends TimerTask {
		public UpdateSDBTask() {
			logger.info("Created UpdateSDBTask");
		}

		public void run() {
			/*
			 * For driver (shai shai)
			 */

			// Chassis
			put("Distace right", Robot.chassis.getRightDistance());
			put("Distace left", Robot.chassis.getLeftDistance());
			put("Speed", Robot.chassis.getSpeed());

			put("Yaw angle", Robot.chassis.getYaw());
//			put("Roll angle", Robot.chassis.getRoll());
//			put("Pitch angle", Robot.chassis.getPitch());
			
//			logger.info("yaw:" + Robot.chassis.getYaw() + ", roll:" + Robot.chassis.getRoll() + ", pitch:" + Robot.chassis.getPitch());

			put("Low Speed", Robot.chassis.isDrivingSlowly());

			put("Elevator position", Robot.elevator.getPosition());
			put("pulse count", Robot.sensors.elevatorEncoder.getRaw());
			
			put("Elevator Level", Robot.elevator.getLevel().toString());
		}

		private void put(String name, double d) {
			SmartDashboard.putNumber(name, d);
		}

		private void put(String name, int i) {
			SmartDashboard.putNumber(name, i);
		}

		private void put(String name, boolean b) {
			SmartDashboard.putBoolean(name, b);
		}

		private void put(String name, String s) {
			SmartDashboard.putString(name, s);
		}
	}

	DBugLogger logger = Robot.logger;
	Config config = Robot.config;

	private UpdateSDBTask updateSDBTask;

	private Hashtable<String, Class<?>> variablesInSDB;

	public SDB() {
		variablesInSDB = new Hashtable<String, Class<?>>();

		initSDB();
	}

	public void timerInit() {
		updateSDBTask = new UpdateSDBTask();
		Robot.timer.schedule(updateSDBTask, 0, 10);
	}

	/**
	 * Adds a certain key in the config to the SmartDashboard
	 * 
	 * @param key
	 *            the key required
	 * @return whether the value was put in the SmartDashboard
	 */
	public boolean putConfigVariableInSDB(String key) {
		Object value = config.get(key);
		if (value != null) {
			Class<?> type = value.getClass();

			boolean constant = Character.isUpperCase(key.codePointAt(0))
					&& Character.isUpperCase(key.codePointAt(key.length() - 1));

			if (type == Double.class) {
				SmartDashboard.putNumber(key, (double) value);
			} else if (type == Integer.class) {
				SmartDashboard.putNumber(key, (int) value);
			} else if (type == Boolean.class) {
				SmartDashboard.putBoolean(key, (boolean) value);
			}

			if (!constant) {
				variablesInSDB.put(key, type);
				logger.info("Added to SDB " + key + " of type " + type + " and allows for its modification");
			} else {
				logger.info("Added to SDB " + key + " of type " + type + " BUT DOES NOT ALLOW for its modification");
			}

			return true;
		}

		return false;
	}

	public Set<Entry<String, Class<?>>> getVariablesInSDB() {
		return variablesInSDB.entrySet();
	}

	private void initSDB() {
		initDriverCameras();
	    
		SmartDashboard.putData(new UpdateVariablesInConfig()); // NEVER REMOVE
		// THIS COMMAND
		
		// Auton
		SmartDashboard.putData(new LeftPositionSwitch(SwitchType.LEFT));
		SmartDashboard.putData(new RightPositionSwitch(SwitchType.RIGHT));
		
		SmartDashboard.putData("drive -1m", new DriveDistance(-1.0));

		// Chassis
		SmartDashboard.putData(new ResetGyro());
		
		// Elevator
		
		SmartDashboard.putData("Toggle Shifting", new DBugToggleCommand(new ShiftGear(Gear.HIGH), new ShiftGear(Gear.LOW)));
		SmartDashboard.putData("HIGH GEAR",new ShiftGear(Gear.HIGH));
		SmartDashboard.putData("LOW GEAR",new ShiftGear(Gear.LOW));
		logger.info("Finished initSDB()");
	}

	private void initDriverCameras() {
		// Cameras
		CameraServer.getInstance().startAutomaticCapture("cam0", 0);
	}

	/**
	 * This method puts in the live window of the test mode all of the robot's
	 * actuators and sensors. It is disgusting.
	 */
	public void initLiveWindow() {
		initLiveWindowActuators();
		initLiveWindowSensors();

		logger.info("Finished initLiveWindow()");
	}

	private void initLiveWindowActuators() {

	}

	private void initLiveWindowSensors() {
		
	}
}