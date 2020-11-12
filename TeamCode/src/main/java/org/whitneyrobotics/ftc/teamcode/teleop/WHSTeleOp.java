package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

public class WHSTeleOp extends OpMode {
     WHSRobotImpl robot;

    @Override
    public void init()  {
        robot = new WHSRobotImpl(hardwareMap);
    }
eh
    @Override
    public void loop(){
    //Intake
        if (gamepad1.right_trigger >0.01) {
            robot.intake.operate(gamepad1.right_trigger > 0.01, gamepad1.left_trigger == 0.0);
        }
        else if (gamepad1.left_trigger >0.01) {
            robot.intake.operate(gamepad1.right_trigger > 0.01, gamepad1.left_trigger > 0.01);
        }
        else {
            robot.intake.operate(gamepad1.right_trigger == 0, gamepad1.left_trigger == 0);
        }

        //Canister
        if (gamepad2.x) {
            robot.canister.operateCanister(gamepad2.x);
        }
        else {
            robot.canister.operateCanister(false);
        }
        //Drivetrain
        robot.estimateHeading();
        robot.drivetrain.switchFieldCentric(gamepad1.b);
        if (gamepad1.left_bumper){
            robot.drivetrain.operateMecanumDrive(gamepad1.left_stick_x/2.54, gamepad1.left_stick_y/2.54, gamepad1.right_stick_x/2.54, robot.getCoordinate().getHeading());
        }else{
            robot.drivetrain.operateMecanumDriveScaled(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, robot.getCoordinate().getHeading());
        }
        //Outtake
        //if (gamepad2.)
    }
}
