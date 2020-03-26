
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{
	static Player p1;
	static Player p2;
	private static ServerSocket ss;
	private static Socket s1;
	private static Socket s2;
	private static int PORT = 9999;
	
	public static void main(String[] args) {
		new Server().run();
	}
	
	@Override
	public void run() {
		try {	
			
			ss=new ServerSocket(PORT);
			
			//this socket is sent from AppMain1
			s1 = ss.accept();
			p1 = new Player(s1, 1);
			System.out.println("recieve connection from " + p1);
			
			//this socket is sent from AppMain2
			s2 = ss.accept();
			p2 = new Player(s2, 2);
			System.out.println("recieve connection from" + p2);

			System.out.println("Now we have two players, game start!");
			while(true) {
				if(s1.isClosed() || s2.isClosed()) {
					
					break;
				}
				
				transit(s1, s2);
				transit(s2, s1);

			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				s1.close();
				s2.close();
				ss.close();
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Server receive the object from s1, and send it to s2
	 * @param s1
	 * @param s2
	 */
	private static void transit(Socket s1, Socket s2) {
		InputStream is = null;
		OutputStream os = null;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		
		Command cmd;
		
		try {
		
			is = s1.getInputStream();
			bis = new BufferedInputStream(is);
			if(bis.available() >0) {
				ois = new ObjectInputStream(bis);
				cmd= (Command) ois.readObject();
				System.out.println("recieve and send COMMAND " + cmd + " from client");
	
			}else {
				return;
			}
			
			os = s2.getOutputStream();
			bos = new BufferedOutputStream(os);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(cmd);
			oos.flush();
		} catch (EOFException e) {

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			is = null;
			os = null;
			bis = null;
			bos = null;
			ois = null;
			oos = null;
		}
	}
	
}
