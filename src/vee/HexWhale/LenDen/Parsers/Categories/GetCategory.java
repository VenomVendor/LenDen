
package vee.HexWhale.LenDen.Parsers.Categories;

import java.util.List;

public class GetCategory{
   	private String error_code;
   	private String error_message;
   	private List<Response> response;
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
 	public List<Response> getResponse(){
		return this.response;
	}
	public void setResponse(List<Response> response){
		this.response = response;
	}
 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
}
