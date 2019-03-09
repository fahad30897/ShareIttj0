package network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.Session;

public class ServerListenerThread implements Runnable {
	
	private String name;
	private Thread t;
	public volatile boolean end = false;
	private Server server;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ServerListenerThread(String n, Server server) {
		this.name = n;
		this.server = server;
		
		t = new Thread(this ,name);
		System.out.println("ss: before server listener start" );
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
				if(this.server.getSocket().isClosed()) {
					break;
				}
				this.ois = new ObjectInputStream(this.server.getSocket().getInputStream());
				this.oos = new ObjectOutputStream(this.server.getSocket().getOutputStream());
				
				System.out.println("Shareit : Waiting for server to write");
				Object object = ois.readObject(); // wait for server to send;
				
				
				if(object instanceof Packet) {
					Packet p = (Packet) object;
					
					switch(p.getPacketType()) {
						case FileSendPermitPacket:
							System.out.println("server Listner: permit packet");
							FilePacket fp = (FilePacket) p;
							boolean hasPermission = Session.askForPermission(fp.getFile() , this.server);
							
							if(hasPermission) {
								
								FilePacket filePacket = new FilePacket(PacketType.FileSendAcceptPacket , fp.getFile());
								
								oos.writeObject(filePacket);
								System.out.println("ShareIt : client Recieving file" );

								//Code to read File
								
								FileOutputStream fos = new FileOutputStream(new File(Session.getPath() + "\\" + fp.getFile().getName() ));
							
								
								
								byte[] fileArr = fp.getFileArr();
								fos.write(fileArr, 0, fileArr.length);
						        System.out.println("ShareIt : server succecsffully transfer file");
						         
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
