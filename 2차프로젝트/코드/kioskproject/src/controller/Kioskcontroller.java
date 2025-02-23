package controller;

import java.net.Socket;

import javafx.scene.Parent;
import server.Client;
//public class Kioskcontroller {
//    Parent parentFxml;
//    public SocketChannel socketChannel;
//    public servercontroller scon;
//    private String serverAddress;
//    private int port;
//    private Client client;
//
//    public Kioskcontroller(String serverAddress, int port, Parent parentFxml) {
//        this.parentFxml = parentFxml;
//        socketChannel = null;
//        this.serverAddress = serverAddress;
//        this.port = port;
//    }
//
//    public String getServerAddress() {
//        return serverAddress;
//    }
//
//    public void setServerAddress(String serverAddress) {
//        this.serverAddress = serverAddress;
//    }
//
//    public int getPort() {
//        return port;
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    public void connectServer() {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    socketChannel = SocketChannel.open();
//                    socketChannel.configureBlocking(false); // 논블로킹 모드 설정
//                    socketChannel.connect(new InetSocketAddress(getServerAddress(), getPort()));
//                    
//                    // 연결이 완료될 때까지 대기
//                    while (!socketChannel.finishConnect()) {
//                        // 연결 완료 대기 (필요에 따라 UI 스레드로 상태 표시 가능)
//                    }
//
//                    client = new Client(socketChannel);
//                    System.out.println("[서버 연결 성공] " + socketChannel.getRemoteAddress());
//                } catch (Exception e) {
//                    if (socketChannel != null && socketChannel.isOpen()) {
//                        closeServer();
//                    }
//                    System.err.println("[서버 접속 실패] " + e.getMessage());
//                    this.stop();
//                }
//            }
//        };
//        thread.start();
//    }
//
//    public void closeServer() {
//        try {
//            // 소켓이 열려있는 상태라면 
//            if (socketChannel != null && socketChannel.isOpen()) {
//                socketChannel.close();
//                System.out.println("[서버 연결 종료] " + client.getAddress() + ": " + Thread.currentThread().getName());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("[서버 종료 실패] " + e.getMessage());
//        }
//    }
//}
public class Kioskcontroller {
	Parent parentFxml;
	public Socket socket;
	public servercontroller scon;
	private String serverAddress;
	private int	port;
	private Client client;
	
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
