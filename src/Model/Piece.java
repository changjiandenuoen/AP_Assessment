package Model;

public abstract class Piece {
	
	protected Player owner;
	protected boolean selected;
	protected Position position;
	
	//getter and setter
	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public abstract String getType();

	//	Constructor
	public Piece(int x, int y, Player p){
		this.position = new Position(x, y);
		this.selected = false;
		this.owner = p;

	}
	
	
	public abstract boolean move(int x, int y);
	
	public abstract boolean eat();
	
	public abstract boolean replace();
	
	public abstract boolean destroy();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "  " + position;
	}
	
}
