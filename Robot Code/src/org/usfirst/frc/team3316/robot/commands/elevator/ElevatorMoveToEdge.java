package org.usfirst.frc.team3316.robot.commands.elevator;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.commands.DBugCommand;
import org.usfirst.frc.team3316.robot.commands.holder.MoveServo;
import org.usfirst.frc.team3316.robot.subsystems.Elevator.Level;

public class ElevatorMoveToEdge extends DBugCommand {
	Level level;
	double v;

	public ElevatorMoveToEdge(Level level) {
		requires(Robot.elevator);
		this.level = level;
	}

	@Override
	protected void init() {
		switch (level) {
		case Bottom:
			v = (double) config.get("elevator_MoveToEdge_DownVoltage");
			(new MoveServo((double) config.get("servo_Initial_Angle"), false)).start();
			break;
		case Top:
			v = (double) config.get("elevator_MoveToEdge_UpVoltage");
			break;
		default:
			v = 0.0;
			break;
		}

	}

	@Override
	protected void execute() {
		if (level == Level.Bottom && Robot.elevator.getLevel() == Level.BrakePoint) {
			v = (double) config.get("elevator_MoveToEdge_SlowDownVoltage");
		}
		
		Robot.elevator.setMotors(v);
	}

	@Override
	protected boolean isFinished() {
		return Robot.elevator.getLevel() == level;
	}

	@Override
	protected void fin() {
		Robot.elevator.setMotors(0.0);
	}

	@Override
	protected void interr() {
		fin();
	}

}
