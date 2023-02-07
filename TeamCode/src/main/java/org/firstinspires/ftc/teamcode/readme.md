## TeamCode Module

This is where all the Bunyips (FTC Team 15215) robot development code will reside!  
This repository contains all code used for FTC robots constructed >2021 using Kotlin (Migration from Java 29/01/23)

## Class prefix naming convention

All OpModes use '<robot name(+ -I if independent class)> OpMode name' in their Driver Station name
declaration to avoid confusion.  
Classes are organised based on their package name and should only be run on their respective robot.

'_independent' packages do not rely on custom common classes and objects and can be run without the
need of external classes. These classes are only used for testing purposes now and proper OpModes
should use the DRY principle common class objects.