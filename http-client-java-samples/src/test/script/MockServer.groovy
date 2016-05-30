import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer

class SimpleHttpHandler implements HttpHandler {

    @Override
    void handle(HttpExchange httpExchange) throws IOException {
        try {
            def builder = new StringBuilder()
            builder << "Accessed URL = ${httpExchange.requestURI}" << "\r\n"
            builder << "Accessed Method = ${httpExchange.requestMethod}" << "\r\n"
            builder << "Accessed Date = ${new Date()}" << "\r\n"

            switch (httpExchange.requestMethod) {
                case "GET":
                    break
                case "POST":
                    builder << "Request Body<<" << "\r\n"
                    builder << httpExchange.requestBody.getText("UTF-8")
                    builder << ">>" << "\r\n"
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
