

//Handle the game Logic
public class Model {
	Player p1;
	Player p2;
	int turn;
	boolean endGame;
	Player winner;
	

	public Model(Player p1, Player p2) {
		winner = null;
		this.p1 = p1;
		this.p2 = p2;
		turn = 0;
	}
	
	public void gameStart() {
		
		while(!endGame) {
			if(!p1.isTurn()) {
				p2.setTurn(true);
				continue;
			}
			
			if(!p2.isTurn()) {
				p1.setTurn(true);
				continue;
			}
		}

	}
	
	//a Player will lose if they lose all pieces or they cannot move
	public void endOfGame() {
		
	}
	
	
}
