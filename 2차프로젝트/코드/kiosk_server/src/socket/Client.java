package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class Client {
	private SocketChannel socket; // 클라이언트의 소켓
    private InetSocketAddress address;
    
 // 생성자: 소켓을 인자로 받아 주소 정보를 초기화
	public Client(SocketChannel socket2) throws IOException {
		 this.socket = socket2; // 소켓 초기화
		 this.address = (InetSocketAddress) socket.getRemoteAddress(); // 원격 소켓 주소 초기화
	}

	public SocketChannel getSocket() {
		return socket;
	}

	public void setSocket(SocketChannel socket) {
		this.socket = socket;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}
	// 클라이언트의 주소를 문자열 형태로 반환하는 메서드
    public String getAddress() throws IOException {
        return socket.getRemoteAddress().toString(); // 주소 반환
    }
}
