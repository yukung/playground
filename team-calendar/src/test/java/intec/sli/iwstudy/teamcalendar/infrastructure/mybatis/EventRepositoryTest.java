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
package intec.sli.iwstudy.teamcalendar.infrastructure.mybatis;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import intec.sli.iwstudy.teamcalendar.domain.model.Event;
import intec.sli.iwstudy.teamcalendar.domain.repository.RepositoryTestBase;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Event エンティティを永続化する Repository の MyBatis 実装のテストケースです。
 * 
 * @author yukung
 *
 */
public class EventRepositoryTest extends RepositoryTestBase {
	
	@Autowired
	private EventRepository sut;
	
	@Before
	public void setUp() throws Exception {
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testPersistWhenCreate() {
		Event event = new Event();
		event.setText("新規イベント追加");
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusDays(1).toDate());
		
		Event persistedEvent = sut.persist(event);
		assertThat(persistedEvent, is(notNullValue()));
		assertThat(persistedEvent.getId(), is(notNullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPersistCaseOfNull() throws Exception {
		sut.persist(null);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Event> events = sut.findAll();
		assertThat(events, is(notNullValue()));
		assertThat(events.size(), is(greaterThan(1)));
	}
	
	@Test
	public void testFindOne() throws Exception {
		Event event = sut.find(Event.class, 45);
		assertThat(event, is(notNullValue()));
		assertThat(event.getId(), is(notNullValue()));
		assertThat(event.getText(), is(notNullValue()));
		assertThat(event.getFrom(), is(notNullValue()));
		assertThat(event.getTo(), is(notNullValue()));
	}
	
	@Test
	public void testCountAll() throws Exception {
		int count = sut.countAll();
		assertThat(count, is(greaterThan(0)));
	}
	
	@Test
	public void testPersistWhenUpdate() throws Exception {
		Event event = sut.find(Event.class, 45);
		Integer id = event.getId();
		String title = event.getText();
		Date from = event.getFrom();
		Date to = event.getTo();
		event.setText("更新したよ");
		
		Event persistedEvent = sut.persist(event);
		assertThat(persistedEvent, is(notNullValue()));
		assertThat(persistedEvent.getId(), is(id));
		assertThat(persistedEvent.getText(), is(not(equalTo(title))));
		assertThat(persistedEvent.getFrom(), is(equalTo(from)));
		assertThat(persistedEvent.getTo(), is(equalTo(to)));
	}
	
	@Test
	public void testPersistWhenUpdateCaseOfInvalidId() throws Exception {
		Event event = sut.find(Event.class, 1); // Invalid ID
		assertThat(event, is(nullValue()));
		event = new Event();
		event.setId(1); // Setting invalid ID
		event.setText("存在しないデータを更新しようとしている");
		Event persistedEvent = sut.persist(event);
		assertThat(persistedEvent, is(nullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPersistWhenUpdateCaseOfAllParamaterNothing() throws Exception {
		Event event = new Event();
		event.setId(45);
		sut.persist(event);
	}
	
	@Test
	public void testRemove() throws Exception {
		Event event = sut.find(Event.class, 45);
		Integer id = event.getId();
		assertThat(id, is(45));
		
		sut.remove(event);
		
		Event removed = sut.find(Event.class, id);
		assertThat(removed, is(nullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveCaseOfNull() throws Exception {
		sut.remove(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveCaseOfNullId() throws Exception {
		Event event = new Event();
		sut.remove(event);
	}
}
