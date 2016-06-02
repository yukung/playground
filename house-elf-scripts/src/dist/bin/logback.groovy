def USER_HOME = System.getProperty("user.home")

appender("CONSOLE", ConsoleAppender) {
	encoder(PatternLayoutEncoder) {
	    pattern = "%d{yyyy-MMM-dd HH:mm:ss.SSS} [%thread] %-5level %marker %logger - %msg%n"
	}
}
appender("FILE", FileAppender) {
	file = "${USER_HOME}/reminder.log"
	encoder(PatternLayoutEncoder) {
	    pattern = "%d{yyyy-MMM-dd HH:mm:ss.SSS} [%thread] %-5level %marker %logger - %msg%n"
	}
}
root(INFO, ["CONSOLE", "FILE"])
