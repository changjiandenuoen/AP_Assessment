package Model;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile extends JLabel{
	
	private Position postion;
	private Piece piece;
	
	//getter and setter
	public Position getPostion() {
		return postion;
	}
	public void setPostion(Position postion) {
		this.postion = postion;
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	//constructor
	public Tile(int x, int y, Player p){
		this.postion = new Position(x, y);
		this.piece = new Man(x, y, p);
//		this.setIcon();
	}
	
	
	public Tile(int x, int y) {
		this.postion = new Position(x, y);
	}
	
	/**
	 * check whether the tile is occupid
	 * @return true if yes, false if no
	 */
	public boolean isOccupied() {
		if(piece != null) return true;
		else return false;
	}
	
	/**
	 * clean the current occupied pieces on this tile
	 */
	public void clean() {
		this.piece = null;
		this.setIcon(null);
	}

	public void occupy(String path) {
		this.setIcon(new ImageIcon(path));
	}
	

}
