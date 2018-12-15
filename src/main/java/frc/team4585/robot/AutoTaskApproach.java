package frc.team4585.robot;

public class AutoTaskApproach extends AutoTask {

	private double _TargetDistance;
	private double _CurrentDistance;
	private double _ApproachSpeed;
	private DistanceSensor _UseSensor;

	private ChassisControl _TheChassisControl;
	
	public AutoTaskApproach(double ApproachDistance, DistanceSensor UseSensor, double ApproachSpeed, ChassisControl TheChassisControl)
	{
		_UseSensor = UseSensor;
		_TargetDistance = ApproachDistance;
		_ApproachSpeed = ApproachSpeed;
		_TheChassisControl = TheChassisControl;
	}
	
	public void StartTask()
	{
		_TheChassisControl.SetMoveAtSpeed(_ApproachSpeed);
		_CurrentDistance = _UseSensor.GetDistanceFeet();
	}
	
	public boolean DoTask()
	{
		boolean CloseEnough = false;

		_CurrentDistance = _UseSensor.GetDistanceFeet();
		if(_CurrentDistance <= _TargetDistance)
		{
			_TheChassisControl.SlowToStop();
			CloseEnough = true;
		}
		
		_TheChassisControl.DoActionMoveAtSpeed();
		return CloseEnough;
	}
	
}
