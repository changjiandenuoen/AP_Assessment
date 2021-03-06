

import java.io.IOException;
import java.net.Socket;
import javax.swing.SwingUtilities;

public class Client extends Thread{
	
	public static void main(String[] args) {
		new Client().run();
	}

	
	private final int port = 9999;
	private int id;
	private Player player;
	private Socket s = null;
	
	@Override
	public void run() {

		try {
			s = new Socket("127.0.0.1",port); 
			id = s.getInputStream().read();
			player = new Player(s, id);
			GameScreen game = new GameScreen(player);
			game.setServerLabel(player + "successfully connect to server" );
			SwingUtilities.invokeLater(game);
			new SwingUpdater(game, player, s).execute();
			
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
