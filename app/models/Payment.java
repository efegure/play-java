package models;

import java.math.BigDecimal;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import enums.BillingRecurrence;
import enums.PaymentMethods;
import enums.Subscription;


@Entity
public class Payment extends Model {
	
	@Id
	@SequenceGenerator(name = "payment", sequenceName = "payment_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment")
	public long id;
	
	public Payment(String subs){
		 if(subs=="old"){
			this.subscription=Subscription.LongTimeUser;
		}
		else {
			this.subscription=Subscription.NewUser;
		}
		
	}
	@OneToOne@JsonBackReference
	public Company company;
	
	@OneToOne@JsonBackReference
	public Billing billing;

	@OneToOne@JsonBackReference
	public PrePaid prepaid;
	
	//payment method
	public PaymentMethods method;
	
	//subscription which usage cost is decided
	public Subscription subscription;
	
	public LocalDate startDate;
	//Usage Limit for freemium Users
	public Integer callLimit;
}
