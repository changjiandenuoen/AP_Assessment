

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class View extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public static final int unit = 40;
	public static final int BoardWidth = 8;
	public static final int BoardHeight = 8;
	
	private Controller controller;
	private Resource resource;
	private Tile[][] board;
	private Tile[][] initialBoard;
	
	//player who control black
	private Player p1;
	//player who control white
	private Player p2;
	
	private JPanel boardPanel;
	private JLabel turnLabel;
	private JLabel 	selectedLabel;
	private JLabel 	gameInfoLabel;
	
	
	public Player getOppoPlayer() {
		if(p1.getSocket() == null) {
			return p1;
		}else{
			return p2;
		}
	}
	public JLabel getTurnLabel() {
		return turnLabel;
	}
	public void setTurnLabel(String turnLabel) {
		this.turnLabel.setText(turnLabel);
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
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public Tile[][] getBoard() {
		return board;
	}
	public void setBoard(Tile[][] board) {
		this.board = board;
	}
	public Tile[][] getInitialBoard() {
		return initialBoard;
	}

	//Constructor
	public View(Player p){
		
		createPlayer(p);
		controller = new Controller(p);
		controller.setView(this);
		resource = new Resource();
		this.setSize(19*unit,10*unit);
		this.setTitle("game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		//this panel is the panel for the whole game screen
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		setBorder(mainPanel, 1,1,1,1);
		
		//this panel seperate screen into two part : chess board and info display screen
		JPanel innerPanel = new JPanel();
		setGrid(innerPanel, 1, 2);
		
		//this panel display the board (left)
		boardPanel = new JPanel();
		setGrid(boardPanel, 8, 8);
		createBoardArea(boardPanel);
		boardPanel.setBorder(BorderFactory.createDashedBorder(null, 1, 0));
		
		//this panel display the infomations (right)
		JPanel OuterInfoPanel = new JPanel();
		setBorder(OuterInfoPanel, 1, 1, 1, 1);
		
		JPanel infoPanel = new JPanel();
		setGrid(infoPanel, 4,1);
		
		
		turnLabel = new JLabel("turn[0] : game start!");
		selectedLabel = new JLabel("nothing selected");
		gameInfoLabel = new JLabel("welcome to the checkers game!");
		infoPanel.add(turnLabel);
		infoPanel.add(selectedLabel);
		infoPanel.add(gameInfoLabel);
		
		JButton replayBtn = new JButton("REPLAY");
		infoPanel.add(replayBtn);
		
		OuterInfoPanel.add(infoPanel);
		
		innerPanel.add(boardPanel);
		innerPanel.add(OuterInfoPanel );
		mainPanel.add(innerPanel);
		this.add(mainPanel);
	}
	

	public void initInfo() {
		turnLabel.setText("turn[1] : p1");
		selectedLabel.setText("P1 select: man (1,0)"); 
		gameInfoLabel.setText("P1 move man (1,0) to (2,1)"); 
	}
	
	/**
	 * update the message from the console of the frame
	 */
	public void updateInfo(int numOfTurn, Player p, Piece selectedPiece, Position pos) {
		turnLabel.setText("turn[" + numOfTurn +"] : " + p + "'s Turn");
		selectedLabel.setText( p + " select: " + selectedPiece); 
		gameInfoLabel.setText( p + "P1 move " + selectedPiece + " to " + pos); 
	}
	
	public void updateInfo(Player p, Piece selectedPiece) {
		selectedLabel.setText( p + " select: " + selectedPiece); 
	}
	
	/**
	 * clean all the pieces on the board, return the initial states of the game
	 */
	public void replay() {
		createBoardArea(boardPanel);
		initInfo();
		repaint();
	}
	
	//Helper Methods
	
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
	
	private void setBorder(JPanel panel, int a, int b, int c, int d) {
		panel.setLayout(new BorderLayout());
		Border emptyBorder = BorderFactory.createEmptyBorder(1*unit, 1*unit, 1*unit, 1*unit);
		panel.setBorder(emptyBorder);
	}
	
	private void setGrid(JPanel panel, int row, int col) {
		panel.setLayout(new GridLayout(row, col));
	}
	
	/**
	 * create a empty chessBoard with black and white areas
	 * @param chessBoardPanel the panel which contain the chessboard
	 */
	private void createBoardArea(JPanel chessBoardPanel) {
		
		board = new Tile[8][8];
		
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
            	
            	
            	//initialize the pieces
                if(((i == 0 || i== 2)&& j %2 == 1)|| (i == 1&& j%2 == 0)) {
//            	if(((i == 2)&& j %2 == 1)) {
                	board[i][j] = new Tile(i,j,p2);
                	
                	board[i][j].occupy(board[i][j].getPiece());
                	 
                }
                
                if(((i == 5 || i== 7)&& j%2 == 0)|| (i == 6 && j%2 == 1) ){
//                if(((i == 5 )&& j%2 == 0) ){
                	board[i][j] = new Tile(i,j,p1);
                	board[i][j].occupy(board[i][j].getPiece());
                }
                
                if(board[i][j] == null) {
                	board[i][j] = new Tile(i,j);
                }
                
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
	 * after each valid move, game will check the whole board to see the situation
	 * 1. check whether the game is end
	 * 2. check whether the player can continuously kill other pieces
	 */
	public void boardCheck(Player p){
		boolean isMustKill = false;
		 
		if(p.getSocket() == null || p.getSelectPiece() == null) {
			return;
		}
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				if(board[i][j].getPiece() !=null && board[i][j].getPiece().getOwner() == p) {
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
	 * If player selected piece have chance to kill other piece
	 * then, return true, else return false;
	 * @param p
	 * @return
	 */
	public boolean multiKillCheck(Player p) {
		
		if(p.getSocket() == null || p.getSelectPiece() == null) {
			return false;
		}
		
		return mustKillCheck(getSelectedTile(p), p);
	}
	
	/**
	 * in the possible direction of active player from the current tile
	 * there are some piece from another player that must kill!
	 * find these possible tiles and add them into killList 
	 * 
	 * @param tile the tile where the player's selected piece from
	 * @param p the active player
	 * @return true if there is a mustKill, else return false
	 */
	public boolean mustKillCheck(Tile selectedTile, Player p) {
		
		boolean flag = false;
		
		//possibleDir List contain all possible direction the active player can move to
		//by using these direction, this method can check whether there is a piece that can kill
		ArrayList<Dir> possibleDir = selectedTile.getPiece().getDirList();
		
		//This is the tiles that player can move to 
		Tile possibleTile;
		
		//check all possible directions and find out all possibleTiles
		for (int i = 0; i <possibleDir.size(); i++) {

			possibleTile = getTileByDir(selectedTile, possibleDir.get(i), 2);
			Tile midTile;
			
			//if possibleTile ==null : do not have possibleDir in that direction
			if(possibleTile == null || possibleTile.isOccupied()) {
				continue;
			}else {
				
				midTile = getMidTile(selectedTile.getPiece(), possibleTile);
				
				if(midTile.isOccupied()) {
					if(midTile.getPiece().getOwner() != p) {
						System.out.println("possible kill move: " + possibleDir.get(i)+ " " +selectedTile.getPosition()+ " to "+ possibleTile.getPosition());
						flag = true;
					}	
				}
			}	
		}
		return flag;
	}

	
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
				boardCheck(player);
				System.out.println(player + " is mustKill? " + player.isMustKill());
				
			//if user click a tile that has no piece
			}else {
				
				if(player.getSelectPiece() != null ) {

					if(player.tryMove(clickedTile) && !player.isMustKill()) {
					
						movePiece(player, clickedTile);
	
					}else if(player.tryKill(clickedTile)) {
						
						killPiece(player, clickedTile);

					}	
				}	
			}
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
	 * the player will move the selected peice to the targetTile
	 * if the player is a active player, after each move they will send a command to server.
	 * @param p
	 * @param targetTile
	 */
	private void movePieceToTile(Player p, Tile targetTile) {
		//check whether player select the right Tile to move
		// when they must kill something
		
		Piece selectedPiece = p.getSelectPiece();	

		gameInfoLabel.setText(p + " move " + selectedPiece + " to " + targetTile.getPosition());
		cleanTile(selectedPiece.getPosition());
		selectedPiece.setPosition(targetTile.getPosition());
		targetTile.occupy(p.getSelectPiece());
		
		//after move, if the selectedPiece reach the end of board, upgrade it.
		if(p.tryUpGrade(targetTile)){
			upgradePiece(p, targetTile);
		}
	
	}
	
	public void movePiece(Player p, Tile targetTile) {
		Tile selectedTile = getSelectedTile(p);
		movePieceToTile(p, targetTile);
		if(p.getSocket() != null) {
			p.sendToServer(new Command(selectedTile.getPosition(), targetTile.getPosition(),p.isKillingSpree()));
		}
		p.unselect();
	}
	
	/**
	 * Player move to the targetTile and kill the piece inbetween two positon
	 * @param p
	 * @param targetTile
	 */
	public void killPiece(Player p, Tile targetTile) {
		Piece midPiece = getMidTile(p.getSelectPiece(), targetTile).getPiece();
		Tile selectedTile = getSelectedTile(p);
		movePieceToTile(p, targetTile);
		
		if(midPiece == null) {
			return;
		}
		
		if(midPiece.getOwner() != p) {
			movePieceToTile(p, targetTile);
			cleanTile(midPiece.getPosition());
		}
		
		if(p.isMustKill()) {
			p.setMustKill(false);
		}

		if(multiKillCheck(p)) {
			System.out.println(p + " is killing spree!");
			p.setMustKill(true);
			p.setKillingSpree(true);
		}else {
			p.setKillingSpree(false);
			p.unselect();
		}
		
		if(p.getSocket() != null) {
			p.sendToServer(new Command(selectedTile.getPosition(), targetTile.getPosition(),p.isKillingSpree()));
		}

	}
	
	/**
	 * player's selected Piece upgrade to King if they reach the top/bottom
	 * @param p
	 * @param targetTile
	 */
	public void upgradePiece(Player p, Tile targetTile) {
		gameInfoLabel.setText( p + " upgrade " + p.getSelectPiece() + " to King " + targetTile.getPosition());
		Piece newKing = new King(targetTile.getPosition(), p);
		p.setSelectPiece(newKing);
		targetTile.occupy(newKing);
	}
	
	/**
	 * remove the piece on the certain tile of the chessboard
	 * @param tile
	 */
	public void cleanTile(Position pos) {
		board[pos.getX()][pos.getY()].clean();
	}
	
	
	/**
	 * 
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
	
	
// the Below methods are main method and all method relatived to servers
	
	@Override
	public void run() {

		this.setVisible(true);
	}

}
