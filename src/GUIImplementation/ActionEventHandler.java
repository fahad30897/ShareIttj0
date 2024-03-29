package GUIImplementation;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import main.Session;
import network.Communication;

public class ActionEventHandler implements ActionListener {
	private GUILogic gui;
	
	public ActionEventHandler(GUILogic g) {
		gui = g;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println("ss: In action performed" );
		if(e.getSource() == gui.connectItem) {
			JLabel IPAddressLabel,portNoLabel;
			JDialog connectDialog = new JDialog(gui, "Connect", true);

			connectDialog.setSize(400, 300);
			connectDialog.setLocationRelativeTo(null);
			connectDialog.setLayout(new FlowLayout(FlowLayout.CENTER));

			IPAddressLabel = new JLabel("IPAddress");
			JTextField IPAddressTextField = new JTextField(15);

			portNoLabel = new JLabel("Port No.:");
			JTextField portNoTextField = new JTextField(15);


			JButton submit = new JButton("Submit");	
			submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String IPAddress = new String(IPAddressTextField.getText());
					String portNo = new String(portNoTextField.getText());
					try {
						Session.connectToServer(InetAddress.getByName(IPAddress), Integer.parseInt(portNo));
						connectDialog.dispose();
					}
					catch(Exception ex) {
						connectDialog.dispose();
						JOptionPane.showMessageDialog(gui, "Error connecting to server" ,"Error" , JOptionPane.ERROR_MESSAGE );
						ex.printStackTrace();
					}
				}
					

			});

			JPanel createPanel = new JPanel(new GridBagLayout());
			connectDialog.getContentPane().add(createPanel);
			addComp(createPanel, IPAddressLabel, 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
			addComp(createPanel, IPAddressTextField, 1, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
			addComp(createPanel, portNoLabel, 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
			addComp(createPanel, portNoTextField, 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE);
			addComp(createPanel, submit, 0, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
			connectDialog.setVisible(true);
		}
		else if(e.getSource() == gui.createItem) {
			//System.out.println("create Item" );
			try {
				System.out.println("ss: create action" );
				Session.createServer();
				gui.bottomTextArea.setText("IP of server: " +Session.getServer().getIP().getHostAddress() + "\nPort " + Session.getPort());
				//JOptionPane.showMessageDialog(gui, "Server: "+Session.getServer() ,"Error" , JOptionPane.ERROR_MESSAGE );
				JOptionPane.showMessageDialog(gui, "here: ","Error" , JOptionPane.ERROR_MESSAGE );
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(gui, "Unknown Host" ,"Error" , JOptionPane.ERROR_MESSAGE );
				e1.printStackTrace();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(gui, "Error" ,"Error" , JOptionPane.ERROR_MESSAGE );
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == gui.filePathItem) {
			String tempFilePath;
			System.out.println("in handler");
			JFileChooser chooser = new JFileChooser();
		    chooser.setCurrentDirectory(new java.io.File("."));
		    chooser.setDialogTitle("Select Folder");
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
		    
		    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		      System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		      tempFilePath=chooser.getSelectedFile().toString();
		      tempFilePath = tempFilePath + "\\ShareItTransferedFiles";
		      new File(tempFilePath).mkdirs();
		      Session.setPath(tempFilePath);
		      
		    } else {
		      System.out.println("No Selection ");
		    }
		  
		}
		else if(e.getSource() == gui.selectFile) {
			JFileChooser fileOpen = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int r = fileOpen.showOpenDialog(null);
			if (r == JFileChooser.APPROVE_OPTION) {
				System.out.println("ShareIt : File selected" + fileOpen.getSelectedFile().getAbsolutePath());
				 gui.fileName.setText(fileOpen.getSelectedFile().getName());
				 Session.setSendFilePath(fileOpen.getSelectedFile().getAbsolutePath());
			}
//				gui.fileName.setText(fileOpen.getSelectedFile().getName());
	        else {
	        	JOptionPane.showMessageDialog(this.gui, "User didn't select any file");
	        	gui.fileName.setText("");
	        }
		}
		else if(e.getSource() == gui.sendFile) {
			try {
				System.out.println("ShareIt : client sending in actino Event handler" );
				Session.sendFile(Session.getSendFilePath(), false);
			} catch (ClassNotFoundException | IOException e1) {
				
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == gui.getDirectory) {
			try {
				Communication com = new Communication();
				if(gui.fileListModel.getSize() == 0) {
					System.out.println("ShareIt: in getDirectory = 0");
					com.getFiles("C:\\");
					
				}
				else {
					System.out.println("ShareIt: in getDirectory != 0");
					int index = gui.fileList.getSelectedIndex();
					File file = Session.getFiles().get(index);
					com.getFiles(file.getAbsolutePath());
				}
				
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	
	public static void addComp(JPanel thePanel, Component comp, int xPos, int yPos, int compWidth, int compHeight, int place,
			int stretch) {

		GridBagConstraints gridConstraints = new GridBagConstraints();

		gridConstraints.gridx = xPos;

		gridConstraints.gridy = yPos;

		gridConstraints.gridwidth = compWidth;

		gridConstraints.gridheight = compHeight;

		gridConstraints.weightx = 100;

		gridConstraints.weighty = 100;

		gridConstraints.insets = new Insets(5, 5, 5, 5);

		gridConstraints.anchor = place;

		gridConstraints.fill = stretch;

		thePanel.add(comp, gridConstraints);

	}
}
