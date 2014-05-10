package intec.iwstudy.backlog

import org.apache.xmlrpc.XmlRpcException
import spock.lang.Specification

class BacklogAPIClientSpec extends Specification {
    static final BACKLOG_API_URL = 'https://intec.backlog.jp/XML-RPC'
    static final PROJECT_KEY = 'IWSTUDY'
    static final USER_NAME = 'yusukei'
    static final PASSWORD = 'xxxxxxxxxxxx' // FIX ME the valid password here.

    def client = new BacklogAPIClient(BACKLOG_API_URL, USER_NAME, PASSWORD)

    def "BacklogAPIClient のインスタンスを取得できること"() {
        expect:
        client != null
    }

    def "BacklogAPIにアクセスするのに必要な Basic 認証が成功してAPIアクセスができること"() {
        when:
        def projects = client.getProjects()
        then:
        notThrown(XmlRpcException)
    }

    def "BacklogAPIにアクセスするのに必要な Basic 認証がパスワードが妥当でなく 401 Unauthorized で返ってくること"() {
        when: "無効なパスワードが設定されている"
        def client = new BacklogAPIClient(BACKLOG_API_URL, USER_NAME, "invalid password.")
        client.getProjects()
        then:
        def e = thrown(XmlRpcException)
        e.message.contains('Unauthorized')
    }

}
