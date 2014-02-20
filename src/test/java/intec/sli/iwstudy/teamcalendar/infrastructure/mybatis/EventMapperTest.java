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

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@code EventMapper} のテストケース。
 * 
 * @author yukung
 *
 */
public class EventMapperTest extends InfrastructureTestBase {
	
	@Autowired
	private EventMapper sut;
	
	@Before
	public void setUp() throws Exception {
		assertThat(sut, is(notNullValue()));
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Event> events = sut.findAll();
		assertThat(events, is(notNullValue()));
		assertThat(events.size(), is(32));
		Event firstRow = events.get(0);
		assertThat(firstRow.getId(), is(38));
		assertThat(firstRow.getText(), is("Third Annual Progressive Marketing Summit"));
		assertThat(firstRow.getFrom(), is(new DateTime(2012, 2, 7, 11, 30).toDate()));
		assertThat(firstRow.getTo(), is(new DateTime(2012, 2, 7, 16, 45).toDate()));
		Event lastRow = events.get(events.size() - 1);
		assertThat(lastRow.getId(), is(75));
		assertThat(lastRow.getText(), is("mloc.js"));
		assertThat(lastRow.getFrom(), is(new DateTime(2013, 2, 14, 9, 0).toDate()));
		assertThat(lastRow.getTo(), is(new DateTime(2013, 2, 16, 19, 0).toDate()));
	}
	
	@Test
	public void testFindById() throws Exception {
		Event event = sut.findById(65);
		assertThat(event, is(notNullValue()));
		assertThat(event.getText(), is("Manchester Hackathon"));
		assertThat(event.getFrom(), is(new DateTime(2013, 2, 16, 14, 0).toDate()));
		assertThat(event.getTo(), is(new DateTime(2013, 2, 17, 18, 0).toDate()));
	}
	
	@Test
	public void testInsert() throws Exception {
		// Check initial data count
		List<Event> events = sut.findAll();
		assertThat(events.size(), is(32));
		// fixture ready
		Event event = new Event();
		event.setText("２バイト文字も入るはず");
		// 1 week event from now
		Date from = new Date();
		Date to = new DateTime(from).plusWeeks(1).toDate();
		event.setFrom(from);
		event.setTo(to);
		
		// この時点では ID は不定なはず
		assertThat(event.getId(), is(nullValue()));
		int count = sut.insert(event);
		assertThat(count, is(1));
		// MyBatis で auto increment 値を Entity に入れてくれているはず
		assertThat(event.getId(), is(76));
		
		// 改めて取り直す
		Event refetch = sut.findById(76);
		assertThat(refetch.getId(), is(76));
		assertThat(refetch.getText(), is("２バイト文字も入るはず"));
		assertThat(refetch.getFrom(), is(equalTo(from)));
		assertThat(refetch.getTo(), is(equalTo(to)));
	}
	
	@Test
	public void testUpdate() throws Exception {
		// Check initial data count
		List<Event> events = sut.findAll();
		assertThat(events.size(), is(32));
		// 更新対象のレコードを１つ取得
		int TARGET_ID = 47;
		Event event = sut.findById(TARGET_ID);
		// タイトルを和訳して、日付を明後日の10時から3日後の19時までにする
		event.setText("HTML5とBaaSによる初めてのモバイルアプリ開発");
		DateTime baseDateTime = new DateTime().withTimeAtStartOfDay().plusDays(2).plusHours(10);
		Date from = baseDateTime.toDate();
		Date to = baseDateTime.plusDays(3).plusHours(9).toDate();
		event.setFrom(from);
		event.setTo(to);
		
		int count = sut.update(event);
		assertThat(count, is(1));
		
		// 改めて取り直す
		Event refetch = sut.findById(TARGET_ID);
		assertThat(refetch.getId(), is(TARGET_ID));
		assertThat(refetch.getText(), is("HTML5とBaaSによる初めてのモバイルアプリ開発"));
		assertThat(refetch.getFrom(), is(equalTo(from)));
		assertThat(refetch.getTo(), is(equalTo(to)));
	}
	
	@Test
	public void testUpdate_ExcludeText() throws Exception {
		// 更新対象のレコードを１つ取得
		int TARGET_ID = 47;
		Event event = sut.findById(TARGET_ID);
		// 変更前の状態
		String text = event.getText();
		Date from = event.getFrom();
		Date to = event.getTo();
		// empty title
		event.setText(null);
		// update duration
		event.setFrom(new Date());
		event.setTo(DateTime.now().plusDays(1).toDate());
		
		int count = sut.update(event);
		assertThat(count, is(1));
		
		// 改めて取り直す
		Event refetch = sut.findById(TARGET_ID);
		assertThat(refetch.getId(), is(TARGET_ID));
		// 更新されていないはず
		assertThat(refetch.getText(), is(equalTo(text)));
		// 更新されているはず
		assertThat(refetch.getFrom(), is(not(equalTo(from))));
		assertThat(refetch.getTo(), is(not(equalTo(to))));
	}
	
	@Test
	public void testUpdate_excludeFrom() throws Exception {
		// 更新対象のレコードを１つ取得
		int TARGET_ID = 47;
		Event event = sut.findById(TARGET_ID);
		// 変更前の状態
		String text = event.getText();
		Date from = event.getFrom();
		Date to = event.getTo();
		// empty from
		event.setFrom(null);
		// update title and to
		event.setText("更新したよ");
		event.setTo(new Date());
		
		int count = sut.update(event);
		assertThat(count, is(1));
		
		// 改めて取り直す
		Event refetch = sut.findById(TARGET_ID);
		assertThat(refetch.getId(), is(TARGET_ID));
		// 更新されているはず
		assertThat(refetch.getText(), is(not(equalTo(text))));
		assertThat(refetch.getTo(), is(not(equalTo(to))));
		// 更新されていないはず
		assertThat(refetch.getFrom(), is(equalTo(from)));
	}
	
	@Test
	public void testUpdate_excludeTo() throws Exception {
		// 更新対象のレコードを１つ取得
		int TARGET_ID = 47;
		Event event = sut.findById(TARGET_ID);
		// 変更前の状態
		String text = event.getText();
		Date from = event.getFrom();
		Date to = event.getTo();
		// empty to
		event.setTo(null);
		// update title and to
		event.setText("更新したよ");
		event.setFrom(new Date());
		
		int count = sut.update(event);
		assertThat(count, is(1));
		
		// 改めて取り直す
		Event refetch = sut.findById(TARGET_ID);
		assertThat(refetch.getId(), is(TARGET_ID));
		// 更新されているはず
		assertThat(refetch.getText(), is(not(equalTo(text))));
		assertThat(refetch.getFrom(), is(not(equalTo(from))));
		// 更新されていないはず
		assertThat(refetch.getTo(), is(equalTo(to)));
	}
	
	@Test
	public void testDelete() throws Exception {
		// Check initial data count
		List<Event> events = sut.findAll();
		int ROW_COUNT = 32;
		assertThat(events.size(), is(ROW_COUNT));
		// 削除対象のレコードを最終行から１つ取得
		int TARGET_ID = 75;
		Event event = sut.findById(TARGET_ID);
		assertThat(event.getId(), is(TARGET_ID));
		
		int count = sut.delete(event);
		assertThat(count, is(1));
		
		// 改めて取り直す
		Event refetch = sut.findById(TARGET_ID);
		assertThat(refetch, is(nullValue()));
		
		List<Event> refetches = sut.findAll();
		assertThat(refetches.size(), is(ROW_COUNT - 1));
	}
	
	@Test
	public void testCountAll() throws Exception {
		int count = sut.countAll();
		assertThat(count, is(32));
	}
}
