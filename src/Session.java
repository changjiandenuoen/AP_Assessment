import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Session extends Thread{
	//num of turn
	private int id;
	private int turn;
	private boolean isGameEnd;
	Socket s1;
	Socket s2;
	
	public Session(Socket s1, Socket s2, int id) {
		this.s1 = s1;
		this.s2 = s2;
		this.id = id;
		this.isGameEnd = false;
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while(true) {
			
			if(!isGameEnd) {
				transit(s1, s2);
			}else {
				System.out.println("session " + id +"is End! Winner is player 1");
				break;
			}
			
			if(!isGameEnd) {
				transit(s2, s1);
			}else {
				System.out.println("session " + id +"is End! Winner is player 2");
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
//		if(!autoLose(s1,s2)) {
//			if((cmd = receiveCommand(s1)) == null) return;
//			sendCommand(s2, cmd);
//		}else {
//			return;
//		}
		if((cmd = receiveCommand(s1)) == null) return;
		sendCommand(s2, cmd);
	}
	
	public Boolean isClientClose(Socket socket){ 
		   try{ 
			   	socket.sendUrgentData(0xFF);
		    	return false; 
		   }catch(Exception e){ 
			   return true; 
		   } 
	} 
	
//	/**
//	 * if one client is disconnection, then the session autometically end
//	 * the winner is the player at another client !
//	 * @param socket the socket who disconnect
//	 * @return true means s1 disconnect and s2 win
//	 */
//	private Boolean autoLose(Socket s1, Socket s2) {
//		if(isClientClose(s1)) {
//			
//			//generate a disconnection type of command and send to another client
//			Command cmd = new Command(false);
//			sendCommand(s2, cmd);
//			isGameEnd = true;
//			return true;
//		}else {
//			return false;
//		}
//	}
//	
	
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
			e.printStackTrace();
		}finally {
			os = null;
			bos = null;
			oos = null;
		}
	}
	
}
