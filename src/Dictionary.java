import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary 
{
	static HashMap<String,Integer> dictionary=new HashMap<String,Integer>();
	public static HashMap<String, Integer> getDictionary() {
		return dictionary;
	}
	public static void makeDictionary(QueryResult qr[])
	{
		int i;
		for(i=0;i<qr.length;i++)
		{
			ArrayList<String> allWords=new ArrayList<String>();
			int j;
			qr[i].setTitle(qr[i].getTitle().replaceAll("\\,", " ").replaceAll("\\.", " ").replaceAll("\\-", " "));
			qr[i].setTitle(qr[i].getTitle().replaceAll(" +", " "));
			String titleWords[]=qr[i].getTitle().split(" ");
			allWords.add(WordClean.clean(titleWords[0]));
			for(j=1;j<titleWords.length;j++)
			{
				titleWords[j]=WordClean.clean(titleWords[j]);
				if(allWords.contains(titleWords[j])==false)
					allWords.add(titleWords[j]);
			}
			qr[i].setDescription(qr[i].getDescription().replaceAll("\\,", " ").replaceAll("\\.", " ").replaceAll("\\-", " "));
			qr[i].setDescription(qr[i].getDescription().replaceAll(" +", " "));
			String desWords[]=qr[i].getDescription().split(" ");
			for(j=0;j<desWords.length;j++)
			{
				desWords[j]=WordClean.clean(desWords[j]);
				if(!allWords.contains(desWords[j]))
					allWords.add(desWords[j]);
			}
			for(j=0;j<allWords.size();j++)
			{
				String s=allWords.get(j);
				if(dictionary.containsKey(s))
					dictionary.put(s,dictionary.get(s)+1);
				else
					dictionary.put(s, 1);
			}
		}
	}

}
