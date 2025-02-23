package kioskdemo;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;

public class servercontroller implements Initializable {
	
	@FXML private ToggleButton runBtn; 
	@FXML private TextArea textArea;
	
	public static ExecutorService threadPool;
	ServerSocket serverSocket;
	
	public static Vector<Client> clients = new Vector<Client>();
	boolean isRunning = false;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		if (runBtn != null) {
			String IP = "127.0.0.1";
			int port = 50001;
			
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
	}
	
	//서버 시작
	public void startServer(String IP, int port) {
		isRunning = true;
		try {
			serverSocket = new ServerSocket(port);
			//serverSocket.bind(new InetSocketAddress(IP, port));
		}catch(Exception e) {
			e.printStackTrace();
		}
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while(isRunning) {
					try {
						// 새로운 클라이언트가 접속할 수 있도록.
						Socket socket = serverSocket.accept();
						// 클라이언트가 접속을 했다면, 클라이언트 배열에 새롭게 접속한 클라이언트를 추가.
						Client newClient = new Client(socket);
						clients.add(newClient);
						//화면에 출력
						Platform.runLater(() -> textArea.appendText("키오스크 연결됨: " +socket.getRemoteSocketAddress() + "\n"));
						System.out.println("[키오스크 접속] "	+ socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName());
						
						Runnable clientclose = new Runnable() {
							@Override
							public void run() {
								try {
									while(!socket.isClosed()) {
										int data=socket.getInputStream().read();
										if(data == -1) {
											throw new Exception("키오스크 접속이 끊겼습니다");
										}
									}
								}catch(Exception e){
									Platform.runLater(() -> textArea.appendText("키오스크 종료됨: " +socket.getRemoteSocketAddress() + "\n"));
									System.out.println("[키오스크 종료] "	+ socket.getRemoteSocketAddress() + ": " + Thread.currentThread().getName());
									clients.remove(newClient);
									try {
										socket.close();
									}catch(Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						};
						threadPool.submit(clientclose);
						
					} catch (Exception e) {
						//오류가 발생했다면 서버를 작동중지시키고 break로 빠져나온다.
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
				
			}
		};
		
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}
	
	//서버 종료
	public void stopServer() {
		isRunning = false;
		try {
	        if (serverSocket != null && !serverSocket.isClosed()) {
	            serverSocket.close();
	        }
	     // 쓰레드 풀 종료하기
	        if(threadPool != null && !threadPool.isShutdown()) {
	     		threadPool.shutdown();
	     	}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
