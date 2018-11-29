# 2018 Team 4585 Offseason Code
Team 4585's code for the 2018 Offseason event at AMES in Cottonwood High School.

This README explains setup instructions as well as the function for each class file in the code.

## Setup Instructions


### General

- Clone the repository
- Run `./gradlew` to download gradle and required libraries
- Run `./gradlew tasks` to see available options

### Eclipse

- Run `./gradlew eclipse`
- Select the `2018PracticeBot` folder as import source in "File > Open Projects"

### IntelliJ

- Run `./gradlew idea`
- Open `2018PracticeBot.ipr` file with IntelliJ

### Building/Deploying to the Robot

- Run `./gradlew build` to build the code. Use the `--info` flag for more details
- Run `./gradlew deploy` when connected to Driver Station to deploy to the robot using Powershell



## Package Functions

### AutoTask.java

- This is an abstract class that all the AutoTask modules inherit from. This will allow them to all be members of the task sequence lists.
- All of the AutoTaskXXX classes are implementations for various simple defined tasks. These all inherit from AutoTask and can be members of a task sequence list.

### AutoTask Packages include:

- AutoTaskApproach.java
- AutoTaskArmRotate.java
- AutoTaskFireTrigger.java
- AutoTaskRevTrackTo.java
- AutoTaskRotateTo.java
- AutoTaskTrackToward.java
- AutoTaskWaitForTime.java
- AutoTaskWaitForTrigger.java

### Chassis.java

- This is the class to control the hardware, motors, and sensors for the chassis to move the robot. It has sensors to report current heading, location, and velocity
    
### ChassisControl.java

- This is the class to make decisions about controlling the chassis. It has PID filters for the speed and turning.
    
### CompassHeading.java
    
- This is a class to serve for controlling directions. It will contain a value from 0 to 359. Multiple instances can be computed even over the wrap-around boundary.
    
### ContactSensor.java
    
- This is a base class for the different types of contact sensors we may implement. It can also be used as a contact sensor by itself.
    
### DistanceSenor.java

- A base class for different types of distance sensors. It is an abstract class, so it cannot be instantiated by itself.
    
### GhostController.java

- This is the main operator of the autonomous code. It will coordinate all the values from various sensors and check what the current desired tasks are. It will then decide instructions to send to the various subsystems.
    
### HeadingPID.java
  
- This is code to run a PID evice that can rotate. It inherits from the regular PID class, but it takes into account moving over the bounary that wraps from 0 to 360.
    
### HumanController.java

- This is the main operator for the teleop code. It reads infromation from the joystick(s) and then sends instructions to the different subsystems on the robot.
    
### HuskyJoystick.java

- This reads the joysticks and may do some filtering on some of the values before they are passed on.
    
### HuskySubsystem.java

- This is an interface that the various subsystems on the robot will implement.
    
### HuskyVector2D.java

- This is a class that extends the basic PID class to work in 2 dimensions.
    
### PID.java
    
- This is an implementation of a PID filter that will allow us to speed up and slow down behaviours in a controlled way.
    
### PivotArm.java
    
- This is similar to `Chassis.java`. It is the hardware control for the arm that was implemented on the T-Shirt cannon robot.
    
### Robot.java

- This is the main class. It creates the major subsystems that the robot currently uses. It has the calls for the autonomous and teleop loops and distributes the calls to the subsystems.
    
### RobotSensor.java

- This is an abstract class that all of the sensors on the robot should inherit from.
    
### SensorList.java

- This is a class that will contain a list of pointers to all the sensors that we have instantiated. It will allow us to do common behaviour and monitor them all as a group.
    
### SensorNames.java

- This is a static class that contains the names for all of the sensors. Having them all here ensures that they are all unique and that they are not lost.
    
### SonarSensor.java

- One type of distance measuring sensor. This inherits from `DistanceSensor.java`
    
### TaskSequence.java

- This contains a list of `AutoTaskXXX`. This class is used to step through each successive task until they are completed.
    
### TaskSequenceArm.java

- A specialized task list for tasks associated with the pivot arm.
    
### TaskSequenceChassis.java

- A specialized task list for tasks associated with the chassis.
    
### TaskSequenceGroup.java

- This is a class that can contain multiple tasks sequences, allowing coordination between different task lists.
    
### TriggerTask.java

- A type of task that doesn't do anything, but sets values that can trigger other task lists to continue.
    
### WiringConstants.java

- A consolidated list of all the wiring ports. This will allow us to easily change ports if needed and will help prevent conflicts in the code.
    
