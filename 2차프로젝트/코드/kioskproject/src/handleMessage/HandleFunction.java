package handleMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import dto.RequestDto;

public class HandleFunction {
	String message;
	public String submit(RequestDto reDto) {
		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open();
			System.out.println("[연결요청]");
	        socketChannel.connect(new InetSocketAddress("10.2.16.14",50001));
	        System.out.println("[연결성공]");
			
	        ByteBuffer byteBuffer = null;
	        Charset charset = Charset.forName("UTF-8");

	        //서버로 데이터를 보냅니다...
	   
	        String request = reDto.requestBuild().toString();
	        System.out.println("보낼 데이터: " + request);
	        if (request == null || request.isEmpty()) {
	            throw new IOException("전송할 요청 데이터가 없습니다.");
	        }
	        byteBuffer = charset.encode(reDto.requestBuild().toString());
	        socketChannel.write(byteBuffer);

	        //서버가 보낸 데이터 받기
	        byteBuffer = ByteBuffer.allocate(200000);
	        int byteNum = socketChannel.read(byteBuffer);
	        if(byteNum == -1){
	            throw new IOException("서버와의 연결이 끊어졌습니다.");
	        }else if (byteNum == 0) {
	            throw new IOException("서버에서 받은 데이터가 없습니다.");
	        }
	        byteBuffer.flip();
	        String message = charset.decode(byteBuffer).toString();
	        System.out.println("받은 데이터: " + message);
	        this.message = message;
		}catch(Exception e) {
			System.err.println("오류 발생: " + e.getMessage());
			e.printStackTrace();
		}finally {
			System.out.println("연결을 끊습니다..");
	        try {
	            socketChannel.close();
	        }catch (Exception e){
	        	System.err.println("오류 발생: " + e.getMessage());
	            e.printStackTrace();
	        }
		}
		return message;
	}
}
