package org.usfirst.frc.team3316.robot.subsystems;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.commands.chassis.TankDrive;
import org.usfirst.frc.team3316.robot.commands.chassis.TankDriveXbox;
import org.usfirst.frc.team3316.robot.robotIO.DBugSpeedController;
import org.usfirst.frc.team3316.robot.utils.Utils;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Chassis extends DBugSubsystem {
	// Actuators
	private DBugSpeedController leftMotor1, rightMotor2, leftMotor2, rightMotor1;

	// Sensors
	private AHRS navx; // For the navX

	private Encoder leftEncoder;
	private Encoder rightEncoder;

	// Variables
	private double pitchOffset, rollOffset, yawOffset = 0.0;
	public double currentLeftV = 0, currentRightV = 0, currentRatio = 0;

	public Chassis() {
		// Actuators
		Robot.actuators.ChassisActuators();

		leftMotor1 = Robot.actuators.chassisLeft1;
		rightMotor2 = Robot.actuators.chassisRight2;
		leftMotor2 = Robot.actuators.chassisLeft2;
		rightMotor1 = Robot.actuators.chassisRight1;

		// Sensors
		Robot.sensors.ChassisSensors();

		leftEncoder = Robot.sensors.chassisLeftEncoder;
		rightEncoder = Robot.sensors.chassisRightEncoder;
		navx = Robot.sensors.navx;

		resetYaw();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new TankDrive());
	}

	/*
	 * Motor methods
	 */
	public void setMotors(double left, double right) {
		// logger.finest("Setting chassis. left: " + left + ", right: " + right);
		SmartDashboard.putNumber("left motor", left);
		SmartDashboard.putNumber("right motor", right);

		leftMotor1.setMotor(left);
		leftMotor2.setMotor(left);

		rightMotor1.setMotor(right);
		rightMotor2.setMotor(right);
	}

	public double getPitch() {
		return navx.getPitch();
	}

	public double getRoll() {
		return navx.getRoll();
	}

	public void resetPitch() {
		pitchOffset = pitchOffset - getPitch();
	}

	public void resetYaw() {
		yawOffset = yawOffset - getYaw();
	}

	public void resetRoll() {
		rollOffset = rollOffset - getRoll();
	}

	public double getYaw() {
		return navx.getAngle() + yawOffset;
	}

	// Returns the same heading in the range (-180) to (180)
	private static double fixYaw(double heading) {
		double toReturn = heading % 360;

		if (toReturn < -180) {
			toReturn += 360;
		} else if (toReturn > 180) {
			toReturn -= 360;
		}
		return toReturn;
	}

	public void setBrake(boolean brakeMode) {
		leftMotor1.switchToBrake(brakeMode);
		leftMotor2.switchToBrake(brakeMode);
		rightMotor1.switchToBrake(brakeMode);
		rightMotor2.switchToBrake(brakeMode);
	}

	/*
	 * Encoder Methods
	 */
	public double getLeftDistance() {
		return leftEncoder.getDistance();
	}

	public double getRightDistance() {
		return rightEncoder.getDistance();
	}

	public double getLeftSpeed() {
		return leftEncoder.getRate(); // Returns the speed in meter per
		// second units.
	}

	public double getRightSpeed() {
		return rightEncoder.getRate(); // Returns the speed in meter per
		// second units.
	}

	public double getDistance() {
		return (rightEncoder.getDistance() + leftEncoder.getDistance()) / 2;
	}

	public double getSpeed() {
		return (getLeftSpeed() + getRightSpeed()) / 2;
	}

	public void resetEncoders() {
		rightEncoder.reset();
		leftEncoder.reset();
	}

	public boolean isDrivingSlowly() {
		return (double) config.get("chassis_SpeedFactor_Current") == (double) config.get("chassis_SpeedFactor_Lower");
	}

	public boolean isDrivingFast() {
		return (double) config.get("chassis_SpeedFactor_Current") == (double) config.get("chassis_SpeedFactor_Higher");
	}
}
