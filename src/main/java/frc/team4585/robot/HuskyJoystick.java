package frc.team4585.robot;
import edu.wpi.first.wpilibj.Joystick;

public class HuskyJoystick extends RobotSensor implements HuskySubsystem  {
	
	
	private Joystick _Stick;
	private double _ForwardBackValue = 0.0;
	private double _LeftRightValue = 0.0;
	private double _TwistValue = 0.0;
	
	private double _ThrottleValue = 0.0;
	private boolean _TopButton = false;
	private boolean _TriggerButton = false;
	
	
	public HuskyJoystick(String Name, int JoyPort)
	{
		super(Name);
		 _Stick = new Joystick(JoyPort);
	}

	public void autoInit()
	{
		
	}
	
	public void doAuto()
	{
		
	}
	
	public void teleopInit()
	{
		
	}
	
	public void doTeleop()
	{
		
	}
	
	
	private double TweakForwardBackValue(double RawValue)
	{
//		final double DEAD_ZONE_THRESHOLD = 0.18;
		final double DEAD_ZONE_THRESHOLD = 0.01;

		double RetVal = 0.0;
		
		if(Math.abs(RawValue) >= DEAD_ZONE_THRESHOLD)
		{
			RetVal = RawValue;
		}
			
		return RetVal;
		
	}
	
	private double TweakLeftRightValue(double RawValue)
	{
		return RawValue;
	}
	
	
	private double TweakTwistValue(double RawValue)
	{
		final double TWIST_MAX = 0.6;
		double RetValue = RawValue;
		if(Math.abs(RawValue) >= TWIST_MAX)
		{
			if(RawValue > 0.0)
			{
				RetValue = TWIST_MAX;
			}
			else
			{
				RetValue = -TWIST_MAX;
			}
		}
		return RetValue;
	}
	
	private double TweakThrottleValue(double RawValue)
	{
		return RawValue;
	}
	
	
	
	
	public void UpdateReadings()
	{
		_ForwardBackValue = TweakForwardBackValue(_Stick.getY());
		_LeftRightValue = TweakLeftRightValue(_Stick.getX());	
		_TwistValue = TweakTwistValue(_Stick.getTwist());
		_ThrottleValue = TweakThrottleValue(_Stick.getThrottle());
		_TopButton = _Stick.getTop();
		_TriggerButton = _Stick.getTrigger();
	}
	
	public double GetForwardBackVal()
	{
		return _ForwardBackValue;
	}
	
	public double GetLeftRightVal()
	{
		return _LeftRightValue;
	}
	
	public double GetTwistValue()
	{
		return _TwistValue;
	}
	
	public double GetThrottleValue()
	{
		return _ThrottleValue;
	}
	
	public boolean GetTopButton()
	{
		return _TopButton;
	}
	
	public boolean GetTriggerButton()
	{
		return _TriggerButton;
	}
	
}
