package models;

import java.math.BigDecimal;
import org.joda.time.DateTime;
import org.joda.time.Period;
import javax.persistence.*;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Invoice")
public class Invoice extends Model {
	
	@ManyToOne@JsonBackReference
	public Company company;
	
	@Id
	@SequenceGenerator(name = "invoice", sequenceName = "invoice_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice")
	public long id;

	public BigDecimal amount;
	
	public DateTime InvoiceDate;
	public DateTime dueDate;
	
	public String moneyType; 
	//TODO make this more complicated class
}
