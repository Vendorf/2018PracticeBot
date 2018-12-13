package frc.team4585.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;


public class Chassis extends RobotSensor implements HuskySubsystem  {
	
	private static final double LEFT_DISTANCE_PER_PULSE = (617.0d / 3.0d) / 13976.0d;
	private static final double RIGHT_DISTANCE_PER_PULSE = (617.0d / 3.0d) / 14032.0d;
	
	
	private DifferentialDrive _robotDrive	= 
			new DifferentialDrive(new Spark(WiringConstants.LEFT_MOTOR_PORT), new Spark(WiringConstants.RIGHT_MOTOR_PORT));

	private Encoder _leftEncoder = new Encoder(WiringConstants.LEFT_ENCODER_PORT_A, WiringConstants.LEFT_ENCODER_PORT_B);
	private Encoder _rightEncoder = new Encoder(WiringConstants.RIGHT_ENCODER_PORT_A, WiringConstants.RIGHT_ENCODER_PORT_B);

	private ADXRS450_Gyro _gyro = new ADXRS450_Gyro();
	private Timer _MasterTimer = null;
	private double _OldTime = 0.0;
	
	private double _ArcadeUseSpeed = 0.0;
	private double _ArcadeUseTwist = 0.0;
	
	private double _PreviousUseSpeed = 0.0;
	private static final double MAX_SPEED_CHANGE = 0.1;
	
	private double _PreviousUseTwist = 0.0;
	//private static final double MAX_TWIST_CHANGE = 0.025;
	private static final double MAX_TWIST_CHANGE = 0.5;


	private HuskyVector2D _CurrentLocation = new HuskyVector2D();
	private HuskyVector2D _CurrentVelocity = new HuskyVector2D();
	private CompassHeading _CurHeading = new CompassHeading();
	

	public Chassis(String Name, Timer MasterTimer)
	{
		super(Name);
		
		_MasterTimer = MasterTimer;
		_OldTime = _MasterTimer.get();
		
		_leftEncoder.setDistancePerPulse(LEFT_DISTANCE_PER_PULSE);
		_rightEncoder.setDistancePerPulse(RIGHT_DISTANCE_PER_PULSE);
	}
	
	
	public void autoInit()
	{
		_gyro.reset();
		
		_rightEncoder.reset();
		_leftEncoder.reset();
		
		_CurrentLocation.SetVals(0.0, 0.0);
		_CurrentVelocity.SetVals(0.0, 0.0);
		_CurHeading.SetValue(0.0);
	}
	
	
	public void doAuto()
	{
		DoAction();
	}

	
	public void teleopInit()
	{
		_gyro.reset();
		
		_rightEncoder.reset();
		_leftEncoder.reset();	
	}
	
	public void doTeleop()
	{
		DoAction();
	}
	
	
	private void DoAction()
	{
		SmartDashboard.putNumber("Arcade Speed", _ArcadeUseSpeed);
		SmartDashboard.putNumber("Arcade Twist", _ArcadeUseTwist);

		double SafeSpeed = _ArcadeUseSpeed;
		double DesiredSpeedDiff = _PreviousUseSpeed - _ArcadeUseSpeed;
		if(Math.abs(DesiredSpeedDiff) > MAX_SPEED_CHANGE)
		{
			if(DesiredSpeedDiff > 0.0)
			{
				SafeSpeed = _PreviousUseSpeed - MAX_SPEED_CHANGE;
			}
			else
			{
				SafeSpeed = _PreviousUseSpeed + MAX_SPEED_CHANGE;
			}
		}
		
		double SafeTwist = _ArcadeUseTwist;
		double DesiredTwistDiff = _PreviousUseTwist - _ArcadeUseTwist;
		if(Math.abs(DesiredTwistDiff) > MAX_TWIST_CHANGE)
		{
			if(DesiredTwistDiff > 0.0)
			{
				SafeTwist = _PreviousUseTwist - MAX_TWIST_CHANGE;
			}
			else
			{
				SafeTwist = _PreviousUseTwist + MAX_TWIST_CHANGE;
			}
		}
		
		_robotDrive.arcadeDrive(SafeSpeed, SafeTwist);
		_PreviousUseSpeed = SafeSpeed;
		_PreviousUseTwist = SafeTwist;
	}
	

	public void SetCurrentLocation(double XVal, double YVal)
	{
		_CurrentLocation.SetVals(XVal, YVal);
	}
	
	public void SetCurrentLocation(HuskyVector2D TargLoc)
	{
		_CurrentLocation.SetVals(TargLoc);
	}
	
	public HuskyVector2D GetCurrentLocation()
	{
		return _CurrentLocation;
	}
	
	public HuskyVector2D GetCurrentVelocity()
	{
		return _CurrentVelocity;
	}
	
	public double GetCurrentSpeed()
	{
		return _CurrentVelocity.GetMagnitude();
	}
	

	private void UpdateLocation()
	{
		double CurTime = _MasterTimer.get();
		double dt = CurTime - _OldTime;
		_OldTime = CurTime;
		
		double encoderVelocity = (_leftEncoder.getRate() + _rightEncoder.getRate()) / 2;
		
		_CurrentVelocity.SetVals(Math.sin(Math.toRadians(_CurHeading.GetValue())) * encoderVelocity * dt, 
				Math.cos(Math.toRadians(_CurHeading.GetValue())) * encoderVelocity * dt);
//		double encoderXPos = Math.sin(Math.toRadians(_CurHeading.GetValue())) * encoderVelocity * dt;
//		double encoderYPos = Math.cos(Math.toRadians(_CurHeading.GetValue())) * encoderVelocity * dt;
		
		_CurrentLocation.AddVec(_CurrentVelocity);
	
		SmartDashboard.putNumber("X Location", _CurrentLocation.GetX());
		SmartDashboard.putNumber("Y Location", _CurrentLocation.GetY());

	}
	
	
	public void UpdateReadings()
	{
		UpdateHeading();
		UpdateLocation();
	}
	
	private void UpdateHeading()
	{
		_CurHeading.SetValue(_gyro.getAngle());
		SmartDashboard.putNumber("Heading", _CurHeading.GetValue());
	}
	
	public double GetCurrentHeading()
	{
		return _CurHeading.GetValue();
	}
	
	public void SetMoveValues(double SpeedVal, double TwistVal)
	{
		_ArcadeUseSpeed = SpeedVal;
		_ArcadeUseTwist = TwistVal;
	}
	
}
