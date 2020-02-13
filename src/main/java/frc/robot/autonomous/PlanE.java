package frc.robot.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.genericrobot.GenericRobot;
import edu.wpi.first.wpilibj.controller.PIDController;

public class PlanE extends GenericAutonomous {

      //change speed depending on robot!! (CaMOElot = .4, TestBot = .3)
      double defaultSpeed = 0.2; //CHANGE WHEN DONE

      static double startingYaw      = 0.0;
      static double startingDistance = 0.0;
      PIDController PIDSteering = new PIDController(5.0e-2, 1.0e-4, 2.0e-4);
      double correction;
      static double currentYaw = 0;
      double outerArcLength = 33;
      double innerArc = 35.45;
      double outerRadius = 32;
      double yawDifference = 0;
      long startingTime;
      double powerDecrement;
      double currentDistance;

    @Override
    protected void printSmartDashboardInternal() {
        SmartDashboard.putNumber("Current Distance", currentDistance);
        SmartDashboard.putNumber("Starting Distance", startingDistance);
        SmartDashboard.putNumber("distanceDifference", currentDistance-startingDistance );
    }

    @Override public void autonomousInit(GenericRobot robot) {
            startingTime = System.currentTimeMillis();
            autonomousStep = -1;
      }

      @Override public void autonomousPeriodic(GenericRobot robot) {
            currentDistance = 0;
            double yawError;
            switch (autonomousStep) {
                  case -1:
                        robot.resetAttitude();
                        robot.resetEncoders();
                        if (System.currentTimeMillis() >= startingTime + 100) {
                              autonomousStep = 0;
                        }
                        break;


                  case 0: //skrts back to zero (account for 28 yaw offset)
                        currentYaw = robot.getYaw();
                        robot.setMotorPowerPercentage(.2, -.2);
                        if(currentYaw > 20){
                              robot.driveForward(0);
                              startingYaw = 28;
                              autonomousStep = 1;

                        }
                        break;

                  case 1:
                        autonomousStep = 2;
                        break;

                  case 2: //PID reset for 1st (left) arc
                        PIDSteering.reset();
                        PIDSteering.disableContinuousInput();
                        startingYaw = robot.getYaw();
                        startingDistance = robot.getDistanceInchesRight();

                        autonomousStep = 3;
                        break;

                  case 3: //1st (left) arc
                        yawDifference = continuousAngleDiff((robot.getYaw() - startingYaw) / 180 * Math.PI);
                        correction = PIDSteering.calculate((robot.getDistanceInchesRight() - startingDistance) + outerRadius * yawDifference);
                        robot.setMotorPowerPercentage((defaultSpeed * .75) * (1 + correction), (defaultSpeed * 1.5) * (1 - correction));
                        currentDistance = robot.getDistanceInchesRight();

                        if (currentDistance - startingDistance > outerArcLength) {
                              autonomousStep = 4;
                        }
                        break;

                  case 4: //PID reset for 2nd (right) arc
                        PIDSteering.reset();
                        PIDSteering.disableContinuousInput();
                        startingDistance = robot.getDistanceInchesLeft();
                        startingYaw = robot.getYaw();
                        autonomousStep = 5;
                        break;

                  case 5: //2nd (right) arc
                        yawDifference = continuousAngleDiff((robot.getYaw() - startingYaw)*Math.PI/180.0);
                        correction = PIDSteering.calculate(outerRadius * yawDifference - (robot.getDistanceInchesLeft() - startingDistance));
                        robot.setMotorPowerPercentage((defaultSpeed * 1.5) * (1 + correction), (defaultSpeed * .75) * (1 - correction));
                        currentDistance = robot.getDistanceInchesLeft();

                        if(currentDistance - startingDistance > outerArcLength) {
                              autonomousStep = 6;
                        }
                        break;


                  case 6: //PID reset for straightaway
                        startingDistance = robot.getDistanceInchesLeft();
                        PIDSteering.reset();
                        PIDSteering.enableContinuousInput(-180,180);
                        currentYaw = 28; //maybe??
                        autonomousStep = 7;
                        break;

                  case 7: //trench run (~200in)
                        correction = PIDSteering.calculate(robot.getYaw() - currentYaw);
                        robot.setMotorPowerPercentage(1.5 * defaultSpeed * (1 + correction), 1.5 * defaultSpeed * (1 - correction));
                        currentDistance = robot.getDistanceInchesLeft();



                        //decrescendo power
                        //if(currentDistance - startingDistance > ){ //start to decrement?
                              //autonomousStep = 4;

                        //
                        if(currentDistance - startingDistance > 120){
                              autonomousStep = 8;

                        }
                        break;

/*
                   case 4: //decrement power
                        currentDistance = robot.getDistanceInchesLeft();
                        double slowToStop = (defaultSpeed - (defaultSpeed / 15)*((currentDistance-startingDistance)-45)); //?
                        correction = PIDSteering.calculate(robot.getYaw() - currentYaw);
                        robot.setMotorPowerPercentage(1.5 * slowToStop * (1 + correction), 1.5 * slowToStop * (1 - correction)); // div by 2 to debug
                        if(currentDistance - startingDistance > 200){
                              autonomousStep = 5;

                        }
                        break;

 */

                  case 8: //cease your autnomous
                        robot.driveForward(0);
                        //                               ¯\_(ツ)_/¯
                        break;



            }


      }
}


