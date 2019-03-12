package main;

import java.io.File;
import java.io.IOException;

import java.net.*;

import GUIImplementation.GUILogic;
import network.*;


public class Session {
	
	private static Client client;// convert to array list for multiple clients
	private static Server server;
	private static int deviceType; // 0 for server , 1 for client
	private static int PORT = 21132;
	private static int BACKLOG = 10;
	private static int SENDBUFFER = 100;
	private static GUILogic gui;
	private static String pathToSave = "C:\\Users\\dell\\eclipse-workspace2\\ShareItGit\\ShareIt\\transferedfiles";
	private static String sendFilePath = "";
	
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
	
	public static String getSendFilePath() {
		return Session.sendFilePath;
	}
	
	public static void setSendFilePath(String f) {
		Session.sendFilePath = f;
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
	
	public static int getSendBuffer() {
		return Session.getSendBuffer();
	}
	
	public static String getPath() {
		return Session.pathToSave;
	}
	
	public static void setPath(String p) {
		Session.pathToSave = p;
	}
	
	public static void showConnectedClient() {
		Session.gui.addClient();
	}
	
	public static void createServer() throws UnknownHostException, IOException {
		Server s = new Server(InetAddress.getLocalHost() ,Session.PORT, Session.BACKLOG );
		Session.setDeviceType(0); // for server
		s.createSocket();
		//Socket clientSocket = s.connectToClient();
		
		//Session.client;
	}
	
	public static void connectToServer(InetAddress ip , int port) throws IOException {
		Client client = new Client();
		Socket soc = client.connect(ip, port);
		Session.server = new Server(ip,port,Session.BACKLOG);
		deviceType = 1; // Client
		Session.server.setSocket(soc);
		
		
	}
	
	
	public static boolean askForPermission(File file, Object object) {
		
		if(object instanceof Client) {
			Client client = (Client) object;
			System.out.println("ShareIt : CLient ask for permission Session");
			boolean hasPermission = Session.gui.askForPermissoin(file , client.getSocket().getInetAddress() , client.getSocket().getPort());
			return hasPermission;	
		}
		else {
			Server server = (Server) object;
			System.out.println("ShareIt : Server ask for permission Session");
			boolean hasPermission = Session.gui.askForPermissoin(file , server.getSocket().getInetAddress() , server.getSocket().getPort());
			return hasPermission;
		}
		
		
	}

	public static void sendFile(String fileString,boolean toAll) throws ClassNotFoundException, IOException {
		System.out.println("ShareIt: in Session sending file " + fileString);
		File file = new File(fileString);
		Communication com = new Communication();
		System.out.println("ShareIt: Before callingg send file on communication" );
		if(Session.getDeviceType() == 1) {
			com.sendFileToServer(file);
		}
		else {
			com.sendFileToClient(file);
		}
		
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
