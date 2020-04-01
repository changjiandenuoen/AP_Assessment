import java.io.Serializable;

public class Command implements Serializable{
	private Position oriPos;
	private Position targetPos;
	private boolean isKillingSpree;
	private CommandType type;
	private int clientType;
	
	
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
	public CommandType getType() {
		return type;
	}
	public void setType(CommandType type) {
		this.type = type;
	}
	public int getClientType() {
		return clientType;
	}
	public void setClientType(int clientType) {
		this.clientType = clientType;
	}
	//constructor for move command
	public Command(Position oriPos, Position targetPos, boolean isKillingSpree) {
		this.type = CommandType.MOVE;
		this.oriPos = oriPos;
		this.targetPos = targetPos;
		this.isKillingSpree = isKillingSpree;
	}
	//constructor for lose command
	public Command(boolean winOrLose) {
		if(winOrLose) {
			type = CommandType.WIN;
		}else {
			type = CommandType.LOSE;
		}
	}
	
	@Override
	public String toString() {
		switch (type) {
		
		case MOVE:
			return " {" + oriPos + " to " + targetPos + ", " + isKillingSpree() +"} ";
		case WIN:
			return "command sender is the winner !";
		case LOSE:
			return "command sender is the loser !";
		default:
			return null;
		}
		
	}

}
