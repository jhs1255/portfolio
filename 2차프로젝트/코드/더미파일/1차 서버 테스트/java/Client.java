package kioskdemo;

import java.net.InetSocketAddress;
import java.net.Socket;

//서버와 키오스크 사이에 작업할 내용을 넣는 부분 ex)DB연동, 서버 연결 및 종료를 감지하기 위해 필요
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
