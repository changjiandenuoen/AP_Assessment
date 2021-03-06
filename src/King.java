import java.util.ArrayList;

public class King extends Piece{
	
	
	public King(int x, int y, Player p) {
		super(x, y, p);
	}
	public King(Position pos, Player p) {
		super(pos, p);
	}
	
	@Override
	public String getType() {
		if(owner.getId() == 1) {
			return "P1KING";
		}else if(owner.getId() == 2) {
			return "P2KING";
		}
		return "";
	}

	@Override
	public boolean move(Dir direction) {
		if(direction == Dir.TOPLEFT || direction == Dir.TOPRIGHT || direction == Dir.BOTLEFT || direction == Dir.BOTRIGHT) {
			return true;
		}else {
			return false;
		}
	}
	
	protected ArrayList<Dir> getAllDir(){
		ArrayList<Dir> dirList = new ArrayList<Dir>();
		dirList.add(Dir.TOPLEFT);
		dirList.add(Dir.TOPRIGHT);
		dirList.add(Dir.BOTLEFT);
		dirList.add(Dir.BOTRIGHT);
		return dirList;
	}

}
