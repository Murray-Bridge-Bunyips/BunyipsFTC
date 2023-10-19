## TeamCode Module

This is where all the Bunyips (FTC Team 15215) and Mulyawonks  (FTC Team 22407) robot development
code will reside!  
This repository contains all code used for FTC robots constructed >2021 using a mix of
Kotlin (>29/01/23) & Java

## Naming convention

All OpModes use should include the robot name in their Driver Station name
declaration to avoid confusion.  
OpModes are organised based on their package name and should only be run on their respective robot.

'_independent' packages do not rely on custom common classes and objects and can be run solely on
the
FIRST SDKs resources. These classes are now only used for learning purposes and
proper OpModes should derive from Bunyips common classes.

All non-active archived code is stored in [archived/](../../../../../archived/) and is not built.
