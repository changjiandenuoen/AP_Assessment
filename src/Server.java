
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingUtilities;

public class Server extends Thread{
	
	public static void main(String[] args) {
		Server.getInstance().run();
	}

	private ServerScreen screen;
	private ServerSocket ss;
	private HashMap<Integer, Session> sessionMap;
	private final static int PORT = 9999;
	
	//private constructor
	private Server() {
		this.screen = new ServerScreen();
		SwingUtilities.invokeLater(screen);
	}
	
	//static inner class
	private static class ServerInstance{
		private final static Server serverInstance = new Server();
	}
	
	//a public getter for Singleton Server
	public static Server getInstance() {
		return ServerInstance.serverInstance;
	}
	
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
				
				Session session = new Session(screen, s1.get(sessionId), s2.get(sessionId), sessionId);
				sessionMap.put(sessionId, session);
				screen.getServerConsole().append("\n"+"two client is connected to the server");
				screen.getServerConsole().append("\n"+"A new session is start! session id is " + sessionId);
				session.execute();
				sessionId++;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
