package server;

import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
	Socket socket;
	InetSocketAddress address;
	public Client(Socket socket) {
		this.socket = socket;
		this.address = (InetSocketAddress)socket.getRemoteSocketAddress();
	}
	public Socket getSocket() {
        return socket;
    }
	public String getAddress() {
		return socket.getRemoteSocketAddress().toString();
	}

}
