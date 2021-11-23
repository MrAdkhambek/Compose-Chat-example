package r2.llc.sch.util

interface Parser {
    operator fun invoke(value: String): Map<String, String>
}

class JoinUserParser : Parser {

    override fun invoke(value: String): Map<String, String> {
        val group = PARSER_REGEX.find(value)?.groupValues ?: return emptyMap()
        val userName = group.getOrNull(1) ?: return emptyMap()
        return mapOf(USER_NAME to userName)
    }

    companion object {
        private val PARSER_REGEX = "\\<i\\>\"?([\\s\\d\\w]+)\"? (joined the chat.)<\\/i>".toRegex()
        const val USER_NAME: String = "user.name"
    }
}


class LeftUserParser : Parser {

    override fun invoke(value: String): Map<String, String> {
        val group = PARSER_REGEX.find(value)?.groupValues ?: return emptyMap()
        val userName = group.getOrNull(1) ?: return emptyMap()
        return mapOf(USER_NAME to userName)
    }

    companion object {
        private val PARSER_REGEX = "\\<i\\>\"?([\\s\\d\\w]+)\"? (left the chat.)<\\/i>".toRegex()
        const val USER_NAME: String = "user.name"
    }
}

class MessageParser : Parser {

    override fun invoke(value: String): Map<String, String> {
        val group = PARSER_REGEX.find(value)?.groupValues ?: return emptyMap()
        val msg = group.getOrNull(2) ?: return emptyMap()
        val senderName = group.getOrNull(1) ?: return emptyMap()

        return mapOf(
            USER_NAME to senderName,
            MESSAGE to msg
        )
    }

    companion object {
        private val PARSER_REGEX = "\\<strong\\>\"?([\\s\\d\\w]+)\"?\\<\\/strong\\>: \"?([\\s\\d\\w]+)\"?".toRegex()
        const val USER_NAME: String = "user.name"
        const val MESSAGE: String = "user.message"
    }
}