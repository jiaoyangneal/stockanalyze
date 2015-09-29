package com.neal.stock.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "all_stock")
public class AllStockModel {

		@Id
	    @GeneratedValue(generator="increment")
		@GenericGenerator(name="increment", strategy="increment")
	    @Column(name="id", nullable = false)
	    private int id;
	 	
	 	
	 	@Column(name = "stock_code")
	 	private String stock_code;
	 	
	 	@Column(name = "full_code")
	 	private String full_code;
	 	
	 	@Column(name = "stock_name")
	 	private String stock_name;
	 	
	 	@Column(name = "exchange")
	 	private String exchange;
	 	
	 	public AllStockModel(){};
	 	
	 	public int getId() {
			return id;
		}



		public String getStock_code() {
			return stock_code;
		}



		public String getFull_code() {
			return full_code;
		}



		public String getStock_name() {
			return stock_name;
		}



		public String getExchange() {
			return exchange;
		}



		public AllStockModel(String stock_code, String full_code, String stock_name, String exchange){
	 		this.stock_code = stock_code;
	 		this.full_code = full_code;
	 		this.stock_name = stock_name;
	 		this.exchange = exchange;
	 	}
}