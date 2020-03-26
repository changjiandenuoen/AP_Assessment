//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.Socket;
//
//public class GameLoop implements Runnable{
//	
//	private boolean isEnd;
//	private Socket s1;
//	private Socket s2;
//	private int turnNum;
//	Player p1;
//	Player p2;
//	
//	
//	public GameLoop(Socket s1, Socket s2) {
//		isEnd = false;
//		this.s1 = s1;
//		this.s2 = s2;
//		p1 = new Player(s1, 1);
//		p2 = new Player(s2, 2);
//		turnNum = 0;
//	}
//}
