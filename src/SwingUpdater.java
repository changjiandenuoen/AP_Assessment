import java.net.Socket;
import java.util.List;

import javax.swing.SwingWorker;

public class SwingUpdater extends SwingWorker<Void, Command> {
	
	View view;
	Player player;
	Socket socket;
	
	public SwingUpdater(View view, Player player, Socket socket) {
		this.view = view;
		this.player = player;
		this.socket = socket;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		while(true) {

			if(socket.getInputStream() != null) {
				publish(player.recieveFromServer());
			}
		}
	}
	
	protected void process(List<Command> cmds) {
		 Command cmd = cmds.get(cmds.size()-1);
		 view.turnMove(view.getOppoPlayer(), cmd.getOriPos(), cmd.getTargetPos());
	}
	
}
