package intec.sli.iwstudy.teamcalendar.domain.model;

import java.util.Date;

public class Event {
	
	private Integer id;
	
	private Date from;
	
	private Date to;
	
	private String text;
	
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getFrom() {
		return from;
	}
	
	public void setFrom(Date from) {
		this.from = from;
	}
	
	public Date getTo() {
		return to;
	}
	
	public void setTo(Date to) {
		this.to = to;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
