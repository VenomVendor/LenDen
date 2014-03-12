/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author		:	VenomVendor
 * Dated		:	12 Mar, 2014 11:54:57 PM
 * Project		:	LenDen-Android
 * Client		:	LenDen
 * Contact		:	info@VenomVendor.com
 * URL			:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)	:	2014-Present, VenomVendor.
 * License		:	This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *					License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *					Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.Parsers.ItemCategory;


public class Items{
   	private String category_id;
   	private String description;
   	private boolean favorite;
   	private Number favorite_count;
   	private String item_id;
   	private boolean like;
   	private Number like_count;
   	private Number location_latitude;
   	private Number location_longitude;
   	private Number selling_price;
   	private String title;
   	private Number trade_mode;
   	private String user_first_name;
   	private String user_id;
   	private String user_last_name;

 	public String getCategory_id(){
		return this.category_id;
	}
	public void setCategory_id(String category_id){
		this.category_id = category_id;
	}
 	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
 	public boolean getFavorite(){
		return this.favorite;
	}
	public void setFavorite(boolean favorite){
		this.favorite = favorite;
	}
 	public Number getFavorite_count(){
		return this.favorite_count;
	}
	public void setFavorite_count(Number favorite_count){
		this.favorite_count = favorite_count;
	}
 	public String getItem_id(){
		return this.item_id;
	}
	public void setItem_id(String item_id){
		this.item_id = item_id;
	}
 	public boolean getLike(){
		return this.like;
	}
	public void setLike(boolean like){
		this.like = like;
	}
 	public Number getLike_count(){
		return this.like_count;
	}
	public void setLike_count(Number like_count){
		this.like_count = like_count;
	}
 	public Number getLocation_latitude(){
		return this.location_latitude;
	}
	public void setLocation_latitude(Number location_latitude){
		this.location_latitude = location_latitude;
	}
 	public Number getLocation_longitude(){
		return this.location_longitude;
	}
	public void setLocation_longitude(Number location_longitude){
		this.location_longitude = location_longitude;
	}
 	public Number getSelling_price(){
		return this.selling_price;
	}
	public void setSelling_price(Number selling_price){
		this.selling_price = selling_price;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
 	public Number getTrade_mode(){
		return this.trade_mode;
	}
	public void setTrade_mode(Number trade_mode){
		this.trade_mode = trade_mode;
	}
 	public String getUser_first_name(){
		return this.user_first_name;
	}
	public void setUser_first_name(String user_first_name){
		this.user_first_name = user_first_name;
	}
 	public String getUser_id(){
		return this.user_id;
	}
	public void setUser_id(String user_id){
		this.user_id = user_id;
	}
 	public String getUser_last_name(){
		return this.user_last_name;
	}
	public void setUser_last_name(String user_last_name){
		this.user_last_name = user_last_name;
	}
}
