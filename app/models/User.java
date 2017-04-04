package models;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

import play.data.validation.Constraints.*;
import utility.Password;

@Entity
@Table(name = "cm_users")
public class User extends Model {

	@Id
	@Email
	@Required
	public String email;
	@Required
	public String name;
	
	@JsonIgnore
	@Required
	public String password;
	@OneToOne
	public TimeTable table;
	
	@ManyToOne@JsonBackReference
	public Company company;
	
	@Required
	public String comName;
	
	//**********
	private String authToken;

	private boolean isRegistered = false;

	private boolean isAdmin = false;

	public User(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
		
	}

	public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);

	public static User authenticate(String email, String password) {
		User user = find.where().eq("email", email).findUnique();
		if (user != null) {
			if (Password.checkPassword(password, user.password)) {
				return user;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	//*************
	public String createToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }
	//****************
	public void deleteAuthToken() {
        authToken = null;
        save();
    }
    //*****************
	 public static User findByAuthToken(String authToken) {
	        if (authToken == null) {
	            return null;
	        }

	        try  {
	            return find.where().eq("authToken", authToken).findUnique();
	        }
	        catch (Exception e) {
	            return null;
	        }
	    }
	
	 public static void login(User user) {
		TimeTable.addTime(user);
		TimeTable.setOnline(user);
		user.save();
	}

	public void register() {
		this.isRegistered = true;
	}

	public boolean getRegistered() {
		return isRegistered;
	}

	public static void logout(User user) {
		TimeTable.setOffline(user);
		Time lastTime = TimeTable.getLastTime(user.table);
		lastTime.setLogoffTime();
		lastTime.save();
		user.save();

	}

	public static void create(User user) {
		String hashed = Password.hashPassword(user.password);
		user.password = hashed;
		user.save();
	}

	public static void createTableToUser(User user) {
		TimeTable.createTable(user);
	}

	// TODO
	// delete works but needs better function in table and time
	public static void deleteUser(String id) {
		User user = User.find.byId(id);
		TimeTable t = TimeTable.find.byId(user.table.getId().toString());
		user.table = null;
		user.save();
		t.user = null;
		t.save();
		for (Time time : t.timeTable) {
			time.table = null;
			time.save();
			time.delete();
		}
		t.delete();
		find.ref(id).delete();

	}

	public void setAsAdmin() {
		this.isAdmin = true;
	}

	public boolean getAdminStatus() {
		return isAdmin;
	}

}