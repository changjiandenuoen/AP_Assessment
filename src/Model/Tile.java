package Model;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile extends JLabel{
	
	private Position position;
	private Piece piece;
	private Resource resource;
	
	//getter and setter
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	//constructor
	public Tile(int x, int y, Player p){
		this.position = new Position(x, y);
		this.piece = new Man(x, y, p);
		this.resource = new Resource();
	}
	
	
	public Tile(int x, int y) {
		this.position = new Position(x, y);
		this.resource = new Resource();
	}
	
	/**
	 * check whether the tile is occupid
	 * @return true if yes, false if no
	 */
	public boolean isOccupied() {
		if(piece == null) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * clean the current occupied pieces on this tile
	 */
	public void clean() {
		this.piece = null;
		this.setIcon(null);
	}

	/**
	 * occupy this tile by a piece (draw a piece on a tile)
	 * @param p the piece that occupy the tile
	 */
	public void occupy(Piece p) {
		this.piece = p;

		drawPiece(p, "P1MAN");
		drawPiece(p, "P1KING");
		drawPiece(p, "P2MAN");
		drawPiece(p, "P2KING");

	}
	
	/**
	 * check the type of piece, and draw the piece on this tile
	 * Notice: p's type and pieceType  is not the same, then will not draw it.
	 * @param p
	 * @param pieceType
	 */
	private void drawPiece(Piece p, String pieceType) {

		if(p.getType().equals(pieceType)) {
			System.out.println(resource);
			this.setIcon(new ImageIcon(resource.getPathMap().get(pieceType)));
		}	
	}


}
