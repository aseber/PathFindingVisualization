import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;


public class DialogWindow extends JDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = -2295277117437760048L;
	JTextField boxCountField;
	JTextField boxSizeField;
	JTextField regionSizeField;

	public DialogWindow(Frame owner) {

		super(owner);
		initComponents();

	}

	public DialogWindow(Dialog owner) {

		super(owner);
		initComponents();

	}

	private void initComponents() {

		JPanel AlgorithmDialogGUI = new JPanel();
		JPanel contentPanel = new JPanel();
		JLabel boxCountLabel = new JLabel();
		JLabel boxSizeLabel = new JLabel();
		JLabel regionSizeLabel = new JLabel();
		boxCountField = new JTextField();
		boxSizeField = new JTextField();
		regionSizeField = new JTextField();
		JButton CancelButton = new JButton();
		JButton AcceptButton = new JButton();

		setTitle("Window Size Selector");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		AlgorithmDialogGUI.setLayout(new BorderLayout());

		contentPanel.setLayout(new FormLayout("3px, 150px, 5px, 100px, 5px", "5px, 25px, 5px, 25px, 5px, 25px, 5px, 50px"));

		boxCountLabel.setText("Box Row/Column Count");
		contentPanel.add(boxCountLabel, CC.xywh(2, 2, 1, 1));

		boxCountField.setText(Integer.toString(VisualizationBase.ROW_COLUMN_COUNT));
		contentPanel.add(boxCountField, CC.xywh(4, 2, 1, 1));

		boxSizeLabel.setText("Box Row/Column Size");
		contentPanel.add(boxSizeLabel, CC.xywh(2, 4, 1, 1));

		boxSizeField.setText(Integer.toString((int) (VisualizationBase.BOX_XY_SIZE)));
		contentPanel.add(boxSizeField, CC.xywh(4, 4, 1, 1));

		regionSizeField.setText(Integer.toString(VisualizationBase.REGION_SIZE));
		contentPanel.add(regionSizeField, CC.xywh(4, 6, 1, 1));

		regionSizeLabel.setText("Region Row/Column Size");
		contentPanel.add(regionSizeLabel, CC.xywh(2, 6, 1, 1));

		CancelButton.setText("Cancel");
		CancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				CancelButtonMouseCLicked();

			}

		});
		contentPanel.add(CancelButton, CC.xywh(2, 8, 1, 1));

		AcceptButton.setText("Accept");
		AcceptButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				AcceptButtonMouseCLicked();

			}

		});
		contentPanel.add(AcceptButton, CC.xywh(4, 8, 1, 1));

		AlgorithmDialogGUI.add(contentPanel, BorderLayout.NORTH);
		contentPane.add(AlgorithmDialogGUI, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	private void CancelButtonMouseCLicked() {

		this.dispose();

	}

	private void AcceptButtonMouseCLicked() {

		VisualizationBase.VISUALIZATION_WINDOW.executor.endPathfinding();

		VisualizationBase.VISUALIZATION_WINDOW.removeBoxRegionField();
		VisualizationBase.ROW_COLUMN_COUNT = Integer.parseInt(boxCountField.getText());
		int sizeInt = Integer.parseInt(boxSizeField.getText());
		VisualizationBase.BOX_XY_SIZE = sizeInt;
		VisualizationBase.REGION_SIZE = Integer.parseInt(regionSizeField.getText());
		Dimension windowSize = new Dimension((int) (VisualizationBase.ROW_COLUMN_COUNT*VisualizationBase.BOX_XY_SIZE), (int) (VisualizationBase.ROW_COLUMN_COUNT*VisualizationBase.BOX_XY_SIZE));
		VisualizationBase.VISUALIZATION_WINDOW.setWindowSize(windowSize);
		Box.initializeStaticVariables();
		Region.initializeStaticVariables();
		VisualizationBase.VISUALIZATION_WINDOW.createBoxRegionField();
		VisualizationBase.VISUALIZATION_WINDOW.invalidate();
		VisualizationBase.VISUALIZATION_WINDOW.repaint();

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
