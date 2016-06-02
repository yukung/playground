package intec.iwstudy.typetalk

import groovyx.net.http.RESTClient

class TypetalkAPIClient {

    private client = new RESTClient('https://typetalk.in')

    private String accessToken
    private String tokenType
    private String refreshToken
    private String clientId
    private String clientSecret
    private String grantType
    private String scope

    TypetalkAPIClient(clientId, clientSecret, scope) {
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.grantType = "client_credentials"
        this.scope = scope
    }

    def postTopic(param) {
        if (accessToken == null) {
            authorize()
        }
        client.post(
                path: "/api/v1/topics/${param.topicId}",
                headers: [Authorization: "$tokenType $accessToken"],
                requestContentType: 'application/x-www-form-urlencoded',
                body: [message: param.message]
        ).data
    }

    def listTopicMembers(param) {
        if (accessToken == null) {
            authorize()
        }
        client.get(
                path: "/api/v1/topics/${param.topicId}/members/status",
                headers: [Authorization: "$tokenType $accessToken"]
        ).data
    }

    def getPost(param) {
        if (accessToken == null) {
            authorize()
        }
        client.get(
                path: "/api/v1/topics/${param.topicId}/posts/${param.postId}",
                headers: [Authorization: "$tokenType $accessToken"]
        ).data
    }

    def deletePost(param) {
        if (accessToken == null) {
            authorize()
        }
        client.delete(
                path: "/api/v1/topics/${param.topicId}/posts/${param.postId}",
                headers: [Authorization: "$tokenType $accessToken"],
                contentType: 'text/plain'
        ).status
    }

    private authorize() {
        def body = client.post(
                path: '/oauth2/access_token',
                requestContentType: 'application/x-www-form-urlencoded',
                body: [client_id: clientId, client_secret: clientSecret, grant_type: grantType, scope: scope]
        ).data
        accessToken = body.access_token
        tokenType = body.token_type
        refreshToken = body.refresh_token
    }
}
