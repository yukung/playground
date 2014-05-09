import spock.lang.Specification

/**
 * Created by yukung on 2014/05/09.
 */
class HelloTest extends Specification {
    def "SayHello"() {
        expect: hello == "Hello, Gradle!"

        where: hello = new Hello().sayHello()
    }
}
