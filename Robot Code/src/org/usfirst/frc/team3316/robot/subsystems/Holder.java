package org.usfirst.frc.team3316.robot.subsystems;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.robotIO.DBugSpeedController;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;

public class Holder extends DBugSubsystem {
	// Actuators
	private DBugSpeedController motor;
	private Servo servo;

	// Sensors
	private DigitalInput limitSwitch;

	/**
	 * Constructor
	 */
	public Holder() {
		// Actuators
		Robot.actuators.HolderActuators();
		this.motor = Robot.actuators.holderMotor;
		this.servo = Robot.actuators.holderServo;

		// Sensors
		Robot.sensors.HolderSensors();
		this.limitSwitch = Robot.sensors.holderLimitSwitch;
	}

	@Override
	public void initDefaultCommand() {
		// TODO: Add default command
	}

	/**
	 * Sets the holder's motor output voltage.
	 * 
	 * @param v
	 *            - The output voltage
	 */
	public void setMotor(double v) {
		this.motor.setMotor(v);
	}

	/**
	 * Inverts the motor's direction.
	 */
	public void changeDirection() {
		this.motor.invert();
	}

	/**
	 * Checks whether the limit switch is pressed or not, indicating whether a cube
	 * is in or not.
	 * 
	 * @return A boolean indicating whether there is a cube in the robot
	 */
	public boolean isCubeIn() {
		return !this.limitSwitch.get();
	}

	public boolean isRollingIn() {
		return this.motor.getVoltage() > 0.0;
	}

	public boolean isRollingOut() {
		return this.motor.getVoltage() < 0.0;
	}
	
	public void moveServo(double angle) {
		this.servo.setAngle(angle);
	}
}
