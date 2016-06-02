/*
 * Copyright (C) 2014 Yusuke Ikeda <yukung.i@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package intec.sli.iwstudy.teamcalendar.domain.model;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Event 内容を表現するドメインオブジェクト。
 * 
 * @author yukung
 *
 */
public class Event {
	
	/** Event の ID */
	private Integer id;
	
	/** Event の開始日時 */
	private Date from;
	
	/** Event の終了日時 */
	private Date to;
	
	/** Event の内容 */
	private String text;
	
	/**
	 * Event の ID を取得します。
	 * 
	 * @return ID
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Event の ID を設定します。
	 * 
	 * @param id ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Event の開始日時を取得します。
	 * 
	 * @return 開始日時
	 */
	public Date getFrom() {
		return from;
	}
	
	/**
	 * Event の開始日時を設定します。
	 * 
	 * @param from 開始日時
	 */
	public void setFrom(Date from) {
		this.from = from;
	}
	
	/**
	 * Event の終了日時を取得します。
	 * 
	 * @return 終了日時
	 */
	public Date getTo() {
		return to;
	}
	
	/**
	 * Event の終了日時を設定します。
	 * 
	 * @param to 終了日時
	 */
	public void setTo(Date to) {
		this.to = to;
	}
	
	/**
	 * Event の内容を取得します。
	 * 
	 * @return 内容
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Event の内容を設定します。
	 * 
	 * @param text 内容
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(id).append(text).append(from).append(to).toString();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(from).append(to).append(text).toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Event other = (Event) obj;
		return new EqualsBuilder()
			.append(this.id, other.id)
			.append(this.from, other.from)
			.append(this.to, other.to)
			.append(this.text, other.text)
			.isEquals();
	}
	
}