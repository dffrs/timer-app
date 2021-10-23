package com.dffrs.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.dffrs.comp.time.Time;
import com.dffrs.comp.time.modifier.TimeModifier;
import com.dffrs.util.writer.JSONWriter;

import java.awt.Font;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

/**
 * TimerApp' GUI.
 * 
 * @author dffrs-iscteiupt.
 *
 */
public class GUI {

	private JFrame frmTimerapp;
	private JTextField queryTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmTimerapp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Main Frame
		frmTimerapp = new JFrame();
		frmTimerapp.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/TimerApp Icon.png"));
		frmTimerapp.setBackground(new Color(192, 192, 192));
		frmTimerapp.setTitle("TimerApp");
		frmTimerapp.setBounds(100, 100, 401, 177);
		frmTimerapp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTimerapp.setResizable(false);
		frmTimerapp.setLocation(((Toolkit.getDefaultToolkit().getScreenSize().width - frmTimerapp.getSize().width) / 2),
				((Toolkit.getDefaultToolkit().getScreenSize().height - frmTimerapp.getSize().height)/ 2));
		
		// Components
		JPanel mainPanel = new JPanel();
		JPanel labelMainPanel = new JPanel();
		JLabel timeLabel = new JLabel("00:00:00");
		JPanel buttonMainPanel = new JPanel();
		
		// Time here
		Time time = new Time(0);
		frmTimerapp.getContentPane().setLayout(new CardLayout(0, 0));
		// Time Modifier
		TimeModifier thread = new TimeModifier(time, timeLabel);
		frmTimerapp.getContentPane().add(mainPanel, "mainPanel");
		mainPanel.setLayout(new GridLayout(0, 1, 0, 0));
		

		mainPanel.add(labelMainPanel);
		labelMainPanel.setLayout(new BorderLayout(0, 0));

		timeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 32));
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		labelMainPanel.add(timeLabel, BorderLayout.CENTER);
		
		mainPanel.add(buttonMainPanel);
		buttonMainPanel.setLayout(new CardLayout(0, 0));
		
		JPanel panelStart = new JPanel();
		buttonMainPanel.add(panelStart, "panelStart");
		JLabel btnStart = new JLabel("");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				changePanel(buttonMainPanel, "panelPause");
				
				thread.start();
				
			}
		});
		panelStart.setLayout(new BorderLayout(0, 0));
		btnStart.setIcon(new ImageIcon("resources/play-button.png"));
		btnStart.setHorizontalAlignment(SwingConstants.CENTER);
		btnStart.setEnabled(false);
		panelStart.add(btnStart);
		
		JPanel panelPause = new JPanel();
		buttonMainPanel.add(panelPause, "panelPause");

		JLabel btnPause = new JLabel("");
		btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changePanel(buttonMainPanel, "panelContinueStop");
				
				// Thread Pausing
				thread.setPaused(true);
				
			}
		});
		panelPause.setLayout(new BorderLayout(0, 0));
		btnPause.setIcon(new ImageIcon("resources/pause.png"));
		btnPause.setHorizontalAlignment(SwingConstants.CENTER);
		btnPause.setEnabled(false);
		panelPause.add(btnPause);
		
		JPanel panelContinueStop = new JPanel();
		buttonMainPanel.add(panelContinueStop, "panelContinueStop");
		JLabel btnStop = new JLabel("");
		btnStop.setHorizontalAlignment(SwingConstants.CENTER);
		btnStop.setIcon(new ImageIcon("resources/stop.png"));
		btnStop.setEnabled(false);
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				changePanel((JPanel) frmTimerapp.getContentPane(), "savePanel");
				
				// Thread interruption
				thread.interrupt();
			}
		});
		panelContinueStop.setLayout(new GridLayout(0, 2, 0, 0));
		panelContinueStop.add(btnStop);
		JLabel btnContinue = new JLabel("");
		btnContinue.setHorizontalAlignment(SwingConstants.CENTER);
		btnContinue.setIcon(new ImageIcon("resources/play-button.png"));
		btnContinue.setEnabled(false);
		btnContinue.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changePanel(buttonMainPanel, "panelPause");
				
				// Thread awake
				thread.setPaused(false);
				synchronized (thread) {
					thread.notify();
				}
			}
		});
		panelContinueStop.add(btnContinue);
		
		JPanel savePanel = new JPanel();
		frmTimerapp.getContentPane().add(savePanel, "savePanel");
		savePanel.setLayout(new CardLayout(0, 0));
		
		JPanel querySavePanel = new JPanel();
		savePanel.add(querySavePanel, "querySavePanel");
		querySavePanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		JPanel labelSavePanel = new JPanel();
		querySavePanel.add(labelSavePanel);
		labelSavePanel.setLayout(new CardLayout(0, 0));
		
		JPanel saveTimepanel = new JPanel();
		labelSavePanel.add(saveTimepanel, "saveTimepanel");
		saveTimepanel.setLayout(new BorderLayout(0, 0));
		
		JLabel labelForSavePanel = new JLabel("Save time ?");
		saveTimepanel.add(labelForSavePanel);
		labelForSavePanel.setFont(new Font("Dialog", Font.PLAIN, 32));
		labelForSavePanel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel buttonSavePanel = new JPanel();
		querySavePanel.add(buttonSavePanel);
		buttonSavePanel.setLayout(new CardLayout(0, 0));
		
		JPanel yesOrNoPanel = new JPanel();
		buttonSavePanel.add(yesOrNoPanel, "yesOrNoPanel");
		
		JLabel buttonYes = new JLabel("");
		buttonYes.setIcon(new ImageIcon("resources/checked.png"));
		buttonYes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Change to another panel
				changePanel(savePanel, "answerSavePanel");
			}
		});
		yesOrNoPanel.setLayout(new GridLayout(1, 0, 0, 0));
		buttonYes.setHorizontalAlignment(SwingConstants.CENTER);
		buttonYes.setEnabled(false);
		yesOrNoPanel.add(buttonYes);
		
		JLabel buttonNo = new JLabel("");
		buttonNo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Close program
				System.exit(0);
			}
		});
		buttonNo.setIcon(new ImageIcon("resources/cancel.png"));
		buttonNo.setEnabled(false);
		buttonNo.setHorizontalAlignment(SwingConstants.CENTER);
		yesOrNoPanel.add(buttonNo);
		
		JPanel answerSavePanel = new JPanel();
		savePanel.add(answerSavePanel, "answerSavePanel");
		answerSavePanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel saveAsPanel = new JPanel();
		answerSavePanel.add(saveAsPanel);
		saveAsPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Save as ?");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 32));
		lblNewLabel.setBounds(0, 0, 401, 38);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		saveAsPanel.add(lblNewLabel);
		
		queryTextField = new JTextField();
		queryTextField.setFont(new Font("Dialog", Font.PLAIN, 22));
		queryTextField.setHorizontalAlignment(SwingConstants.CENTER);
		queryTextField.setBounds(42, 37, 314, 27);
		saveAsPanel.add(queryTextField);
		queryTextField.setColumns(10);
		
		JPanel OkPanel = new JPanel();
		answerSavePanel.add(OkPanel);
		OkPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel buttonSave = new JLabel("");
		buttonSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String userInput = queryTextField.getText();
				if (!userInput.isEmpty()) {
					Map<String, List<String>> map = new HashMap<String, List<String>>();
					map.put(new Date().toString(), List.of(thread.getTime().toString()));
					
					JSONWriter.createJSONFile(userInput, map);
					
					// Close program
					System.exit(0);
				}
			}
		});
		buttonSave.setIcon(new ImageIcon("resources/down-arrow.png"));
		buttonSave.setEnabled(false);
		buttonSave.setHorizontalAlignment(SwingConstants.CENTER);
		OkPanel.add(buttonSave);	
	}
	
	/***
	 * Method to change between panels, after every button press.
	 * 
	 * @param buttonPanel Panel with the CardLayout that will be switching between added panels.
	 * @param panelName String that represents the name of the panel to be displayed.
	 */
	private void changePanel(JPanel buttonPanel, String panelName) {
		((CardLayout)buttonPanel.getLayout()).show(buttonPanel, panelName);
	}
}