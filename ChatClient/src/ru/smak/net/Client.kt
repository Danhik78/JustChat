package ru.smak.net

import ConsoleUI
import GraphicUI
import kotlinx.coroutines.*
import java.io.Console
import java.net.Socket

class Client(
    host: String,
    port: Int,
) {
    private val s: Socket
    private val cmn: Communicator
    private val mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        s = Socket(host, port)
        cmn = Communicator(s)
    }

    fun start() = mainCoroutineScope.launch {
        launch {
            cmn.startReceiving {
                parse(it)
            }
        }
    }
        val ui = GraphicUI(cmn::sendData)
            //val ui = ConsoleUI(cmn::sendData)
    var parse =ui::parse


}