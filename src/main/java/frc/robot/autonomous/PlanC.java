package frc.robot.autonomous;

import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.genericrobot.GenericRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PlanC extends GenericAutonomous {

      //change speed depending on robot!! (CaMOElot = .4, TestBot = .2)
      double defaultSpeed = 0.25;

      static double startingYaw = 0.0; //start at an angle, figure out later
      static double startingDistance = 0.0;
      double correction;
      static double currentYaw = 0;
      double outerArcLength = 50;
      double innerArc = 35.45;
      double innerRadius = 30;
      double outerRadius = 50;
      double yawDifference = 0;
      double prevStartingDistance = 0;
      long startingTime = System.currentTimeMillis();


      @Override
      public void autonomousInit(GenericRobot robot) {
            startingTime = System.currentTimeMillis();
            autonomousStep = 0;
      }

      @Override
      public void autonomousPeriodic(GenericRobot robot) {
            PIDController PIDSteering = new PIDController(robot.getPIDmaneuverP(), robot.getPIDmaneuverI(), robot.getPIDmaneuverD());

            double currentDistance = 0;
            double yawError;
            switch (autonomousStep) {


                  case 0: //resets everything and waits
                        robot.resetAttitude();
                        robot.resetEncoders();
                        if (System.currentTimeMillis() >= startingTime + 100) {
                              autonomousStep = 1;
                        }
                        break;

                  case 1: //auto-align
                        autonomousStep = 2;
                        break;

                  case 2: //PID reset for straightaway
                        startingDistance = robot.getDistanceInchesLeft();
                        PIDSteering.reset();
                        PIDSteering.enableContinuousInput(-180,180);
                        currentYaw = 0;
                        autonomousStep = 3;
                        break;

                  case 3: //straightaway
                        correction = PIDSteering.calculate(robot.getYaw() - currentYaw);
                        robot.setMotorPowerPercentage(defaultSpeed * (1 + correction), defaultSpeed * (1 - correction));
                        currentDistance = robot.getDistanceInchesLeft();
                        if (currentDistance - startingDistance > 80) { //maybe change depending on how far we need to go
                              robot.driveForward(0);
                              autonomousStep = 4;
                        }
                        break;

                  case 4: //reset for backward straight-away
                        startingDistance = robot.getDistanceInchesLeft();
                        PIDSteering.reset();
                        PIDSteering.enableContinuousInput(-180,180);
                        currentYaw = 0;
                        autonomousStep = 5;
                        break;

                  case 5: //backward straight-away
                        correction = PIDSteering.calculate(robot.getYaw() - currentYaw);
                        robot.setMotorPowerPercentage(-1 * defaultSpeed * (1 - correction), -1 * defaultSpeed * (1 + correction));
                        currentDistance = robot.getDistanceInchesLeft();
                        SmartDashboard.putNumber("startDistance", startingDistance);
                        SmartDashboard.putNumber("currentDistance", currentDistance);
                        SmartDashboard.putNumber("distanceDifference", currentDistance - startingDistance);
                        if (currentDistance - startingDistance < -40) { //maybe change depending on how far we need to go
                              robot.driveForward(0);
                              autonomousStep = 6;
                        }
                        break;

                  case 6: //reset for arc
                        startingDistance = robot.getDistanceInchesRight();
                        PIDSteering.reset();
                        PIDSteering.disableContinuousInput();
                        startingYaw = robot.getYaw();
                        autonomousStep = 7;
                        break;

                  case 7: //left arc to pick up third ball
                        yawDifference = continuousAngleDiff((robot.getYaw() - startingYaw) / 180 * Math.PI);
                        correction = PIDSteering.calculate((robot.getDistanceInchesRight() - startingDistance) + outerRadius * yawDifference);
                        robot.setMotorPowerPercentage((defaultSpeed * .75) * (1 + correction), (defaultSpeed * 1.5) * (1 - correction));
                        currentDistance = robot.getDistanceInchesRight();
                        if (currentDistance - startingDistance > outerArcLength) {
                              autonomousStep = 8;
                        }
                        break;

                  case 8: //reset for inverse arc (not resetting starting distance)
                        PIDSteering.reset();
                        PIDSteering.disableContinuousInput();
                        startingYaw = robot.getYaw();
                        prevStartingDistance = startingDistance;
                        startingDistance = robot.getDistanceInchesRight();
                        autonomousStep = 9;
                        break;

                  case 9: //backwards arc to previous position
                        yawDifference = continuousAngleDiff((robot.getYaw() - startingYaw) / 180 * Math.PI);
                        correction = PIDSteering.calculate((robot.getDistanceInchesRight() - startingDistance) + outerRadius * yawDifference);
                        robot.setMotorPowerPercentage((defaultSpeed * -.75) * (1 - correction), (defaultSpeed * -1.5) * (1 + correction));
                        currentDistance = robot.getDistanceInchesRight();
                        if (currentDistance - prevStartingDistance <= 0) {
                              autonomousStep = 10;
                        }
                        break;

                  case 10: //cease your autnomous
                        robot.driveForward(0);
                        //                               ¯\_(ツ)_/¯
                        autonomousStep = 11;
                        break;

                  case 11: //auto-align
                        break;

            }
      }
}
/*

      Position / Proportion  = How Far away we are
      Integral
      Derivative

      wheel to wheel: 23in

 */