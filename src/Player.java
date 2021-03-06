
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Player {
	private int id;
	private boolean turn;
	private boolean isActive;
	//when player have to kill a peice, this will be true
	private boolean mustKill;
	//when player already kill a piece, and it can continue kill another one.
	private boolean killingSpree;
	private boolean isWin;
	private Piece selectPiece;
	private Socket socket;
	
	public Player(Socket socket, int i) {
		mustKill = false;
		killingSpree = false;
		this.socket = socket;
		this.id = i;
		this.turn = false;
	}
	
//	getter and setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isTurn() {
		return turn;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	public boolean isActive() {
		if(socket != null) {
			return true;
		}else {
			return false;
		}
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Piece getSelectPiece() {
		return selectPiece;
	}
	public void setSelectPiece(Piece selectPiece) {
		this.selectPiece = selectPiece;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public boolean isMustKill() {
		return mustKill;
	}
	public void setMustKill(boolean mustKill) {
		this.mustKill = mustKill;
	}
	public boolean isKillingSpree() {
		return killingSpree;
	}
	public void setKillingSpree(boolean killingSpree) {
		this.killingSpree = killingSpree;
	}
	public boolean isWin() {
		return isWin;
	}
	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}
	
	@Override
	public String toString() {
		return "Player " + id;
	}
	
	
	public void select(Piece piece) {
		unselect();
		this.selectPiece = piece;
		piece.setSelected(true);
	}
	
	public void unselect() {
		if(this.selectPiece != null) {
			this.selectPiece.setSelected(false);
		}
		this.selectPiece = null;
	}
	
	/**
	 * player click a certain tile and try to move the selected piece to that tile.
	 * This method check whether player can successfully move to the tile.
	 * This method assume that player already selected a piece
	 * 
	 * @param tile the target tile
	 * @param distance the distance between the piece you selected and the targetTile
	 * @return true if can move, else return false
	 */
	public boolean tryMove(Tile tile, int distance) {
		Position selectPos = selectPiece.getPosition();
		Position targetPos = tile.getPosition();
		Dir direction = selectPos.checkDirection(targetPos, distance);
		
		if(direction == null) {
			return false;
		}
		
		if(selectPos.checkDistance(targetPos, distance)) {
			return selectPiece.move(direction);
			
		}
		
		return false;
	}

	/**
	 * check whether the current tile allow piece to upgrade(man -> king)
	 * @param tile
	 * @return
	 */
	public boolean tryUpGrade(Tile tile) {
		if((tile.getPosition().getX() == 0 && id == 1) || (tile.getPosition().getX() == 7 && id == 2)) {
			return true;
		}else {
			return false;
		}
	}
	
	public void endTurn() {
		turn = !turn;
	}
	
	/**
	 * send command from this active player to the server
	 * @param cmd
	 */
	public void sendToServer(Command cmd) {
		if (socket == null) {
			return;
		}
		
		OutputStream os = null;
		BufferedOutputStream bos;
		ObjectOutputStream oos = null;
		
		try {
			os = socket.getOutputStream();
			bos = new BufferedOutputStream(os);
			oos = new ObjectOutputStream(bos);
			oos.writeObject(cmd);
			oos.writeObject(null);
			oos.flush();

			//if active player is killingspree(Multiple jumps), do not end turn.
			if(!killingSpree) {
				endTurn();
			}
			
		}catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally {
			oos = null;
			bos = null;
			os = null;
		}
	}

	/**
	 * The active player will recieve a command from server
	 * which can help the view update the chessboard
	 * @return
	 */
	public Command recieveFromServer() {
		InputStream is = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		Command cmd = null;
		try {
			is = socket.getInputStream();
			bis = new BufferedInputStream(is);
			if(bis.available() >0) {
				ois = new ObjectInputStream(bis);
				cmd= (Command) ois.readObject();
			}else {
				return null;
			}
			
			
			//if the other player is killing spree, then do not change turn.
			if(cmd.isKillingSpree()) {
				setTurn(false);
			}else {
				setTurn(true);
			}
			

		}catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			ois = null;
			bis = null;
			is = null;
		}
		return cmd;	
	}
}
