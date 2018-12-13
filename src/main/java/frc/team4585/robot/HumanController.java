package frc.team4585.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HumanController {

	private Chassis _TheChassis;
	private PivotArm _TheArm;
	
	private HuskyJoystick _TheJoystick;

	private SonarSensor _FrontSonar;

	private PivotArmControl _PivotArmController = new PivotArmControl(Robot.ARM_MIN_ANGLE, Robot.ARM_MAX_ANGLE);

	HumanController()
	{
		_TheChassis = (Chassis) SensorList.GetSensor(SensorNames.MASTER_CHASSIS_NAME);
		_TheArm = (PivotArm) SensorList.GetSensor(SensorNames.PIVOT_ARM_NAME);
		_TheJoystick = (HuskyJoystick) SensorList.GetSensor(SensorNames.MASTER_JOYSTICK_NAME);

		_FrontSonar = (SonarSensor)SensorList.GetSensor(SensorNames.FRONT_SONAR_NAME);
	}
	
	void EvaluateDecisions()
	{
		double ForBack = _TheJoystick.GetForwardBackVal();
		double LeftRight = _TheJoystick.GetLeftRightVal();
		double Twist = _TheJoystick.GetTwistValue();
		double Throttle = _TheJoystick.GetThrottleValue();
		boolean TopButton = _TheJoystick.GetTopButton();
		boolean TriggerButton = _TheJoystick.GetTriggerButton();
		
		double SonarValue = _FrontSonar.GetDistanceFeet();

		SmartDashboard.putNumber("Joystick ForBack", ForBack);
		SmartDashboard.putNumber("Joystick LeftRight", LeftRight);
		SmartDashboard.putNumber("Joystick Twist", Twist);
		SmartDashboard.putNumber("Joystick Throttle", Throttle);

		SmartDashboard.putBoolean("Top Button", TopButton);
		SmartDashboard.putBoolean("Trigger Button", TriggerButton);
		SmartDashboard.putNumber("Sonar Distance", SonarValue);

	//	DoPivotArm(TriggerButton, TopButton);
		
	//	_TheArm.SetMotorValue(_PivotArmController.GetArmMotorValue());
		_TheChassis.SetMoveValues(ForBack, Twist);
	}
	
	
	void DoPivotArm(boolean UpButton, boolean DownButton)
	{
		_PivotArmController.SetCurrentAngle(_TheArm.GetCurrentAngle());
		SmartDashboard.putNumber("Arm Angle", _PivotArmController.GetCurrentAngle());
		
		if(UpButton)
		{
			_PivotArmController.MoveUp();
		}
		else
		{
			if(DownButton)
			{
				_PivotArmController.MoveDown();
			}
			else
			{
				_PivotArmController.Stop();
			}
		}
		
		_PivotArmController.DoActionMove();
	}
}
