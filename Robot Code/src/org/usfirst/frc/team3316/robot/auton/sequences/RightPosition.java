package org.usfirst.frc.team3316.robot.auton.sequences;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.auton.commands.AutonMode;
import org.usfirst.frc.team3316.robot.auton.commands.DriveDistance;
import org.usfirst.frc.team3316.robot.auton.commands.TurnByGyro;
import org.usfirst.frc.team3316.robot.auton.commands.TurnByGyroBB;
import org.usfirst.frc.team3316.robot.chassis.paths.PathFollowCommand;
import org.usfirst.frc.team3316.robot.commands.DBugCommandGroup;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorMoveToEdge;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorShaken;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorToLevel;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorMoveToBottom;
import org.usfirst.frc.team3316.robot.commands.elevator.ElevatorMoveToTop;
import org.usfirst.frc.team3316.robot.commands.holder.HolderEjection;
import org.usfirst.frc.team3316.robot.commands.holder.MoveServo;
import org.usfirst.frc.team3316.robot.sequences.CollectCube;
import org.usfirst.frc.team3316.robot.subsystems.Elevator.Level;
import org.usfirst.frc.team3316.robot.utils.falcon.PathPoints;

import edu.wpi.first.wpilibj.command.WaitCommand;

public class RightPosition extends AutonPosition {
	PathFollowCommand startPath;

	public RightPosition() {
		// TODO Auto-generated constructor stub
		requires(Robot.emptySubsystem);
	}
	
	@Override
	public void analizeMode() {
		if (mode != null && mode == AutonMode.Switch) {
			toSwitch();
		} else {
			toScale();
		}
	}
	
	private void toSwitch() {
		if (switchType == SwitchScaleType.RIGHT) {
			logger.info("I'll go straight to Right Switch");
			toRightSwitch();
		} else {
			logger.info("I'll go behind to Left Switch");
			toLeftSwitch();
		}
	}

	private void toScale() {
		if (scaleType == SwitchScaleType.RIGHT) {
			logger.info("I'll go straight to Right Scale");
			toRightScale();
			
			if (cubeType == CubeState.TwoCubes) {
				logger.info("And I'll install another cube");
				anotherCube();
			}
		} else {
//			toSwitch();
			logger.info("I'll go behind to Left Scale");
			toLeftScale();
			
			if (cubeType == CubeState.TwoCubes) {
				logger.info("And I'll install another cube");
				anotherCube();
			}
		}
	}
	
	private void toRightScale() {
		PathPoints startPoints = new PathPoints();
		startPoints.addPathPoint(0.0, 0.0);
		startPoints.addPathPoint(0.0, 4.06);
		startPoints.addPathPoint(-0.61, 6.15);
	
		
		addParallel(new ElevatorMoveToTop());
		addSequential(new PathFollowCommand(startPoints, 4));
		addSequential(new ElevatorMoveToTop());
		addSequential(new HolderEjection());
		addSequential(new DriveDistance(-0.5));
		addSequential(new ElevatorMoveToBottom());
		addSequential(new TurnByGyroBB(-105.0));
		addParallel(new DriveDistance(1.0));
		addSequential(new CollectCube());
	}
	
	private void toLeftScale() {
		PathPoints startPoints = new PathPoints();
		startPoints.addPathPoint(0.0, 0.0);
		startPoints.addPathPoint(-0.2, 4.06);
		startPoints.addPathPoint(-0.2, 5.5);
		startPoints.addPathPoint(-1.6, 5.5);
		startPoints.addPathPoint(-1.8, 6.1);
	
		
		addParallel(new ElevatorMoveToTop());
		addSequential(new PathFollowCommand(startPoints, 6));
		addSequential(new ElevatorMoveToTop());
		addSequential(new HolderEjection());
		addSequential(new DriveDistance(-0.5));
		addSequential(new ElevatorMoveToBottom());
		addSequential(new TurnByGyroBB(105.0));
		addParallel(new DriveDistance(1.0));
		addSequential(new CollectCube());
	}
	
	private void anotherCube() {
		addParallel(new MoveServo((double) config.get("servo_Final_Angle"), false));
		addSequential(new ElevatorToLevel(Level.Switch));
		addSequential(new HolderEjection());
		addSequential(new WaitCommand(1.0));
	}
	
	private void toRightSwitch() {
		PathPoints startPoints = new PathPoints();
		startPoints.addPathPoint(0.0, 0.0);
		startPoints.addPathPoint(0.0, 2.7);
		startPoints.addPathPoint(-0.5, 3.3);
		startPath = new PathFollowCommand(startPoints, 3);
		
		addParallel(new MoveServo((double) config.get("servo_Final_Angle"), false));
		addSequential(startPath);
		addSequential(new HolderEjection());
		addSequential(new WaitCommand(1.0));
	}
	
	private void toLeftSwitch() {
		PathPoints startPoints = new PathPoints();
		startPoints.addPathPoint(0.0, 0.0);
		startPoints.addPathPoint(0.0, 3.0);
		startPath = new PathFollowCommand(startPoints, 3);
		
		addParallel(new MoveServo((double) config.get("servo_Final_Angle"), false));
		addSequential(startPath);
	}

}
