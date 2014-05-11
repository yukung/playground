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

    def "プロジェクトキーを指定してプロジェクト情報を取得できる"() {
        when:
        def project = client.getProject(PROJECT_KEY)
        then:
        project != null
        project.name == 'IWStudy'
        project.url == 'https://intec.backlog.jp/projects/IWSTUDY'
    }

    def "プロジェクトIDを指定してカテゴリを取得できる"() {
        when:
        def projectId = client.getProject(PROJECT_KEY).id
        def components = client.getComponents(projectId)
        then:
        components != null
        components.each { it == Object[] }
        components.id.each { it == Integer }
        components.name.each { it == String }
    }

    def "プロジェクトIDとカテゴリ名を指定して、課題が検索できる"() {
        when:
        def projectId = client.getProject(PROJECT_KEY).id
        def componentId = client.getComponents(projectId).find { it.name == 'お題'}.id
        assert componentId != null
        def issue = client.findIssue([projectId: projectId, componentId: componentId])
        then:
        issue != null
        issue.each { println "${it.summary} : ${it.description.split(/\n/)[0]} : ${it.versions}" }
    }
}
