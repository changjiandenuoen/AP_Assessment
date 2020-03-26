

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingWorker;


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
		Piece clickedPiece = null;
		
		//get the Tile user clicked
		if(e.getSource() instanceof Tile) {
			clickedTile = ((Tile) e.getSource());
			clickedPiece = clickedTile.getPiece();
		}
		
		//if user click a tile that has a piece
		if(clickedTile.isOccupied()) {
			if(!clickedPiece.isSelected() && clickedPiece.getOwner() == player) {
				
				//if the piece in the tile is not selected and player do not select any piece
				if(clickedPiece != null ) {
					player.unselect();	
				}
				player.select(clickedPiece);		
			
			// if user click the piece that he already selected, unselect the piece
			}else if(player.getSelectPiece() == clickedPiece ){
				player.unselect();
			}
			
		//if user click a tile that has no piece
		}else {
			if(player.getSelectPiece() != null) {
				//if a normal move is successful, move the selected peice to the clickedTile
				if(player.tryMove(clickedTile)) {
					view.movePiece(player, clickedTile);

					
				}else if(player.tryKill(clickedTile)) {
					view.killPiece(player, clickedTile);

				}	
				//if a upgrade move is successful, upgrade the selected piece of the player.
				if(player.tryUpGrade(clickedTile)){
					view.upgradePiece(player, clickedTile);
				}
				
			}
			
			player.unselect();
			
		}
	if(player.getSelectPiece() != null) {
		view.getSelectedLabel().setText(player.getSelectPiece().toString());
	}else {
		view.setSelectedLabel("please select a piece!");
	}
	view.repaint();
	}
}
