package models;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.Period;

import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

@Entity
public class Time extends Model{

	private DateTime loginTime;
	private DateTime logoffTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="time_id")
	public Long id;
	
	@ManyToOne
	@JoinColumn(name="timetable_id")
	public TimeTable table;
	
	public static Finder<String, Time> find = new Finder<String, Time>(
			String.class, Time.class);

	public Time(){
		loginTime = new DateTime();
	}
	/*
	public  static Time createTime(User user){
		User us = User.find.byId(user.email);
		Time time = new Time(us.table,us.table.id);
		time.save();
		return time;
	}*/
	
	public DateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime() {
		this.loginTime = new DateTime();
	}

	public DateTime getLogoffTime() {
		return logoffTime;
	}

	public void setLogoffTime() {
		this.logoffTime = new DateTime();
	}

	public long calculateTimeDiff() {
		Period timeDiff = new Period(loginTime, logoffTime);
		return timeDiff.getSeconds();

	}
}
