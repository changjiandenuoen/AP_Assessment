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
		 
		 if(cmd.getOriPos() != null && cmd.getTargetPos() != null) {
			game.turnMove(game.getOppoPlayer(), cmd.getOriPos(), cmd.getTargetPos());
		}
		 
		 if(cmd.getType() == CommandType.WIN || cmd.getType() == CommandType.LOSE) {
			game.gameEnd(player, cmd.getType());
		 }
	}	
}
