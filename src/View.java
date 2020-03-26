

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

public class View extends JFrame implements Runnable{
	
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
//                if(((i == 0 || i== 2)&& j %2 == 1)|| (i == 1&& j%2 == 0)) {
            	if(((i == 2)&& j %2 == 1)) {
                	board[i][j] = new Tile(i,j,p2);
                	
                	board[i][j].occupy(board[i][j].getPiece());
                	 
                }
                
//                if(((i == 5 || i== 7)&& j%2 == 0)|| (i == 6 && j%2 == 1) ){
                if(((i == 5 )&& j%2 == 0) ){
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
	
	
	
	public void movePiece(Player p, Tile targetTile) {
		Piece selectedPiece = p.getSelectPiece();	
		
		gameInfoLabel.setText(p + " move " + selectedPiece + " to " + targetTile.getPosition());
		cleanTile(selectedPiece.getPosition());
		selectedPiece.setPosition(targetTile.getPosition());
		targetTile.occupy(p.getSelectPiece());
	}
	
	public void killPiece(Player p, Tile targetTile) {
		Piece midPiece = getMidTile(p.getSelectPiece(), targetTile).getPiece();
		
		if(midPiece == null) {
			return;
		}
		
		if(midPiece.getOwner() != p) {
			movePiece(p, targetTile);
			cleanTile(midPiece.getPosition());
		}
		
	}
	
	public void upgradePiece(Player p, Tile targetTile) {
		gameInfoLabel.setText( p + " upgrade " + p.getSelectPiece() + " to King " + targetTile.getPosition());
		Piece newKing = new King(targetTile.getPosition(), p);
		p.setSelectPiece(newKing);
		targetTile.occupy(newKing);
	}
	
	public void pieceUpgrade() {
		
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
			System.out.println("two tiles are too close or too far");
			return null;
		}
		
	}
	
	
// the Below methods are main method and all method relatived to servers
	
	@Override
	public void run() {

		this.setVisible(true);
	}
	
	/**
	 * send the click infomation to the server
	 * @param pos
	 */
	public void sendClickInfo(Position pos) {
		
	}
	
	/**
	 * server will 
	 */
	public void recieveServerRequest() {
		
	}

}
