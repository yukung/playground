package intec.iwstudy.backlog

import org.apache.xmlrpc.XmlRpcException
import spock.lang.Shared
import spock.lang.Specification

class BacklogAPIClientSpec extends Specification {
    static final BACKLOG_API_URL = 'https://intec.backlog.jp/XML-RPC'
    static final PROJECT_KEY = 'IWSTUDY'
    @Shared
    def client

    def setupSpec() {
        def config = new ConfigSlurper().parse(getClass().classLoader.getResource('config.groovy'))
        def username = config.reminder.provider.backlog.username
        def password = config.reminder.provider.backlog.password
        client = new BacklogAPIClient(BACKLOG_API_URL, username, password)
    }

    def "BacklogAPIClient のインスタンスを取得できること"() {
        expect:
        client != null
    }

    def "BacklogAPIにアクセスするのに必要な Basic 認証が成功してAPIアクセスができること"() {
        when:
        client.getProjects()
        then:
        notThrown(XmlRpcException)
    }

    def "BacklogAPIにアクセスするのに必要な Basic 認証がパスワードが妥当でなく 401 Unauthorized で返ってくること"() {
        when: "無効なパスワードが設定されている"
        def client = new BacklogAPIClient(BACKLOG_API_URL, 'invalid user', 'invalid PASSWORD.')
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
        def componentId = client.getComponents(projectId).find { it.name == 'お題' }.id
        assert componentId != null
        def issue = client.findIssue([projectId: projectId, componentId: componentId])
        then:
        issue != null
        issue.each {
            it.summary != null
            it.description != null
            it.versions != null
        }
    }
}
