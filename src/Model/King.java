package Model;

public class King extends Piece{
	
	
	public King(int x, int y, Player p) {
		super(x, y, p);

	}

	@Override
	public boolean move(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean eat() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean replace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean destroy() {
		// TODO Auto-generated method stub
		return false;
	}
}
