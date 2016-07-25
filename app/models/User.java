package models;



import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;
import play.data.validation.Constraints.*;

@Entity
@Table(name = "cmusers")
public class User extends Model {

    @Id @Email @Required
    public String email;
    @Required
    public String name;
    @Required
    public String password;
    private boolean isAdmin=false;
    
    public User(String email, String name, String password) {
      this.email = email;
      this.name = name;
      this.password = password;
    }

    public static Finder<String,User> find = new Finder<String,User>(
        String.class, User.class
    ); 
    
    
    public static User authenticate(String email, String password) {
        return find.where().eq("email", email)
            .eq("password", password).findUnique();
    }
    public static void create(User user) {
        if(User.find.all().size() == 0){
            user.setAsAdmin();
        }
    	  user.save();
    	}
    	
    private void setAsAdmin(){
        this.isAdmin=true;
    }
    public boolean getAdminStatus(){
        return isAdmin;
    }
    
}