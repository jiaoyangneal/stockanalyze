package com.neal.stock.data.db;

import java.io.IOException;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.neal.stock.data.model.AllStockModel;
import com.neal.stock.stockdata.EastmoneyStockList;

public class AllStockCodeDB {

	public static void allStockCodeToDB() throws ClientProtocolException, IOException{
		
		Map<String, String> map = EastmoneyStockList.allStockList();
		
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

		Session session = sessionFactory.openSession();
        session.beginTransaction();
        
        for(String key : map.keySet()){
        	String[] vec = key.split("\\.");
        	String stock_code = vec[0].trim();
        	String exchage = vec[1].trim();
        	String full_code = key;
        	String stock_name = map.get(key).trim();
        	if(0 == stock_code.indexOf("00") || 0 == stock_code.indexOf("60") || 0 == stock_code.indexOf("30")){
        		AllStockModel model = new AllStockModel(stock_code, full_code, stock_name, exchage);
        		session.save(model);
        	}
        }
        
        session.getTransaction().commit();
        session.close();
        
        sessionFactory.close();
	}

	public static void main(String[] args) {
		try {
			allStockCodeToDB();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
