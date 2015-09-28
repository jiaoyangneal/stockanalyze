package com.neal.stock.stockdata;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class YahooStockData {

	private static final String base_url = "http://table.finance.yahoo.com/table.csv?s=%s";
	
	public static String crawlerStockData(String stockCode) throws IOException, InterruptedException{
		
		String content = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		String stock_url = String.format(base_url, stockCode);
		
		HttpGet httpGet = new HttpGet(stock_url);
		
		httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.77 Safari/537.1");
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Connection","keep-alive");
		httpGet.setHeader("Content-Type","multipart/form-data; boundary=---------------------------9994580724461");
		
		CloseableHttpResponse response = httpclient.execute(httpGet);
		
		try{
			if( 200 == response.getStatusLine().getStatusCode()){
				HttpEntity entity = response.getEntity();
				content = EntityUtils.toString(entity);
			}
		} finally {
			response.close();
			httpclient.close();
		}
		
		return content;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String data = crawlerStockData("000001.sz");
		System.out.println(data);
	}

}
