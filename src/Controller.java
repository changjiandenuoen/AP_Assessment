
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * Handle the interface between user input and GUI
 */
public class Controller extends ControllerAdapter {
	
	private GameScreen game;
	private Player player;
	
	public Controller(Player p){
		this.player = p;
	}
	
	//getter and setter
	public void setGameScreen(GameScreen game) {
		this.game = game;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		//if its not your turn, mouse click will recieve nothing
		if(!player.isTurn()) {
			game.setGameInfoLabel("Please wait for other player move");
			System.out.println(player + " - " + player.isTurn());
			return;
		}
		
		//get the click position
		Tile clickedTile = null;
		
		//get the Tile user clicked
		if(e.getSource() instanceof Tile) {
			clickedTile = ((Tile) e.getSource());
		}
		
		//move it
		game.turnMove(player, clickedTile.getPosition());
	}
	
	
	/**
	 * when user close the Jframe during the game
	 * user will autometically lose the game as it will send a lose command to server
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		
			super.windowClosing(e);
			player.sendToServer(new Command(false));
			
	}
	
}
