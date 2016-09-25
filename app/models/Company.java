package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;


@Entity
public class Company extends Model {
	
	@Id
	public String cName;
	
	@OneToOne
	public User representative;
	
	@OneToMany
	public List<User> cUsers = new ArrayList<>();
	
	public Company(String cName){
		this.cName=cName;
	}
	
	@OneToOne
	public Payment payment;
	
	public static Finder<String, Company> find = new Finder<String, Company>(String.class, Company.class);
	
	
	public static void createCompany(Company company){
		company.save();
	}
	
	public void addUser(User user){
		this.cUsers.add(user);
	}
	
}
