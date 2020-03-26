

public class Man extends Piece {
	
	public Man(int x, int y, Player p) {
		super(x, y, p);
	}
	public Man(Position pos, Player p) {
		super(pos, p);
	}
	
	@Override
	public String getType() {
		
		if(owner.getId() == 1) {
			
			return "P1MAN";
		}else if(owner.getId() == 2) {
			return "P2MAN";
		}else {
			return "null";
		}
	}
	
	@Override
	public boolean move(Dir direction) {
		
		if((owner.getId() == 1 && (direction == Dir.TOPLEFT || direction == Dir.TOPRIGHT))
				|| (owner.getId() == 2 && (direction == Dir.BOTLEFT || direction == Dir.BOTRIGHT))) {
			return true;
		}else {
			return false;
		}
	}

}
