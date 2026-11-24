package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;

import java.util.ArrayList;

//Live mapping robot sees, can be used for machine learning & auto turning during autonomous
@Configurable
public class Machine {
    public static double startFieldX = 0; //🚨🚨🚨 Need measurement
    public static double startFieldY = 0; //🚨🚨🚨 Need measurement
    public static double robotRadius = 7; //🚨🚨🚨 Need measurement
    public static double trailSpace = 2; //add a trail dot
    public static int maxTrail = 500; //dots cap

    private Odometry odometry;
    private FieldManager field;

    private ArrayList<Double> trailX = new ArrayList<>();
    private ArrayList<Double> trailY = new ArrayList<>();
}
