/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autonomous.DriveStraightOneSecond;
import frc.robot.autonomous.GenericAutonomous;
import frc.robot.autonomous.Win;
import frc.robot.genericrobot.*;

public class Robot extends TimedRobot {

    GenericAutonomous autoProgram = new Win();
    GenericRobot robot = new KeerthanPracticeOne();
    Joystick leftJoystick = new Joystick(0);
    double deadZone = 0.1;

    @Override
    public void robotInit() {

    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("Left  Encoder Ticks", robot.getDistanceTicksLeft());
        SmartDashboard.putNumber("Right Encoder Ticks", robot.getDistanceTicksRight());
        SmartDashboard.putNumber("Navx Yaw", robot.getYaw());
        SmartDashboard.putNumber("Navx Pitch", robot.getPitch());
        SmartDashboard.putNumber("Navx Roll", robot.getRoll());

        SmartDashboard.putNumber("Left  Motor Power", robot.getMotorPowerLeft());
        SmartDashboard.putNumber("Right Motor Power", robot.getMotorPowerRight());
        SmartDashboard.putNumber("Upper Shooter Power", robot.getShooterPowerUpper());
        SmartDashboard.putNumber("Lower Shooter Power", robot.getShooterPowerLower());
        SmartDashboard.putNumber("Control Panel Power", robot.getControlPanelSpinnerPower());

        SmartDashboard.putNumber("AutoStep", autoProgram.autonomousStep);
        SmartDashboard.putBoolean("Shifter state", robot.getShifterState());
    }

    @Override
    public void disabledPeriodic() {
        if (leftJoystick.getTriggerPressed()) {
            System.out.println("AAAAAAAA");
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

    }

    @Override
    public void teleopPeriodic() {
        double leftPower = -leftJoystick.getY() + leftJoystick.getX();
        double rightPower = -leftJoystick.getY() - leftJoystick.getX();

        if (leftPower < -deadZone) {
            leftPower = (leftPower + deadZone) / (1 - deadZone);
        } else if (leftPower > deadZone) {
            leftPower = (leftPower - deadZone) / (1 - deadZone);
        } else {
            leftPower = 0;
        }

        if (rightPower < -deadZone) {
            rightPower = (rightPower + deadZone) / (1 - deadZone);
        } else if (rightPower > deadZone) {
            rightPower = (rightPower - deadZone) / (1 - deadZone);
        } else {
            rightPower = 0;
        }

        robot.setMotorPowerPercentage(leftPower, rightPower);
        robot.setShooterPowerPercentage(0);

        if (leftJoystick.getRawButtonPressed(16)) {
            robot.shiftLow();
        }

        if (leftJoystick.getRawButtonPressed(11)) {
            robot.shiftHigh();
        }
        if (leftJoystick.getTrigger()) {
            robot.driveForward(.1);
        }

    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

}
