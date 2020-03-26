import javax.swing.SwingWorker;

public class SwingUpdater extends SwingWorker<Void, Void> {
	
	View view;
	Player player;
	Position oriPos;
	Position targetPos;
	
	public SwingUpdater(View view, Player player, Command cmd) {
		this.view = view;
		this.player = player;
		this.oriPos = cmd.getOriPos();
		this.targetPos = cmd.getTargetPos();
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		System.out.println("update the piece of" + player );
		return null;
	}
	
	@Override
	protected void done() {
		view.turnMove(player, oriPos, targetPos);
	}

}
