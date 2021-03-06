/**
 * ***Copyright(c)	:	2014-Present, VenomVendor.***
 * Author		:	VenomVendor
 * Dated		:	3 Apr, 2014 8:39:27 PM
 * Project		:	LenDen-Android
 * Client		:	LenDen
 * Contact		:	info@VenomVendor.com
 * URL			:	https://www.google.co.in/search?q=VenomVendor
 * Copyright(c)	:	2014-Present, VenomVendor.
 * License		:	This work is licensed under Attribution-NonCommercial 3.0 Unported (CC BY-NC 3.0).
 *					License info at http://creativecommons.org/licenses/by-nc/3.0/deed.en_US
 *					Read More at http://creativecommons.org/licenses/by-nc/3.0/legalcode
 **/

package vee.HexWhale.LenDen.Parsers.AddItems;


public class GetAddItems{
   	private String error_code;
   	private String error_message;
   	private Response response;
   	private String status;

 	public String getError_code(){
		return this.error_code;
	}
	public void setError_code(String error_code){
		this.error_code = error_code;
	}
 	public String getError_message(){
		return this.error_message;
	}
	public void setError_message(String error_message){
		this.error_message = error_message;
	}
 	public Response getResponse(){
		return this.response;
	}
	public void setResponse(Response response){
		this.response = response;
	}
 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
}
