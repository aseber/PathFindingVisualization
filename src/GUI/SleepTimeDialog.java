package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;


public class SleepTimeDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2295277117437760048L;
	JSlider SleepValueSlider;
	JTextField SleepValue;

	public SleepTimeDialog(Frame owner) {
		
		super(owner);
		initComponents();
		
	}
	
	public SleepTimeDialog(Dialog owner) {
		
		super(owner);
		initComponents();
		
	}
	
	ChangeListener sliderListener = new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
			
			ActionListener listeners[] = SleepValue.getActionListeners();
			
			for (ActionListener listener : listeners) {
				
				SleepValue.removeActionListener(listener);
				
			}
			
			int value = (int) Math.floor(Math.pow(1.1, SleepValueSlider.getValue())) - 1;
			SleepValue.setText(Integer.toString(value));
			
			for (ActionListener listener : listeners) {
				
				SleepValue.addActionListener(listener);
				
			}
			
		}
		
	};
	
	ActionListener actionListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			
			ChangeListener listeners[] = SleepValueSlider.getChangeListeners();
			
			for (ChangeListener listener : listeners) {
						
				SleepValueSlider.removeChangeListener(listener);
						
			}
					
			int value = (int) Math.max(0,  Math.min(1000, Math.log(VisualizationBase.SLEEP_TIMER)/Math.log(1.1)));
			SleepValueSlider.setValue(value);
			
			for (ChangeListener listener : listeners) {
				
				SleepValueSlider.addChangeListener(listener);
						
			}
			
		}
		
	};
	
	private void initComponents() {
		
		JPanel AlgorithmDialogGUI = new JPanel();
		JPanel contentPanel = new JPanel();
		JLabel SleepLabel = new JLabel();
		SleepValue = new JTextField();
		int value = 0;
		
		if (VisualizationBase.SLEEP_TIMER != 0) {
			
			value = (int) (Math.log(VisualizationBase.SLEEP_TIMER)/Math.log(1.1));
			
		} else {
			
			value = 0;
			
		}
		
		SleepValueSlider = new JSlider(JSlider.HORIZONTAL, 0, 73, value);
		JButton CancelButton = new JButton();
		JButton AcceptButton = new JButton();
			
		setTitle("Sleep Time Selector");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		AlgorithmDialogGUI.setLayout(new BorderLayout());
		
		contentPanel.setLayout(new FormLayout("3px, 75px, 5px, 100px, 5px, 100px, 5px, 50px", "5px, 25px, 5px, 50px"));
		
		SleepLabel.setText("Sleep Time");
		contentPanel.add(SleepLabel, CC.xywh(2, 2, 1, 1));
		
		
		SleepValue.setText(Integer.toString(VisualizationBase.SLEEP_TIMER));
		SleepValue.addActionListener(actionListener);
		contentPanel.add(SleepValue, CC.xywh(8, 2, 1, 1));	
		
		SleepValueSlider.addChangeListener(sliderListener);
		contentPanel.add(SleepValueSlider, CC.xywh(4, 2, 3, 1));
		
		CancelButton.setText("Cancel");
		CancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				CancelButtonMouseCLicked();
				
			}
			
		});
		contentPanel.add(CancelButton, CC.xywh(4, 4, 1, 1));
		
		AcceptButton.setText("Accept");
		AcceptButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				AcceptButtonMouseCLicked();
				
			}
			
		});
		contentPanel.add(AcceptButton, CC.xywh(6, 4, 1, 1));
		
		AlgorithmDialogGUI.add(contentPanel, BorderLayout.NORTH);
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
		
		VisualizationBase.SLEEP_TIMER = Integer.parseInt(SleepValue.getText());
		
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
