package Model;

public class Position {
	private int x;
	private int y;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

//	constructor
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * change the position based on the direction and distance
	 * @param direction
	 */
	public void change(Dir direction, int distance) {
			x += direction.getX(distance);
			y += direction.getY(distance);
	}


	/**
	 *  determine whether two positions have a certain diagdistance 
	 * @param pos the position need to compare 
	 * @param distance for example, (0,0) and (1,1) has distance 1
	 * @return true if it has certain distance, else false
	 */
	public boolean checkDistance(Position pos, int distance) {
		if((Math.abs(x - pos.getX()) == distance ) && (Math.abs(y - pos.getY()) == distance )) {
			return true;
		}else {
			return false;
		}
	}
	
	//for examle this 0,0 that 1,1 Dir: top-right
	public Dir checkDirection(Position pos, int distance) {
		int disX = pos.getX() - x;
		int disY = pos.getY() - y;

		return Dir.getDirByPos(disX, disY, distance);
	}
	
	/**
	 * get the mid position of two position
	 * @param pos
	 * @return
	 */
	public Position mid(Position pos) {
		return new Position((x +pos.getX()) /2 , (y +pos.getX()) /2);

	}
	
	@Override
	public String toString() {
		return "(" + x +" , " +y+")";
	}
	
	
}
