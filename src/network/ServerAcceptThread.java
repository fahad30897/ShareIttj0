package network;

import java.net.*;

import main.Session;

import java.io.*;

public class ServerAcceptThread implements Runnable{
	
	private String name;
	private Thread t;
	public volatile boolean end = false;
	private Server server;
	
	public ServerAcceptThread(String n, Server server) {
		this.name = n;
		this.server = server;
		t = new Thread(this ,name);
		System.out.println("ss: before start" );
		t.setDaemon(true);
		t.start();
	}
	
	public void run() {
		System.out.println("ss: before while" );
		while(true) {
			if(end) {
				break;
			}
			try {
				System.out.println("ss: before accept" );
				Socket socket = server.getServerSocket().accept();
				
				System.out.println("ss: " + socket.getInetAddress().getHostName());
				//for multiple clients add to array list
				Client client = new Client();
				client.setSocket(socket);
				client.setIP(socket.getInetAddress());
				client.setPort(socket.getPort());
				
				Session.setClient(client);
				
				//TODO: Move this into setClient
				Session.showConnectedClient();
				
				ClientListenerThread cli = new ClientListenerThread("Client Listener " + socket.getInetAddress().getHostName() , client);
				
				System.out.println("client connected: " + socket.getInetAddress().getHostName());
				
			} catch (IOException e) {
				this.end = true;
				e.printStackTrace();
			}
		}
		
	}
	
	public synchronized void stop() {
		end = true;
	}
	
	public Thread getThread() {
		return this.t;
	}
	

}
