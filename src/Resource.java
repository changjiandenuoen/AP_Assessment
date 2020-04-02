

import java.util.HashMap;

/**
 * A class store all asserts path.
 */
public class Resource {
	
	private final HashMap<String, String> pathMap = new HashMap<String, String>();
	
	public HashMap<String, String> getPathMap() {
		return pathMap;
	}

	public Resource() {
		
		pathMap.put("P1KING", "asserts/p1-king.png");
		pathMap.put("P1MAN", "asserts/p1-man.png" );
		pathMap.put("P2KING", "asserts/p2-king.png");
		pathMap.put("P2MAN", "asserts/p2-man.png");
	}
}
