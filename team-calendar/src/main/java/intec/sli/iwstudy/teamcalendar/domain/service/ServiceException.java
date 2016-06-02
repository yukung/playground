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

/**
 * サービス実行時の例外クラス。
 * 
 * <p>詳細な原因が区別できる場合は、このクラスを継承しより具体的な例外クラスを作成すること。
 * 
 * @author yukung
 *
 */
public class ServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 8696329535430941287L;
	
	/**
	 * デフォルトコンストラクタ。
	 */
	public ServiceException() {
	}
	
	/**
	 * メッセージ付きのコンストラクタ。
	 * 
	 * @param message メッセージ
	 */
	public ServiceException(String message) {
		super(message);
	}
	
	/**
	 * 例外原因付きのコンストラクタ。
	 * 
	 * @param cause 原因
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * メッセージと例外原因付きのコンストラクタ。
	 * 
	 * @param message メッセージ
	 * @param cause 原因
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}