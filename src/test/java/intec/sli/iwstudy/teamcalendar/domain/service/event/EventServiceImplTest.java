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
package intec.sli.iwstudy.teamcalendar.domain.service.event;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import intec.sli.iwstudy.teamcalendar.domain.model.Event;
import intec.sli.iwstudy.teamcalendar.domain.service.ServiceTestBase;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Event サービスのテストケースです。
 * @author yukung
 *
 */
public class EventServiceImplTest extends ServiceTestBase {
	
	@Autowired
	private EventService sut;
	
	@Test
	public void testList() throws Exception {
		List<Event> list = sut.list();
		assertThat(list, is(notNullValue()));
		assertThat(list.size(), is(greaterThan(0)));
	}
	
	@Test
	public void testCreate() throws Exception {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusDays(1).toDate());
		
		int id = sut.create(event);
		assertThat(id, is(not(equalTo(0))));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateCaseOfNull() throws Exception {
		sut.create(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateCaseOfEmptyTitle() throws Exception {
		Event event = new Event();
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusHours(1).toDate());
		
		sut.create(event);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateCaseOfEmptyFrom() throws Exception {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setTo(DateTime.now().plusHours(1).toDate());
		
		sut.create(event);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateCaseOfEmptyTo() throws Exception {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setFrom(DateTime.now().toDate());
		
		sut.create(event);
	}
	
	@Test
	public void testUpdate() throws Exception {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusDays(1).toDate());
		int id = sut.create(event);
		assertThat(id, is(not(equalTo(0))));
		event.setId(id);
		event.setText("イベント内容更新");
		
		boolean result = sut.update(event);
		assertThat(result, is(true));
		
		event.setText(null);
		
		boolean result2 = sut.update(event);
		assertThat(result2, is(true));
		
		event.setTo(null);
		
		boolean result3 = sut.update(event);
		assertThat(result3, is(true));
		
		event.setFrom(null);
		event.setText("内容のみ更新");
		
		boolean result4 = sut.update(event);
		assertThat(result4, is(true));
		
		event.setTo(new Date());
		
		boolean result5 = sut.update(event);
		assertThat(result5, is(true));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateCaseOfNull() throws Exception {
		sut.update(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateCaseOfemptyId() throws Exception {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusDays(1).toDate());
		int id = sut.create(event);
		assertThat(id, is(not(equalTo(0))));
		event.setId(null);
		event.setText("イベント内容更新");
		event.setFrom(new DateTime(2014, 2, 23, 12, 0).toDate());
		event.setFrom(new DateTime(2014, 2, 23, 19, 0).toDate());
		
		sut.update(event);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateCaseOfEmptyParameter() throws Exception {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusDays(1).toDate());
		int id = sut.create(event);
		assertThat(id, is(not(equalTo(0))));
		event.setFrom(null);
		event.setTo(null);
		event.setText(null);
		
		sut.update(event);
	}
	
	@Test
	public void testDelete() throws Exception {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusDays(1).toDate());
		int id = sut.create(event);
		assertThat(id, is(not(equalTo(0))));
		
		sut.delete(event);
		boolean result = sut.update(event);
		assertThat(result, is(false));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteCaseOfNull() throws Exception {
		sut.delete(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteCaseOfEmptyId() throws Exception {
		Event event = new Event();
		sut.delete(event);
	}
	
	@Test
	public void testEventEquals() throws Exception {
		Event event = new Event();
		Event event2 = new Event();
		assertThat(event.equals(event2), is(true));
		event2.setId(1);
		assertThat(event.equals(event2), is(false));
	}
}
