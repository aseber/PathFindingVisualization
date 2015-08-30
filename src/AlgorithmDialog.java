import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;


public class AlgorithmDialog extends JDialog implements KeyListener {

	Container contentPane;
	JPanel AlgorithmDialogGUI;
	JPanel customPanel;
	JRadioButton astarButton;
	JRadioButton dijkstrasButton;
	JRadioButton customButton;
	JSlider GValueSlider;
	JSlider HValueSlider;
	JSlider weightSlider;
	JTextField GValue;
	JTextField HValue;
	JTextField weightValue;
	JCheckBox hierarchicalPathfinding;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7388554549130678395L;

	public AlgorithmDialog(Frame owner) {
		
		super(owner);
		initComponents();
		
	}
	
	public AlgorithmDialog(Dialog owner) {
		
		super(owner);
		initComponents();
		
	}
	
	private void initComponents() {
		
		AlgorithmDialogGUI = new JPanel();
		JPanel contentPanel = new JPanel();
		customPanel = new JPanel();
		JPanel contentPanel2 = new JPanel();
		astarButton = new JRadioButton("A Star");
		customButton = new JRadioButton("Custom");
		dijkstrasButton = new JRadioButton("Dijkstra");
		JLabel GLabel = new JLabel();
		GValue = new JTextField();
		JLabel HLabel = new JLabel();
		HValue = new JTextField();
		JLabel weightLabel = new JLabel();
		weightValue = new JTextField();
		GValueSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (VisualizationBase.gModifier*1000));
		HValueSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, (int) (VisualizationBase.hModifier*1000));
		weightSlider = new JSlider(JSlider.HORIZONTAL, 0, 10000, (int) (VisualizationBase.weightModifier*1000));
		hierarchicalPathfinding = new JCheckBox("Enable Hierarchical Pathfinding");
		JButton CancelButton = new JButton();
		JButton AcceptButton = new JButton();
		
		ChangeListener sliderListener = new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				
				JSlider source = (JSlider)e.getSource();
				
				if (source == GValueSlider) {
					
					HValueSlider.removeChangeListener(this);
					double gValue = GValueSlider.getValue();
					double hValue = 1000 - gValue;
					HValueSlider.setValue((int) hValue);
					GValue.setText(Double.toString(gValue/1000));
					HValue.setText(Double.toString(hValue/1000));
					HValueSlider.addChangeListener(this);
					
				}
				
				else if (source == HValueSlider) {
					
					GValueSlider.removeChangeListener(this);
					double hValue = HValueSlider.getValue();
					double gValue = 1000 - hValue;
					GValueSlider.setValue((int) gValue);
					GValue.setText(Double.toString(gValue/1000));
					HValue.setText(Double.toString(hValue/1000));
					GValueSlider.addChangeListener(this);
					
				}
				
				else if (source == weightSlider) {
					
					double newWeight = weightSlider.getValue();
					weightValue.setText(Double.toString(newWeight/1000));
					
				}
				
			}
			
		};
		
		ActionListener actionListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				JTextField source = (JTextField) e.getSource();
				
				if (source == GValue) {
					
					HValue.removeActionListener(this);
					double gValue = Math.max(0.0,  Math.min(1.0, Double.parseDouble(GValue.getText())));
					double hValue = 1000 - gValue*1000;
					GValueSlider.setValue((int) gValue);
					HValueSlider.setValue((int) hValue);
					HValue.setText(Double.toString(hValue/1000));
					HValue.addActionListener(this);
					
				}
				
				else if (source == HValue) {
					
					GValue.removeActionListener(this);
					double hValue = Math.max(0.0,  Math.min(1.0, Double.parseDouble(HValue.getText())));
					double gValue = 1000 - hValue*1000;
					GValueSlider.setValue((int) gValue);
					HValueSlider.setValue((int) hValue);
					GValue.setText(Double.toString(gValue/1000));
					GValue.addActionListener(this);
					
				}
				
				else if (source == weightValue) {
					
					double newWeight = Math.max(0.0, Math.min(10, Double.parseDouble(weightValue.getText())));
					weightSlider.setValue((int) (newWeight*1000));
					
				}
				
			}
			
		};
		
		ActionListener radioActionListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				JRadioButton source = (JRadioButton) e.getSource();
				
				if (source == astarButton) {
					
					if (customButton.isSelected()) {
						
						customPanel.setVisible(false);
						pack();
						
					}
					
					weightSlider.setEnabled(true);
					weightValue.setEnabled(true);
					dijkstrasButton.setSelected(false);
					customButton.setSelected(false);
					
				} else if (source == dijkstrasButton) {
					
					if (customButton.isSelected()) {
						
						customPanel.setVisible(false);
						pack();
						
					}
					
					weightSlider.setEnabled(true);
					weightValue.setEnabled(true);
					astarButton.setSelected(false);
					customButton.setSelected(false);
					
				} else if (source == customButton) {
					
					customPanel.setVisible(true);
					pack();
					
					weightSlider.setEnabled(false);
					weightValue.setEnabled(false);
					astarButton.setSelected(false);
					dijkstrasButton.setSelected(false);
					
				}
				
			}
			
		};
		
		setTitle("Algorithm Selector");
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		AlgorithmDialogGUI.setLayout(new BorderLayout());
		
		contentPanel.setLayout(new FormLayout("3px, 100px, 5px, 100px, 5px, 100px, 5px", "25px"));
		
		astarButton.addActionListener(radioActionListener);
		if (VisualizationBase.CURRENT_ALGORITHM == VisualizationBase.ASTAR) {astarButton.setSelected(true);}
		contentPanel.add(astarButton, CC.xywh(2, 1, 1, 1));
		
		dijkstrasButton.addActionListener(radioActionListener);
		if (VisualizationBase.CURRENT_ALGORITHM == VisualizationBase.DIJKSTRA) {dijkstrasButton.setSelected(true);}
		contentPanel.add(dijkstrasButton, CC.xywh(4, 1, 1, 1));
		
		customButton.addActionListener(radioActionListener);
		if (VisualizationBase.CURRENT_ALGORITHM == VisualizationBase.CUSTOM) {customButton.setSelected(true);}
		contentPanel.add(customButton, CC.xywh(6, 1, 1, 1));
		
		customPanel.setLayout(new FormLayout("3px, 50px, 5px, 100px, 5px, 100px, 5px, 50px, 5px", "5px, 25px, 5px, 25px"));
		
		GLabel.setText("G Value");
		customPanel.add(GLabel, CC.xywh(2, 2, 1, 1));
		
		HLabel.setText("H Value");
		customPanel.add(HLabel, CC.xywh(2, 4, 1, 1));
		
		GValue.setText(Double.toString(VisualizationBase.gModifier));
		GValue.addActionListener(actionListener);
		customPanel.add(GValue, CC.xywh(8, 2, 1, 1));
		
		HValue.setText(Double.toString(VisualizationBase.hModifier));
		HValue.addActionListener(actionListener);
		customPanel.add(HValue, CC.xywh(8, 4, 1, 1));
		
		GValueSlider.addChangeListener(sliderListener);
		customPanel.add(GValueSlider, CC.xywh(4, 2, 3, 1));
		
		HValueSlider.addChangeListener(sliderListener);
		customPanel.add(HValueSlider, CC.xywh(4, 4, 3, 1));
		
		contentPanel2.setLayout(new FormLayout("3px, 50px, 5px, 100px, 5px, 100px, 5px, 50px, 5px", "5px, 25px, 5px, 25px, 5px, 50px"));
		
		weightLabel.setText("Weight");
		contentPanel2.add(weightLabel, CC.xywh(2, 2, 1, 1));
		
		weightValue.setText(Double.toString(VisualizationBase.weightModifier));
		weightValue.addActionListener(actionListener);
		contentPanel2.add(weightValue, CC.xywh(8, 2, 1, 1));
		
		weightSlider.addChangeListener(sliderListener);
		contentPanel2.add(weightSlider, CC.xywh(4, 2, 3, 1));
		
		hierarchicalPathfinding.setSelected(VisualizationBase.hierarchicalPathfinding);
		contentPanel2.add(hierarchicalPathfinding, CC.xywh(2, 4, 6, 1));
		
		CancelButton.setText("Cancel");
		CancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				CancelButtonMouseCLicked();
				
			}
			
		});
		contentPanel2.add(CancelButton, CC.xywh(4, 6, 1, 1));
		
		AcceptButton.setText("Accept");
		AcceptButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				AcceptButtonMouseCLicked();
				
			}
			
		});
		contentPanel2.add(AcceptButton, CC.xywh(6, 6, 1, 1));
		
		AlgorithmDialogGUI.add(contentPanel, BorderLayout.NORTH);
		AlgorithmDialogGUI.add(customPanel, BorderLayout.CENTER);
		if (VisualizationBase.CURRENT_ALGORITHM != VisualizationBase.CUSTOM) {customPanel.setVisible(false);}
		AlgorithmDialogGUI.add(contentPanel2, BorderLayout.SOUTH);
		contentPane.add(AlgorithmDialogGUI, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(true);
		
	}

	private void CancelButtonMouseCLicked() {
		
		this.dispose();
		
	}
	
	private void AcceptButtonMouseCLicked() {
		
		if (astarButton.isSelected()) {
			
			VisualizationBase.CURRENT_ALGORITHM = VisualizationBase.ASTAR;
			
		} else if (dijkstrasButton.isSelected()) {
			
			VisualizationBase.CURRENT_ALGORITHM = VisualizationBase.DIJKSTRA;
			
		} else if (customButton.isSelected()) {
			
			VisualizationBase.CURRENT_ALGORITHM = VisualizationBase.CUSTOM;
			
			double g = GValueSlider.getValue();
			double h = HValueSlider.getValue();
			
			VisualizationBase.gModifier = g/1000;
			VisualizationBase.hModifier = h/1000;
			
		}
		
		VisualizationBase.hierarchicalPathfinding = hierarchicalPathfinding.isSelected();
		
		double weight = weightSlider.getValue();
		VisualizationBase.weightModifier = weight/1000;
		
		this.dispose();
		
	}
	
	public void keyPressed(KeyEvent key) {
		
		if (key.getKeyCode() == KeyEvent.VK_ENTER) { 
			
			AcceptButtonMouseCLicked();
	
		}
		
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			
			CancelButtonMouseCLicked();
			
		}
		
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
	
}