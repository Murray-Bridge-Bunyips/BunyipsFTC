## TeamCode Module

This package contains all maintained code for FTC robots constructed after 2021 using Java and
Kotlin (>29/01/23).

## Conventions

- All OpModes should include the robot name in their Driver Station name, group, and file prefix
  to avoid confusion.
- OpModes should derive from
  the [BunyipsOpMode ecosystem](https://github.com/Murray-Bridge-Bunyips/BunyipsFTC/tree/stable/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/example),
  by extending
  LinearOpMode wrappers such as BunyipsOpMode, AutonomousBunyipsOpMode,
  RoadRunnerAutonomousBunyipsOpMode etc and integrating appropriate developer tools (RobotConfig,
  BunyipsComponent, Task etc..)
- Robots should be organised based on `teleop`, `autonomous`, `debug`, `components`, and `tasks`
  packages.<br><br>
  See the [example/](example/) directory and robots for examples of applying the full suite of
  BunyipsOpMode developer abstractions.

## File structure

OpModes are organised based on their package name and should only be run on their respective robot.

All non-active archived code is stored in [archived/](../../../../../../../../archived/) and is not
built.  
This code is not maintained and may be using deprecated or broken functionality.
