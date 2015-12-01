
public class WordClean {
	public static String clean(String a){
		a=a.replaceAll("('s)", "").replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
		return a;
	}

}
