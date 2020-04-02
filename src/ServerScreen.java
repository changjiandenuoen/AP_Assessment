import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerScreen extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	private JTextArea serverConsole;
	
	//getter
	public JTextArea getServerConsole() {
		return serverConsole;
	}

	public ServerScreen() {

		this.setSize(19*GameScreen.unit,10*GameScreen.unit);
		this.setTitle("Server Console");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		
		serverConsole = new JTextArea();
		serverConsole.setLineWrap(true); 
		serverConsole.setWrapStyleWord(true);
		serverConsole.setEditable(false);
		this.add(new JScrollPane(serverConsole));

		
		this.setVisible(true);
	}

	@Override
	public void run() {
		this.setVisible(true);
	}
}
