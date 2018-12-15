package frc.team4585.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GhostController {

	@SuppressWarnings("unused")
	private Timer _MasterTimer;
	
	private Chassis _TheChassis;
	private PivotArm _TheArm;
	private ContactSensor _FrontContactSwitch;
	private SonarSensor _FrontSonarSensor;
	
	private ChassisControl _ChassisController = new ChassisControl();
	private PivotArmControl _PivotArmController = new PivotArmControl(Robot.ARM_MIN_ANGLE, Robot.ARM_MAX_ANGLE);
	
	private TaskSequenceGroup _CurrentTaskSequenceGroup;

	private TaskSequenceGroup _TaskSequenceGroup1 = new TaskSequenceGroup("SeqGroup1");
	private TaskSequenceGroup _TaskSequenceGroup2 = new TaskSequenceGroup("SeqGroup2");;
	private TaskSequenceGroup _TaskSequenceGroup3 = new TaskSequenceGroup("SeqGroup3");;
	private TaskSequenceGroup _TaskSequenceGroup4 = new TaskSequenceGroup("SeqGroup4");;
	private TaskSequenceGroup _TaskSequenceGroup5 = new TaskSequenceGroup("SeqGroup5");;
	
	private SendableChooser<String> _ActionChooser = new SendableChooser<>();
	
	private double _DebugMaxSpeed = 0.0;
	

	public GhostController(Timer MasterTimer)
	{
		_MasterTimer = MasterTimer;
		
		_TheChassis = (Chassis) SensorList.GetSensor(SensorNames.MASTER_CHASSIS_NAME);
		_TheArm = (PivotArm) SensorList.GetSensor(SensorNames.PIVOT_ARM_NAME);
		_FrontContactSwitch = (ContactSensor)SensorList.GetSensor(SensorNames.FRONT_CONTACT_NAME);
		_FrontSonarSensor = (SonarSensor)SensorList.GetSensor(SensorNames.FRONT_SONAR_NAME);

		TasksSetup1();
		TasksSetup2();
		TasksSetup3();
		TasksSetup4();
		TasksSetup5();
	
	}
	
	// called at the beginning of the program
	public void InitialSetup()
	{
		_ActionChooser.addDefault("Do Sequence 1", "Seq 1");
		_ActionChooser.addObject("Do Sequence 2", "Seq 2");
		_ActionChooser.addObject("Do Sequence 3", "Seq 3");
		_ActionChooser.addObject("Do Sequence 4", "Seq 4");
		_ActionChooser.addObject("Do Sequence 5", "Seq 5");

		SmartDashboard.putData("Auto modes", _ActionChooser);
	}
	
	// called from autonomousInit
	public void Initialize()
	{
		String ActionChoice = _ActionChooser.getSelected();

		switch(ActionChoice)
		{
			default:
			case "Seq 1":
				_CurrentTaskSequenceGroup = _TaskSequenceGroup1;
				break;
				
			case "Seq 2":
				_CurrentTaskSequenceGroup = _TaskSequenceGroup2;
				break;

			case "Seq 3":
				_CurrentTaskSequenceGroup = _TaskSequenceGroup3;
				break;

			case "Seq 4":
				_CurrentTaskSequenceGroup = _TaskSequenceGroup4;
				break;

			case "Seq 5":
				_CurrentTaskSequenceGroup = _TaskSequenceGroup5;
				break;

		}
		
		_ChassisController.SetCurrentLocation(0.0, 0.0);
		SmartDashboard.putString("Chosen Sequence Group", _CurrentTaskSequenceGroup.GetGroupName());
	}

	/**
	 * Called for every tick of autonomous loop from Robot
	 */
	public void EvaluateDecisions()
	{
		double CurrentHeading = _TheChassis.GetCurrentHeading();
		HuskyVector2D CurrentLoc = _TheChassis.GetCurrentLocation();
		double CurrentSpeed = _TheChassis.GetCurrentSpeed();
		
		_ChassisController.SetCurrentLocation(CurrentLoc);
		_ChassisController.SetCurrentHeading(CurrentHeading);
		_ChassisController.SetCurrentSpeed(CurrentSpeed);

		if(CurrentSpeed > _DebugMaxSpeed)
		{
			_DebugMaxSpeed = CurrentSpeed;		
		}
		
		SmartDashboard.putBoolean("Front Sensor Closed", _FrontContactSwitch.IsClosed());
		SmartDashboard.putNumber("Front Sonar Reading", _FrontSonarSensor.GetDistanceFeet());
		SmartDashboard.putNumber("Max Speed So Far", _DebugMaxSpeed);

		
		if(_CurrentTaskSequenceGroup.DoTasks()) //this will return true when all TaskSequence threads complete
		{
			// task list all done
			_TheChassis.SetMoveValues(0.0, 0.0);
			_TheArm.SetMotorValue(0.0);
		}
		else
		{
			_TheChassis.SetMoveValues(_ChassisController.GetUseSpeed(), _ChassisController.GetUseTwist());
			_TheArm.SetMotorValue(_PivotArmController.GetArmMotorValue());
		}
	}
	
	private void TasksSetup1()
	{
		TaskSequenceChassis ChassisTaskSequence = new TaskSequenceChassis("Chassis Seq 1", _ChassisController);
		ChassisTaskSequence.AddTaskTrackTo(0.0, 10.0);
		ChassisTaskSequence.AddTaskRotateTo(90.0);
		ChassisTaskSequence.AddTaskTrackTo(10.0, 10.0);
		ChassisTaskSequence.AddTaskRotateTo(-90.0);
		ChassisTaskSequence.AddTaskTrackTo(-10.0, 10.0);
		ChassisTaskSequence.AddTaskRotateTo(90.0);
		ChassisTaskSequence.AddTaskTrackTo(0.0, 10.0);
		ChassisTaskSequence.AddTaskRotateTo(180.0);
		
		_TaskSequenceGroup1.AddSequence(ChassisTaskSequence);
	}
 
	private void TasksSetup2()
	{
		TaskSequenceChassis ChassisTaskSequence = new TaskSequenceChassis("Chassis Seq 2", _ChassisController);
		ChassisTaskSequence.AddTaskTrackTo(10.0, 10.0);
		ChassisTaskSequence.AddTaskRevTrackTo(0.0, 20.0);
		
		_TaskSequenceGroup2.AddSequence(ChassisTaskSequence);
	}

	private void TasksSetup3()
	{
		TaskSequenceArm ArmTaskSequence = new TaskSequenceArm("Arm Seq 3", _PivotArmController);
		ArmTaskSequence.AddTaskRotateArm(70.0);
		ArmTaskSequence.AddTaskRotateArm(15.0);
		
		_TaskSequenceGroup3.AddSequence(ArmTaskSequence);
	}

	private void TasksSetup4()
	{
		TaskSequenceChassis ChassisTaskSequence = new TaskSequenceChassis("Chassis Seq 4", _ChassisController);
		ChassisTaskSequence.AddTaskTrackToward( 8.0,  6.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(8.0,  11.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(2.0,  15.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(-8.0, 15.5, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(-8.0, 22.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(-2.0, 27.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(4.0,  27.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(8.0,  21.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(8.0,  13.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(2.0,  10.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(-8.0, 9.5, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(-8.0, 3.0, 6.0);
		ChassisTaskSequence.AddTaskTrackToward(-2.0, -1.0, 6.0);
		ChassisTaskSequence.AddTaskTrackTo(0.0, 0.0);
		ChassisTaskSequence.AddTaskRotateTo(0.0);
		
		_TaskSequenceGroup4.AddSequence(ChassisTaskSequence);
	}

	private void TasksSetup5()
	{
		TaskSequenceChassis ChassisTaskSequence5 = new TaskSequenceChassis("Chassis Seq 5", _ChassisController);
		ChassisTaskSequence5.AddTaskRotateTo(90.0);
		ChassisTaskSequence5.AddTaskWaitForTrigger(3);
		ChassisTaskSequence5.AddTaskRotateTo(-90.0);
		
		TaskSequence GenericTaskSequence5 = new TaskSequence("Generic Seq 5");
		GenericTaskSequence5.AddTaskWaitForContact(_FrontContactSwitch);
		GenericTaskSequence5.AddTaskFireTrigger(3);

		_TaskSequenceGroup5.AddSequence(ChassisTaskSequence5);
		_TaskSequenceGroup5.AddSequence(GenericTaskSequence5);
	}

}
