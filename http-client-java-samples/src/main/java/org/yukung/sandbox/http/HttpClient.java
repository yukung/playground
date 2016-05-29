package org.yukung.sandbox.http;

/**
 * HTTP クライアントのサンプルインターフェース。
 *
 * @author yukung
 */
public interface HttpClient {

    /**
     * GET リクエストを行います。
     *
     * @param url リクエスト先 URL
     * @return レスポンスボディの文字列
     */
    String get(String url);

    /**
     * POST リクエストを行います。
     *
     * @param url  リクエスト先 URL
     * @param body リクエストボディの文字列
     * @return レスポンスボディの文字列
     */
    String post(String url, String body);

    /**
     * コネクションをクローズします。
     */
    void close();
}
