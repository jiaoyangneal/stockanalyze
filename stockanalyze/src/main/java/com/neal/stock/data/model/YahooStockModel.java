package com.neal.stock.data.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "stock_value")
public class YahooStockModel {

	
	@Id
    @GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
    @Column(name="id", nullable = false)
    private int id;
	
	@Column(name = "date")
 	private Date date;
 	
 	@Column(name = "open")
 	private double open;
 	
 	@Column(name = "high")
 	private double high;
 	
 	@Column(name = "low")
 	private double low;
	
 	@Column(name = "close")
 	private double close;
 	
 	@Column(name = "volume")
 	private long volume;
 	
 	@Column(name = "adj_close")
 	private double adj_close;
 	
 	@Column(name = "stock_code")
 	private String stock_code;
 	
 	public YahooStockModel(){
 		
 	}
 	
 	public YahooStockModel(Date date, double open, double high, double low, double close, long volume, double adj_close, String stock_code){
 		this.date = date;
 		this.open = open;
 		this.high = high;
 		this.low = low;
 		this.close = close;
 		this.volume = volume;
 		this.adj_close = adj_close;
 		this.stock_code = stock_code;
 	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public double getAdj_close() {
		return adj_close;
	}

	public void setAdj_close(double adj_close) {
		this.adj_close = adj_close;
	}

	public String getStock_code() {
		return stock_code;
	}

	public void setStock_code(String stock_code) {
		this.stock_code = stock_code;
	}
 	
}
