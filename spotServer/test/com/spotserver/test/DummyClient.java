package com.spotserver.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.omg.CORBA.NamedValue;

import com.spotserver.model.Location;
import com.spotserver.model.spotservice.SpotReportRequest;

public class DummyClient {

	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	//public static final String SERVICE_URL = "http://localhost:9000";
	public static final String SERVICE_URL = "http://spotserver.cloudfoundry.com";
	
	public static final String SPOTS_SERVICE_URL = SERVICE_URL + "/spots";
	public static final String SPOTREPORT_SERVICE_URL = SERVICE_URL + "/spotreport";
	public static final String ENSURE_INDEX_SERVICE_URL = SERVICE_URL + "/admin/ensurespotindex";

	/**
	 * @param args
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {

		//ensureDbINdex();
		
		// chez moi :-)
		reportSpot(50.831733, 4.379951);

		// la chasse
		reportSpot(50.831733,4.389951);

		// rue de la faucille
		reportSpot(50.831733,4.489951);

		// cinquantenaire
		reportSpot(50.841733,4.389951);
		
		getSpots(50.831733, 4.379951 , 0.10 );

	}
	
	
	
	public static void getSpots (double latitude, double longitude, double range) throws JsonGenerationException, JsonMappingException, IOException {

		String queriedURL = SPOTS_SERVICE_URL + "?lat="+ latitude + "&longit=" + longitude + "&range=" + range;
		System.out.println("querying this URL: \n" + queriedURL);
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(queriedURL);
		HttpResponse response = client.execute(get);
		
		System.out.println("GETting this:");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}		
	}
	
	public static void reportSpot (double latitude, double longitude) throws JsonGenerationException, JsonMappingException, IOException {
		
		SpotReportRequest request = new SpotReportRequest(new Location(latitude, longitude));
		String jsonRequest = jsonMapper.writeValueAsString(request);
		System.out.println("POSTing this (as named value 'report': " + jsonRequest);
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(SPOTREPORT_SERVICE_URL);
		
		List<BasicNameValuePair> data = new LinkedList<BasicNameValuePair>();
		data.add(new BasicNameValuePair("report", jsonRequest));
		post.setEntity(new UrlEncodedFormEntity(data));
		
		HttpResponse response = client.execute(post);
		
		System.out.println("received this:");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}		
		
		
	}
	
	public static void ensureDbINdex () throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println("POSTing to': " + ENSURE_INDEX_SERVICE_URL);

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(ENSURE_INDEX_SERVICE_URL);
		HttpResponse response = client.execute(post);
		
		System.out.println("received this:");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		while ((line = rd.readLine()) != null) {
			System.out.println(line);
		}		
		
		
		
	}
	
	
	
	
	
	
}
