package GUIImplementation;

import javax.swing.*;
import javax.swing.border.Border;

import main.Session;

import java.awt.*;

public class GUILogic extends JFrame{
	public JMenuBar menubar;
	public JMenu optionsMenu;
	public JMenuItem connectItem, createItem;
	public JPanel centerPanel = new JPanel(new GridLayout(0, 2));
	public JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel bottomPanel = new JPanel(new BorderLayout());
	public JTextArea bottomTextArea;
	public JButton selectFile,sendFile;
	public DefaultListModel connectedClientListModel = new DefaultListModel();
	public JList connectedClients = new JList(connectedClientListModel);
	public JTextField fileName;
	
	public GUILogic() {
		super("ShareIT");
		this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		setLayout(new BorderLayout());
		
		
		menubar = new JMenuBar();
		
		optionsMenu = new JMenu("Options");
		this.createItem = new JMenuItem("Create");
		this.connectItem = new JMenuItem("Connect");
		
		optionsMenu.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		
		createItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		connectItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		
		optionsMenu.add(createItem);
		optionsMenu.add(connectItem);
		
		menubar.add(optionsMenu);
		this.setJMenuBar(menubar);
		centerPanel.add(leftPanel, BorderLayout.CENTER);
		centerPanel.add(rightPanel, BorderLayout.CENTER);
		
		
		
		bottomTextArea = new JTextArea(2, 15);
		bottomPanel.add(bottomTextArea, BorderLayout.CENTER);
		bottomTextArea
		.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		bottomTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		bottomTextArea.setEditable(false);
		
		selectFile = new JButton("Select");
		sendFile = new JButton("Send");
		fileName = new JTextField(15);
		fileName.setEditable(false);
		rightPanel.add(selectFile);
		rightPanel.add(fileName);
		rightPanel.add(sendFile);
		
		
		
		leftPanel.add(connectedClients);
		
		add(bottomPanel, BorderLayout.SOUTH);
		add(centerPanel , BorderLayout.CENTER);
		
		Session.setGUI(this);
		this.setupListeneres();
	}
	
	public void addClient() {
		connectedClientListModel.addElement(Session.getClient());
	}
	
	public void setVisibility() {
		leftPanel.setVisible(true);
		rightPanel.setVisible(true);
		centerPanel.setVisible(true);
		
	}
	
	public void setupListeneres() {
		System.out.println("ss: setup listener" );
		connectItem.addActionListener(new ActionEventHandler(GUILogic.this));
		createItem.addActionListener(new ActionEventHandler(GUILogic.this));
		this.addWindowListener(new WindowEventHandler(this));
	}

}
