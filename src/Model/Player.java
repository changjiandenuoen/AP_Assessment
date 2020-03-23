package Model;

public class Player {
	private int id;
	private boolean turn;
	private Piece selectPiece;
	
	public Player(int i) {
		this.id = i;
		if(id == 1) {
			turn = true;
		}else {
			turn = false;
		}
	}
//	getter and setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isTurn() {
		return turn;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	public Piece getSelectPiece() {
		return selectPiece;
	}
	public void setSelectPiece(Piece selectPiece) {
		this.selectPiece = selectPiece;
	}
	@Override
	public String toString() {
		return "Player " + id;
	}
	
	
	public void select(Piece piece) {
		unselect();
		this.selectPiece = piece;
		piece.setSelected(true);
	}
	
	public void unselect() {
		if(this.selectPiece != null) {
			this.selectPiece.setSelected(false);
		}
		this.selectPiece = null;
	}
	
	/**
	 * player click a certain tile and try to move the selected piece to that tile.
	 * this method check whether player can succeed.
	 * This method assume that player already selected a piece
	 * 
	 * @param tile the target tile
	 * @return
	 */
	public boolean tryMove(Tile tile) {
		Position selectPos = selectPiece.getPosition();
		Position targetPos = tile.getPosition();
		Dir direction = selectPos.checkDirection(targetPos, 1);
		
		if(direction == null) {
			return false;
		}
		
		if(selectPos.checkDistance(targetPos, 1)) {
			return selectPiece.move(direction);
			
		}
		
		return false;
	}
	

	public boolean tryKill(Tile tile) {
		Position selectPos = selectPiece.getPosition();
		Position targetPos = tile.getPosition();
		Dir direction = selectPos.checkDirection(targetPos, 2);
		
		if(direction == null) {
			return false;
		}
		
		if(selectPos.checkDistance(targetPos, 2)) {
			
			return selectPiece.kill(direction);
			
		}
		return true;
	}
	
	public boolean tryUpGrade(Tile tile) {
		if((tile.getPosition().getX() == 0 && id == 1) || (tile.getPosition().getX() == 7 && id == 2)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	
}
