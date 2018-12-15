/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team4585.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	private Timer _MasterTimer = new Timer();
	private Chassis _TheChassis = new Chassis(SensorNames.MASTER_CHASSIS_NAME, _MasterTimer);
	@SuppressWarnings("unused")
	private HuskyJoystick _TheJoystick = new HuskyJoystick(SensorNames.MASTER_JOYSTICK_NAME, WiringConstants.JOYSTICK_PORT);
	@SuppressWarnings("unused")
	private ContactSensor _FrontContact = new ContactSensor(SensorNames.FRONT_CONTACT_NAME, WiringConstants.FRONT_CONTACT_PORT);
	@SuppressWarnings("unused")
	private SonarSensor _FrontSonar = new SonarSensor(SensorNames.FRONT_SONAR_NAME, WiringConstants.FRONT_SONAR_SENSOR_PORT);
	
	public static final double ARM_MIN_ANGLE = 5.0;	// should be private
	public static final double ARM_MAX_ANGLE = 85.0;
	private PivotArm _TheArm = new PivotArm(SensorNames.PIVOT_ARM_NAME, WiringConstants.ARM_SENSOR_PORT, WiringConstants.ARM_MOTOR_PORT);
	
	private GhostController _TheGhost = new GhostController(_MasterTimer);
	private HumanController _TheHuman = new HumanController();



	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		_TheGhost.InitialSetup();
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		_MasterTimer.reset();
		_MasterTimer.start();
		
		_TheChassis.autoInit();
		_TheArm.autoInit();
		_TheGhost.Initialize();
	}

	/**
	 * This function is called periodically during autonomous.
     * Called once per tick of loopFunc() in IterativeRobotBase class (main running loop)
	 */
	@Override
	public void autonomousPeriodic() {
		SensorList.ReadSensors();
		
		_TheGhost.EvaluateDecisions();
		
		_TheChassis.doAuto();
		_TheArm.doAuto();
		
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
		_TheChassis.teleopInit();
		_TheArm.teleopInit();
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		SensorList.ReadSensors();
		
		_TheHuman.EvaluateDecisions();
		
		_TheChassis.doTeleop();
		_TheArm.doTeleop();
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
