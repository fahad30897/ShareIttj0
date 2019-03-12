package GUIImplementation;

import javax.swing.*;
import javax.swing.border.Border;

import main.Session;

import java.awt.*;
import java.io.File;
import java.net.InetAddress;

public class GUILogic extends JFrame{
	public JMenuBar menubar;
	public JMenu optionsMenu;
	public JMenuItem connectItem, createItem;
	public JPanel centerPanel = new JPanel(new GridLayout(0, 2));
	public JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
		rightPanel.add(rightBottomPanel,BorderLayout.SOUTH);
		rightPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		rightBottomPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		
		
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
		rightBottomPanel.add(selectFile);
		rightBottomPanel.add(fileName);
		rightBottomPanel.add(sendFile);
		selectFile.setPreferredSize(new Dimension(100, 50));
		sendFile.setPreferredSize(new Dimension(100, 50));
		fileName.setPreferredSize(new Dimension(100, 50));
		
		
		
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
		selectFile.addActionListener(new ActionEventHandler(GUILogic.this));
		sendFile.addActionListener(new ActionEventHandler(GUILogic.this));
		this.addWindowListener(new WindowEventHandler(this));
	}

	public boolean askForPermissoin(File file, InetAddress inetAddress, int port) {
		// TODO Show a dialog asking for permission
		System.out.println("ShareIt : Server In asForPermission GUI");
		int permit = JOptionPane.showConfirmDialog(this, "Accept File " + file.getName());
		
		return (permit == JOptionPane.OK_OPTION) ? true : false;
	}

}
