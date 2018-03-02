package org.usfirst.frc.team3316.robot.auton.sequences;

import org.usfirst.frc.team3316.robot.chassis.paths.PathFollowCommand;
import org.usfirst.frc.team3316.robot.commands.DBugCommandGroup;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorToLevel;
import org.usfirst.frc.team3316.robot.commands.holder.HolderEjection;
import org.usfirst.frc.team3316.robot.subsystems.Elevator.Level;
import org.usfirst.frc.team3316.robot.utils.falcon.PathPoints;

public class RightPositionSwitch extends DBugCommandGroup {

	public RightPositionSwitch(SwitchType type) {

		PathPoints startPoints = new PathPoints();
		startPoints.addPathPoint(0.0, 0.0); // Robot starting point
		startPoints.addPathPoint(0.0, 2.5); // Right position switch 
		startPoints.addPathPoint(-0.6, 3.0); // Rotation to be able to install a cube legally
		PathFollowCommand startPath = new PathFollowCommand(startPoints, 4);

		addParallel(startPath);
		addSequential(new ElevatorToLevel(Level.Switch));
		addSequential(new HolderEjection());

	}

}
