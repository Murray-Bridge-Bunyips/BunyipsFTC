package org.firstinspires.ftc.teamcode.lisa.tasks

import org.firstinspires.ftc.teamcode.common.BunyipsOpMode
import org.firstinspires.ftc.teamcode.common.tasks.Task
import org.firstinspires.ftc.teamcode.common.tasks.TaskImpl
import org.firstinspires.ftc.teamcode.lisa.components.LisaDrive

class LisaEncoderDriveTask(
    opMode: BunyipsOpMode,
    time: Double,
    private val drive: LisaDrive,
    leftCM: Double,
    rightCM: Double,
    leftPower: Double,
    rightPower: Double
) : Task(opMode, time), TaskImpl {
    private val leftDistance: Double
    private val rightDistance: Double
    private val leftPower: Double
    private val rightPower: Double

    init {

        // UltraPlanetary HD Hex Motor specification

        // TODO: Verify these formulas as they don't work as intended
        val TICKS_PER_REVOLUTION = 28.0
        val WHEEL_DIAMETER_CM = 8.5
        leftDistance = leftCM * 10 * TICKS_PER_REVOLUTION / (WHEEL_DIAMETER_CM / 10 * Math.PI)
        rightDistance = rightCM * 10 * TICKS_PER_REVOLUTION / (WHEEL_DIAMETER_CM / 10 * Math.PI)
        this.leftPower = leftPower
        this.rightPower = rightPower
    }

    override fun init() {
        super.init()
        drive.setEncoder(true)
        drive.setTargetPosition(leftDistance, rightDistance)
        drive.setPower(leftPower, rightPower)
    }

    override fun isFinished(): Boolean {
        return super.isFinished() || drive.targetPositionReached()
    }

    override fun run() {
        if (isFinished()) {
            drive.setPower(0.0, 0.0)
            drive.update()
            drive.setEncoder(false)
            return
        }
        drive.update()
    }
}