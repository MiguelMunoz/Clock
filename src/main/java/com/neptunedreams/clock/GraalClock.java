package com.neptunedreams.clock;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 5/17/19
 * <p>Time: 7:37 PM
 *
 * @author Miguel Mu\u00f1oz
 */
public final class GraalClock  {

	public static void main(String[] args) {
		// Commented out to (vainly) prevent the java.awt.Component class from getting initialized at build time. 
		// Removing this line means the application doesn't work. (I was hoping that, by specifying delayed loading,
		// the class would load anyay, which would fire up the main Window.

//		Face.doLaunch();
	}
	
	private GraalClock() {
	}
}
