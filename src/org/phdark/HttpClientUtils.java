package org.phdark;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 
 * 类HttpClientUtils.java的实现描述：TODO 类实现描述 
 * @author haojiabin 2016年5月18日 下午4:47:33
 */
public class HttpClientUtils
{
	
	/**
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getBodyAsString(String url) throws ClientProtocolException, IOException {
		if (url==null) {
			return null;
		}
		
		//HttpClients.createMinimal(HttpClientConnectionManager)
		HttpClient client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig);
		
		CloseableHttpResponse resp = (CloseableHttpResponse) client.execute(get);
		try{
			int code = resp.getStatusLine().getStatusCode();
			if (code < 200 || code >299) {
				throw new ConnectException("http status code is :"+code);
			}
			return EntityUtils.toString(resp.getEntity(),"UTF-8");
		} finally {
			resp.close();
		}
	}
	
	public static HttpResponse get(String url) throws ClientProtocolException, IOException {
		if (url == null) {
			return null;
		}
		HttpClient client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig);
		
		return client.execute(get);
	}
	
	public static String getBody(HttpEntity entity) throws ParseException, IOException {
		return EntityUtils.toString(entity,"UTF-8");
	}
	
	public static int getStatusCode(HttpResponse resp) {
		if (resp==null) {
			throw new IllegalArgumentException("HttpResponse is null");
		}
		return resp.getStatusLine().getStatusCode();
	}
}
