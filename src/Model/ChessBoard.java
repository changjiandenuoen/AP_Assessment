package Model;

import java.io.Serializable;

//0 present tile without piece
//1 present tile with p1's man
//2 present tile with p1's king
//3 present tile with p2's man
//4 present tile with p2's king
public class ChessBoard implements Serializable{
	
	private int[][] board ;
	
	public ChessBoard(Tile[][] tileBoard) {
		board = new int[8][8];
		for (int i = 0; i < tileBoard.length; i++) {
			for (int j = 0; j < tileBoard[i].length; j++) {
				if(!tileBoard[i][j].isOccupied() ) {
					board[i][j] =0;
					continue;
				}else {
					switch (tileBoard[i][j].getPiece().getType()) {
					case "P1MAN":
						board[i][j] = 1;
						break;
					case "P1KING":
						board[i][j] = 2;
						break;
					case "P2MAN":
						board[i][j] = 3;
						
						break;
					case "P2KING":
						board[i][j] = 4;
						break;

					default:
						break;
					}
				}
				
				
			}
		}
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				str += board[i][j] + " ";
			}
			str += "\n";	
		}
		return str;
	}

}
