

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class GameScreen extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public static final int unit = 40;
	public static final int boardWidth = 8;
	public static final int boardHeight = 8;
	
	private Controller controller;
	private Tile[][] board;
	
	//player who control black
	private Player p1;
	//player who control white
	private Player p2;
	
	//the chessBoard and three labels
	private JPanel boardPanel;
	private JLabel serverLabel;
	private JLabel 	selectedLabel;
	private JLabel 	gameInfoLabel;
	
	//getter and setters
	public Player getActivePlayer() {
		if(p1.isActive()) {
			return p1;
		}else{
			return p2;
		}
	}
	public Player getOppoPlayer() {
		if(!p1.isActive()) {
			return p1;
		}else{
			return p2;
		}
	}
	public JLabel getServerLabel() {
		return serverLabel;
	}
	public void setServerLabel(String serverLabel) {
		this.serverLabel.setText(serverLabel);
	}
	public JLabel getSelectedLabel() {
		return selectedLabel;
	}
	public void setSelectedLabel(String selectedLabel) {
		this.selectedLabel.setText(selectedLabel);
	}
	public JLabel getGameInfoLabel() {
		return gameInfoLabel;
	}
	public void setGameInfoLabel(String gameInfoLabel) {
		this.gameInfoLabel.setText(gameInfoLabel);
	}
	public Controller getController() {
		return controller;
	}
	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Tile[][] getBoard() {
		return board;
	}
	public void setBoard(Tile[][] board) {
		this.board = board;
	}

	//Constructor
	public GameScreen(Player p){

		//create a player with socket(active player) 
		//and a player without socket
		createPlayer(p);
		controller = new Controller(p);
		controller.setGameScreen(this);
		this.setSize(19*unit,10*unit);
		this.setTitle(p.toString());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.addWindowListener(controller);
		this.setResizable(false);
		
		//this panel is the panel for the whole game screen
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		setBorder(mainPanel, 1,1,1,1);
		
		//this panel seperate screen into two part : chess board and info display screen
		JPanel innerPanel = new JPanel();
		setGrid(innerPanel, 1, 2);
		
		//this panel display the board (left)
		boardPanel = new JPanel();
		setGrid(boardPanel, boardHeight, boardWidth);
		createBoardArea(boardPanel);
		boardPanel.setBorder(BorderFactory.createDashedBorder(null, 1, 0));
		
		//this panel display the infomations (right)
		JPanel OuterInfoPanel = new JPanel();
		setBorder(OuterInfoPanel, 1, 1, 1, 1);
		
		JPanel infoPanel = new JPanel();
		setGrid(infoPanel, 3,1);
		
		serverLabel = new JLabel("Server now start a new game!");
		selectedLabel = new JLabel("Please select a piece!");
		gameInfoLabel = new JLabel("welcome to the game! ");
		infoPanel.add(serverLabel);
		infoPanel.add(selectedLabel);
		infoPanel.add(gameInfoLabel);
		
		OuterInfoPanel.add(infoPanel);
		
		innerPanel.add(boardPanel);
		innerPanel.add(OuterInfoPanel );
		mainPanel.add(innerPanel);
		this.add(mainPanel);
	}
	
	@Override
	public void run() {
		this.setVisible(true);
	}
	
	/**
	 * turnMove method handle the whole process when player click the mouse on a tile
	 * @param player
	 * @param targetPos
	 */
	public void turnMove(Player player, Position targetPos) {
		Tile clickedTile = board[targetPos.getX()][targetPos.getY()];
		Piece clickedPiece = clickedTile.getPiece();

		
		//if user click a tile that has a piece
			if(clickedTile.isOccupied()) {
				if(!clickedPiece.isSelected() && clickedPiece.getOwner() == player) {
					
					//if the piece in the tile is not selected and player do not select any piece
					if(clickedPiece != null ) {
						player.unselect();	
					}
					player.select(clickedPiece);		
				
				// if user click the piece that he already selected, unselect the piece
				}else if(player.getSelectPiece() == clickedPiece ){
					player.unselect();
				}
		
				//check whether the whole board contian a mustKill?
				//if so, set mustKill to true
				boardMustKillCheck(player);
				
			//if user click a tile that has no piece
			}else {
				
				if(player.getSelectPiece() != null ) {
					
					//if player can move piece, move piece
					if(player.tryMove(clickedTile, 1) && !player.isMustKill()) {
						movePiece(player, clickedTile);
						
					//if player can kill, kill piece
					}else if(player.tryMove(clickedTile, 2)) {
						killPiece(player, clickedTile);

					}	
				}	
			}
			
		//after the click, show the selected piece on the selectedLabel
		if(player.getSelectPiece() != null) {
			getSelectedLabel().setText(player.getSelectPiece().toString());
		}else {
			setSelectedLabel("please select a piece!");
		}
		
		repaint();
	}
	
	/**
	 * when the active player recieve command from server
	 * the local p2 will update its move based on the command
	 * 
	 * the process: 
	 * 1. remote p2 send command to p1
	 * 2. p1 recieve the command from remote p2
	 * 3. p1 update the local p2 by using the command
	 * 
	 * @param player
	 * @param oriPos
	 * @param targetPos
	 */
	public void turnMove(Player player, Position oriPos, Position targetPos) {
		Tile selectedTile = board[oriPos.getX()][oriPos.getY()];
		player.select(selectedTile.getPiece());
		turnMove(player, targetPos);
	}

	/**
	 *  end game based on the cmdType of recieving command
	 *  For example, if recieving a lose type command from other player, 
	 *  the current player win the game
	 *  
	 * @param p the active player
	 * @param cmdType the type of command recieved
	 */ 
	public void gameEnd(Player p, CommandType cmdType) {
		if(cmdType == CommandType.WIN) {
			getOppoPlayer().setWin(true);
			setServerLabel("This session is end!");
			setSelectedLabel("Oh no!");
			setGameInfoLabel("You Lose! Game Over!");
		}
		
		if(cmdType == CommandType.LOSE) {
			p.setWin(true);
			setServerLabel("This session is end!");
			setSelectedLabel("Congruaduations!");
			setGameInfoLabel("You win!");
		}
		
		removeAllMouseListenerOnBoard();
	}
	
	
	/**
	 * if a piece and a tile has diag-distance 2, then return the midTile.
	 * @param p1 the piece you choose
	 * @param t2 the tile you click
	 * @return
	 */
	public Tile getMidTile(Piece p1, Tile t2) {
		int x1 = p1.getPosition().getX();
		int y1 = p1.getPosition().getY();
		int x2 = t2.getPosition().getX();
		int y2 = t2.getPosition().getY();
		if(Math.abs(x1-x2) == 2 && Math.abs(y1-y2) == 2) {
			return board[ (x1 + x2) /2][(y1 + y2)/2];
		}else {
			return null;
		}
	}
	
	/**
	 * based on the given tile, return the tile in certain direction with distance n
	 * for example, if given tile is (0,0), direction is BOTRIGHT, distance is 1,
	 * then return tile at (1,1)
	 * 
	 * @param tile
	 * @param direction
	 * @param Distance
	 * @return
	 */
	public Tile getTileByDir(Tile tile, Dir direction, int distance) {
		
		Position targetPos = tile.getPosition().getByDir(direction, distance);
		if(!targetPos.isOutOfBound()) {
			return board[targetPos.getX()][targetPos.getY()];
		}else {
			return null;
		}
	}
	
	/**
	 * get the tile where player selected piece is.
	 * @param p
	 * @return
	 */
	public Tile getSelectedTile(Player p) {
		if(p.getSelectPiece() == null) {
			return null;
		}else {
			Position selectedPos = p.getSelectPiece().getPosition();
			return board[selectedPos.getX()][selectedPos.getY()];			
		}
	}
	
	/**
	 * remove the piece on the certain tile of the chessboard
	 * @param tile
	 */
	public void cleanTile(Position pos) {
		board[pos.getX()][pos.getY()].clean();
	}
	
	//-----------------Helper Method below ---------------
	
	/**
	 * In a local game, there is a active player and a inactive player
	 * This method create both players and initialize the turn
	 * @param p
	 */
	private void createPlayer(Player p) {
		if(p.getId() == 1) {
			p1 = p;
			p2 = new Player(null, 2);
			
		}else if (p.getId() == 2){
			p2 = p;
			p1 = new Player(null, 1);
		}
		
		p1.setTurn(true);
		p2.setTurn(false);
	}
	
	/**
	 * a easier way to set border
	 * @param panel
	 * @param n north
	 * @param e east
	 * @param s south
	 * @param w west
	 */
	private void setBorder(JPanel panel, int n, int e, int s, int w) {
		panel.setLayout(new BorderLayout());
		Border emptyBorder = BorderFactory.createEmptyBorder(1*unit, 1*unit, 1*unit, 1*unit);
		panel.setBorder(emptyBorder);
	}
	
	/**
	 * A eaiser way to set grid
	 * @param panel
	 * @param row
	 * @param col
	 */
	private void setGrid(JPanel panel, int row, int col) {
		panel.setLayout(new GridLayout(row, col));
	}
	
	/**
	 * create a initial chess board for the game
	 * 
	 * @param chessBoardPanel the panel which contain the chessboard
	 */
	private void createBoardArea(JPanel chessBoardPanel) {
		
		board = new Tile[boardWidth][boardHeight];
		
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
            	
            	
            	//initialize the white pieces
                if(((i == 0 || i== 2)&& j %2 == 1)|| (i == 1&& j%2 == 0)) {
                	board[i][j] = new Tile(i,j,p2);
                	
                	board[i][j].occupy(board[i][j].getPiece());
                	 
                }
                
                //initialize the black pieces
                if(((i == 5 || i== 7)&& j%2 == 0)|| (i == 6 && j%2 == 1) ){
                	board[i][j] = new Tile(i,j,p1);
                	board[i][j].occupy(board[i][j].getPiece());
                }
                
                //initialize the tile with no pieces
                if(board[i][j] == null) {
                	board[i][j] = new Tile(i,j);
                }
                
                //add mouseListeners
                board[i][j].addMouseListener(controller);
                board[i][j].setOpaque(true);
                
                //render the board with wood color
                if((i+j)%2==0) {
                	board[i][j].setBackground(new Color(230,200,180));

                }else if((i+j)%2 ==1){
                	board[i][j].setBackground( new Color(140,90,41));
                }
                
                //add Label to the grid
                chessBoardPanel.add(board[i][j]);     
 
            }
        }
	}

	/**
	 * the player will move the selected piece to the targetTile
	 * this method will used by movePiece and killPiece method
	 * @param player
	 * @param targetTile
	 */
	private void movePieceToTile(Player p, Tile targetTile) {
		//check whether player select the right Tile to move
		// when they must kill something
		
		Piece selectedPiece = p.getSelectPiece();	
		
		cleanTile(selectedPiece.getPosition());
		selectedPiece.setPosition(targetTile.getPosition());
		targetTile.occupy(p.getSelectPiece());
		
		//after move, if the selectedPiece reach the end of board, upgrade it.
		if(p.tryUpGrade(targetTile)){
			upgradePiece(p, targetTile);
		}
	
	}
	
	/**
	 * Player move to the targetTile, and then send move command to server
	 * @param p
	 * @param targetTile
	 */
	private void movePiece(Player p, Tile targetTile) {
		Tile selectedTile = getSelectedTile(p);
		movePieceToTile(p, targetTile);
		
		if(boardWinCheck(p)) {
			p.setWin(true);
		}
		gameInfoLabel.setText(p + " move " + selectedTile.getPosition() + " to " + targetTile.getPosition());
		p.sendToServer(new Command(selectedTile.getPosition(), targetTile.getPosition(),p.isKillingSpree()));
		p.unselect();
	}
	
	/**
	 * Player move to the targetTile and kill the piece inbetween ori tile and target tile
	 * after killing the piece, this method will check wether player is winner/ mustKill/ multi-kill states
	 * @param p
	 * @param targetTile
	 */
	private void killPiece(Player p, Tile targetTile) {
		Piece midPiece = getMidTile(p.getSelectPiece(), targetTile).getPiece();
		Tile selectedTile = getSelectedTile(p);
		
		if(midPiece == null) {
			return;
			
		//if can kill, kill it.
		}else {
			movePieceToTile(p, targetTile);
			
			if(midPiece.getOwner() != p) {
				movePieceToTile(p, targetTile);
				cleanTile(midPiece.getPosition());
			}
		}
		
		//after kill a piece, check the whole board to see any pieces left
		//if the oppo-player do not have pieces, then player win!
		if(boardWinCheck(p)) {
			p.setWin(true);
		}
		
		//if player is mustKill status, set it to false.
		if(p.isMustKill()) {
			p.setMustKill(false);
		}
		
		//if player can continue the killin, set Mustkill and killingspree to true
		//also, do not unselect the piece, improve the user experience.
		if(multiKillCheck(p)) {
			setGameInfoLabel(p + " is killing spree!");
			p.setMustKill(true);
			p.setKillingSpree(true);
		}else {
			p.setKillingSpree(false);
			p.unselect();
		}
		gameInfoLabel.setText(p + " move " + selectedTile.getPosition() + " to " + targetTile.getPosition());
		
		//player must send the kill to the server
		//if player is killing spree, game will not switch the turn.
		Command cmd = new Command(selectedTile.getPosition(), targetTile.getPosition(),p.isKillingSpree());
		if(p.isWin()) {
			if(p.isActive()) {
				cmd.setType(CommandType.WIN);
				gameEnd(p, CommandType.LOSE);
			}else {
				gameEnd(p, CommandType.WIN);
			}
		}
		p.sendToServer(cmd);

	}
	
	
	/**
	 * player's selected Piece upgrade to King if they reach the top/bottom
	 * @param p
	 * @param targetTile
	 */
	private void upgradePiece(Player p, Tile targetTile) {
		gameInfoLabel.setText( p + " upgrade " + p.getSelectPiece() + " to King " + targetTile.getPosition());
		Piece newKing = new King(targetTile.getPosition(), p);
		p.setSelectPiece(newKing);
		targetTile.occupy(newKing);
	}
	
	
	/**
	 * check the whole board and see is there any piece that can be killed
	 * if so, set player to be "must Kill" status
	 * @param p
	 */
	private void boardMustKillCheck(Player p){
		boolean isMustKill = false;
		 
		if(!p.isActive() || p.getSelectPiece() == null) {
			return;
		}
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(board[i][j].isOccupied() && board[i][j].getPiece().getOwner() == p) {
					if(mustKillCheck(board[i][j], p)) {
						isMustKill = true;
					}	
				}
			}
		}
		
		if(isMustKill) {
			p.setMustKill(true);
		}
			
	}

	/**
	 * If player selected piece and have chance to kill other piece
	 * then, return true, else return false;
	 * @param p
	 * @return
	 */
	private boolean multiKillCheck(Player p) {
		
		if(p.getSelectPiece() == null) {
			return false;
		}
		
		return mustKillCheck(getSelectedTile(p), p);
	}
	
	/**
	 * check whether the piece at the selectedTile have chance to kill other piece
	 * by searching all the possible move directions of this piece. 
	 * @param tile the tile where the player's selected piece from
	 * @param p the active player
	 * @return true if there is a mustKill, else return false
	 */
	private boolean mustKillCheck(Tile selectedTile, Player p) {
		
		boolean flag = false;
		
		//possibleDir List contain all possible direction the active player can move to
		//by using these direction, this method can check whether there is a piece that can kill
		ArrayList<Dir> possibleDir = selectedTile.getPiece().getDirList();
		
		//This is the tiles that player can move to 
		Tile possibleTile;
		
		//check all possible directions and find out all possibleTiles
		for (int i = 0; i <possibleDir.size(); i++) {
			
			//find the possible Tile that piece can move to kill.
			possibleTile = getTileByDir(selectedTile, possibleDir.get(i), 2);
			Tile midTile;
			
			//if possibleTile ==null : do not have possibleDir in that direction
			if(possibleTile == null || possibleTile.isOccupied()) {
				continue;
			}else {
				
				midTile = getMidTile(selectedTile.getPiece(), possibleTile);
				
				if(midTile.isOccupied()) {
					if(midTile.getPiece().getOwner() != p) {
						setGameInfoLabel("possible kill move: " + possibleDir.get(i)+ " " +selectedTile.getPosition()+ " to "+ possibleTile.getPosition());
						flag = true;
					}	
				}
			}	
		}
		return flag;
	}
	
	/**
	 * check the whole board and see whether player p is win
	 * if another player do not have any peice on the board, p will win for sure
	 * @return
	 */
	private boolean boardWinCheck(Player p) {
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				//If board contain p2's pieces
				if(board[i][j].isOccupied() && board[i][j].getPiece().getOwner() != p) {
					return false;
				}	
			}
		}

		return true;
	}
	
	/**
	 * helper method: remove all mouse Listener from the chess board
	 */
	private void removeAllMouseListenerOnBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
            	board[i][j].removeMouseListener(controller);
            }
        }
	}
}