import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Session extends Thread{

	private int id;
	private boolean isGameEnd;
	private Socket s1;
	private Socket s2;
	
	public Session(Socket s1, Socket s2, int id) {
		this.s1 = s1;
		this.s2 = s2;
		this.id = id;
		this.isGameEnd = false;
	}

	@Override
	public void run() {
		while(true) {
			
			if(!isGameEnd) {
				transit(s1, s2);
			}else {
				System.out.println("session " + id +" is End! Winner is player 1");
				break;
			}
			
			if(!isGameEnd) {
				transit(s2, s1);
			}else {
				System.out.println("session " + id +" is End! Winner is player 2");
				break;
			}
		}
	}
	
	/**
	 * Server receive the object from s1, and send it to s2
	 * @param s1
	 * @param s2
	 */
	private void transit(Socket s1, Socket s2) {
		Command cmd;
		
		//check connection with socket

		if((cmd = receiveCommand(s1)) == null) { 
			return;
		}
		
		if(cmd.getType() == CommandType.LOSE || cmd.getType() == CommandType.WIN) {
			isGameEnd = true;
		}
		
		sendCommand(s2, cmd);
	}
	
	/**
	 * if one client is disconnection, then the session autometically end
	 * the winner is the player at another client !
	 * @param socket the socket who disconnect
	 * @return true means s1 disconnect and s2 win
	 */
//	private Boolean autoLose(Socket s1, Socket s2) {
//		if(s1.isClosed()) {
//			
//			//generate a lose type of command and send to another client
//			//indicates this player is lose
//			Command cmd = new Command(false);
//			sendCommand(s2, cmd);
//			isGameEnd = true;
//			try {
//				s2.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return true;
//		}else {
//			return false;
//		}
//	}
	
	
	private Command receiveCommand(Socket sender) {
		
		InputStream is = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		
		Command cmd = null;
		
		try {
			is = sender.getInputStream();
			bis = new BufferedInputStream(is);
			if(bis.available() >0) {
				ois = new ObjectInputStream(bis);
				cmd= (Command) ois.readObject();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			is = null;
			bis = null;
			ois = null;
		}
		return cmd;
	}
	
	private void sendCommand(Socket reciever, Command cmd) {

		OutputStream os = null;
		BufferedOutputStream bos = null;
		ObjectOutputStream oos = null;
		
		try {
			os = reciever.getOutputStream();
			bos = new BufferedOutputStream(os);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(cmd);
			oos.flush();
			
		} catch (IOException e) {
			try {
				reciever.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			os = null;
			bos = null;
			oos = null;
		}
	}
}
