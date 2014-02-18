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
package intec.sli.iwstudy.teamcalendar.domain.service;

public class DataNotFoundException extends ServiceException {
	
	private static final long serialVersionUID = -5001865138104363666L;
	
	public DataNotFoundException() {
	}
	
	public DataNotFoundException(String message) {
		super(message);
	}
	
	public DataNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}