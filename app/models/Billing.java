package models;

import java.math.BigDecimal;


import javax.persistence.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import com.avaje.ebean.Model;

import enums.BillingRecurrence;

@Entity
@Table(name = "billedPayment")
public class Billing extends Model {
	
	@OneToOne
	public Payment payment;
	
	@Id
	@SequenceGenerator(name = "billing", sequenceName = "billing_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing")
	public long id;

	public Billing(LocalDate start,String recurrence){
		this.startDate=start;
		if(recurrence=="weekly"){
			this.recurrence=BillingRecurrence.Weekly;
			endDate=startDate.plusMonths(1);
		}
		else{
			this.recurrence=BillingRecurrence.Monthly;
			endDate=startDate.plusWeeks(1);
		}
	}
	public LocalDate startDate;
	
	public LocalDate endDate;
	
	public BillingRecurrence recurrence;
	
	
}
