package org.usfirst.frc.team3316.robot.utils;

import edu.wpi.first.wpilibj.AnalogInput;

public class Utils {
	public static double scale(double x, Point a, Point b) {
		return ((x - a.x) * (a.y - b.y) / (a.x - b.x) + a.y);
	}

	public static boolean isOnTarget(double currentValue, double setpoint, double tolerance) {
		return (Math.abs(currentValue - setpoint) <= tolerance);
	}

	/**
	 * Converts a analog sensor output to digital values.
	 * 
	 * @param value
	 *            The current read from the sensor.
	 * @param thresh
	 *            The point that separates between TRUE and FALSE.
	 * @return The converted value.
	 */
	public static boolean AnalogToDigitalInput(double value, double thresh) {
		return value < thresh; // WAS value > thresh
	}

	/**
	 * Converts a analog sensor output to digital values.
	 * 
	 * @param analogInput
	 *            The current sensor to read from.
	 * @param thresh
	 *            The point that separates between TRUE and FALSE.
	 * @return The converted value.
	 */
	public static boolean AnalogToDigitalInput(AnalogInput analogInput, double thresh) {
		return AnalogToDigitalInput(analogInput.getValue(), thresh);
	}

	/*
	 * returns a linear interpolation from a lookup table assuming x=0 is for x
	 * values and x=1 is for y values if the requiredIndex is lower than the min x
	 * value or higher than the max x value, returns the minimum or maximum value
	 * accordingly
	 */
	public static double valueInterpolation(double requiredIndex, double table[][]) {
		if (requiredIndex < table[0][0]) {
			return table[1][0];
		}
		if (requiredIndex > table[0][table[0].length - 1]) {
			return table[1][table[1].length - 1];
		}

		// binary search to find the appropriate indexes
		int bot = 0, top = table[0].length - 1;

		int mid = (bot + top) / 2;
		while (mid != bot) {
			if (table[0][mid] > requiredIndex) {
				top = mid;
				mid = (bot + top) / 2;
			} else {
				bot = mid;
				mid = (bot + top) / 2;
			}
		}

		// linear interpolation between the points in the lookup table
		double valueToReturn = scale(requiredIndex, new Point(table[0][bot], table[1][bot]),
				new Point(table[0][top], table[1][top]));
		return valueToReturn;
	}

	/**
	 * Returns all the values which are bigger then the lowestValue
	 * 
	 * @param value
	 *            - (double) the given value to be filtered
	 * @param lowestValue
	 *            - (double) the absolute lowest value to be
	 * @param defaultValue
	 * @return
	 */
	public static double lowPassFilter(double value, double lowestValue, double defaultValue) {
		if (Math.abs(value) >= Math.abs(lowestValue)) {
			return value;
		} else {
			return defaultValue;
		}
	}

	public static double convertFootToMeter(double ft) {
		return ft * 0.3048000;
	}

	public static double convertMeterToFoot(double m) {
		return m / 0.3048000;
	}

	/**
	 * Fixes the robot's left swerve by scaling the applied voltage.
	 * 
	 * @param v
	 *            The applied voltage
	 * @param r
	 *            The ratio between each side's angle
	 * @return The scaled voltage
	 */
	public static double calculateLeftVoltage(double v, double r) {
		if (v > 0) { // Driving forward
			if (r > 0) { // Swerving right
				return v * (-r + 1);
			} else { // Swerving left
				return v;
			}
		} else { // Driving back
			if (r < 0) { // Swerving right
				return v * (r + 1);
			} else { // Swerving left
				return v;
			}
		}
	}

	/**
	 * Fixes the robot's right swerve by scaling the applied voltage.
	 * 
	 * @param v
	 *            The applied voltage
	 * @param r
	 *            The ratio between each side's angle
	 * @return The scaled voltage
	 */
	public static double calculateRightVoltage(double v, double r) {
		if (v > 0) { // Driving forward
			if (r < 0) { // Swerving left
				return v * (r + 1);
			} else { // Swerving right
				return v;
			}
		} else { // Driving back
			if (r > 0) { // Swerving left
				return v * (-r + 1);
			} else { // Swerving right
				return v;
			}
		}
	}

	/**
	 * Checks whether a real number x is in an epsilon neighborhood of another real number L.
	 */
	public static boolean isInNeighborhood(double x, double L, double epsilon) {
		return L - epsilon <= x && x <= L + epsilon;
	}
}
