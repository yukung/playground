package intec.iwstudy.typetalk

import groovyx.net.http.HttpResponseException
import spock.lang.Shared
import spock.lang.Specification

class TypetalkAPIClientSpec extends Specification {
    static final TOPIC_ID = 5042
    static final MESSAGE = 'API経由でのポストだよん'
    @Shared
    private TypetalkAPIClient client

    def setupSpec() {
        def typetalk = new ConfigSlurper().parse(getClass().classLoader.getResource('config.groovy')).reminder.provider.typetalk
        client = new TypetalkAPIClient(typetalk.clientId, typetalk.clientSecret, 'topic.read,topic.post')
    }

    def "Typetalk API にアクセスして、メッセージの投稿、取得、削除ができること"() {
        when:
        def response1 = client.postTopic([topicId: TOPIC_ID, message: MESSAGE])
        then:
        notThrown(HttpResponseException)
        when:
        def postId = response1.post.id
        def response2 = client.getPost([topicId: TOPIC_ID, postId: postId])
        then:
        response2.post.message == MESSAGE
        expect:
        client.deletePost([topicId: TOPIC_ID, postId: postId]) == 200
    }

    def "Typetalk API にアクセスして、トピック参加メンバーを取得できること"() {
        when:
        def response = client.listTopicMembers(topicId: TOPIC_ID)
        then:
        response.accounts.size == 2
        response.accounts.find { it.account.name == 'iwbot' } != null
    }
}
