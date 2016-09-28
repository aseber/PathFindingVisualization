package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;


public class DialogClearBoxes extends JDialog implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1668287380439231540L;
	JCheckBox searchedBoxesCheckBox;
	JCheckBox fullBarrierBoxesCheckBox;
	JCheckBox partialBarrierBoxesCheckBox;
	JCheckBox shortBoxesCheckBox;
	JCheckBox startBoxCheckBox;
	JCheckBox endBoxCheckBox;
	JCheckBox queuedBoxesCheckBox;
	
	public DialogClearBoxes(Frame owner) {
		
		super(owner);
		initComponents();
		
	}
	
	public DialogClearBoxes(Dialog owner) {
		
		super(owner);
		initComponents();
		
	}
	
	private void initComponents() {
		
		JPanel BoxGUI = new JPanel();
		JPanel contentPanel = new JPanel();
		searchedBoxesCheckBox = new JCheckBox("Searched Boxes");
		fullBarrierBoxesCheckBox = new JCheckBox("Barrier Boxes");
		partialBarrierBoxesCheckBox = new JCheckBox("Partial Barrier Boxes");
		shortBoxesCheckBox = new JCheckBox("Shortest PathfindingAlgorithms.Path Boxes");
		startBoxCheckBox = new JCheckBox("Start BoxSystem.Box");
		endBoxCheckBox = new JCheckBox("End BoxSystem.Box");
		queuedBoxesCheckBox = new JCheckBox("Queued Boxes");
		JButton CancelButton = new JButton();
		JButton AcceptButton = new JButton();
			
		setTitle("Clear Boxes");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		BoxGUI.setLayout(new BorderLayout());
		
		contentPanel.setLayout(new FormLayout("3px, 100px, 5px, 100px, 5px", "5px, 25px, 5px, 25px, 5px, 25px, 5px, 25px, 5px, 25px, 5px, 25px, 5px, 25px, 5px, 50px"));
		
		contentPanel.add(searchedBoxesCheckBox, CC.xywh(2, 2, 3, 1));
		contentPanel.add(fullBarrierBoxesCheckBox, CC.xywh(2, 4, 3, 1));
		contentPanel.add(partialBarrierBoxesCheckBox, CC.xywh(2, 6, 3, 1));
		contentPanel.add(shortBoxesCheckBox, CC.xywh(2, 8, 3, 1));
		contentPanel.add(startBoxCheckBox, CC.xywh(2, 10, 3, 1));
		contentPanel.add(endBoxCheckBox, CC.xywh(2, 12, 3, 1));
		contentPanel.add(queuedBoxesCheckBox, CC.xywh(2, 14, 3, 1));
		
		CancelButton.setText("Cancel");
		CancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				CancelButtonMouseCLicked();
				
			}
			
		});
		contentPanel.add(CancelButton, CC.xywh(2, 16, 1, 1));
		
		AcceptButton.setText("Accept");
		AcceptButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				AcceptButtonMouseCLicked();
				
			}
			
		});
		contentPanel.add(AcceptButton, CC.xywh(4, 16, 1, 1));
		
		BoxGUI.add(contentPanel, BorderLayout.NORTH);
		contentPane.add(BoxGUI, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(true);
		
	}

	private void CancelButtonMouseCLicked() {
		
		this.dispose();
		
	}
	
	private void AcceptButtonMouseCLicked() {
		
		ArrayList<Box.flags> flags = new ArrayList<Box.flags>(10);
		
		if (searchedBoxesCheckBox.isSelected()) {
			
			flags.add(Box.flags.SEARCHED);
			
		}
		
		if (fullBarrierBoxesCheckBox.isSelected()) {
			
			flags.add(Box.flags.FULL_BARRIER);
			
		}
		
		if (partialBarrierBoxesCheckBox.isSelected()) {
			
			flags.add(Box.flags.PARTIAL_BARRIER);
			
		}
		
		if (shortBoxesCheckBox.isSelected()) {
			
			flags.add(Box.flags.SHORTEST_PATH);
			
		}

		if (startBoxCheckBox.isSelected()) {
			
			flags.add(Box.flags.START);
			
		}
		
		if (endBoxCheckBox.isSelected()) {
			
			flags.add(Box.flags.END);
			
		}
		
		if (queuedBoxesCheckBox.isSelected()) {
			
			flags.add(Box.flags.QUEUED);
			
		}
		
		VisualizationBase.VISUALIZATION_WINDOW.clearBoxFieldFlags((Box.flags[]) flags.toArray());
		Box.checkBeginningAndEndState();
		
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
