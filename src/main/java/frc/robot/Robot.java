/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.robot.autonomous.*;
import frc.robot.commands.*;
import frc.robot.genericrobot.*;

import static frc.robot.Util.*;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;

public class Robot extends TimedRobot {

    //WheelOfFortune    colorWheel   = new WheelOfFortune();
    GenericAutonomous autoProgram  = new PlanA(); //Auto routine to be used?
    GenericCommand    activeCommand = GenericCommand.doNothingCommand;
    GenericRobot      robot        = new Falcon();
    Joystick          leftJoystick = new Joystick(0);
    XboxController    xboxJoystick = new XboxController(1);

    double            deadZone     = 0.10;

    @Override public void robotInit() {
        Logger.log( "STARTUP","Klaatu barada nikto");
    }

    @Override
    public void robotPeriodic() {
        robot.updateMotorPowers();
        robot        .printSmartDashboard();
        autoProgram  .printSmartDashboard();
        activeCommand.printSmartDashboard();

        //SmartDashboard.putString("Instant Color", colorWheel.getAndStoreInstantColor().toString());
        //SmartDashboard.putString("Inferred Color",  colorWheel.getInferredColor().toString());

    }

    @Override
    public void disabledPeriodic() {
        if (leftJoystick.getTriggerPressed()) {
            Logger.logTTL("DISABLEDPERIODIC","AAAAAAAA", 1000);
            robot.resetAttitude();
            robot.resetEncoders();
        }


    }

    @Override
    public void autonomousInit() {
        autoProgram.autonomousInit(robot);
    }

    @Override
    public void autonomousPeriodic() {
        autoProgram.autonomousPeriodic(robot);
    }

    @Override
    public void teleopInit() {
        LiveWindow.setEnabled(false);
    }

    @Override
    public void teleopPeriodic() {
        if (leftJoystick.getRawButtonPressed(11)) {
            Logger.log("COMMAND", "Disabled");
            activeCommand.setEnabled(false);
        }

        if (activeCommand.isEnabled()) {
            activeCommand.step(robot);
            if (activeCommand.locksControls()) return;
        }
        double leftPower = -leftJoystick.getY() + leftJoystick.getX();
        double rightPower = -leftJoystick.getY() - leftJoystick.getX();

        double driverRestriction = 0.75;

         leftPower = driverRestriction*deadzoneValue( leftPower,deadZone);
        rightPower = driverRestriction*deadzoneValue(rightPower,deadZone);

        robot.setMotorPowerPercentage(leftPower, rightPower);
        robot.setShooterPowerPercentage(0);

        if (leftJoystick.getRawButtonPressed(16)) {
            robot.shiftLow();
        }

        if (leftJoystick.getRawButtonPressed(11)) {
            robot.shiftHigh();
        }
        if (leftJoystick.getRawButton(13)) {
            robot.driveForward(-.2);
        }
        if (leftJoystick.getRawButton(14)) {
            robot.driveForward(.2);
        }

        if(leftJoystick.getRawButtonPressed(2)){
            Logger.log("COMMAND", "Enabling Limelight Align");
            activeCommand.setEnabled(true);
        }

        //Collector
        if (xboxJoystick.getTriggerAxis(GenericHID.Hand.kRight) > 0) {
            robot.collectorIn(1.0);
        } else if (xboxJoystick.getTriggerAxis(GenericHID.Hand.kLeft) > 0) {
            robot.collectorOut(1.0);
        } else {
            robot.setCollectorPower(0);
        }

        //Escalator
        if (xboxJoystick.getXButton()) {
            robot.escalatorUp(.5);
        } else if (xboxJoystick.getAButton()) {
            robot.escalatorDown(.5);
        } else {
            robot.setEscalatorPower(0);
        }

        //Shooter
        if (xboxJoystick.getYButtonPressed()) {
            robot.setShooterPowerPercentage(1.0);
        } else if (xboxJoystick.getBButtonPressed()) {
            robot.setShooterPowerPercentage(0);
        }

        //Indexer
        if (xboxJoystick.getBumper(GenericHID.Hand.kRight)) {
            robot.indexerLoad(1.0);
        } else if (xboxJoystick.getBumper(GenericHID.Hand.kLeft)) {
            robot.indexerUnload(1.0);
        } else {
            robot.setIndexerPower(0);
        }
    }

    @Override
    public void testInit()  {
        LiveWindow.setEnabled(false);
    }


    @Override
    public void testPeriodic() {

        double leftPower = -leftJoystick.getY() + leftJoystick.getX();
        double rightPower = -leftJoystick.getY() - leftJoystick.getX();

        leftPower = deadzoneValue( leftPower,deadZone);
        rightPower = deadzoneValue(rightPower,deadZone);

        robot.setMotorPowerPercentage(leftPower, rightPower);

        //Collector
        if (leftJoystick.getRawButton(11)) {
            robot.collectorIn(1.0);
        } else if (leftJoystick.getRawButton(16)) {
            robot.collectorOut(1.0);
        } else {
            robot.setCollectorPower(0);
        }

        //Escalator
        if (leftJoystick.getRawButton(12)) {
            robot.escalatorUp(.5);
        } else if (leftJoystick.getRawButton(15)) {
            robot.escalatorDown(.5);
        } else {
            robot.setEscalatorPower(0);
        }

        //Shooter
        if (leftJoystick.getRawButton(13)) {
            robot.setShooterPowerPercentage(1.0);
        } else if (leftJoystick.getRawButton(14)) {
            robot.setShooterPowerPercentage(0);
        }

        //Indexer
        if (leftJoystick.getRawButton(7)) {
            robot.indexerLoad(1.0);
        } else if (leftJoystick.getRawButton(8)) {
            robot.indexerUnload(1.0);
        } else {
            robot.setIndexerPower(0);
        }

        //Vert Adjust
        if (leftJoystick.getRawButton( 6)) {
            robot.aimUp(.4);
        } else if (leftJoystick.getRawButton( 9)) {
            robot.aimDown(.4);
        } else {
            robot.setAngleAdjusterPower(0);
        }

        //CP
        if (leftJoystick.getRawButton( 5)) {
            robot.spinControlPanel(-.2);
        } else if (leftJoystick.getRawButton(10)) {
            robot.spinControlPanel(.2);
        } else {
            robot.spinControlPanel(0);
        }

        //Climb vert
        if (leftJoystick.getRawButton( 2)) {
            robot.climbDown(.2);
        } else if (leftJoystick.getRawButton( 1)) {
            robot.climbUp(.2);
        } else {
            robot.climbVertical(0);
        }

        //climb horiz
        if (leftJoystick.getRawButton( 3)) {
            robot.climberBalanceLeft(-.2);
        } else if (leftJoystick.getRawButton( 4)) {
            robot.climberBalanceRight(.2);
        } else {
            robot.setBalancePower(0);
        }
    }

}
