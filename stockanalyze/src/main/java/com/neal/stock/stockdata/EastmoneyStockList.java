package com.neal.stock.stockdata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EastmoneyStockList {

	private static final String url = "http://quote.eastmoney.com/stocklist.html";
	
	private static final Pattern pattern = Pattern.compile("(.*)?\\((\\d+)\\)");
	
	public static Map<String, String> allStockList() throws ClientProtocolException, IOException{
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		String content = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpGet httpGet = new HttpGet(url);
		
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
				content = EntityUtils.toString(entity,"gbk");
			}
		} finally {
			response.close();
			httpclient.close();
		}
		
		if (null == content)
			return null;
		
		Document doc = Jsoup.parse(content);
		Element stock_div = doc.getElementById("quotesearch");
		
		Elements eles = stock_div.getElementsByTag("li");
		
		for(int i = 0; i < eles.size(); i++){
			Element ele = eles.get(i);
			String str = ele.toString();
			if(str.indexOf("sz") >= 0){
				String text = ele.getAllElements().get(0).text();
				Matcher matcher = pattern.matcher(text);
				if(matcher.matches()){
					String stock_name = matcher.group(1);
					String stock_code = matcher.group(2) + ".sz";
					map.put(stock_code, stock_name);
				}
			}
			if(str.indexOf("sh") >= 0){
				String text = ele.getAllElements().get(0).text();
				Matcher matcher = pattern.matcher(text);
				if(matcher.matches()){
					String stock_name = matcher.group(1);
					String stock_code = matcher.group(2) + ".sh";
					map.put(stock_code, stock_name);
				}
			}
		}

		return map;
	}
	
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		Map<String, String> map = EastmoneyStockList.allStockList();
	}

}
