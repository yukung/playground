import com.zaxxer.hikari.HikariConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.exec.Blocking
import ratpack.form.Form
import ratpack.groovy.sql.SqlModule
import ratpack.groovy.template.TextTemplateModule
import ratpack.hikari.HikariModule
import ratpack.jackson.Jackson
import ratpack.rx.RxRatpack
import ratpack.server.Service
import ratpack.server.StartEvent

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack

final Logger logger = LoggerFactory.getLogger(Ratpack.class)

ratpack {
    bindings {
        module HikariModule, { HikariConfig c ->
            c.addDataSourceProperty("URL", "jdbc:h2:mem:dev;INIT=CREATE SCHEMA IF NOT EXISTS DEV")
            c.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource")
        }
        module SqlModule
        module MessageModule
        module TextTemplateModule

        bindInstance Service, new Service() {
            @Override
            void onStart(StartEvent event) throws Exception {
                logger.info "Initializing RX"
                RxRatpack.initialize()
                event.registry.get(MessageService).init()
            }
        }
    }

    handlers { MessageService messageService ->
        get {
            render groovyTemplate("index.html", title: "Message App")
        }

        path('messages') {
            byMethod {
                get {
                    redirect '/'
                }
                post {
                    parse(Form).then { Form form ->
                        def contents = form.get('contents', '')
                        if (!contents) {
                            render groovyTemplate('index.html',
                                    title: "Message App",
                                    errorMessage: 'メッセージを入力してください'
                            )
                        } else {
                            Blocking.op {
                                messageService.create(contents)
                            }.then {
                                redirect '/'
                            }
                        }
                    }
                }
            }
        }

        delete('messages/:id') {
            def id = pathTokens['id'].toLong()
            Blocking.op {
                messageService.delete(id)
            }.then {
                render Jackson.json('success')
            }
        }

        path('api') {
            messageService.all().subscribe { List<Message> messages ->
                render Jackson.json(messages)
            }
        }

        files { dir "public" }
    }
}
