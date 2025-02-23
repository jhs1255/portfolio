package socket2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;

public class Server implements Initializable {

	@FXML private ToggleButton runBtn;
	@FXML private TextArea textArea;
	private static ExecutorService threadPool;
	private ServerSocketChannel serverSocketChannel;
	private Selector selector; //동시접속 스레드를 서버에서 관리하기 위해 선언
	public static Vector<Client> clients = new Vector<>();
	boolean isRunning = false; //서버상태 플래그
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (runBtn != null) {
			String IP = "127.0.0.1"; //서버에서 사용할 아이피 0.0.0.0으로 설정해서 테스트
			int port = 50001; //서버에서 사용할 포트
			
			runBtn.setOnAction(event->{
				if(runBtn.getText().equals("서버열기")) {
					startServer(IP,port);
					Platform.runLater(()->{
						String message = String.format("[서버 시작] IP=%s, Port=%d]\n",IP, port);
						textArea.appendText(message);
						runBtn.setText("종료하기");
					});
				}
				else {
					stopServer();
					Platform.runLater(()->{
						String message = String.format("[서버 종료] IP=%s, Port=%d]\n",IP, port);
						textArea.appendText(message);
						runBtn.setText("서버열기");
					});
				}
			});
	    } else {
	        System.err.println("runBtn is null.");
	    }

	}//initialize
	
	public void startServer(String IP, int port) {
        isRunning = true; // 서버가 실행 중임을 표시
        try {
            selector = Selector.open(); // 선택자 생성
            serverSocketChannel = ServerSocketChannel.open(); // 서버 소켓 채널 생성
            serverSocketChannel.bind(new InetSocketAddress(IP, port)); // IP와 포트에 바인딩
            serverSocketChannel.setOption(java.net.StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.configureBlocking(false); // 비블로킹 모드로 설정
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 선택자에 등록

            threadPool = Executors.newCachedThreadPool(); // 캐시 가능한 스레드 풀 생성
            threadPool.submit(() -> { // 스레드 풀에 서버 작업 제출
                while (isRunning) { // 서버가 실행 중일 동안
                    try {
						selector.select();
						Iterator<SelectionKey> keys = selector.selectedKeys().iterator(); // 선택된 키 반복자 생성
	                    while (keys.hasNext()) { // 선택된 키가 있을 동안
	                        SelectionKey key = keys.next(); // 다음 선택된 키 가져오기
	                        keys.remove(); // 현재 키를 선택된 키 목록에서 제거
	                        if (key.isAcceptable()) { // 클라이언트 접속 요청이 있을 때
	                            // 새로운 클라이언트 접속 처리
	                            SocketChannel socketChannel = serverSocketChannel.accept(); // 클라이언트 소켓 채널 수락
	                            socketChannel.configureBlocking(false); // 비블로킹 모드로 설정
	                            socketChannel.register(selector, SelectionKey.OP_READ); // 선택자에 등록
	                            Client newClient = new Client(socketChannel); // 새로운 클라이언트 객체 생성
	                            clients.add(newClient); // 클라이언트 목록에 추가
	                            Platform.runLater(() -> {
									try {
										textArea.appendText("키오스크 연결됨: " + socketChannel.getRemoteAddress() + "\n");
									} catch (IOException e) {
										e.printStackTrace();
									}
								}); // UI에 연결 메시지 추가
	                            System.out.println("[키오스크 접속] " + socketChannel.getRemoteAddress() + ": " + Thread.currentThread().getName()); // 콘솔에 접속 메시지 출력

	                            // 클라이언트 처리 스레드 생성
	                            Runnable clientHandler = new ClientHandler(socketChannel, newClient); // 클라이언트 핸들러 생성
	                            threadPool.submit(clientHandler); // 클라이언트 핸들러를 스레드 풀에 제출
	                        } else if (key.isReadable()) { // 클라이언트가 데이터를 보낼 수 있을 때
	                            // 읽을 수 있는 클라이언트 처리
	                            // 이 부분은 ClientHandler로 분리할 수도 있습니다.
	                        }
	                    }
					} catch (IOException e) {
						e.printStackTrace();
					} // 선택자에서 이벤트를 기다림
                    
                }
            });
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
        }
    }

    public void stopServer() {
        isRunning = false; // 서버 실행 중지
        try {
            if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close(); // 서버 소켓 채널 닫기
            }
            if (selector != null && selector.isOpen()) {
                selector.close(); // 선택자 닫기
            }
            if (threadPool != null && !threadPool.isShutdown()) {
                threadPool.shutdown(); // 스레드 풀 종료
            }
        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
        }
    }

    private class ClientHandler implements Runnable { // 클라이언트 처리 클래스
        private SocketChannel socketChannel; // 클라이언트 소켓 채널
        private Client client; // 클라이언트 객체

        public ClientHandler(SocketChannel socketChannel, Client client) {
            this.socketChannel = socketChannel; // 소켓 채널 초기화
            this.client = client; // 클라이언트 객체 초기화
        }

        @Override
        public void run() { // Runnable 인터페이스의 run 메서드 구현
            try {
                ByteBuffer buffer = ByteBuffer.allocate(256); // 데이터 전송을 위한 버퍼 생성
                while (socketChannel.isOpen()) { // 소켓 채널이 열려있는 동안
                    int bytesRead = socketChannel.read(buffer); // 데이터 읽기
                    if (bytesRead == -1) { // 읽은 바이트 수가 -1이면 연결 종료
                        throw new Exception("키오스크 접속이 끊겼습니다"); // 예외 발생
                    }
                    // 추가적인 데이터 처리 로직 구현
                    buffer.clear(); // 다음 읽기를 위해 버퍼를 비움
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
					try {
						textArea.appendText("키오스크 종료됨: " + socketChannel.getRemoteAddress() + "\n");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}); // UI에 종료 메시지 추가
                clients.removeIf(c -> c.getSocket() == socketChannel); // 연결된 클라이언트 목록에서 제거
                try {
                    socketChannel.close(); // 소켓 채널 닫기
                } catch (Exception ex) {
                    ex.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
                }
            }
        }
    }
	
}
