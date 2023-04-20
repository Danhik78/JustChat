package ru.smak.net

import BDOper
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.smak.net.Communicator
import java.awt.Color
import java.net.Socket
import java.util.Random

/**
 * Организация работы с подключенным клиентом на стороне сервера
 * @param socket сокет подключенного клиента
 */
class ConnectedClient(
    private val socket: Socket,
) {
    companion object {
        private val _list = mutableListOf<ConnectedClient>()
        val list: List<ConnectedClient>
            get() = _list.toList()
    }

    private val cmn = Communicator(socket)

    private val r = Random(System.currentTimeMillis())
    var name: String? = null
        private set(value) {
            value?.let { vl ->
                if (list.find { it.name == value } == null) {
                    field = vl
                    clr= Color(r.nextInt(100)+100,r.nextInt(100)+100,r.nextInt(100)+100)
                    println("Пользователь $vl успешон вошёл")
                    sendToAllConnectedClients({ if (it == this) "NAMEOK" else "NEW" },
                        { if (it != this) vl else "" }
                    )
                    cmn.sendData("USRS"+writeList(_list))
                } else cmn.sendData("REINTR:Пользователь уже авторизован")
            } ?: cmn.sendData("REINTR:Не указан логин")
        }

    init {
        _list.add(this)
    }

    suspend fun start() {
        coroutineScope {
            launch {
                try {
                    cmn.startReceiving { parse(it) }
                } catch (e: Throwable) {
                    _list.remove(this@ConnectedClient)
                    name?.let {
                        sendToAllConnectedClients({ "EXIT" }, { _ -> it })
                    }
                }
            }
            launch {
                cmn.sendData("INTR:")
            }
        }
    }
    val bd = BDOper()
    var clr :Color?=Color.BLACK
    fun parse(data: String) {
        /*
        if (name != null) sendToAllConnectedClients({ if (it==this)"URMSG" else "MSG:${clr?.rgb}" }, { "${if (it == this) "" else name+':'} $data" })
        else name = data
        */

        val str = data.split(":",limit=2)
        when (str[0]){
            "MSG"->{
                if (name != null) sendToAllConnectedClients({ if (it==this)"URMSG" else "MSG:${clr?.rgb}" }, { "${if (it == this) "" else name+':'}${str[1]}" })
                else cmn.sendData("INTR:")
            }
            "REG"->{
                if(name!=null) {
                    cmn.sendData("SYS:Вы уже авторизованы")
                    return
                }
                val logps = str[1].split(":",limit=2)
                println("isFree"+bd.isUserFree(logps[0]))
                if(bd.isUserFree(logps[0])){
                    bd.addUser(logps[0],logps[1])
                    cmn.sendData("REGOK:Регистрация прошла успешно")
                    println("Зарегистрирован пользователь ${logps[0]}")
                }
                else cmn.sendData("REREG:Логин занят")
            }
            "LOG"->{
                if(name!=null) {
                    cmn.sendData("SYS:Вы уже авторизованы")
                    return
                }
                val logps = str[1].split(":",limit=2)
                if(bd.CheckUser(logps[0],logps[1])){
                    name = logps[0]
                }else cmn.sendData("REINTR:Неверный логин или пароль")
            }
            else ->{
                cmn.sendData("SYS:Неизвестная команда")
            }
        }
    }

    fun writeList(list: MutableList<ConnectedClient>): String {
        val s = java.lang.StringBuilder()
        list.forEach {
            s.append(":"+it.name)
        }
        return s.toString()
    }
    private fun sendToAllConnectedClients(cmd: (ConnectedClient) -> String, data: (ConnectedClient) -> String) {
        _list.forEach {
            it.name?.let {_->
                it.cmn.sendData("${cmd(it)}:${data(it)}");
            }
        }
    }
}