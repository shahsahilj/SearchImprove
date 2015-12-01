import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Query {
	//this method calculates the two words to be added to the query using a modified version of Rocchio's algorithm
	public static String queryMaker(String[] queryWords,HashMap<String,Double> queryvect,DocumentJ d[],HashMap<String,Integer> dictionary,QueryResult qr[])
	{
		double alpha=1,beta=0.9,gamma=0.9;int i;
		int relevantNo=0;
		for( i=0;i<qr.length;i++)
		{
			if(qr[i].isRelevant())
				relevantNo++;
		}
		Iterator<String> queryIt= queryvect.keySet().iterator();
		//Updating the query vector
		while(queryIt.hasNext()){
			double temp=0;
			String w= queryIt.next();
			for(i=0;i<qr.length;i++){
				if(qr[i].isRelevant())
					temp+=(beta*d[i].doc.get(w).weight)/relevantNo*dictionary.get(w);
				else
					temp-=(gamma*d[i].doc.get(w).weight)/(qr.length-relevantNo)*dictionary.get(w);
			}
			temp+=alpha*queryvect.get(w);
			queryvect.put(w, temp);
			
		}
		String queryAdd="";
		queryIt= queryvect.keySet().iterator();
		String s1="",s2="";
		double max1=0.0,max2=0.0;
		//The following is an intuitive attempt to avoid redundant words and ignore the already existing query words 
		String invalid[]={"the","who","them","their","they","for"};
		while(queryIt.hasNext()){
			String w=queryIt.next();
			if(w.length()<=2|| Arrays.asList(queryWords).contains(w)||Arrays.asList(invalid).contains(w))
				continue;
			if(queryvect.get(w)>max1){
				max2=max1;
				max1=queryvect.get(w);
				s2=s1;
				s1=w;
			}
			else 
				if(queryvect.get(w)>max2){
					max2=queryvect.get(w);
					s2=w;
			}
				
		}
		
		queryAdd=reOrder(s1, s2, qr, queryWords,dictionary);
		
		return queryAdd;
		
	}
	//This method reorders the words to be augmented to the original query
	public static String reOrder(String s1, String s2,QueryResult qr[],String queryWords[],HashMap<String,Integer> dictionary)
	{
		String t="";
		double sum1=0,sum2=0;
		for(int i=0;i<qr.length;i++){
			ArrayList<String> allWords=new ArrayList<String>();
			String titleWords[]=qr[i].getTitle().split(" ");
			allWords.add(WordClean.clean(titleWords[0]));
			for(int j=1;j<titleWords.length;j++)
			{
				titleWords[j]=WordClean.clean(titleWords[j]);
				if(allWords.contains(titleWords[j])==false)
					allWords.add(titleWords[j]);
			}
			String desWords[]=qr[i].getDescription().split(" ");
			for(int j=0;j<desWords.length;j++)
			{
				desWords[j]=WordClean.clean(desWords[j]);
				if(!allWords.contains(desWords[j]))
					allWords.add(desWords[j]);
			}
			for(int k=0;k<queryWords.length;k++){
				if(allWords.contains(s1)&&allWords.contains(queryWords[k]))
					sum1+=Math.abs(allWords.indexOf(s1)-allWords.indexOf(queryWords[k]));
				if(allWords.contains(s2)&&allWords.contains(queryWords[k]))
					sum2+=Math.abs(allWords.indexOf(s2)-allWords.indexOf(queryWords[k]));
			}
		}
		sum1/=dictionary.get(s1);
		sum2/=dictionary.get(s2);
		if(sum1<sum2)
			t=s1+" "+s2;
		else
			t=s2+" "+s1;
		return t;
	}


}
