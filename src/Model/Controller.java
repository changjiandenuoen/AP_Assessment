package Model;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Controller extends MouseAdapter {
	
	private Client client;
	private Player player;
	
	public Controller(Player p){
		this.player = p;
	}
	
	//getter and setter
	public void setClient(Client client) {
		this.client = client;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		//get the click position
		Tile clickedTile = null;
		Piece clickedPiece = null;
		Position clickedPosition = null;
		
		//get the Tile user clicked
		if(e.getSource() instanceof Tile) {
			clickedTile = ((Tile) e.getSource());
			clickedPiece = clickedTile.getPiece();
			clickedPosition = clickedTile.getPosition();
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
				
			}else {
				System.out.println("where r u click?");
			}
			
		//if user click a tile that has no piece
		}else {
			if(player.getSelectPiece() != null) {
				//if a normal move is successful, move the selected peice to the clickedTile
				if(player.tryMove(clickedTile)) {
					Piece selectedPiece = player.getSelectPiece();	
					client.cleanTile(selectedPiece.getPosition());
					selectedPiece.setPosition(clickedPosition);
					clickedTile.occupy(player.getSelectPiece());
					player.unselect();
					
				//if a kill move is successful, move the selected peice to the clicked Tile
				// and kill the piece in the middle.
				}else if(player.tryKill(clickedTile)) {
					
					
				//if a upgrade move is successful, upgrade the selected piece of the player.
				}else if(player.tryUpGrade(clickedTile)){
					
				}
			}

		}
	if(player.getSelectPiece() != null) {
		client.getSelectedLabel().setText(player.getSelectPiece().toString());
	}else {
		client.getSelectedLabel().setText("please select a piece!");
	}
	client.repaint();
	}


}
