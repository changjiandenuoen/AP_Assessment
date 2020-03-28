

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Controller extends MouseAdapter {
	
	private View view;
	private Player player;
	
	public Controller(Player p){
		this.player = p;
	}
	
	//getter and setter
	public void setView(View view) {
		this.view = view;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		//if its not your turn, mouse click will recieve nothing

		if(!player.isTurn()) {
			view.setGameInfoLabel("Please wait for other player move");
			System.out.println(player + " - " + player.isTurn());
			return;
		}
		//get the click position
		Tile clickedTile = null;
		
		//get the Tile user clicked
		if(e.getSource() instanceof Tile) {
			clickedTile = ((Tile) e.getSource());
		}

		view.turnMove(player, clickedTile.getPosition());

		

	}
}
