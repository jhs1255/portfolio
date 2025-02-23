package kioskdemo;

import java.net.Socket;
import javafx.scene.Parent;

public class Kioskcontroller {
	Parent parentFxml;
	public Socket socket;
	public servercontroller scon;
	private String serverAddress;
	private int	port;
	private Client client;
	
	//서버와 연결하기 위하여 kiosk에서 IP주소와 포트번호, fxml의 정보를 가져온다.
	public Kioskcontroller(String serverAddress, int port, Parent parentFxml) {
		this.parentFxml = parentFxml;
		socket = null;
		this.serverAddress = serverAddress;
		this.port = port;
	}

	
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	//서버 연결하는 부분
	public void connectServer() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket(getServerAddress(), getPort());
					client = new Client(socket);
					System.out.println("[서버 연결 성공] " + socket.getRemoteSocketAddress());
				} catch (Exception e) {
					if(!socket.isClosed()) {
						//stopClient 메소드를 호출해서 클라이언트를 종료
						closeServer();
						System.err.println("[서버 접속 실패]"+e.getMessage());
						//프로그램 자체를 종료시킨다.
						this.stop();
					}
				}
			}
		};
		thread.start();
	}
	
	//서버 연결 끊는 부분
	public void closeServer() {
		try {
			// 소켓이 열려있는 상태라면 
			if(socket != null && !socket.isClosed()) {
				socket.close();
				System.out.println("[서버 연결 종료]"+client.getAddress()+ ": " + Thread.currentThread().getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[서버 종료 실패]"+e.getMessage());
		}
	}
}
