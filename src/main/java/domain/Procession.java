
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Procession extends DomainEntity {

	private String			title;
	private String			description;
	private Date			moment;
	private String			ticker;
	private Boolean			isDraftMode;
	private Integer			rowNumber;
	private Integer			columnNumber;

	private List<Coach>		coachs;
	private List<Request>	requests;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@NotNull
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@Pattern(regexp = "[0-9]{2}[0-1]{1}[0-9]{3}-([A-Z]{5})")
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
	public Integer getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(final Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	@NotNull
	public Integer getColumnNumber() {
		return this.columnNumber;
	}

	public void setColumnNumber(final Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	@ManyToMany
	public List<Coach> getCoachs() {
		return this.coachs;
	}

	public void setCoachs(final List<Coach> coachs) {
		this.coachs = coachs;
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
