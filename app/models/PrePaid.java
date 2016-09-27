package models;

import java.math.BigDecimal;

import javax.persistence.*;

import com.avaje.ebean.Model;

@Entity
@Table(name = "prepaidPayment")
public class PrePaid extends Model {
	
	@OneToOne
	public Payment payment;

	@Id
	@SequenceGenerator(name = "prepaid", sequenceName = "prepaid_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prepaid")
	public long id;
	
	public BigDecimal remainingtime;
	//TODO make this more complicated class
}
