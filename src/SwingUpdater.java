import java.net.Socket;
import java.util.List;

import javax.swing.SwingWorker;

public class SwingUpdater extends SwingWorker<Void, Command> {
	
	GameScreen game;
	Player player;
	Socket socket;
	
	public SwingUpdater(GameScreen game, Player player, Socket socket) {
		this.game = game;
		this.player = player;
		this.socket = socket;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		Command cmd;
		while(true) {
			
			//keep reading the socket, and publish the command if they recieve.
			if(socket.getInputStream() != null) {
				if((cmd =player.recieveFromServer()) != null) {
					publish(cmd);
				}
			}
		}
	}
	
	protected void process(List<Command> cmds) {
		 Command cmd = cmds.get(cmds.size()-1);
		 if(cmd == null) {
			 return;
		 }
		 //if the command contain oriPos and targetPos
		 //which means this is a move command (or win-move command )
		 if(cmd.getOriPos() != null && cmd.getTargetPos() != null) {
			game.turnMove(game.getOppoPlayer(), cmd.getOriPos(), cmd.getTargetPos());
		}
		 
		 //if the type of command is WIN or LOSE, end game
		 if(cmd.getType() == CommandType.WIN || cmd.getType() == CommandType.LOSE) {
			game.gameEnd(player, cmd.getType());
		 }
	}	
}
