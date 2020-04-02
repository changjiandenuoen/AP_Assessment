
import java.util.ArrayList;


public abstract class Piece {
	
	protected Player owner;
	protected boolean selected;
	protected Position position;
	protected ArrayList<Dir> dirList;
	
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
	public ArrayList<Dir> getDirList() {
		return dirList =getAllDir();
	}
	public void setDirList(ArrayList<Dir> dirList) {
		this.dirList = dirList;
	}
	public abstract String getType();

	//	Constructor
	public Piece(int x, int y, Player p){
		this.position = new Position(x, y);
		this.selected = false;
		this.owner = p;
		

	}
	public Piece(Position pos, Player p) {
		this.position = pos;
		this.selected = false;
		this.owner = p;
	}
	
	/**
	 * check whether the piece can move to the certain direction
	 * @param direction
	 * @return
	 */
	public abstract boolean move(Dir direction);
	
	/**
	 * get all the possible direction that the piece can move to 
	 * @return a direciton arrayList
	 */
	protected abstract ArrayList<Dir> getAllDir();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "  " + position;
	}
	
}
