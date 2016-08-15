package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.db.ebean.*;

@Entity
public class TimeTable extends Model {

	@Id
	public String id;

	private boolean online;

	@OneToOne()
	public User user;

	@OneToMany(cascade =  {CascadeType.PERSIST, CascadeType.REMOVE}, targetEntity = Time.class)
	public List<Time> timeTable ;

	public TimeTable(User user, String id) {
		this.user = user;
		this.id = id;
		this.timeTable = new ArrayList<Time>();
	}

	public static Finder<String, TimeTable> find = new Finder<String, TimeTable>(
			String.class, TimeTable.class);

	public static void createTable(User user) {
		User us = User.find.byId(user.email);
		TimeTable table = new TimeTable(us, us.email);
		us.table = table;
		Time time =Time.createTime(us);
		table.timeTable.add(time);
		table.save();
		us.save();
	}

	public static void addTime(User user) {
		User.createTableToUser(user);
		//User us = User.find.byId(user.email);
		//TimeTable ustable = TimeTable.find.byId(table.id);
		/*us.table.timeTable.add(t);
		us.table = ustable;
		us.table.save();
		us.save();*/
		/*
		 * user.table=tt; user.table.addT(t); user.save();
		 */
	}

	public void addT(Time time) {
		this.timeTable.add(time);
	}

	public static void setOffline(TimeTable table, User user) {
		User us = User.find.byId(user.email);
		us.table.setOff();
		us.table.save();
	}

	public static void setOnline(User user) {
		User us = User.find.byId(user.email);
		us.table.setOn();
		us.table.save();
	}

	public void setOn() {
		this.online = true;
	}

	public void setOff() {
		this.online = false;
	}

	public boolean getStatus() {
		return online;
	}

	public static long calculateTotalTime(TimeTable table) {
		long totalTime = 0;
		for (int i = 0; i < table.timeTable.size() - 1; i++) {
			Time time = table.timeTable.get(i);
			totalTime = totalTime + time.calculateTimeDiff();
		}
		return totalTime;
	}

	public static Time getLastTime(TimeTable table) {
		return table.timeTable.get(table.timeTable.size() - 1);
	}
}
