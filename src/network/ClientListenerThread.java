package network;

import java.net.*;
import java.util.*;


import exceptions.ShareItException;
import main.Session;

import java.io.*;

public class ClientListenerThread implements Runnable{

	
	private String name;
	private Thread t;
	public volatile boolean end = false;
	private Client client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientListenerThread(String n, Client client) {
		this.name = n;
		this.client = client;
		
		t = new Thread(this ,name);
		System.out.println("ss: before client listener start" );
		t.setDaemon(true);
		t.start();
	}
	
	
	
	@Override
	public void run() {
		try {
			
			
			while(true) {
				if(end) {
					break;
				}
				if(this.client.getSocket().isClosed()) {
					break;
				}
				this.ois = new ObjectInputStream(this.client.getSocket().getInputStream());
				this.oos = new ObjectOutputStream(this.client.getSocket().getOutputStream());
				
				System.out.println("Shareit : Waiting for client to write");
				Object object = ois.readObject(); // wait for client to send;
				
				
				if(object instanceof Packet) {
					Packet p = (Packet) object;
					
					switch(p.getPacketType()) {
						case FileSendPermitPacket:
							System.out.println("Client Listner: permit packet");
							FilePacket fp = (FilePacket) p;
							boolean hasPermission = Session.askForPermission(fp.getFile() , this.client);
							
							if(hasPermission) {
								
								FilePacket filePacket = new FilePacket(PacketType.FileSendAcceptPacket , fp.getFile());
								
								oos.writeObject(filePacket);
								System.out.println("ShareIt : Server Recieving file" );

								//Code to read File
								
								FileOutputStream fos = new FileOutputStream(new File(Session.getPath() + "\\" + fp.getFile().getName() ));
							
								
								
								byte[] fileArr = fp.getFileArr();
								fos.write(fileArr, 0, fileArr.length);
						        System.out.println("ShareIt : ServerFile transfer success");
						         
						        fos.close();
						 
//						        ois.close();
//						        oos.close();
//								
								
							}
							else {
								
								FilePacket filePacket = new FilePacket(PacketType.FileSendRejectPacket , fp.getFile());
								
								oos.writeObject(filePacket);
								
							}
							
						break;
						case RequestDirectoryPacket:
							MessagePacket msg = (MessagePacket) p;
							String directory = msg.getMessage();
							System.out.println("ShareIt: Server RequestDirectoryPacket " + directory);
							File folder = new File(directory);
							System.out.println("Hello World");
							
							if(folder.isDirectory()) {
								File[] files = folder.listFiles();
								List<File> fileList = new ArrayList<File>(Arrays.asList(files));
								
								DirectoryPacket dir = new DirectoryPacket(PacketType.RecievedDirectoryPacket , directory , fileList);
								
								oos.writeObject(dir);
							}
							else {
								Communication com = new Communication();
								
								com.sendFileToClient(folder, false);
								System.out.println("ShareIt: Server RequestDirectoryPacket else condition "+directory);
								int index=directory.lastIndexOf('\\');
								System.out.println(index);
								directory = directory.substring(0,index);
							    System.out.println("ShareIt: Server RequestDirectoryPacket else condition "+directory);
							    File lastFolder = new File(directory);
							    File[] files = lastFolder.listFiles();
								List<File> fileList = new ArrayList<File>(Arrays.asList(files));
								
								DirectoryPacket dir = new DirectoryPacket(PacketType.RecievedDirectoryPacket , directory , fileList);
								
								oos.writeObject(dir);
							}
							
						break;
					}
					
				}
			
			
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			
			// Send an invalid packet message to client
			
			e.printStackTrace();
		} 
	}

	public synchronized void stop() {
		end = true;
	}
	
	public Thread getThread() {
		return this.t;
	}
	
}
