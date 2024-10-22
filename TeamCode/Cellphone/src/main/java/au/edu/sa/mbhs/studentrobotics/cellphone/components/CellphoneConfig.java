package au.edu.sa.mbhs.studentrobotics.cellphone.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import au.edu.sa.mbhs.studentrobotics.bunyipslib.BunyipsOpMode;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.RobotConfig;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.external.Mathf;
import au.edu.sa.mbhs.studentrobotics.bunyipslib.hardware.Motor;

/**
 * Cellphone
 */
public class CellphoneConfig extends RobotConfig {
    /**
     * Back camera
     */
    public CameraName cameraB;
    /**
     * Front camera
     */
    public CameraName cameraF;
    /**
     * Dummy motor, not a real device
     */
    public Motor dummy;

    @Override
    protected void onRuntime() {
        cameraB = ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.BACK);
        cameraF = ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(BuiltinCameraDirection.FRONT);

        DummyMotor m = new DummyMotor();
        dummy = new Motor(new DcMotorImpl(m, -1));

        BunyipsOpMode.ifRunning(o -> o.onActiveLoop(m::update));
    }

    @SuppressWarnings({"all", "deprecation"})
    private static class DummyMotor implements DcMotorControllerEx {
        private final ElapsedTime timer = new ElapsedTime();
        private final int TPS = 600;
        private final int ACCEL = 1200;
        private double power;
        private double pos;
        private int target;
        private MotorConfigurationType conf = MotorConfigurationType.getUnspecifiedMotorType();
        private DcMotor.RunMode mode;
        private double velo;

        public void update() {
            // update this dummy motor by feeding power into pos, delta time approach
            velo = Mathf.moveTowards(velo, power * TPS, ACCEL * timer.seconds());
            pos += velo * timer.seconds();
            timer.reset();
        }

        @Override
        public void setMotorType(int motor, MotorConfigurationType motorType) {
            conf = motorType;
        }

        @Override
        public MotorConfigurationType getMotorType(int motor) {
            return conf;
        }

        @Override
        public void setMotorMode(int motor, DcMotor.RunMode mode) {
            this.mode = mode;
        }

        @Override
        public DcMotor.RunMode getMotorMode(int motor) {
            return mode;
        }

        @Override
        public void setMotorPower(int motor, double power) {
            this.power = power;
        }

        @Override
        public double getMotorPower(int motor) {
            return power;
        }

        @Override
        public boolean isBusy(int motor) {
            return false;
        }

        @Override
        public void setMotorZeroPowerBehavior(int motor, DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        }

        @Override
        public DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int motor) {
            return DcMotor.ZeroPowerBehavior.BRAKE;
        }

        @Override
        public boolean getMotorPowerFloat(int motor) {
            return false;
        }

        @Override
        public void setMotorTargetPosition(int motor, int position) {
            target = position;
        }

        @Override
        public int getMotorTargetPosition(int motor) {
            return target;
        }

        @Override
        public int getMotorCurrentPosition(int motor) {
            return (int) pos;
        }

        @Override
        public void resetDeviceConfigurationForOpMode(int motor) {

        }

        @Override
        public Manufacturer getManufacturer() {
            return null;
        }

        @Override
        public String getDeviceName() {
            return "Dummy";
        }

        @Override
        public String getConnectionInfo() {
            return "";
        }

        @Override
        public int getVersion() {
            return 0;
        }

        @Override
        public void resetDeviceConfigurationForOpMode() {

        }

        @Override
        public void close() {

        }

        @Override
        public void setMotorEnable(int motor) {

        }

        @Override
        public void setMotorDisable(int motor) {

        }

        @Override
        public boolean isMotorEnabled(int motor) {
            return true;
        }

        @Override
        public void setMotorVelocity(int motor, double ticksPerSecond) {
            pos += ticksPerSecond * timer.seconds();
            timer.reset();
        }

        @Override
        public void setMotorVelocity(int motor, double angularRate, AngleUnit unit) {

        }

        @Override
        public double getMotorVelocity(int motor) {
            return velo;
        }

        @Override
        public double getMotorVelocity(int motor, AngleUnit unit) {
            return velo;
        }

        @Override
        public void setPIDCoefficients(int motor, DcMotor.RunMode mode, PIDCoefficients pidCoefficients) {

        }

        @Override
        public void setPIDFCoefficients(int motor, DcMotor.RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {

        }

        @Override
        public PIDCoefficients getPIDCoefficients(int motor, DcMotor.RunMode mode) {
            return null;
        }

        @Override
        public PIDFCoefficients getPIDFCoefficients(int motor, DcMotor.RunMode mode) {
            return null;
        }

        @Override
        public void setMotorTargetPosition(int motor, int position, int tolerance) {
            target = position;
        }

        @Override
        public double getMotorCurrent(int motor, CurrentUnit unit) {
            return 0;
        }

        @Override
        public double getMotorCurrentAlert(int motor, CurrentUnit unit) {
            return 0;
        }

        @Override
        public void setMotorCurrentAlert(int motor, double current, CurrentUnit unit) {

        }

        @Override
        public boolean isMotorOverCurrent(int motor) {
            return false;
        }
    }
}
