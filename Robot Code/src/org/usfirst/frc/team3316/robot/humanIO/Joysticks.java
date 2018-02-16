/**
 * Class for joysticks and joystick buttons
 */
package org.usfirst.frc.team3316.robot.humanIO;

import org.usfirst.frc.team3316.robot.Robot;
import org.usfirst.frc.team3316.robot.auton.commands.AlignToCube;
import org.usfirst.frc.team3316.robot.commands.chassis.BrakeMode;
import org.usfirst.frc.team3316.robot.commands.chassis.CoastMode;
import org.usfirst.frc.team3316.robot.commands.chassis.DriveOneAxis;
import org.usfirst.frc.team3316.robot.commands.holder.HolderCollection;
import org.usfirst.frc.team3316.robot.commands.holder.HolderEjection;
import org.usfirst.frc.team3316.robot.commands.holder.HolderRoll;
import org.usfirst.frc.team3316.robot.commands.holder.HolderRollType;
import org.usfirst.frc.team3316.robot.commands.intake.IntakeRoll;
import org.usfirst.frc.team3316.robot.commands.intake.IntakeRollType;
import org.usfirst.frc.team3316.robot.config.Config;
import org.usfirst.frc.team3316.robot.logger.DBugLogger;

import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.Button;

public class Joysticks {
    /*
     * Defines a button in a gamepad POV for an array of angles
     */
    private class POVButton extends Button {
	Joystick m_joystick;
	int m_deg;

	public POVButton(Joystick joystick, int deg) {
	    m_joystick = joystick;
	    m_deg = deg;
	}

	public boolean get() {
	    if (m_joystick.getPOV() == m_deg) {
		return true;
	    }
	    return false;
	}
    }

    Config config = Robot.config;
    DBugLogger logger = Robot.logger;

    public Joystick joystickLeft, joystickRight;
    public Joystick joystickOperator;
    public Joystick joystickElevator;
    public DBugJoystickButton intakeInBtn, intakeOutBtn;
    public AnalogTrigger intakeDirectionalBtn;
    public DBugJoystickDigitalAxis DriveOneAxisAxisButton1;

    /**
     * Initializes the joysticks.
     */
    public Joysticks() {
	joystickLeft = new Joystick((int) Robot.config.get("JOYSTICK_LEFT"));
	joystickRight = new Joystick((int) Robot.config.get("JOYSTICK_RIGHT"));
	joystickOperator = new Joystick((int) Robot.config.get("JOYSTICK_OPERATOR"));
    }

    /**
     * Initializes the joystick buttons. This is done separately because they
     * usually require the subsystems to be already instantiated.
     */
    public void initButtons() {
	/*
	 * Chassis
	 */
	DBugJoystickButton toggleChassisBrakeMode = new DBugJoystickButton(joystickOperator,
		"button_Chassis_Break_Toggle");
	toggleChassisBrakeMode.whenPressed(new DBugToggleCommand(new BrakeMode(), new CoastMode()));
	
	/*
	 * Intake
	 */
	DBugJoystickButton toggleIntakeRollIn = new DBugJoystickButton(joystickOperator, "button_Intake_RollIn");
	toggleIntakeRollIn.whenPressed(
		new DBugToggleCommand(new IntakeRoll(IntakeRollType.RollIn), new IntakeRoll(IntakeRollType.Stop)));

	DBugJoystickButton toggleIntakeRollOut = new DBugJoystickButton(joystickOperator, "button_Intake_RollOut");
	toggleIntakeRollOut.whenPressed(
		new DBugToggleCommand(new IntakeRoll(IntakeRollType.RollOut), new IntakeRoll(IntakeRollType.Stop)));
	
	/*
	 * Holder
	 */
	DBugJoystickButton toggleHolderRollIn = new DBugJoystickButton(joystickOperator, "button_Holder_RollIn");
	toggleHolderRollIn.whenPressed(
		new DBugToggleCommand(new HolderCollection(), new HolderRoll(HolderRollType.Stop)));

	DBugJoystickButton toggleHolderRollOut = new DBugJoystickButton(joystickOperator, "button_Holder_RollOut");
	toggleHolderRollOut.whenPressed(
		new DBugToggleCommand(new HolderEjection(), new HolderRoll(HolderRollType.Stop)));
    
	/*
	 * Other
	 */
	DBugJoystickButton alignToCube = new DBugJoystickButton(joystickOperator, "button_Auton_Align");
	alignToCube.whenPressed(
	new AlignToCube());
    
    }
}
