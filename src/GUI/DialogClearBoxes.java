package GUI;

import BoxState.IBoxState;
import BoxSystem.Box;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static BoxState.BoxState.*;
import static Settings.WindowSettings.VISUALIZATION_WINDOW;


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
		
		ArrayList<IBoxState> flags = new ArrayList<>(10);
		
		if (searchedBoxesCheckBox.isSelected()) {
			
			flags.add(SEARCHED_BOX_STATE);
			
		}
		
		if (fullBarrierBoxesCheckBox.isSelected()) {
			
			flags.add(BARRIER_STATE);
			
		}
		
		if (partialBarrierBoxesCheckBox.isSelected()) {
			
			flags.add(BARRIER_STATE);
			
		}
		
		if (shortBoxesCheckBox.isSelected()) {
			
			flags.add(SHORTEST_PATH_BOX_STATE);
			
		}

		if (startBoxCheckBox.isSelected()) {
			
			flags.add(START_BOX_STATE);
			
		}
		
		if (endBoxCheckBox.isSelected()) {
			
			flags.add(END_BOX_STATE);
			
		}
		
		if (queuedBoxesCheckBox.isSelected()) {
			
			flags.add(QUEUED_BOX_STATE);
			
		}
		
		VISUALIZATION_WINDOW.clearBoxFieldFlags((IBoxState[]) flags.toArray());
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
