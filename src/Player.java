
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Player {
	private int id;
	private boolean turn;
	private Piece selectPiece;
	private Socket socket;
	
	public Player(Socket socket, int i) {
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
	 * this method check whether player can succeed.
	 * This method assume that player already selected a piece
	 * 
	 * @param tile the target tile
	 * @return
	 */
	public boolean tryMove(Tile tile) {
		Position selectPos = selectPiece.getPosition();
		Position targetPos = tile.getPosition();
		Dir direction = selectPos.checkDirection(targetPos, 1);
		
		if(direction == null) {
			return false;
		}
		
		if(selectPos.checkDistance(targetPos, 1)) {
			return selectPiece.move(direction);
			
		}
		
		return false;
	}
	

	public boolean tryKill(Tile tile) {
		Position selectPos = selectPiece.getPosition();
		Position targetPos = tile.getPosition();
		Dir direction = selectPos.checkDirection(targetPos, 2);
		
		if(direction == null) {
			return false;
		}
		
		if(selectPos.checkDistance(targetPos, 2)) {
			
			return selectPiece.move(direction);
			
		}
		return true;
	}
	
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
	
	
	public void sendToServer(Position pos) {
		OutputStream os = null;
		ObjectOutputStream oos = null;
		
		try {
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(pos);
			oos.writeObject(null);
			oos.flush();
			System.out.println(this.toString() + " send postion "+ pos );
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			oos = null;
			os = null;
		}
	}

	public Position recieveFromServer() {
		InputStream is = null;
		ObjectInputStream ois = null;
		Position pos = null;
		try {
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			pos = (Position) ois.readObject();
			System.out.println(this.toString() + " recieve postion "+ pos );


		}catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			ois = null;
			is = null;
		}
		return pos;	
	}
	
	
	
}
