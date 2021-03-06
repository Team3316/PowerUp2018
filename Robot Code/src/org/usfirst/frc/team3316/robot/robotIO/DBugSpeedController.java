package org.usfirst.frc.team3316.robot.robotIO;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.config.Config;
import org.usfirst.frc.team3316.robot.logger.DBugLogger;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * A unique speed controller class team 3316. This class contains a special
 * setMotor method, which stops the motor if it's in stall.
 * 
 * @author D-Bug
 *
 */
public class DBugSpeedController {
	DBugLogger logger = Robot.logger;
	Config config = Robot.config;
	PowerDistributionPanel pdp = Robot.sensors.pdp;

	private boolean reverse; // Negative factor of velocity
	private boolean isSetLimit;
	private int pdpChannel; // The channel in the PDP of the speed controller
	private double maxCurrent; // The high threshold for current control

	private SpeedController sc;

	/**
	 * This method is using for adding a new speed controller to this subsystem.
	 * This method offers a better way to control all of your speed controllers.
	 * 
	 * @param sc
	 *            The speed controller to add.
	 * @param reverse
	 *            Set true if you want to reverse the voltage of the motor,
	 *            otherwise set false.
	 * @param pdpChannel
	 *            The channel of the speed controller on the PDP.
	 * @param maxCurrent
	 *            The stall current of the motor.
	 */
	public DBugSpeedController(SpeedController sc, boolean reverse, int pdpChannel, double maxCurrent) {
		this.sc = sc;
		this.reverse = reverse;
		this.isSetLimit = true;
		this.pdpChannel = pdpChannel;
		this.maxCurrent = maxCurrent;

		this.sc.setInverted(reverse);
	}

	/**
	 * This method is using for adding a new D-Bug Speed Controller to this
	 * subsystem. This method offers a better way to control all of your D-Bug Speed
	 * Controllers.
	 * 
	 * @param chassisLeft1SC
	 *            The D-Bug Speed Controller
	 * @param reverse
	 *            Set true if you want to reverse the voltage of the motor,
	 *            otherwise set false.
	 * @param pdpChannel
	 *            The pdp channel of the D-Bug speed controller.
	 */
	public DBugSpeedController(SpeedController chassisLeft1SC, boolean reverse, int pdpChannel) {
		this.sc = chassisLeft1SC;
		this.reverse = reverse;
		this.pdpChannel = pdpChannel;
		isSetLimit = false;

		this.sc.setInverted(reverse);
	}

	/**
	 * This method sets the voltage for this D-Bug Speed Controller.
	 * 
	 * @param v
	 *            The voltage (velocity) to set for this D-Bug Speed Controller.
	 * @return A boolean of the process success - true if it succeeded or false if
	 *         it failed.
	 */
	public boolean setMotor(double v) {
		if (!isSetLimit || !isInStall()) {
			v = Math.max(v, -1.0);
			v = Math.min(v, 1.0);
			sc.set(v);
			return true;
		} else {
			sc.set(0);
			return false;
		}
	}

	public double getCurrent() {
		if (sc instanceof WPI_TalonSRX) {
			return pdp.getCurrent(pdpChannel);
		}
		return 0.0;
	}
	
	public boolean isInStall() {
		return getCurrent() > maxCurrent;
	}

	/**
	 * Returns the set speed of the DBugSpeedController between -1 to 1.
	 */
	public double getVoltage() {
		return sc.get();

	}

	public void switchToBrake(boolean brakeMode) {
		if (sc instanceof WPI_TalonSRX) {
			NeutralMode mode = brakeMode ? NeutralMode.Brake : NeutralMode.Coast;
			((WPI_TalonSRX) sc).setNeutralMode(mode);
		}
	}

	public void invert() {
		boolean r = this.reverse;
		this.sc.setInverted(!r);
		this.reverse = !r;
	}
}
