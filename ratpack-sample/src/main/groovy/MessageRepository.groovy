import groovy.sql.Sql
import groovy.util.logging.Slf4j
import rx.Observable

import javax.inject.Inject
import java.sql.Timestamp

/**
 * @author Yusuke Ikeda
 */
@Slf4j
class MessageRepository {

    @Inject
    private Sql sql

    void initialize() {
        log.info "Creating tables..."
        sql.executeUpdate "DROP TABLE IF EXISTS messages"
        sql.executeUpdate "CREATE TABLE IF NOT EXISTS messages (id IDENTITY PRIMARY KEY, created_at TIMESTAMP, contents VARCHAR(255))"
    }

    Observable<List<Message>> findAll() {
        Observable.from(sql.rows("SELECT id, created_at, contents FROM messages ORDER BY created_at")).map {
            new Message(id: it.id, createdAt: it.created_at, contents: it.contents)
        }.toList()
    }

    def insert(Message message) {
        sql.executeInsert("INSERT INTO messages (created_at, contents) VALUES (?, ?)", new Timestamp(message.createdAt.getTime()), message.contents)
    }

    def delete(long id) {
        sql.executeUpdate("DELETE FROM messages WHERE id = ?", id)
    }
}
