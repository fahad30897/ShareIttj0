package GUIImplementation;

import java.awt.event.WindowAdapter;
import java.net.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import network.*;

import javax.swing.JOptionPane;

import main.Session;


public class WindowEventHandler extends WindowAdapter{
	GUILogic gui;
	public WindowEventHandler(GUILogic gui) {
		this.gui = gui;
	}
	
	
	@Override
	public void windowClosing(WindowEvent e) {
			if(Session.getServer() != null){
				try {
					if(Session.getServer().getServerSocket() != null)
						Session.getServer().getServerSocket().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			gui.dispose();
		}
	
}
