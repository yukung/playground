import groovy.transform.Canonical

/**
 * @author Yusuke Ikeda
 */
@Canonical
class Message {
    Long id
    Date createdAt
    String contents
}
