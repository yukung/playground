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
/**
 * ドメイン層のサービスを使って、アプリケーション固有の処理を提供するクラスが
 * 収められたパッケージです。
 * 
 * Application 層のサービスは、ユーザーからのインターフェースとなる層で、
 * UI層に位置づけられます。
 * 
 * このアプリケーションでは、Web アプリケーション及び REST API としてサービスを提供するため、
 * サブパッケージとして web パッケージと rest パッケージ を置きます。
 * 
 * @author yukung
 *
 */
package intec.sli.iwstudy.teamcalendar.app;