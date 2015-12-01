import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


public class BingSearch
{
	static QueryResult qr[]= new QueryResult[10];
	static String query = "";
		
	public static QueryResult[] getSearchResults(String question, String accountKey) throws IOException
	{
		HttpClient httpclient = new DefaultHttpClient();
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		
		try {
			
			String q="\'"+question+"\'";
			String query="https://api.datamarket.azure.com/Bing/Search/Web?$top=10&$format=Json&Query="+URLEncoder.encode(q, "UTF-8");
            HttpGet httpget = new HttpGet(query);
            accountKey = "";
            byte[] accountKeyBytes = Base64.encodeBase64((":" + accountKey).getBytes());
            String accountKeyEnc = new String(accountKeyBytes);
            httpget.setHeader("Authorization", "Basic "+accountKeyEnc);

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            JSONObject jsonObj = new JSONObject(responseBody);
            JSONObject jObj = jsonObj.getJSONObject("d");;
            JSONArray arr = jObj.getJSONArray("results");
            for (int i = 0; i < arr.length(); i++) {
            	JSONObject ite = arr.getJSONObject(i);
            	String A=ite.getString("Title");
                String B = ite.getString("Url");
                String C= ite.getString("Description");
                qr[i]=new QueryResult();
                qr[i].setDescription(C);
                qr[i].setTitle(A);
                qr[i].setUrl(B);
                System.out.println((i+1)+" Title: "+A);
                System.out.println("Description: "+C);
                System.out.println("Url: "+B);
                System.out.println("Enter the relevance of this query, Enter Y for yes, Enter N ");
                String c=br.readLine();
                if(c.equalsIgnoreCase("y"))
                	qr[i].setRelevant(true);
                else if(c.equalsIgnoreCase("n"))
                	qr[i].setRelevant(false);
                System.out.println("________________________________________________________________\n");

            }


        } catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
            httpclient.getConnectionManager().shutdown();
        }
		return qr;
		
	}
	

}