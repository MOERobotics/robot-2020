package frc.robot.genericrobot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SPI;

public class Falcon extends GenericRobot{

    AHRS navx = new AHRS(SPI.Port.kMXP, (byte) 50);

    CANSparkMax leftDriveA      = new CANSparkMax(13, MotorType.kBrushless);
    CANSparkMax leftDriveB      = new CANSparkMax(14, MotorType.kBrushless);
    CANSparkMax leftDriveC      = new CANSparkMax(15, MotorType.kBrushless);
    CANSparkMax rightDriveA     = new CANSparkMax(20, MotorType.kBrushless);
    CANSparkMax rightDriveB     = new CANSparkMax( 1, MotorType.kBrushless);
    CANSparkMax rightDriveC     = new CANSparkMax( 2, MotorType.kBrushless);

    CANSparkMax climberA        = null;//= new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax climberB        = null;//new CANSparkMax( 3, CANSparkMaxLowLevel.MotorType.kBrushless);
    CANSparkMax generatorShift  = null;//new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);

    CANSparkMax shooterA                   = new CANSparkMax( 5, MotorType.kBrushless);
    CANPIDController shooterAPIDController = new CANPIDController(shooterA);
    CANSparkMax shooterB                   = new CANSparkMax( 4, MotorType.kBrushless);
    CANPIDController shooterBPIDController = new CANPIDController(shooterB);
    CANSparkMax indexer         = new CANSparkMax( 6, MotorType.kBrushed);
    CANSparkMax escalator       = new CANSparkMax( 7, MotorType.kBrushless);
    CANSparkMax angleAdj        = new CANSparkMax( 8, MotorType.kBrushless);

    CANSparkMax controlPanel    = null;//= new CANSparkMax( 9, CANSparkMaxLowLevel.MotorType.kBrushless);

    CANSparkMax collector       = new CANSparkMax(10, MotorType.kBrushed);

    CANEncoder encoderRight     = new CANEncoder(rightDriveA);
    CANEncoder encoderLeft      = new CANEncoder( leftDriveA);
    CANEncoder encoderShootA    = new CANEncoder(shooterA);
    CANEncoder encoderShootB    = new CANEncoder(shooterB);
    Lidar lidar = new Lidar();


    public Falcon() {
        leftDriveC .follow( leftDriveA);
        leftDriveB .follow( leftDriveA);
        rightDriveB.follow(rightDriveA);
        rightDriveC.follow(rightDriveA);

        rightDriveA.setIdleMode(IdleMode.kBrake);
        rightDriveB.setIdleMode(IdleMode.kBrake);
        rightDriveC.setIdleMode(IdleMode.kBrake);

        leftDriveA .setIdleMode(IdleMode.kBrake);
        leftDriveB .setIdleMode(IdleMode.kBrake);
        leftDriveC .setIdleMode(IdleMode.kBrake);

        rightDriveA.setInverted(true);

        escalator.setIdleMode(IdleMode.kBrake);

        shooterAPIDController.setP(7.5e-5);
        shooterAPIDController.setI(1.0e-6);
        shooterAPIDController.setD(1.0e-2);
        shooterAPIDController.setFF(1.67e-4);
        shooterAPIDController.setIZone(200);
        shooterAPIDController.setDFilter(0);

        shooterBPIDController.setP(7.5e-5);
        shooterBPIDController.setI(1.0e-6);
        shooterBPIDController.setD(1.0e-2);
        shooterBPIDController.setFF(1.67e-4);
        shooterBPIDController.setIZone(200);
        shooterBPIDController.setDFilter(0);
    }

    @Override
    protected void printSmartDashboardInternal() {
        super.printSmartDashboardInternal();
    }

    @Override
    protected void setMotorPowerPercentageInternal(double leftPower, double rightPower) {
        rightDriveA.set (rightPower /2);
        rightDriveB.set (rightPower /2);
        rightDriveC.set (rightPower /2);
        leftDriveA.set  (leftPower /2 );
        leftDriveB.set  (leftPower  /2);
        leftDriveB.set  (leftPower  /2);
    }
    @Override
    public double getShooterVelocityUpper(){
        return encoderShootA.getVelocity();
    }
    @Override
    public double getShooterVelocityLower(){
        return encoderShootB.getVelocity();
    }

    @Override
    public double getDistanceRatioLeft() {
        return 0.306;
    }

    @Override
    public double getDistanceTicksLeft() {
        return encoderLeft.getPosition();
    }

    @Override
    public double getDistanceRatioRight() {
        return 0.306;
        }


    @Override
    public double getDistanceTicksRight() {
        return encoderRight.getPosition();
    }

    @Override
    public void resetEncoders() {
        super.resetEncoders();
    }

    @Override
    public void resetEncoderLeft() {
        encoderLeft.setPosition(0.0);
    }

    @Override
    public void resetEncoderRight() {
        encoderRight.setPosition(0.0);
    }

    @Override
    public double getYaw() {
        return navx.getYaw();
    }

    @Override
    public double getPitch() {
        return navx.getPitch();
    }

    @Override
    public double getRoll() {
        return navx.getRoll();
    }

    @Override
    public void resetAttitude() {
        navx.reset();
    }

    @Override
    public void setShooterRPMInternal(double upperRPM, double lowerRPM) {
        shooterAPIDController.setReference(-upperRPM, ControlType.kVelocity);
        shooterBPIDController.setReference( lowerRPM, ControlType.kVelocity);
    }

    @Override
    protected void setShooterPowerPercentageInternal(double upperPower, double lowerPower) {
        shooterA.set(-upperPower);
        shooterB.set(lowerPower);
    }

   /* @Override
    protected void spinControlPanelInternal(double power) {
        controlPanel.set(power);
    }

    @Override
    public char getCurrentControlPanelColor() {
        return super.getCurrentControlPanelColor();
    }

    */

    @Override
    public Lidar getLidarSubsystem() {return lidar; }

    @Override
    public Double getLidarDistanceInchesFront() {
        return lidar.getDistanceInches(3);
    }

    @Override
    public Double getLidarDistanceInchesRear() {
        return lidar.getDistanceInches(2);
    }

    @Override
    public Double getLidarDistanceInchesLeft() {
        return lidar.getDistanceInches(0);
    }

    @Override
    public Double getLidarDistanceInchesRight() {
        return lidar.getDistanceInches(1);
    }

    @Override
    protected void setIndexerPowerInternal(double indexerPower) {
        indexer.set(indexerPower);
    }

    @Override
    protected void setCollectorPowerInternal(double collectorPower) {
        collector.set(collectorPower);
    }

    /*@Override
    protected void climbVerticalInternal(double climberPower) {
        climberA.set( climberPower);
        climberB.set(-climberPower);
    }
     */

    @Override
    protected void setEscalatorPowerInternal(double escalatorPower) {
        escalator.set(escalatorPower);
    }

   /* @Override
    protected void generatorShiftInternal(double shiftPower) {
        generatorShift.set(shiftPower);
    }

    */

    @Override
    protected void setAngleAdjusterPowerInternal(double aimPower) {
        angleAdj.set(aimPower);
    }


}
