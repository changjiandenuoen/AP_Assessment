

import java.io.Serializable;

/**
 * After player move/kill a piece, they will send a Position to the server
 * And server will send this position to another client.
 * Another Client then use this position to call the relative method to update their view.
 */

public class Position implements Serializable{

	private static final long serialVersionUID = 1L;
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
	 * get a postion based on a given position, direction, and distance.
	 * @param direction
	 * @param distance
	 * @return
	 */
	public Position getByDir(Dir direction, int distance) {
		Position pos = new Position(x, y);
		pos.change(direction, distance);
		return pos;
	}

		/**
		 * check whether the x and y is out of bounds
		 * @return
		 */
	public boolean isOutOfBound() {
		if(x > GameScreen.boardHeight - 1 || y > GameScreen.boardWidth - 1 || x < 0 || y < 0) {
			return true;
		}else {
			return false;
		}
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

	/**
	 * Check the direction by calculating the axis of two postion
	 * for examle this pos is 0,0 and that pos is 1,1 
	 * then the direction is: bottom-right
	 * 
	 * @param pos
	 * @param distance
	 * @return
	 */
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
