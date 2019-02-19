package network;

import java.io.*;
import java.util.Arrays;

import main.Session;

public class Communication {

	
public void sendFileToServer(File file) throws IOException, ClassNotFoundException {
	System.out.println("ShareIt: in Communication sendFile to Server" + file.getAbsolutePath());
		if(file.exists()) {
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			try {
				System.out.println("ShareIt: in Communication in try" + Session.getServer().getSocket());
				oos = new ObjectOutputStream(Session.getServer().getSocket().getOutputStream());
				System.out.println("ShareIt: in communication between object stream"+ Session.getServer().getSocket().getInputStream() );
				ois = new ObjectInputStream(Session.getServer().getSocket().getInputStream());
				System.out.println("ShareIt: in Communication object stream created" + file.getAbsolutePath());
				FilePacket filePacket = new FilePacket(PacketType.FileSendPermitPacket , file);
				System.out.println("ShareIt : client sending file" + file.getAbsolutePath());
				oos.writeObject(filePacket);
				System.out.println("ShareIt : client waiting for permission" );
				Object object = ois.readObject();
				System.out.println("ShareIt : client read object" + object );
				if(object instanceof Packet) {
					Packet p = (Packet) object;
					
					if(p.getPacketType() == PacketType.FileSendAcceptPacket) {
						//Code to send File
						
						FileInputStream fis = new FileInputStream(file);
				        byte [] buffer = new byte[Session.getSendBuffer()];
				        Integer bytesRead = 0;
				        
				        while ((bytesRead = fis.read(buffer)) > 0) {
				            oos.writeObject(bytesRead);
				            oos.writeObject(Arrays.copyOf(buffer, buffer.length));
				        }
				 
						
					}
					else if(p.getPacketType() == PacketType.FileSendRejectPacket) {
						// Show message to user
					}
				}
			}
			finally {
				if(oos != null) oos.close();
	        	if(ois != null) ois.close();
	        }
			
		}
		
	}

}
