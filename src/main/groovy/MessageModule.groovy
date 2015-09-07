import com.google.inject.AbstractModule
import com.google.inject.Scopes

/**
 * @author Yusuke Ikeda
 */
class MessageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageRepository).in(Scopes.SINGLETON)
        bind(MessageService).in(Scopes.SINGLETON)
    }
}
