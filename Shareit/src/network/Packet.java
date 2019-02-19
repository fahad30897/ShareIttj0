package network;

import java.io.*;

public class Packet implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private PacketType packetType;
	private String message;
	
	public Packet(PacketType packetType , String message) {
		this.setPacketType(packetType);
		this.setMessage(message);
	}
	
	public Packet(PacketType packetType) {
		this(packetType , "");
	}
	
	
	public PacketType getPacketType() {
		return this.packetType;
	}
	
	public void setPacketType(PacketType p) {
		this.packetType = p;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String msg) {
		this.message = msg;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Packet) {
			Packet p = (Packet) o;
			return (p.getPacketType() == this.packetType && p.getMessage().equals(this.message)) ? true: false;
		}
		return false;
	}

}
