
package domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Procession extends DomainEntity {

	private String			title;			//
	private String			description;	//
	private Date			moment;		//
	private String			ticker;
	private Boolean			isDraftMode;	//
	private Integer			rowNumber;		//
	private Integer			columnNumber;	//

	private List<Float>		floats;
	private List<Request>	requests;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@Pattern(regexp = "[0-9]{2}[0-1]{1}[0-9]{3}-([A-Za-z0-9]{6})")
	@NotBlank
	@Column(unique = true)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotNull
	public Boolean getIsDraftMode() {
		return this.isDraftMode;
	}

	public void setIsDraftMode(final Boolean isDraftMode) {
		this.isDraftMode = isDraftMode;
	}

	@NotNull
	@Min(1)
	public Integer getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(final Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	@NotNull
	@Min(1)
	public Integer getColumnNumber() {
		return this.columnNumber;
	}

	public void setColumnNumber(final Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	@ManyToMany
	public List<Float> getFloats() {
		return this.floats;
	}

	public void setFloats(final List<Float> floats) {
		this.floats = floats;
	}

	@Valid
	@OneToMany(mappedBy = "procession")
	public List<Request> getRequests() {
		return this.requests;
	}

	public void setRequests(final List<Request> requests) {
		this.requests = requests;
	}

}
