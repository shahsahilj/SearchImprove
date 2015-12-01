import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Project1 {
	public static void main(String args[]) throws IOException{
		String query;
		double precision;
		int n=10;
		query=args[2];
		precision=Double.parseDouble(args[1]);
		String accountKey=args[0];
		System.out.println("Account Key: "+accountKey);
		System.out.println("Query: "+query);
		System.out.println("Precision: "+precision);
		System.out.println("_________________________________________");
		do{
			QueryResult qr[]=BingSearch.getSearchResults(query, accountKey);
			Dictionary.makeDictionary(qr);
			HashMap<String,Integer> dictionary=Dictionary.getDictionary();
			String queryWords[]=query.split(" ");
			for(int i=0;i<queryWords.length;i++)
			{
				queryWords[i]=WordClean.clean(queryWords[i]);
			}
			HashMap<String,Double> queryvect=new HashMap<String,Double>();
			Iterator<String> dictI= dictionary.keySet().iterator();
			//Adding all dictionary words to the query vector
			while(dictI.hasNext())
			{
				String s=dictI.next();
				if(Arrays.asList(queryWords).contains(s))
					queryvect.put(s, 1.0);
				else
					queryvect.put(s, 0.0);
			
			}
		
			DocumentJ d[]= new DocumentJ[10];
			for(int i=0;i<n;i++)
			{
				d[i]=new DocumentJ();
				d[i].makeDoc(qr, i);
			}
			
			int relevantNo=0;
			for(int i=0;i<qr.length;i++)
			{
				if(qr[i].isRelevant())
					relevantNo++;
			}
			System.out.println("Precision@10: "+relevantNo/10.0);
			if(relevantNo==0){
				System.out.println("Since the precision is 0.0, we cannot augment the query ");
				break;
			}
			if((double)relevantNo/qr.length<precision){
				query=query+" "+Query.queryMaker(queryWords,queryvect,d,dictionary,qr);
				System.out.println("________________________________________________");
				System.out.println("Augmented Query: "+query);
				System.out.println("________________________________________________");
			}
			else{
				System.out.println("Success! Since precision obtained satisfies the precision required");
				break;
			}
		}while(true);

	}

}
