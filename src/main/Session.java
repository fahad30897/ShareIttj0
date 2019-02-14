package main;

import java.io.IOException;
import java.net.*;

import GUIImplementation.GUILogic;
import network.*;
import exceptions.*;

public class Session {
	
	private static Client client;// convert to array list for multiple clients
	private static Server server;
	private static int deviceType; // 0 for server , 1 for client
	private static int PORT = 21132;
	private static int BUFFER = 100;
	private static GUILogic gui;
	
	public static GUILogic getGUI() {
		return Session.gui;
	}
	
	public static void setGUI(GUILogic g) {
		Session.gui = g;
	}
	
	public static Client getClient() {
		return Session.client;
	}
	
	public static void setClient(Client client) {
		Session.client = client;
	}
	
	public static Server getServer() {
		return Session.server;
	}
	
	public static void setServer(Server server) {
		Session.server = server;
	}
	
	public static boolean isServer() {
		return deviceType == 0 ? true :false;
	}
	
	public static boolean isClient() {
		return deviceType == 1 ? true :false;
	}
	
	public static int getDeviceType() {
		return Session.deviceType;
	}
	
	public static void setDeviceType(int device) {
		Session.deviceType = device;
	}
	
	public static int getPort() {
		return Session.PORT;
	}
	
	public static void setPort(int p) {
		Session.PORT = p;
	}
	
	public static void showConnectedClient() {
		Session.gui.addClient();
	}
	
	public static void createServer() throws UnknownHostException, IOException {
		Server s = new Server(InetAddress.getLocalHost() ,Session.PORT, Session.BUFFER );
		Session.setDeviceType(0); // for server
		s.createSocket();
		//Socket clientSocket = s.connectToClient();
		
		//Session.client;
	}
	
	public static void connectToServer(InetAddress ip , int port) throws IOException {
		Client client = new Client();
		Socket soc = client.connect(ip, port);
		server = new Server(ip,port,Session.BUFFER);
		deviceType = 1; // Client
		server.setSocket(soc);
		
	}
	
	public static void main(String[] args) {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			System.out.println("Host name:" + ip.getHostName());
			System.out.println("Host address:" + ip.getHostAddress());
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	

}
