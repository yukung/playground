import groovy.util.logging.Slf4j

import javax.inject.Inject

/**
 * @author Yusuke Ikeda
 */
@Slf4j
class MessageService {

    @Inject
    MessageRepository repository

    void init() {
        repository.initialize()
    }

    def all() {
        repository.findAll()
    }

    def create(String contents) {
        Message message = new Message(createdAt: new Date(), contents: contents)
        repository.insert(message)
    }

    def delete(long id) {
        repository.delete(id)
    }
}
