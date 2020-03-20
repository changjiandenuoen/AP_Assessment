package Model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class GameScreen extends JFrame {
	
	public static final int unit = 40;
	public static final int BoardWidth = 8;
	public static final int BoardHeight = 8;
	
	private Tile[][] board;
	private Player p1 = new Player();
	private Player p2 = new Player();
	
	
	//Constructor
	public GameScreen(){
		
		this.setSize(19*unit,12*unit);
		this.setTitle("game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		//this panel is the panel for the whole game screen
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		setBorder(mainPanel, 1,1,1,1);
		
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(1,2));
		
		JPanel boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(8,8));
		
//		boardPanel.setBackground(Color.RED);
		createBoardArea(boardPanel);
		boardPanel.setBorder(BorderFactory.createDashedBorder(null, 1, 0));
		
		JPanel functionPanel = new JPanel();
		
		innerPanel.add(boardPanel);
		innerPanel.add(functionPanel );
		mainPanel.add(innerPanel);
		this.add(mainPanel);
		
		
		
	}
	
	public static void main(String[] args) {
		JFrame g = new GameScreen();
		g.setVisible(true);
		
	}
	
	
	
	/**
	 * redraw the board based on the model
	 */
	public void updateBoard() {
		
	}
	
	/**
	 * update the message from the console of the frame
	 */
	public void updateConsole() {
		
	}
	
	/**
	 * clean all the pieces on the board, return the initial states of the game
	 */
	public void replay() {
		
	}
	
	/**
	 * exit the game
	 */
	public void exit() {
		
	}
	
	//Helper Methods
	private void setBorder(JPanel panel, int a, int b, int c, int d) {
		panel.setLayout(new BorderLayout());
		Border emptyBorder = BorderFactory.createEmptyBorder(1*unit, 1*unit, 1*unit, 1*unit);
		panel.setBorder(emptyBorder);
	}
	
	/**
	 * create a empty chessBoard with black and white areas
	 * @param chessBoardPanel the panel which contain the chessboard
	 */
	private void createBoardArea(JPanel chessBoardPanel) {
		
		Tile[][] board = new Tile[8][8];
		
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
            	
//            	board[i][j] = new Tile(i,j);
//            	board[i][j].setOpaque(true);
//            	
            	//create black and white area
//                if((i+j)%2==0) {
//                	board[i][j].setBackground(Color.white);
//
//                }else if((i+j)%2 ==1){
//                	board[i][j].setBackground(Color.black);
//                }
                //get all pieces on the board
                if(((i == 0 || i== 2)&& j %2 == 1)|| (i == 1&& j%2 == 0)) {
                	board[i][j] = new Tile(i,j,p2);
                	board[i][j].occupy(PathList.P2MAN.toString());
                	
                }
                
                if(((i == 5 || i== 7)&& j%2 == 0)|| (i == 6 && j%2 == 1) ){
                	board[i][j] = new Tile(i,j,p2);
                	board[i][j].occupy(PathList.P1KING.toString());
                }
                
                if(board[i][j] == null) {
                	board[i][j] = new Tile(i,j);
                	
                }
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
	
}
