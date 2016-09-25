package models;

import javax.persistence.*;

import com.avaje.ebean.Model;


@Entity
public class Payment extends Model {
	
	@Id
	@SequenceGenerator(name = "payment", sequenceName = "payment_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment")
	public long id;
	
	@OneToOne
	public Company company;
	
	@OneToOne
	public Billing billing;

	@OneToOne
	public PrePaid prepaid;
	
	public String method;
}
