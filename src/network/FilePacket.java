package network;

import java.io.*;

public class FilePacket extends Packet {
	
	private File file;
	
	public FilePacket(PacketType p , File f) {
		super(p);
		this.setFile(f);
	}
	
	public FilePacket(PacketType p ) {
		this(p , null);
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void setFile(File f) {
		this.file = f;
	}

}
