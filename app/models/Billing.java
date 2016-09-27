package models;

import javax.persistence.*;

import com.avaje.ebean.Model;

@Entity
public class Billing extends Model {
	
	@OneToOne
	public Payment payment;
	
	@Id
	@SequenceGenerator(name = "billing", sequenceName = "billing_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing")
	public long id;

	public double bill;
	//TODO make this more complicated class
}
