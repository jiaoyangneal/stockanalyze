package com.neal.stock.stockdata;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.neal.stock.data.model.AllStockModel;
import com.neal.stock.data.model.YahooStockModel;

public class YahooStockData {

	private static Logger logger = Logger.getLogger(YahooStockData.class);
	
	private static final String base_url = "http://table.finance.yahoo.com/table.csv?s=%s";
	
	public static String crawlerStockData(String stockCode) throws IOException, InterruptedException{
		
		String content = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		String stock_url = String.format(base_url, stockCode);
		logger.info("get stock data url : " + stock_url);
		
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
	
	public static void stockDataToDB() throws IOException, InterruptedException, ParseException{
		
		List<String> full_codes = new ArrayList<String>();
		
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

		Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        List<AllStockModel> list = session.createQuery("from AllStockModel").list();
        for(AllStockModel model : list){
        	full_codes.add(model.getFull_code());
        }
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
        for(int i = 0; i < full_codes.size(); i++){
        	logger.info(i + "\t" + full_codes.get(i));
        	String full_code = full_codes.get(i);
        	String stock_code = full_code.split("\\.")[0].trim();
        	
        	String data = YahooStockData.crawlerStockData(full_code);
        	
        	if (null == data || data.length() < 10)
        		continue;
        	
        	logger.info("get " + full_code + " data done");
        	session.beginTransaction();
        	
        	String[] items = data.split("\n");
        	for(int j = 1; j < items.length; j++){
        		String[] vec = items[j].split(",");
        		Date date = formatter.parse(vec[0].trim());
        		double open = Double.parseDouble(vec[1].trim());
        		double high = Double.parseDouble(vec[2].trim());
        		double low = Double.parseDouble(vec[3].trim());
        		double close = Double.parseDouble(vec[4].trim());
        		long volume = Long.parseLong(vec[5].trim());
        		double adj_close = Double.parseDouble(vec[6].trim());
        		session.save(new YahooStockModel(date, open, high, low, close, volume, adj_close, stock_code));
        	}
        	session.getTransaction().commit();
        	
        	logger.info("save stock " + full_codes.get(i) + " done!");
        }
        
        session.close();
        
        logger.info("all done");
        
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ParseException {
		
		PropertyConfigurator.configure("./conf/log4j.properties");
		
		logger.info("started crawler stock data");
//		String data = crawlerStockData("000001.sz");
//		System.out.println(data);
		
		YahooStockData.stockDataToDB();
	}

}
