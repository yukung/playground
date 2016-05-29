import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer

class SimpleHttpHandler implements HttpHandler {

    @Override
    void handle(HttpExchange httpExchange) throws IOException {
        try {
            def builder = new StringBuilder()
            builder << "Accessed URL = ${httpExchange.requestURI}" << System.properties['line.separator']
            builder << "Accessed Method = ${httpExchange.requestMethod}" << System.properties['line.separator']
            builder << "Accessed Date = ${new Date()}" << System.properties['line.separator']

            switch (httpExchange.requestMethod) {
                case "GET":
                    break
                case "POST":
                    builder << "Request Body<<" << System.properties['line.separator']
                    builder << httpExchange.requestBody.getText("UTF-8")
                    builder << ">>" << System.properties['line.separator']
            }

            def bytes = builder.toString().getBytes("UTF-8")
            httpExchange.sendResponseHeaders(200, bytes.length)
            httpExchange.responseBody.withStream { it.write(bytes) }
        } catch (e) {
            e.printStackTrace()

            def message = "Server Error ${e}"
            def bytes = message.getBytes("UTF-8")
            httpExchange.sendResponseHeaders(500, bytes.length)
            httpExchange.requestBody.withStream { it.write(bytes) }
        }
    }
}

server = HttpServer.create(new InetSocketAddress(8080), 0)
server.createContext("/", new SimpleHttpHandler())
server.start()

println("LightHttpd Startup. ${new Date()}")
