
package vee.HexWhale.LenDen.Parsers.Profile;


public class Response{
   	private String creation_date;
   	private String email;
   	private String first_name;
   	private String id;
   	private String last_login;
   	private String last_name;

 	public String getCreation_date(){
		return this.creation_date;
	}
	public void setCreation_date(String creation_date){
		this.creation_date = creation_date;
	}
 	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email = email;
	}
 	public String getFirst_name(){
		return this.first_name;
	}
	public void setFirst_name(String first_name){
		this.first_name = first_name;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getLast_login(){
		return this.last_login;
	}
	public void setLast_login(String last_login){
		this.last_login = last_login;
	}
 	public String getLast_name(){
		return this.last_name;
	}
	public void setLast_name(String last_name){
		this.last_name = last_name;
	}
}
