import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A session is a game with a peer of players
 * when someone win the game, the session will end.
 */
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
			//This is the game loop, it is quite simple
			//s1 indicates player 1 and s2 indicates player 2
			
			if(!isGameEnd) {
				transit(s1, s2);
			}else {
				System.out.println("session" + id + "end! Winner is player 2!");
				break;
			}
			
			if(!isGameEnd) {
				transit(s2, s1);
			}else {
				System.out.println("session" + id + "end! Winner is player 1!");
				break;
			}
		}
	}
	
	/**
	 * Server receives the command from s1, and send it to s2
	 * if the command is LOSE or WIN type, set isGameEnd to be true
	 * @param s1
	 * @param s2
	 */
	private void transit(Socket s1, Socket s2) {
		Command cmd;
		
		//check whether the cmd is null
		if((cmd = receiveCommand(s1)) == null) { 
			return;
		}else {
			System.out.println("server recieve a " + cmd.getType() + " command from" + s1 );
			sendCommand(s2, cmd);
		}
		
		//check whether server recieve a LOSE/WIN type command
		if(cmd.getType() == CommandType.LOSE || cmd.getType() == CommandType.WIN) {
			isGameEnd = true;
		}
	}
	
	/**
	 * recieve commands from sender socket
	 * @param sender
	 * @return
	 */
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
	
	/**
	 * send command to the reciever socket
	 * @param reciever
	 * @param cmd
	 */
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
		} finally {
			os = null;
			bos = null;
			oos = null;
		}
	}
}
