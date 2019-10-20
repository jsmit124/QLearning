package edu.westga.cs3270.cliffwalking.model;

import java.text.NumberFormat;

public final class Format {

	/**
	 * Format a percent. Using 6 decimal places.
	 * 
	 * @param e
	 *            The percent to format.
	 * @return The formatted percent.
	 */
	public static String formatPercent(final double e) {
		if( Double.isNaN(e) || Double.isInfinite(e) ) 
			return "NaN";
		final NumberFormat f = NumberFormat.getPercentInstance();
		f.setMinimumFractionDigits(6);
		return f.format(e);
	}
	
}
