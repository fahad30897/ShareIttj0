package GUIImplementation;

import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;

import main.History;
import main.Session;
import java.util.*;
import java.awt.*;
import java.io.File;
import java.net.InetAddress;

public class GUILogic extends JFrame{
	public JMenuBar menubar;
	public JMenu optionsMenu;
	public JMenu filePath;
	public JMenuItem connectItem, createItem,filePathItem;
	public JPanel centerPanel = new JPanel(new GridLayout(0, 2));
	public JPanel leftPanel = new JPanel(new BorderLayout(0,10));
	public JPanel leftTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel leftCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightPanel = new JPanel(new BorderLayout(0,10));
	public JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel rightCenterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public JPanel bottomPanel = new JPanel(new BorderLayout());
	public JTextArea bottomTextArea;
	public JButton selectFile,sendFile;
	public JButton getDirectory;
	public DefaultListModel connectedClientListModel = new DefaultListModel();
	public JList connectedClients = new JList(connectedClientListModel);
	public DefaultListModel fileListModel = new DefaultListModel();
	public JList fileList = new JList(fileListModel);
	public JTextField fileName;
	
	public String[] columnNames = {"File" , "Sender" , "Reciever","Size" };
	public Object[][] data = new Object[0][4];
	public DefaultTableModel tableModel = new DefaultTableModel(data , columnNames);
	public JTable historyTable;
	
	public GUILogic() {
		super("ShareIT");
		this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		setLayout(new BorderLayout());
		
		
		menubar = new JMenuBar();
		
		optionsMenu = new JMenu("Options");
		filePath = new JMenu("FilePath");
		this.createItem = new JMenuItem("Create");
		this.connectItem = new JMenuItem("Connect");
		this.filePathItem = new JMenuItem("Set File Path");
		
		optionsMenu.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		filePath.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		
		createItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		connectItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		filePathItem.setFont(new Font(Font.SANS_SERIF , Font.PLAIN, 20));
		
		optionsMenu.add(createItem);
		optionsMenu.add(connectItem);
		filePath.add(filePathItem);
		
		menubar.add(optionsMenu);
		menubar.add(filePath);
		this.setJMenuBar(menubar);
		centerPanel.add(leftPanel);
		centerPanel.add(rightPanel);
		rightPanel.add(rightTopPanel,BorderLayout.NORTH);
		rightPanel.add(rightCenterPanel,BorderLayout.CENTER);
		rightPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		rightTopPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		rightCenterPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		
		
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
		rightTopPanel.add(selectFile);
		rightTopPanel.add(fileName);
		rightTopPanel.add(sendFile);
		selectFile.setPreferredSize(new Dimension(100, 50));
		sendFile.setPreferredSize(new Dimension(100, 50));
		fileName.setPreferredSize(new Dimension(100, 50));
		
		
		
		getDirectory = new JButton("Get Directory");
		getDirectory.setPreferredSize(new Dimension(150, 50));
		leftPanel.add(leftTopPanel,BorderLayout.NORTH);
		leftPanel.add(leftCenterPanel,BorderLayout.CENTER);
		leftPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		leftTopPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		leftCenterPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		leftTopPanel.add(connectedClients);
		leftCenterPanel.add(new JScrollPane(fileList));
		leftTopPanel.add(getDirectory);
		
		historyTable = new JTable(data, columnNames);
		rightCenterPanel.add(new JScrollPane(historyTable));
		add(bottomPanel, BorderLayout.SOUTH);
		add(centerPanel , BorderLayout.CENTER);
		
		Session.setGUI(this);
		this.setupListeneres();
	}
	
	public void addClient() {
		connectedClientListModel.addElement(Session.getClient());
	}
	
	public void setFiles() {
		fileListModel.removeAllElements();
		for(File file : Session.getFiles()) {
			fileListModel.addElement(file.getAbsolutePath());
		}
	}
	
	
	
	public void setVisibility() {
		leftPanel.setVisible(true);
		rightPanel.setVisible(true);
		centerPanel.setVisible(true);
		
	}
	
	public void setupListeneres() {
		System.out.println("ss: setup listener" );
		getDirectory.addActionListener(new ActionEventHandler(GUILogic.this));
		connectItem.addActionListener(new ActionEventHandler(GUILogic.this));
		createItem.addActionListener(new ActionEventHandler(GUILogic.this));
		filePathItem.addActionListener(new ActionEventHandler(GUILogic.this));
		selectFile.addActionListener(new ActionEventHandler(GUILogic.this));
		sendFile.addActionListener(new ActionEventHandler(GUILogic.this));
		this.addWindowListener(new WindowEventHandler(this));
	}

	public boolean askForPermissoin(File file, InetAddress inetAddress, int port) {
		// TODO Show a dialog asking for permission
		System.out.println("ShareIt : Server In asForPermission GUI " + file.getName());
		int permit = JOptionPane.showConfirmDialog(this, "Accept File " + file.getName());
		
		return (permit == JOptionPane.OK_OPTION) ? true : false;
	}
	
	public void refreshTable() {
		while(this.tableModel.getRowCount() > 0) {
			tableModel.removeRow(0);
		}
		fetchData();
		for(int i = 0 ; i < data.length; i++) {
			tableModel.addRow(data[i]);
		}
		historyTable.setModel(tableModel);
	}
	
	public void fetchData() {
		int i = 0;
		List<History> history = Session.getHistory();
		System.out.println("in fetch data");
		data = new Object[history.size()][4];
		for(History h: history) {
			if(h != null) {
				data[i][0] = h.getFile().getName();
				data[i][1] = h.getSenderIP().getHostName();
				data[i][2] = h.getRecieverIP().getHostName();
				data[i][3] = h.getFile().length();
			}
			i++;
		}
		
	}

}
