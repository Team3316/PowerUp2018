package org.usfirst.frc.team3316.robot.auton.commands;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.commands.DBugCommand;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 *
 */
public class TurnByGyro extends DBugCommand {

	private double angle, velocity = 0.0;
	private PIDController pid;

	public TurnByGyro(double angle) {
		requires(Robot.chassis);
		this.angle = angle;
		initPID();
	}

	private void initPID() {
		pid = new PIDController(0, 0, 0, new PIDSource() {
			public void setPIDSourceType(PIDSourceType pidSource) {
				return;
			}

			public double pidGet() {
				double currentAngle = Robot.chassis.getYaw();

				return currentAngle;
			}

			public PIDSourceType getPIDSourceType() {
				return PIDSourceType.kDisplacement;
			}
		}, new PIDOutput() {

			public void pidWrite(double output) {
				velocity = output;

			}
		});
	}

	// Called just before this Command runs the first time
	protected void init() {
		Robot.chassis.setBrake(false);

		pid.setOutputRange(-1.0, 1.0);

		pid.setAbsoluteTolerance((double) config.get("chassis_TurnByGyro_PID_Tolerance"));

		pid.setPID((double) config.get("chassis_TurnByGyro_PID_KP") / 10000,
				(double) config.get("chassis_TurnByGyro_PID_KI") / 10000,
				(double) config.get("chassis_TurnByGyro_PID_KD") / 10000);

		pid.setSetpoint(angle);

		pid.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.chassis.setMotors(velocity, -velocity);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		double velocityFilter = (double) config.get("chassis_TurnByGyro_VelocityFilter");
		return Math.abs(velocity) < velocityFilter && pid.onTarget();
	}

	// Called once after isFinished returns true
	protected void fin() {
		pid.reset();
		pid.disable();
		Robot.chassis.setMotors(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interr() {
		fin();
	}
}
