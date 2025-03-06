# Common

This is the common package with code that is shared between all robots. This differs from
BunyipsLib which is shared between theoretically all robots, and is a collection of classes and
objects that are used to make writing OpModes easier.

This package is a collection of classes to be used internally between our own robots,
and is not intended to be used by other teams.

# Making a new robot

In order to make a new robot, you'll need to make a new Gradle configuration for it, so you can
build and deploy robot specific code without having to group OpModes on different incompatible
robots.
By default, BunyipsLib will always be included with your builds, using the
build.common.gradle file in the root of the TeamCode directory.

You can make your own robot by copying the `../Template` directory and doing the following:

1. Renaming the copied directory name to your robot's name
2. Uncommenting the contents of build.gradle
5. Adding your robot to /settings.gradle with your directory name
6. Removing the .gitkeep file and populating the folder with your own code
7. Consider adding your robot as an Inspection Scope for UserCode if applicable

Ensure to run a Gradle sync (Ctrl + Shift + O) after making these changes.
