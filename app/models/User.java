package models;

import javax.persistence.*;


import com.avaje.ebean.*;

import play.data.validation.Constraints.*;
import utility.Password;

@Entity
@Table(name = "CM_USERS")
public class User extends Model {

	@Id
	@Email
	@Required
	public String email;
	@Required
	public String name;
	@Required
	public String password;
	@OneToOne
    public TimeTable table;

	
	private boolean isAdmin = false;

	public User(String email, String name, String password,TimeTable table) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.table=table;
	}

	public static Finder<String, User> find = new Finder<String, User>(
			String.class, User.class);

	public static User authenticate(String email, String password) {
		User user = find.where().eq("email", email).findUnique();
		if (Password.checkPassword(password, user.password)) {
			return user;
		} else {
			return null;
		}
	}

	public static void login(User user){
		TimeTable.addTime(user);
		TimeTable.setOnline(user);
		user.save();
	}
	
	public static void logout(User user){
		TimeTable.setOffline(user.table,user);
		Time lastTime = TimeTable.getLastTime(user.table);
		lastTime.setLogoffTime();
		lastTime.save();
		user.save();
		
	}

	public static void create(User user) {
		if (User.find.all().size() == 0) {
			user.setAsAdmin();
		}
		String hashed = Password.hashPassword(user.password);
		user.password = hashed;
		user.save();
	}
	
	public static void createTableToUser(User user){
		TimeTable.createTable(user);
	}

	public static void deleteUser(String id) {
		  find.ref(id).delete();
		}
	
	private void setAsAdmin() {
		this.isAdmin = true;
	}

	public boolean getAdminStatus() {
		return isAdmin;
	}

}