package network;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import main.Session;

public class Communication {

	
	public void sendFileToServer(File file) throws IOException, ClassNotFoundException {
		System.out.println("ShareIt: in Communication sendFile to Server" + file.getAbsolutePath());
		if(file.exists()) {
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			try {
				System.out.println("ShareIt: in Communication in try " + Session.getServer().getSocket());
				oos = new ObjectOutputStream(Session.getServer().getSocket().getOutputStream());
				
				
				System.out.println("ShareIt: in Communication object stream created" + file.getAbsolutePath());
				
				byte[] fileArr = Files.readAllBytes(file.toPath());
				FilePacket filePacket = new FilePacket(PacketType.FileSendPermitPacket , file , fileArr);
				System.out.println("ShareIt : client sending file" + file.getAbsolutePath());
				oos.writeObject(filePacket);
				System.out.println("ShareIt : client waiting for permission" );
				InputStream is = Session.getServer().getSocket().getInputStream();
				
				ois = new ObjectInputStream(is);
				
				Object object = ois.readObject();
				
				if(object instanceof Packet) {
					Packet p = (Packet) object;
					
					if(p.getPacketType() == PacketType.FileSendAcceptPacket) {
						//File sent show user that file is accepted
						
				        System.out.println("ShareIt : Client File sent");
						
					}
					else if(p.getPacketType() == PacketType.FileSendRejectPacket) {
						// Show message to user that file was rejected
					}
				}
			}
			finally {
				//if(oos != null) oos.close();
	        	//if(ois != null) ois.close();
	        }
			
		}
		
	}
	
	public void sendFileToClient(File file) throws IOException, ClassNotFoundException {
		System.out.println("ShareIt: in Communication sendFile to Server" + file.getAbsolutePath());
		if(file.exists()) {
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			try {
				System.out.println("ShareIt: in Communication in try " + Session.getServer().getSocket());
				oos = new ObjectOutputStream(Session.getClient().getSocket().getOutputStream());
				InputStream is = Session.getClient().getSocket().getInputStream();
				
				System.out.println("ShareIt: in Communication object stream created" + file.getAbsolutePath());
				
				byte[] fileArr = Files.readAllBytes(file.toPath());
				FilePacket filePacket = new FilePacket(PacketType.FileSendPermitPacket , file , fileArr);
				System.out.println("ShareIt : client sending file" + file.getAbsolutePath());
				oos.writeObject(filePacket);
				System.out.println("ShareIt : client waiting for permission" );
				
				
				ois = new ObjectInputStream(is);
				
				Object object = ois.readObject();
				
				if(object instanceof Packet) {
					Packet p = (Packet) object;
					
					if(p.getPacketType() == PacketType.FileSendAcceptPacket) {
						//File sent show user that file is accepted
						
				        System.out.println("ShareIt : Client File sent");
						
					}
					else if(p.getPacketType() == PacketType.FileSendRejectPacket) {
						// Show message to user that file was rejected
					}
				}
			}
			finally {
				//if(oos != null) oos.close();
	        	//if(ois != null) ois.close();
	        }
			
		}
		
	}

}
