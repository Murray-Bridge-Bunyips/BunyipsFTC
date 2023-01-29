/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.firstinspires.ftc.teamcode.lisa_independent

//import com.qualcomm.robotcore.eventloop.opmode.Disabled
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp
//import com.qualcomm.robotcore.hardware.DcMotor
//import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
//import com.qualcomm.robotcore.util.ElapsedTime
//import com.qualcomm.robotcore.util.Range

// This Class is outdated since the migration from Java to Kotlin.
// Therefore, this is no longer up to date and should not be used.

// This line indicates that everything below is under the TeamCode directory, the purpose is
// similar to a file name; it helps with organisation of code. These lines are usually auto-filled.
// These import statements are used to import special 'libraries' which will allow certain things
// to work in our code. Libraries are basically links to other peoples code which has even more
// code to configure the devices on our robot. It's a great thing, because we can be lazy and
// not have to configure all the stuff on our robots from the ground up.
/*
###################################################################################################

Welcome! This is the reference document for robot operations regarding the OpModes that are created
straight from Java! If you're here, you already should know a thing or two about Blocks
programming. If you haven't, I would recommend you start off with Blocks or at the bare minimum
have knowledge of blocks-based coding (i.e. Scratch) and have knowledge on concepts such as
variables, functions, and loops.

This explained reference sheet for coding FTC robots is to support the knowledge of Java programming
and explain what all the code means, so anyone can level up their skills straight from the source.

This reference code will cover all aspects of Java needed for FTC robot coding that is not already
iterated in Blocks.

This document is maintained and written by Lucas Bubner - Coder of FTC Team 15215, Murray Bridge Bunyips.
Created 25/05/22

The most simple concept to start in Java is the comment. Single line comments are represented with //
and multi-line comments are represented with an asterisk and slash (just like this comment)
The code will not read comments, so you can say whatever you want in them. Comments are a great
practise in coding and every single webpage and program you use will utilise comments in some way.
You might also be familiar with the comment block from Blocks programming, this is exactly that.

The next and most critical concept to Java is the curly-brace syntax. Remember how Blocks programming
surrounded blocks inside of blocks? The curly braces are a way of doing this, but in a more
code-like way. (they don't look as good either)
For example, the RunOpMode function in Blocks would look like this in Java.


public void runOpMode() {
}


The curly braces (the { and }) are just a way of telling the computer that these are the wrap-arounds
that Blocks does. Don't get confused either! You can have multiple open at the same time! You can
format curly braces either on one line or separated between lines, for example,


if (x == 1) {
do something
}


is the same as,


if (x == 1) { do something }


There are plenty more concepts to learn in Java, and this is only the extreme basics,
but for understanding of more Java based syntax, I'd
strongly suggest going to w3schools or another website and learning more advanced theories.

Every line of code written that is not a method or class statement must be ended with a semicolon (;),
or you'll have problems. Fortunately technology is sometimes useful and will pick up if you've
missed one out.

Read through the code, and refer back to it when you make your own!
Remember, Google is your best friend. It is not a shameful practice to search everything up
on the internet (trust me, everyone does it).

###################################################################################################
*/
//@TeleOp(name = "Reference - Linear OpMode") // This line is required at the top of every OpMode. Replace @TeleOp with @Autonomous to change the
//// code type to Autonomous. This is where you put the name of the OpMode between the quotes
//// in (name="").
//@Disabled // Comment out the @Disabled text to make your OpMode visible in the Driver Station app. This
//// reference sheet is currently disabled as it is a reference code! When you make one yourself,
//// you'll need to comment this out with > //
//
//class AllReferenceLinearTeleOp : LinearOpMode() {
//    // Java is a language based upon 'classes'. What this means is that everything ran in Java
//    // is under a sort of template that allows you to write code. It is not important to know
//    // the specifics of classes, just that the code 'public class' means that you are declaring
//    // to the computer that you are going to open a new public class.
//    // Learning public private and protected attributes is not super important, but all you
//    // need to know for the moment if you're just starting out that in the context of classes -
//    // public = allows for the code inside this class to be referenced and ran by other classes
//    // The file name should be after public class, if not already done for you.
//    // 'extends LinearOpMode' allows for this code to inherit properties of a library. This
//    // is not important to understand, just know it is required to get us in the works by extending
//    // the already written code for the LinearOpMode into our class to let us code our robot.
//    // In this section of the code, we place variable declarations, and other library declarations.
//    private val runtime = ElapsedTime()
//    private var leftDrive: DcMotor? = null
//    private var rightDrive: DcMotor? = null
//
//    // These three lines assign a few things, which will require a better understanding of
//    // Java in order to fully understand. In a nutshell, the top line of these three lines is interpreted as:
//    // private = Declares that the variable we're going to make cannot be accessed outside of this class (in this case, the class we just made)
//    // ElapsedTime = Remember the classes we imported at the very start of the code? (Go look, they're at the top)
//    // ^ We are going to use the ElapsedTime utility from the robotcore library to define our variable (these are alike the light green blocks from Blocks under
//    // ^ the Utilities section)
//    // runtime = Declares the reference name, in this case, it's 'runtime'. This is just like a variable in Blocks but for device allocation.
//    // new ElapsedTime() = Creates a new object. Object oriented programming is the vital aspect of
//    // Java coding. The coding done in Blocks is technically Java programming, just except it
//    // was made simple with the use of blocks. I would HIGHLY recommend you to do advanced research
//    // into objects and classes, as it is too much to explain here. For here, all we need to know
//    // is that the code will create a new object called ElapsedTime from the ElapsedTime library and
//    // allow us to view how much time has passed.
//    // null = States to the computer that this variable should not be defined yet.
//    // These are the hardest parts of going from Blocks to Java, so don't get stressed out
//    // if this seems like a lot!
//    // The @Override attribute tells the computer that the method (function) we are about to declare
//    // is meant to override the superclasses elements.
//    // You will need to research more into OOP (object-oriented programming) in order to fully
//    // understand this. In essence, it tells the computer that this is a completely fresh
//    // and new function, independent from any higher classes.
//    // For FTC Java coding, knowing about superclasses and OOP isn't necessary
//    override fun runOpMode() {
//        // public void runOpMode() {} declares to the computer that we are opening a new function.
//        // It is better called a method, but for the sake of simplicity and comparison to Blocks,
//        // I will refer to them as a function, as they are the same thing.
//        // runOpMode is better known as the purple function that you start off with. This
//        // is the same thing.
//
//        // This code is important and is required for initialisation. The hardwareMap object
//        // is used to tell the computer what the motors are actually called and how to refer
//        // to them when required. The variable in deviceName must match what the robot calls it,
//        // just like Blocks. If this isn't followed, you'll get an error.
//        // Once you do this, you must refer to the motor in the code with the variable on the left which was declared previously.
//        leftDrive = hardwareMap.get(DcMotor::class.java, "Left Motor")
//        rightDrive = hardwareMap.get(DcMotor::class.java, "Right Motor")
//        telemetry.addData("Key", "Text")
//        // Finally! Some code that can be cross-transferred from Blocks. This snippet of code
//        // simply acts the same way the Telemetry Add Data block behaves, just except we are
//        // typing it instead of dragging it.
//        // The syntax is displayed above and must be done like the example.
//        telemetry.update()
//        // Updates the telemetry to the Driver Station. Any values that a specific Call block had
//        // is now represented between the brackets ().
//
//        // States which way the motors will spin. This is exactly like the Set Direction block.
//        // The syntax for these commands can be auto-filled for you with the help of Android Studio
//        // so don't panic if this looks hard to remember! You won't have to!
//        leftDrive?.direction = Direction.FORWARD
//        rightDrive?.direction = Direction.REVERSE
//
//        // Wait for the driver to press start (exactly the same as the call.name waitForStart block)
//        waitForStart()
//        // Resets the ElapsedTimer object we made earlier back to zero.
//        runtime.reset()
//
//        // It is time to start the code that lets us do things! This is the most simple part but
//        // requires the knowledge Java syntax.
//        while (opModeIsActive()) {
//            // We are using the while loop, just like Blocks' while loop. It will repeat whatever is
//            // between these semicolons while in this case our OpMode is active.
//
//            // In Java, we use primitive data types to declare what *type* of variable we are going
//            // to declare. There are eight of them and each do their own thing. For this example,
//            // we will use 'double', which is the same as saying "i am going to declare a 'double'
//            // type variable called...". These two lines below declares two variables (just like blocks),
//            // called leftPower, and rightPower. These will be used for the motor speed. I'd advise
//            // learning about the other variable types, such as boolean, String, int, char, and the others.
//            var leftPower: Double
//            var rightPower: Double
//
//            // Here we declare two more variables, called drive and turn. The purpose of drive and turn
//            // is to convert our gamepad movements into something the computer can deal with.
//            // Yes, we can just directly use the gamepad.stick inputs to send to the motor, but
//            // for this example, we will use variables to make it easier to read.
//            val drive = -gamepad1.left_stick_y.toDouble() // Grab negative value of left stick Y
//            val turn = gamepad1.right_stick_x.toDouble() // Grav value of right stick X
//            // All math operations and logic are done with the symbols +, -, *, /, <, >, ==, !, and many others.
//            leftPower = Range.clip(drive + turn, -1.0, 1.0)
//            rightPower = Range.clip(drive - turn, -1.0, 1.0)
//            // Here we declare what the variables leftPower and rightPower will be set to.
//            // The Range.clip utility allows for the code to format and limit the values received
//            // by our previous variables we just set, drive and turn.
//            // We first tell the leftPower variable to be the drive variable plus the turn variable,
//            // allowing a maximum value of 1 and minimum value of -1. This keeps our inputs clean.
//            // The same is done for the rightPower, but it subtracts the drive from the turn variables.
//
//            // Now, we send the variables off to the motors, which will set the power accordingly.
//            // It is important we use Range.clip, so we don't accidentally send commands that
//            // our motors will have difficulty reading.
//            leftDrive?.power = leftPower
//            rightDrive?.power = rightPower
//
//            // We can format these into one line for simplicity with:
//            // leftDrive.setPower(Range.clip(-gamepad1.left_stick_y + gamepad1.right_stick_x, -1.0, 1.0);
//            // rightDrive.setPower(Range.clip(-gamepad1.left_stick_y - gamepad1.right_stick_x, -1.0, 1.0);
//            // However, I do not recommend this especially for bigger operations as
//            // it can get extremely hard to read this code. Using variables like this demo
//            // is much easier to understand and won't impact performance to any significant amount.
//            // Coding is not just about if it works, it is about the optimisations to readability
//            // that makes some code more superior to others.
//
//            // Now, we send off data to the Driver Station about our ElapsedTime runtime, and the motor power.
//            // It might look a bit complex, but all this code does is
//            telemetry.addData("Status", "Run Time: $runtime")
//            // Retrieve the runtime variable and convert it to a String (readable text), then send it to telemetry
//            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower)
//            // Retrieve the left and right motor power variables and send them to telemetry
//            // %.2f prints the motor power as a floating point type, which in essence only allows two digits after the decimal point to actually be printed. (similar to rounding to the hundredth)
//            telemetry.update()
//            // Send it to the Driver Station
//
//            // That's it! The code will see that the while loop is still on during the code execution,
//            // and return back to the other curly bracket up the code, which will repeat everything again.
//        }
//    }
//}