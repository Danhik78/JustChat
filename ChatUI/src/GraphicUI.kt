import ru.danhik.net.bank.client.gui.LogInWindow
import java.awt.Color
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*


class GraphicUI(sendFunction: (String) -> Unit) : JFrame(), ChatUI {
    companion object {
        const val GROW = GroupLayout.DEFAULT_SIZE
        const val SHRINK = GroupLayout.PREFERRED_SIZE
        const val URS = 1
        const val MSG = 2
        const val SYS = 0
    }

    fun writeListInLabel() {
        val s = java.lang.StringBuilder()
        s.append("<html>")
        usersList.forEach {
            s.append(it + "<br>")
        }
        s.append("</html>")
        listLabel.text = s.toString()
    }

    val usersList = mutableListOf<String>()
    val listLabel = JLabel()
    val chatWind = JPanel().apply { this.background = Color.WHITE }
    val scrChat = JScrollPane(chatWind).apply { autoscrolls = true }
    val txtField = JTextField().apply {
        addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {
            }

            override fun keyPressed(e: KeyEvent?) {
                if (e?.keyCode == KeyEvent.VK_ENTER && !e.isShiftDown) {
                    sendFunction("MSG:" + text)
                    text = ""
                }
            }

            override fun keyReleased(e: KeyEvent?) {

            }
        })
    }
    val btnSend = JButton("Отправить").apply {
        addActionListener {
            sendFunction("MSG:" + txtField.text)
            txtField.text = ""
        }
    }

    private val vl = VerticalLayout()

    init {

        setLocation(300, 50)
        size = Dimension(400, 600)
        minimumSize = Dimension(120, 90)
        isVisible = true
        title = "Чатовый чат"
        defaultCloseOperation = EXIT_ON_CLOSE

        val gl = GroupLayout(this.contentPane)


        chatWind.layout = vl

        gl.setVerticalGroup(
                gl.createParallelGroup()
                        .addGroup(
                                gl.createSequentialGroup()
                                        .addGap(8)
                                        .addGroup(gl.createParallelGroup()
                                                .addComponent(scrChat)
                                                .addComponent(listLabel)
                                        )
                                        .addGap(8)
                                        .addGroup(
                                                gl.createParallelGroup()
                                                        .addComponent(txtField, SHRINK, SHRINK, SHRINK)
                                                        .addComponent(btnSend)
                                        )
                                        .addGap(8)
                        )
        )

        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGap(8)
                        .addGroup(
                                gl.createParallelGroup()
                                        .addGroup(
                                                gl.createSequentialGroup()
                                                        .addComponent(txtField, SHRINK, SHRINK, GROW)
                                                        .addGap(2)
                                                        .addComponent(btnSend)
                                        )
                                        .addGroup(gl.createSequentialGroup()
                                                .addComponent(listLabel, SHRINK, SHRINK, SHRINK)
                                                .addComponent(scrChat)
                                        )
                        )
                        .addGap(8)
        )
        layout = gl
    }


    fun writeInChat(str: String, code: Int, colorRgb: Int = 0) {

        chatWind.add(JLabel().apply {
            name = code.toString()
            text = str
            when (code) {
                SYS -> {
                    foreground = Color(150, 150, 150)
                }

                URS -> {
                }

                MSG -> {
                    text = makeHtmlColorText(str, colorRgb)
                }
            }

        })

        scrChat.verticalScrollBar.value = scrChat.verticalScrollBar.maximum
        revalidate()

        scrChat.verticalScrollBar.value = scrChat.verticalScrollBar.maximum
    }

    fun makeHtmlColorText(str: String, colorRgb: Int): String {
        val sb = StringBuilder("<html>")
        val clrstr = str.split(":", limit = 2)
        val hex = String.format("#%06X", 0xFFFFFF and colorRgb)
        sb.append("<font color=$hex>")
        sb.append(clrstr[0])
        sb.append("</font>:")
        sb.append(clrstr[1])
        sb.append("</html>")
        return sb.toString()
    }

    override fun parse(data: String) {
        data.split(":", limit = 2).let {
            when (it[0]) {
                "INTR" -> {
                    openLogInWin()
                }

                "REINTR" -> {

                    logInWindow.logLabel.apply {
                        this.foreground = Color.RED
                        this.text = it[1]
                    }
                    logInWindow.revalidate()
                }

                "REGOK" -> {
                    regWindow.isVisible = false
                    logInWindow.isVisible = true
                }

                "REREG" -> {
                    regWindow.logLabel.apply {
                        this.foreground = Color.RED
                        this.text = it[1]
                    }
                    regWindow.revalidate()
                }

                "NAMEOK" -> {
                    isEnabled = true
                    requestFocus()
                    logInWindow.isVisible = false
                    writeInChat("Вы успешно вошли в чат", SYS)
                }

                "URMSG" -> {
                    writeInChat(it[1], URS)
                }

                "USRS" -> {
                    it[1].split(":").forEach { v -> usersList.add(v) }
                    writeListInLabel()
                }

                "SYS" -> {
                    writeInChat(it[1], SYS)
                }

                "MSG" -> {
                    val split2 = it[1].split(":", limit = 2)
                    writeInChat(split2[1], MSG, split2[0].toInt())
                }

                "NEW" -> {
                    writeInChat("Пользователь ${it[1]} вошёл в чат", SYS)
                    usersList.add(it[1])
                    writeListInLabel()
                }

                "EXIT" -> {
                    writeInChat("Пользователь ${it[1]} покинул чат", SYS)
                    usersList.remove(it[1])
                    writeListInLabel()
                }

                else -> {
                    writeInChat("Получена неизвестная команда: " + it[0], SYS)
                }
            }
        }
    }

    val logInWindow = LogInWindow().apply {
        isVisible = false

        btn.addActionListener {
            val pas = pf.text
            sendFunction("LOG:${tf.text}:$pas")
        }

        regbtn.addActionListener {
            isVisible = false
            regWindow.isVisible = true
        }
    }
    val regWindow = RegWindow().apply {
        isVisible = false

        btn.addActionListener {
            if (pf.text == pf2.text)
                sendFunction("REG:${tf.text}:${pf.text}")
            else {
                logLabel.foreground = Color.RED
                logLabel.text = "Пароли не совпадают"
            }
        }
    }

    private fun openLogInWin() {
        this.isEnabled = false
        logInWindow.isVisible = true
    }
}