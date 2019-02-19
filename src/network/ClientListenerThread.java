package network;

import java.net.*;

import exceptions.ShareItException;
import main.Session;

import java.io.*;

public class ClientListenerThread implements Runnable{

	
	private String name;
	private Thread t;
	public volatile boolean end = false;
	private Client client;
	
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
		
		while(true) {
			if(end) {
				break;
			}
			try {
				ObjectInputStream ois = new ObjectInputStream(this.client.getSocket().getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(this.client.getSocket().getOutputStream());
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
								
								//Code to read File
								byte [] buffer = new byte[Session.getSendBuffer()];
								FileOutputStream fos = new FileOutputStream(new File(Session.getPath() + fp.getFile().getName() ));
								Integer bytesRead = 0;
								Object o;
						        do {
						            o = ois.readObject();
						 
						            if (!(o instanceof Integer)) {
						                throw new ShareItException("Something is wrong"); // create own exception
						            }
						 
						            bytesRead = (Integer)o;
						 
						            o = ois.readObject();
						 
						            if (!(o instanceof byte[])) {
						                throw new ShareItException("Something is wrong"); // create own exception
						            }
						 
						            buffer = (byte[])o;
						 
						            // 3. Write data to output file.
						            fos.write(buffer, 0, bytesRead);
						           
						        } while (bytesRead == Session.getSendBuffer());
						         
						        System.out.println("File transfer success");
						         
						        fos.close();
						 
						        ois.close();
						        oos.close();
								
								
							}
							else {
								
								FilePacket filePacket = new FilePacket(PacketType.FileSendRejectPacket , fp.getFile());
								
								oos.writeObject(filePacket);
								
							}
							
						break;
					}
					
				}
			
			
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				
				// Send an invalid packet message to client
				
				e.printStackTrace();
			} catch (ShareItException e) {
				//Send invalid packet message to client
				
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
