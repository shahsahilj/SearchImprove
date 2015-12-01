import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DocumentJ {
	
	HashMap<String,TermIJ> doc;
	public void makeDoc(QueryResult qr[],int docI)
	{
		int n=10;
		 doc=new HashMap<String,TermIJ>();
		HashMap<String,Integer> dictionary=Dictionary.getDictionary();
		Iterator<String> dictI= dictionary.keySet().iterator();
		//Adding all dictionary words to the document vector
		while(dictI.hasNext())
		{
			TermIJ t= new TermIJ();
			doc.put(dictI.next(), t);
		}
		//Adding all title and description words to the arraylist allwords
		ArrayList<String> allWords=new ArrayList<String>();
		int j;
		qr[docI].setTitle(qr[docI].getTitle().replaceAll("\\,", " ").replaceAll("\\.", " ").replaceAll("\\-", " "));
		qr[docI].setTitle(qr[docI].getTitle().replaceAll(" +", " "));
		String titleWords[]=qr[docI].getTitle().split(" ");
		allWords.add(WordClean.clean(titleWords[0]));
		for(j=1;j<titleWords.length;j++)
		{
			titleWords[j]=WordClean.clean(titleWords[j]);
			allWords.add(titleWords[j]);
		}
		qr[docI].setDescription(qr[docI].getDescription().replaceAll("\\,", " ").replaceAll("\\.", " ").replaceAll("\\-", " "));
		qr[docI].setDescription(qr[docI].getDescription().replaceAll(" +", " "));
		String desWords[]=qr[docI].getDescription().split(" ");
		for(j=0;j<desWords.length;j++)
		{
			desWords[j]=WordClean.clean(desWords[j]);
			allWords.add(desWords[j]);
		}
		Iterator<String> allI=allWords.iterator();
		//Calculating term frequencies for all words in this document
		
		while(allI.hasNext())
		{
			String s=allI.next();
			TermIJ t=new TermIJ();
			t.termFrequency=doc.get(s).termFrequency+1;
			doc.put(s, t);
		}
		Iterator<String> docWords=doc.keySet().iterator();
		while(docWords.hasNext())
		{
			String s=docWords.next();
			TermIJ temp=new TermIJ();
			temp.weight=doc.get(s).termFrequency*Math.log(n/dictionary.get(s));
			temp.termFrequency=doc.get(s).termFrequency;
			doc.put(s, temp);
		}
	}
}
