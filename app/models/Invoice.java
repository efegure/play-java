package models;

import java.math.BigDecimal;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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

	private boolean isCurrent; 
	private boolean isPaid; 
	public BigDecimal amount;
	
	public LocalDate InvoiceDate;
	public LocalDate dueDate;
	
	//TODO make this more complicated class
	
	public Invoice(){
		isCurrent=true;
		isPaid=false;
		LocalDate duedate= new LocalDate();
		InvoiceDate = duedate;
		dueDate = duedate.plusWeeks(1);
	}
	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
}
