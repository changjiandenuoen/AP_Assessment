public class AppMain2 {
	public static void main(String[] args) {
		ClientHandler c = new ClientHandler("127.0.0.2", 9999,2);
		c.run();
	}
}

