package models;

import java.math.BigDecimal;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.avaje.ebean.Model;

@Entity
@Table(name = "prepaidPayment")
public class PrePaid extends Model {
	
	@OneToOne
	public Payment payment;

	public PrePaid(LocalDate start,LocalDate end){
		this.startDate=start;
		this.endDate=end;
	}
	
	@Id
	@SequenceGenerator(name = "prepaid", sequenceName = "prepaid_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prepaid")
	public long id;
	
	public LocalDate startDate;
	
	public LocalDate endDate;
	
	
}
