fun logStacktrace() {
    val name = Thread.currentThread().name
    println(Exception("thread: $name").stackTraceToString())
}