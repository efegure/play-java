package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import play.db.ebean.*;

@Entity
@Table(name = "timetable")
public class TimeTable extends Model {

	@Id
	@Column(name = "timetable_id")
	@SequenceGenerator(name="identifier", sequenceName="table_id_seq", allocationSize=1)  
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="identifier")  
	public Long id;

	private boolean online;

	@OneToOne()
	public User user;

	@OneToMany(mappedBy = "table")
	public List<Time> timeTable = new ArrayList<>();
/*
	public TimeTable(User user) {
		this.user = user;
	}
*/
	public static Finder<String, TimeTable> find = new Finder<String, TimeTable>(String.class, TimeTable.class);

	public static void createTable(User user) {
		User us = User.find.byId(user.email);
		TimeTable table = new TimeTable();
		table.setUser(user);
		table.save();
		us.table = table;

		us.save();
	}

	public static void addTime(User user) {
		User us = User.find.byId(user.email);
		Time time = new Time();
		time.setTable(us.table);
		time.save();
		us.save();
	}

	public void setUser(User user){
		this.user=user;
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

	public long calculateTotalTime() {
		TimeTable table = this;
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
