import kotlinx.coroutines.*

class ConsoleUI(sendFunction: (String) -> Unit) : ChatUI {
    override fun parse(data: String) {
        data.split(":", limit = 2).let {
            when (it[0]) {
                "INTR" -> {
                    print("Представьте себя: ")
                }

                "REINTR" -> {
                    print("Имя занято, выберите другое: ")
                }

                "NAMEOK" -> {
                    println("Вы успешно вошли в чат")
                }
                "URMSG" ->{
                    println("Вы:"+it[1])
                }
                "MSG" -> {
                    println(it[1].split(":", limit = 2)[1])
                }

                "NEW" -> {
                    println("Пользователь ${it[1]} вошёл в чат")
                }

                "EXIT" -> {
                    println("Пользователь ${it[1]} покинул чат")
                }
            }
        }
    }
    init{
        CoroutineScope(Dispatchers.IO + Job()).launch {
            while (true) {
                val s = readlnOrNull() ?: ""
                sendFunction(s)
            }
        }
    }
}