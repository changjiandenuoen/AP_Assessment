import java.io.Serializable;

public class Command implements Serializable{
	private Position oriPos;
	private Position targetPos;
	private boolean isKillingSpree;
	
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
	public boolean isKillingSpree() {
		return isKillingSpree;
	}
	public void setKillingSpree(boolean isKillingSpree) {
		this.isKillingSpree = isKillingSpree;
	}
	
	//constructor
	public Command(Position oriPos, Position targetPos, boolean isKillingSpree) {
		this.oriPos = oriPos;
		this.targetPos = targetPos;
		this.isKillingSpree = isKillingSpree;
	}
	
	@Override
	public String toString() {
		return " {" + oriPos + " to " + targetPos + ", " + isKillingSpree() +"} ";
	}
}
