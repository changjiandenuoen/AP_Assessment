import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.SwingWorker;

/**
 * A session is a game with a peer of players
 * when someone win the game, the session will end.
 */
public class Session extends SwingWorker<Void, Void>{

	private ServerScreen screen;
	private int id;
	private boolean isGameEnd;
	private Socket s1;
	private Socket s2;
	private int winner;
	
	public Session(ServerScreen screen, Socket s1, Socket s2, int id) {
		this.screen = screen;
		this.s1 = s1;
		this.s2 = s2;
		this.id = id;
		this.isGameEnd = false;
	}
	
	/**
	 * Server receives the command from s1, and send it to s2
	 * if the command is LOSE or WIN type, set isGameEnd to be true
	 * and set winner varible which allows ServerScreen to print the result of the session
	 * @param s1
	 * @param s2
	 */
	private void transit(Socket s1, Socket s2) {
		Command cmd;
		
		//check whether the cmd is null
		if((cmd = receiveCommand(s1)) == null) { 
			return;
		}else {
			sendCommand(s2, cmd);
		}
		
		//check whether server recieve a LOSE/WIN type command
		if(cmd.getType() == CommandType.LOSE) {
			if(s1 == this.s1) {
				winner = 2;
			}else {
				winner = 1;
			}
			isGameEnd = true;
		}
		
		if( cmd.getType() == CommandType.WIN) {
			if(s1 == this.s1) {
				winner = 1;
			}else {
				winner = 2;
			}
			winner = 1;
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

	@Override
	protected Void doInBackground() throws Exception {
		while(true) {
			//This is the game loop, it is quite simple
			//s1 indicates player 1 and s2 indicates player 2
			if(!isGameEnd) {
				transit(s1, s2);
			}else {
				break;
			}
			
			if(!isGameEnd) {
				transit(s2, s1);
			}else {
				break;
			}
		}
		return null;
	}
	
	@Override
	protected void done() {
		//while loop is done, means the session is end, print the result.
		screen.getServerConsole().append("\n" +"session " + id + " is end, Winner is Player " +winner+" !!");
	}
}
