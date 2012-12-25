package gradesys;

public class Util {

	public static boolean isEmpty(String text) {
		if(text == null || text.length()  == 0) {
			return true;
		}
		return false;
	}
}
