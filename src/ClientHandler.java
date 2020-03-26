
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ClientHandler extends Thread{
	
	private String ip;
	private int port = 9999;
	private int id;
	private Player player;
	private Socket s = null;
	private OutputStream os = null;
	private BufferedOutputStream bos = null;
	private ObjectOutputStream oos = null;
	
	public String getIp() {
		return ip;
	}

	public ClientHandler(String ip, int port, int id) {
		this.ip = ip;
		this.port = port;
		this.id = id;
	}
	
	
	@Override
	public void run() {

		try {
			s = new Socket(ip,port); // IP Address and port
			player = new Player(s, id);
			
			System.out.println(player);
			View view = new View(player);
			SwingUtilities.invokeLater(view);
			
			while(true) {
				if(this.isInterrupted()) {
					break;
				}
				
				if(s.getInputStream() != null) {
					Command cmd= (Command) player.recieveFromServer();
					new SwingUpdater(view, view.getOppoPlayer(), cmd).execute();
					
				}
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
