package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import client.dto.ReservationDTO;
import client.service.ReservationService;
import enumcode.RequestCode;
import server.container.Container;
import server.controller.MainController;

public class Server {
    private final MainController mainControllerManager;
    private final client.controller.MainController mainControllerClient;
    public Server(){
        this.mainControllerManager = Container.setMainController();
        this.mainControllerClient = client.container.Container.setMainController();
        InitServer();
    }

    public void InitServer() {
        try {
            //스레드 풀 생성합니다..
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            //ServerSocketChannel 열기
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //ServerSocketChannel 포트 바인딩
            serverSocketChannel.bind(new InetSocketAddress("10.2.16.14",50001));
            System.out.println("[서버 시작]");

            executorService.execute(() -> {
                try {
                    //지속적인 클라이언트의 연결 요청 수락
                    while (true) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        InetSocketAddress isa = (InetSocketAddress) socketChannel.getRemoteAddress();
                        System.out.println(isa.getHostName() + "연결 수락..");

                        //스레드풀 작업정리
                        executorService.execute(() -> {
                            //작업 스레드 이름 얻기
                            String threadName = Thread.currentThread().getName();
                            try {
                                Charset charset = Charset.forName("UTF-8");
                                //클라이언트가 보낸 데이터 받기

                                ByteBuffer byteBuffer = ByteBuffer.allocate(200000);
                                int byteNum = socketChannel.read(byteBuffer);
                                if (byteNum == -1) {
                                    throw new IOException();
                                }
                                byteBuffer.flip();
                                String message  = charset.decode(byteBuffer).toString();
                                System.out.println("[" + threadName + "]" +
                                         isa.getHostName() + " 데이터 받기: " + message);

                                //messageJson to RequestDto
                                ObjectMapper objectMapper = new ObjectMapper();
                                HashMap<String, Object> jsonMap = objectMapper.readValue(message, new TypeReference<HashMap<String, Object>>() {});
                                RequestCode requestCode = RequestCode.valueOf(jsonMap.get("requestCode").toString());
                                switch (requestCode) {
                                	case RequestCode.GET_PHONE_VAILD:
                                		System.out.println("입력한 전화번호: " + jsonMap.get("body").toString());
                                		String saveNum = jsonMap.get("body").toString();
                                		String returnsaveNum = mainControllerClient.getPhoneVaild(saveNum);
	                                    System.out.println("보낼 데이터: " + returnsaveNum);
	                                    byteBuffer = charset.encode(returnsaveNum);
	                                    socketChannel.write(byteBuffer);
                                		break;
                                		
                                	case RequestCode.GET_PRINTRESNUM_STRING: // 예매번호로 조회
                                		System.out.println("입력한 예매번호: " + jsonMap.get("body").toString());
                                		String resNum = jsonMap.get("body").toString();
                                		String returnResNum = mainControllerClient.getPrintNumInfo(resNum);
	                                    System.out.println("보낼 데이터: " + returnResNum);
	                                    byteBuffer = charset.encode(returnResNum);
	                                    socketChannel.write(byteBuffer);
                                		break;
                                		
                                	case RequestCode.GET_PRINTPHONE_STRING: //전화번호로 조회
                                		System.out.println("입력한 전화번호: " + jsonMap.get("body").toString());
                                		String phoneNum = jsonMap.get("body").toString();
                                		String returnInfo = mainControllerClient.getPrintInfo(phoneNum);
	                                    System.out.println("보낼 데이터: " + returnInfo);
	                                    byteBuffer = charset.encode(returnInfo);
	                                    socketChannel.write(byteBuffer);
                                		break;
                                		
	                                case RequestCode.GET_PHONENUMBER_STRING: //포인트 사용하기 위한 조회
	                                	System.out.println("입력한 전화번호: " + jsonMap.get("body").toString());
                                		String phoneN = jsonMap.get("body").toString();
                                		String returnMInfo = mainControllerClient.getMemberInfo(phoneN);
	                                    System.out.println("보낼 데이터: " + returnMInfo);
	                                    byteBuffer = charset.encode(returnMInfo);
	                                    socketChannel.write(byteBuffer);
                                		break;
	                                    
	                                case RequestCode.GET_PLAYINFO_TIME: //영화 상영정보 가져오기
	
	                                	System.out.println("원하는 날짜: " + jsonMap.get("body").toString());
	                                    String body = jsonMap.get("body").toString();
	                                    LocalDateTime dateTime;

	                                    // 날짜만 있는 경우 처리
	                                    if (body.length() == 10) { // "YYYY-MM-DD" 형식
	                                        LocalDate date = LocalDate.parse(body);
	                                        dateTime = date.atStartOfDay(); // 시작 시간으로 변환
	                                    } else {
	                                        // 날짜와 시간이 모두 있는 경우
	                                        dateTime = LocalDateTime.parse(body); 
	                                    }

	                                    String returnValue = mainControllerClient.getPlayInfo(dateTime);
	                                    System.out.println("보낼 데이터: " + returnValue);
	                                    byteBuffer = charset.encode(returnValue);
	                                    socketChannel.write(byteBuffer);
	                                    break;
                                        //로그인 요청을 할 경우
                                    case RequestCode.POST_LOGIN_IDANDPASS:

                                        HashMap<String, Object> admin = objectMapper.convertValue(jsonMap.get("body"), new TypeReference<HashMap<String,Object>>() {
                                        });
                                     // Null 체크 추가
                                        Object adminId = admin.get("admin");
                                        Object password = admin.get("password");
                                        
                                        if (adminId == null || password == null) {
                                            System.err.println("아이디 또는 비밀번호가 없습니다.");
                                            // 에러 메시지 반환
                                            byteBuffer = charset.encode("아이디 또는 비밀번호가 누락되었습니다.");
                                            socketChannel.write(byteBuffer);
                                            break;
                                        }
                                        String answer = mainControllerManager.login(adminId.toString(), password.toString());
                                        byteBuffer =charset.encode(answer);
                                        socketChannel.write(byteBuffer);
                                        break;
                                    default:
                                        System.err.println("알 수 없는 요청 코드: " + requestCode);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                //연결끊기...
                                try {
                                    socketChannel.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("클라이언트 연결 오류: " + e.getMessage());
                }finally {
                    try {
                        //serverSocketChannel닫기..
                        serverSocketChannel.close();
                        //스레드풀 종료..
                        executorService.shutdown();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            };
        });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
