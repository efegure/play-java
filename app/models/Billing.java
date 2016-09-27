package models;

import java.math.BigDecimal;

import javax.persistence.*;

import com.avaje.ebean.Model;

@Entity
@Table(name = "billedPayment")
public class Billing extends Model {
	
	@OneToOne
	public Payment payment;
	
	@Id
	@SequenceGenerator(name = "billing", sequenceName = "billing_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing")
	public long id;

	public BigDecimal bill;
	//TODO make this more complicated class
}
