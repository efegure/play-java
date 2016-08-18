package utility;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import models.TimeTable;
import models.User;

@Singleton
public class OnStartup {

    @Inject
    public OnStartup() {
        if (User.find.findRowCount() == 0) {
            String email = "a";
            String password = "aaa";
            String fullName = "TheAdmin";
            TimeTable table=new TimeTable();
            User user = new User(email, fullName, password,null);
            user.setAsAdmin();
            user.save();
            table.setUser(user);
            table.save();
            user.table=table;
            user.register();
            String hashed = Password.hashPassword(user.password);
            user.password=hashed;
            user.save();
        }
    }
}