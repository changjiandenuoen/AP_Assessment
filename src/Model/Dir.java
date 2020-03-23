package Model;

public enum Dir {
	TOPLEFT(-1, -1), 
	TOPRIGHT(1, -1), 
	BOTLEFT(-1, 1), 
	BOTRIGHT(1, 1);
	
	private int x;
	private int y;
	private String name;
	
	private Dir(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//getters
	public int getX(int i) {
		return mulitpleNum(i, x);
	}
	public int getY(int i) {
		return mulitpleNum(i, y);
	}
	public String getName() {
		return name;
	}
	
	public static Dir getDirByPos(int x, int y, int distance) {
		if(distance <= 0) {
			return null;
		}

		if (x == -1*distance && y == - 1*distance) {
			return TOPLEFT;
		}
		if(x == 1*distance && y == - 1*distance) {
			return BOTLEFT;
		}
		
		if(x == -1*distance && y == 1*distance) {
			return TOPRIGHT;
		}
		if(x == 1*distance && y == 1*distance ) {
			return BOTRIGHT;
		}

		return null;
	}

	private int mulitpleNum(int mul, int i) {
		return mul*i;
	}
	
}
