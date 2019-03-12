package network;

import java.net.*;
import java.util.*;

import main.Session;

import java.io.*;

public class Client {
	
	private int port;
	private InetAddress clientIP;
	private Socket clientSocket;
	
	public Client() {}
	
	public void setSocket(Socket cs) {
		this.clientSocket = cs;
	}

	public Socket getSocket() {
		return this.clientSocket;
	}
	
	public void setIP(InetAddress clientIP) {
		this.clientIP = clientIP;
	}
	
	public InetAddress getIP() {
		return this.clientIP;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public Socket connect(InetAddress ip , int port) {
		
		try {
			Socket s = new Socket(ip , port);
			
			Server server = new Server(ip,port,10);
			server.setSocket(s);
			
			//todo tell client that it is connected
			
			//ServerListenerThread slt= new ServerListenerThread("server listener", server);
			
			System.out.println("ShareIt: server listener created");
					
					
			return  s;
			
		} catch (IOException e) {
		
			e.printStackTrace();
			return null;
		}	
	}
	
	
	
	@Override
	public String toString() {
		return "IP: " + this.clientIP.getHostAddress() + " Port: " +this.port + " Host: " + this.clientIP.getHostName(); 
	}
	
	
	
}
