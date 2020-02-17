package frc.robot.commands;

import frc.robot.genericrobot.GenericRobot;
import edu.wpi.first.wpilibj.DriverStation;

public class ControlPanelPosition extends GenericCommand{

    @Override
    public void step(GenericRobot robot){
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if(gameData.length() > 0)
        {
            switch (gameData.charAt(0))
            {
                case 'B' :
                    //Blue case code
                    break;
                case 'G' :
                    //Green case code
                    break;
                case 'R' :
                    //Red case code
                    break;
                case 'Y' :
                    //Yellow case code
                    break;
                default :
                    //This is corrupt data
                    break;
            }
        } else {
            //Code for no data received yet
        }
    }
}
