import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

public class VisualizationGUI extends JFrame {

	protected VisualizationWindow mainWindow;
	private JLabel openCounter = new JLabel("| Nodes Open: 0 ");
	private JLabel closedCounter = new JLabel("| Nodes Closed: 0 ");
	private JLabel pathLengthCounter = new JLabel("| Path Length: 0 ");
	private JLabel runTimeCounter = new JLabel("| Run Time: 0 ms");
	private JSlider weightSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (VisualizationBase.WEIGHT*1000));
	private JTextField weightValue = new JTextField(Double.toString(VisualizationBase.WEIGHT));
	private JButton runButton = new JButton("Start Simulation");
	
	private static final long serialVersionUID = -6664286942946303464L;
	
	VisualizationGUI() {
		
		initializeGUI();
		VisualizationBase.VISUALIZATION_WINDOW.createBoxRegionField();
		
	}
	
	public void setWeightSlider(int value) {
		
		weightSlider.setValue(value);
		
	}
	
	public void setWeightValue(double value) {
		
		weightValue.setText(Double.toString(value));
		
	}
	
	ChangeListener sliderListener = new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
			
			JSlider source = (JSlider)e.getSource();
			
			if (source == weightSlider) {
				
				double newWeight = weightSlider.getValue();
				weightValue.setText(Double.toString(newWeight/1000));
				VisualizationBase.WEIGHT = ((double) weightSlider.getValue())/1000;
				VisualizationBase.VISUALIZATION_WINDOW.requestFocus();
				
			}
			
		}
		
	};
	
	ActionListener actionListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			
			JTextField source = (JTextField) e.getSource();
			
			if (source == weightValue) {
				
				double newWeight = Math.max(0.0, Math.min(10, Double.parseDouble(weightValue.getText())));
				weightSlider.setValue((int) (newWeight*1000));
				VisualizationBase.WEIGHT = ((double) weightSlider.getValue())/1000;
				VisualizationBase.VISUALIZATION_WINDOW.requestFocus();
				
			}
			
		}
		
	};
	
	public void setOpenCounter(int var) {
		
		openCounter.setText("| Nodes Open: " + var + " ");
		
	}
	
	public void resetOpenCounter() {
		
		setOpenCounter(0);
		
	}
	
	public void setClosedCounter(int var) {
		
		closedCounter.setText("| Nodes Closed: " + var + " ");
		
	}
	
	public void resetClosedCounter() {
		
		setClosedCounter(0);
		
	}
	
	public void setPathLengthCounter(int var) {
		
		pathLengthCounter.setText("| Path Length: " + var + " ");
		
	}
	
	public void resetPathLengthCounter() {
		
		setPathLengthCounter(0);
		
	}
	
	public void setRunTimeCounter(long var) {
		
		runTimeCounter.setText("| Run Time: " + var + " ms ");
		
	}
	
	public void resetRunTimeCounter() {
		
		setRunTimeCounter(0);
		
	}
	
	public void setRunButtonState(boolean paused) {
		
		if (paused) {
			
			runButton.setText("Start Simulation");
			
		}
		
		else {
			
			runButton.setText("Pause Simulation");
			
		}
		
	}
	
	private void initializeGUI() {
		
		VisualizationWindow mainWindow = new VisualizationWindow();
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenuGroup = new JMenu("File");
		JMenu fileResetBoxes = new JMenu("Reset Boxes");
		JMenuItem fileResetAllBoxes = new JMenuItem("Reset All Boxes");
		JMenuItem fileResetSearchedBoxes = new JMenuItem("Reset Searched Boxes");
		JMenuItem fileResetFullBarrierBoxes = new JMenuItem("Reset Barrier Boxes");
		JMenuItem fileResetPartialBarrierBoxes = new JMenuItem("Reset Partial Barrier Boxes");
		JMenuItem fileResetShortBoxes = new JMenuItem("Reset Shortest Path Boxes");
		JMenuItem fileResetStartBox = new JMenuItem("Reset Start Box");
		JMenuItem fileResetEndBox = new JMenuItem("Reset End Box");
		JMenuItem fileResetQueuedBoxes = new JMenuItem("Reset Queued Boxes");
		JMenuItem fileResetSomeBoxes = new JMenuItem("Reset Boxes...");
		JMenuItem fileExit = new JMenuItem("Exit program");
		JMenu settingsMenuGroup = new JMenu("Settings");
		JMenuItem settingsChangeAlgorithm = new JMenuItem("Modify Algorithm");
		JMenuItem settingsChangeWindow = new JMenuItem("Modify Window");
		JMenuItem settingsChangeSleepTimer = new JMenuItem("Change Sleep Timer");
		
		VisualizationBase.VISUALIZATION_WINDOW = mainWindow;
		
		this.mainWindow = mainWindow;
		
		//FormLayout layout = new FormLayout();
		
		setLayout(new FormLayout("5px, 1px:grow, 50px, 5px", "25px, 1px:grow, 5px, 25px, 5px, 20px, 5px"));
		
		//PanelBuilder builder = new PanelBuilder(layout);
		
		add(menuBar, CC.xywh(1, 1, 4, 1, CC.FILL, CC.FILL));
			menuBar.add(fileMenuGroup);
				fileMenuGroup.add(fileResetBoxes);
					fileResetBoxes.add(fileResetAllBoxes);
					fileResetAllBoxes.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetAllBoxes();
							
						}
						
					});
					fileResetBoxes.add(fileResetSearchedBoxes);
					fileResetSearchedBoxes.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetSearchedBoxes();
							
						}
						
					});
					fileResetBoxes.add(fileResetFullBarrierBoxes);
					fileResetFullBarrierBoxes.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetFullBarrierBoxes();
							
						}
						
					});
					fileResetBoxes.add(fileResetPartialBarrierBoxes);
					fileResetPartialBarrierBoxes.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetPartialBarrierBoxes();
							
						}
						
					});
					fileResetBoxes.add(fileResetShortBoxes);
					fileResetShortBoxes.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetShortBoxes();
							
						}
						
					});
					fileResetBoxes.add(fileResetStartBox);
					fileResetStartBox.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetStartBox();
							
						}
						
					});
					fileResetBoxes.add(fileResetEndBox);
					fileResetEndBox.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetEndBox();
							
						}
						
					});
					fileResetBoxes.add(fileResetQueuedBoxes);
					fileResetQueuedBoxes.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetQueuedBoxes();
							
						}
						
					});
					fileResetBoxes.add(fileResetSomeBoxes);
					fileResetSomeBoxes.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0) {
						
							fileResetSomeBoxes();
							
						}
						
					});
				
				fileMenuGroup.add(fileExit);
				fileExit.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
					
						fileExit();
						
					}
					
				});
			menuBar.add(settingsMenuGroup);
				settingsMenuGroup.add(settingsChangeAlgorithm);
				settingsChangeAlgorithm.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
					
						settingsChangeAlgorithm();
						
					}
					
				});
				settingsMenuGroup.add(settingsChangeWindow);
				settingsChangeWindow.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
					
						settingsChangeWindow();
						
					}
					
				});
				settingsMenuGroup.add(settingsChangeSleepTimer);
				settingsChangeSleepTimer.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
					
						settingsChangeSleepTimer();
						
					}
					
				});
				
			menuBar.add(openCounter);
			menuBar.add(closedCounter);
			menuBar.add(pathLengthCounter);
			menuBar.add(runTimeCounter);
				
		add(mainWindow, CC.xywh(1, 2, 4, 1, CC.FILL, CC.FILL));
			
		weightSlider.addChangeListener(sliderListener);
		weightValue.addActionListener(actionListener);
		add(weightSlider, CC.xywh(1, 4, 2, 1, CC.FILL, CC.FILL));
		add(weightValue, CC.xywh(3, 4, 1, 1, CC.FILL, CC.FILL));
		
		runButton.setEnabled(false);
		add(runButton, CC.xywh(2, 6, 2, 1, CC.FILL, CC.FILL));
		runButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				runSimulation();
				
			}
			
		});
		
		pack();
		
		int sizeX = (int) (VisualizationBase.ROW_COLUMN_COUNT*VisualizationBase.BOX_XY_SIZE + 17);
		int sizeY = (int) (VisualizationBase.ROW_COLUMN_COUNT*VisualizationBase.BOX_XY_SIZE + 125);
		setSize(sizeX, sizeY);
		
	}
	
	public void runSimulation() {
		
		VisualizationBase.VISUALIZATION_WINDOW.runPathfinder();
		VisualizationBase.VISUALIZATION_WINDOW.requestFocus();
		
	}
	
	public void setRunButtonEnabledState(boolean enabled) {
		
		runButton.setEnabled(enabled);
		
	}
	
	private void fileResetAllBoxes() {
		
		Box.flags[] flags = {Box.flags.SEARCHED, Box.flags.PARTIAL_BARRIER, Box.flags.FULL_BARRIER, Box.flags.SHORTEST_PATH, Box.flags.START, Box.flags.END, Box.flags.QUEUED};
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlags(flags);
		
	}
	
	private void fileResetSearchedBoxes() {
		
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlag(Box.flags.SEARCHED);
		
	}
	
	private void fileResetFullBarrierBoxes() {
			
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlag(Box.flags.FULL_BARRIER);
			
	}
	
	private void fileResetPartialBarrierBoxes() {
		
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlag(Box.flags.PARTIAL_BARRIER);
			
	}
	
	private void fileResetShortBoxes() {
		
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlag(Box.flags.SHORTEST_PATH);
		
	}
	
	private void fileResetStartBox() {
		
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlag(Box.flags.START);
		Box.checkBeginningAndEndState();
		
	}
	
	private void fileResetEndBox() {
		
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlag(Box.flags.END);
		Box.checkBeginningAndEndState();
		
	}
	
	private void fileResetQueuedBoxes() {
		
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlag(Box.flags.QUEUED);
		
	}
	
	private void fileResetSomeBoxes() {
		
		DialogClearBoxes BoxGUI = new DialogClearBoxes(this);
		BoxGUI.setModalityType(ModalityType.APPLICATION_MODAL);
		BoxGUI.setVisible(true);
		
	}
	
	private void fileExit() {
		
		System.exit(0);
		
	}
	
	private void settingsChangeAlgorithm() {
		
		DialogAlgorithm AlgorithmGUI = new DialogAlgorithm(this);
		AlgorithmGUI.setModalityType(ModalityType.APPLICATION_MODAL);
		AlgorithmGUI.setVisible(true);
		
	}
	
	private void settingsChangeWindow() {
		
		DialogWindow WindowGUI = new DialogWindow(this);
		WindowGUI.setModalityType(ModalityType.APPLICATION_MODAL);
		WindowGUI.setVisible(true);
		
	}
	
	private void settingsChangeSleepTimer() {
		
		SleepTimeDialog SleepTimeGUI = new SleepTimeDialog(this);
		SleepTimeGUI.setModalityType(ModalityType.APPLICATION_MODAL);
		SleepTimeGUI.setVisible(true);
		
	}
	
}
