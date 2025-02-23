package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

//public class Client {
//	SocketChannel socket;
//	InetSocketAddress address;
//	public Client(SocketChannel socketChannel) throws IOException {
//		this.socket = socketChannel;
//		this.address = (InetSocketAddress)socketChannel.getRemoteAddress();
//	}
//	public SocketChannel getSocket() {
//        return socket;
//    }
//	public String getAddress() throws IOException {
//		return socket.getRemoteAddress().toString();
//	}
//
//}
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
