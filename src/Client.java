

import java.io.IOException;
import java.net.Socket;
import javax.swing.SwingUtilities;

public class Client extends Thread{
	
	public static void main(String[] args) {
		new Client(9999).run();
	}

	
	private int port = 9999;
	private int id;
	private Player player;
	private Socket s = null;

	public Client(int port) {
		this.port = port;
	}
	
	
	@Override
	public void run() {

		try {
			s = new Socket("127.0.0.1",port); 
			id = s.getInputStream().read();
			player = new Player(s, id);
			System.out.println(player + "successfully connect to server" );
			
			View view = new View(player);
			SwingUtilities.invokeLater(view);
			new SwingUpdater(view, player, s).execute();
			
			while(true) {}

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
