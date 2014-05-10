package intec.iwstudy.backlog

import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl

class BacklogAPIClient {
    private XmlRpcClient client

    BacklogAPIClient(url, username, password) {
        def config = new XmlRpcClientConfigImpl()
        config.serverURL = new URL(url)
        config.basicUserName = username
        config.basicPassword = password
        client = new XmlRpcClient()
        client.setConfig(config)
    }

    @Override
    Object invokeMethod(String name, Object args) {
        return client.execute("backlog.$name", args as Object[])
    }
}
