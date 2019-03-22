package network;

import java.io.*;
import java.net.*;
import main.Session;

public class ServerListenerThread implements Runnable {
	
	private String name;
	private Thread t;
	public volatile boolean end = false;
	private Server server;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private InetAddress ip;
	
	public ServerListenerThread(String n, Server server, InetAddress ip) {
		this.name = n;
		this.server = server;
		this.ip = ip;
		t = new Thread(this ,name);
		System.out.println("ss: before server listener start" );
		t.setDaemon(true);
		t.start();
	}
	
	
	
	@Override
	public void run() {
		ServerSocket ss = null;
		Socket socket = null;
		try {
			
			
			
			
			
			System.out.println("ShareIt: in serverlistenerThread before while");
			while(true) {
				if(end) {
					break;
				}
//				if(this.server.getSocket().isClosed()) {
//					break;
//				}
				
				ss = new ServerSocket(Session.getClientPort() ,10 , this.ip);
				System.out.println("ShareIt: in serverlistenerThread before accept");
				socket = ss.accept();
				this.ois = new ObjectInputStream(socket.getInputStream());
				this.oos = new ObjectOutputStream(socket.getOutputStream());
				
				System.out.println("ShareIt: in serverlistenerThread before Streams");
				
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
						case GetFilePacket:
							System.out.println("server Listner: get file packet");
							FilePacket fp1 = (FilePacket) p;
								
							FilePacket filePacket = new FilePacket(PacketType.FileSendAcceptPacket , fp1.getFile());
							
							oos.writeObject(filePacket);
							System.out.println("ShareIt : client Recieving file" );

							//Code to read File
							
							FileOutputStream fos = new FileOutputStream(new File(Session.getPath() + "\\" + fp1.getFile().getName() ));
						
							
							
							byte[] fileArr = fp1.getFileArr();
							fos.write(fileArr, 0, fileArr.length);
					        System.out.println("ShareIt : server succecsffully transfer file");
					         
					        fos.close();
					 
//						        ois.close();
//						        oos.close();
//								
							
						
						break;
					}
					
				}
				socket.close();
				ss.close();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			
			// Send an invalid packet message to client
			
			e.printStackTrace();
		} 
		finally {
			try {
				if(ss!= null && !ss.isClosed())
				ss.close();
				if(socket != null && !socket.isClosed())
				socket.close();
			}
			catch(Exception e) {
				System.out.println("unexpected error");
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
