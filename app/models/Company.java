package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Company extends Model {

	@Id
	public String cName;
	
	private int callAmount=0;

	@OneToOne
	@JsonBackReference
	public User representative;

	@OneToMany
	@JsonBackReference
	public List<Invoice> InvoiceList;

	@OneToMany
	@JsonBackReference
	public List<User> cUsers = new ArrayList<>();

	public Company(String cName) {
		this.cName = cName;
	}

	private String apiKey;

	@OneToOne
	@JsonBackReference
	public Payment payment;

	public static Finder<String, Company> find = new Finder<String, Company>(String.class, Company.class);

	public static void createCompany(Company company) {
		company.save();
	}
	
	public static Company findByApiKey(String apiKey) {
    if (apiKey == null) {
        return null;
    }
    try  {
        return find.where().eq("apiKey", apiKey).findUnique();
    }
    catch (Exception e) {
        return null;
    }
}

	// *************
	public String createToken() {
		apiKey = UUID.randomUUID().toString();
		save();
		return apiKey;
	}

	// ****************
	public void deleteAuthToken() {
		apiKey = null;
		save();
	}
	public Invoice getCurrentInvoice(){
		for(Invoice invoice : InvoiceList){
			if(invoice.isCurrent()){
				return invoice;
			}
		}
		return null;
	}

	public void addUser(User user) {
		this.cUsers.add(user);
	}
	
	public void resetCall(){
		this.callAmount=0;
	}
	
	public void apiCall(){
		callAmount++;
	}
	
	public int getCallAmount(){
		return callAmount;
	}

}
