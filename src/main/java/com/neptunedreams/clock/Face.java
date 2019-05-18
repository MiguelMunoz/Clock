package com.neptunedreams.clock;

import java.awt.BorderLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 5/17/19
 * <p>Time: 8:21 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings("WeakerAccess")
public class Face {
	private static final int GAP = 8;
	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
	private static AtomicBoolean launched = new AtomicBoolean(false);

	private JPanel mainPanel = new JPanel(new BorderLayout());

	static {
		doLaunch();
	}
	
	@SuppressWarnings("WeakerAccess")
	public static void doLaunch() {
		//noinspection HardCodedStringLiteral
		if (!launched.get()) {
			launched.set(true);
			JFrame frame = new JFrame("Graal Clock");
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setLocationByPlatform(true);
			frame.add(new Face().getMainPanel());
			frame.pack();
			frame.setVisible(true);
		}
	}

	private final JLabel timeLabel = new JLabel(getTime());

	Face() {
		mainPanel.setBorder(BorderFactory.createMatteBorder(GAP, GAP, GAP, GAP, mainPanel.getBackground()));
		mainPanel.add(BorderLayout.CENTER, timeLabel);
		JButton timeButton = new JButton("getTime()"); //NON-NLS
		mainPanel.add(BorderLayout.PAGE_END, timeButton);
		timeButton.addActionListener(e -> doGetTime());
	}

	JPanel getMainPanel() { return mainPanel; }

	private void doGetTime() {
		timeLabel.setText(getTime());
	}

	private String getTime() {
		LocalDateTime now = LocalDateTime.now();
		return FORMAT.format(now);
	}

}
