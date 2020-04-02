
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Server extends Thread{
	
	public static void main(String[] args) {
		new Server().run();
	}

	private ServerSocket ss;
	private HashMap<Integer, Session> sessionMap;
	private final static int PORT = 9999;
	
	@Override
	public void run() {

		try {	
			
			ss=new ServerSocket(PORT);
			sessionMap = new HashMap<Integer, Session>();
			ArrayList<Socket> s1 = new ArrayList<Socket>();
			ArrayList<Socket> s2 = new ArrayList<Socket>();
			int sessionId = 0;
			OutputStream output = null;
			
			while(true) {
				s1.add(ss.accept());
				output = s1.get(sessionId).getOutputStream();
				output.write(1);

				s2.add(ss.accept());
				output = s2.get(sessionId).getOutputStream();
				output.write(2);
				
				Session session = new Session(s1.get(sessionId), s2.get(sessionId), sessionId);
				sessionMap.put(sessionId, new Session(s1.get(sessionId), s2.get(sessionId), sessionId));
				System.out.println("two client is connected to the server");
				System.out.println("A new session is start! session id is " + sessionId);
				session.start();
				sessionId++;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
