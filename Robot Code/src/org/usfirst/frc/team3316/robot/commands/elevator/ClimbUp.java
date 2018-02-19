package org.usfirst.frc.team3316.robot.commands.elevator;

import org.usfirst.frc.team3316.robot.commands.DBugCommandGroup;
import org.usfirst.frc.team3316.robot.subsystems.Elevator.Gear;
import org.usfirst.frc.team3316.robot.subsystems.Elevator.Level;

public class ClimbUp extends DBugCommandGroup {
	public ClimbUp() {
		addParallel(new ElevatorMoveToEdge(Level.Top));
		addSequential(new ShiftGear(Gear.HIGH));
		addSequential(new ElevatorMoveVoltage(-0.5));
	}
}
