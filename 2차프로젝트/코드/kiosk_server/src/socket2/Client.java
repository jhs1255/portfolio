package socket2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class Client {
	SocketChannel socket;
	InetSocketAddress address;
	
	public Client(SocketChannel socketChannel) throws IOException {
		this.socket = socketChannel;
		this.address = (InetSocketAddress)socketChannel.getRemoteAddress();
	}
	
	public SocketChannel getSocket() {
        return socket;
    }
	
	public String getAddress() throws IOException {
		return socket.getRemoteAddress().toString();
	}

}
