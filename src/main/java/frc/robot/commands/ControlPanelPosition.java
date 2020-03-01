package frc.robot.commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.genericrobot.GenericRobot;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.genericrobot.WheelOfFortune;

public class ControlPanelPosition extends GenericCommand{

    WheelOfFortune controlPanel = new WheelOfFortune();
    Color endColor = new WheelOfFortune.GoodColor(0, 0, 0, "Default");

    @Override
    public void begin(GenericRobot robot){

        String gameData;
        gameData = "Green";
        if(gameData.length() > 0)
        {
            switch (gameData.charAt(0))
            {
                // Red --> Blue, Yellow --> Green, Blue --> Red, Green --> Yellow
                case 'B' :
                    //Blue case code
                    endColor = WheelOfFortune.kRedTarget;
                    break;
                case 'G' :
                    //Green case code
                    endColor = WheelOfFortune.kYellowTarget;
                    break;
                case 'R' :
                    //Red case code
                    endColor = WheelOfFortune.kBlueTarget;
                    break;
                case 'Y' :
                    //Yellow case code
                    endColor = WheelOfFortune.kGreenTarget;
                    break;
            }
        }
    }

    @Override
    public void step(GenericRobot robot){
        robot.spinControlPanel(0.1);
        if(controlPanel.getInferredColor() == endColor){
            robot.spinControlPanel(0.0);
        }
    }
}


