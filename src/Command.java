import java.io.Serializable;

public class Command implements Serializable{
	private Position oriPos;
	private Position targetPos;
	
	//getter and setter
	public Position getOriPos() {
		return oriPos;
	}
	public void setOriPos(Position oriPos) {
		this.oriPos = oriPos;
	}
	public Position getTargetPos() {
		return targetPos;
	}
	public void setTargetPos(Position targetPos) {
		this.targetPos = targetPos;
	}

	//constructor
	public Command(Position oriPos, Position targetPos) {
		this.oriPos = oriPos;
		this.targetPos = targetPos;
	}
	
	@Override
	public String toString() {
		return " {" + oriPos + " to " + targetPos + "} ";
	}
}
