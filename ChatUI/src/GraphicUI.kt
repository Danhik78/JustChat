
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


    val chatWind = JPanel().apply { this.background = Color.WHITE }
    val scrChat = JScrollPane(chatWind).apply { autoscrolls=true }
    val txtField = JTextField().apply {
        addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) {
            }

            override fun keyPressed(e: KeyEvent?) {
                if (e?.keyCode == KeyEvent.VK_ENTER && !e.isShiftDown) {
                    sendFunction(text)
                    text = ""
                }
            }

            override fun keyReleased(e: KeyEvent?) {

            }
        })
    }
    val btnSend = JButton("Отправить").apply {
        addActionListener {
            sendFunction(txtField.text)
            txtField.text = ""
        }
    }

    private val vl  = VerticalLayout()
    init {
        size = Dimension(400, 600)
        minimumSize = Dimension(120, 90)
        isVisible = true
        title = "Чатовый чат"
        defaultCloseOperation = EXIT_ON_CLOSE
        val gl = GroupLayout(this.contentPane)
        layout = gl

        chatWind.layout=vl

        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(8)
                .addComponent(scrChat)
                .addGap(8)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(txtField, SHRINK, SHRINK, SHRINK)
                        .addComponent(btnSend)
                )
                .addGap(8)
        )

        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(8)
                .addGroup(
                    gl.createParallelGroup()
                        .addGroup(
                            gl.createSequentialGroup()
                                .addComponent(txtField)
                                .addGap(2)
                                .addComponent(btnSend)
                        )
                        .addComponent(scrChat)
                )
                .addGap(8)
        )
    }




    fun writeInChat(str:String,code:Int,colorRgb:Int = 0)
    {

        chatWind.add(JLabel().apply {
            name = code.toString()
            text=str
            when (code){
                SYS->{
                    foreground = Color(150,150,150)
                }
                URS->{
                }
                MSG->{
                    text = makeHtmlColorText(str,colorRgb)
                }
            }

        })

        scrChat.verticalScrollBar.value= scrChat.verticalScrollBar.maximum
        revalidate()
    }

    fun makeHtmlColorText(str:String,colorRgb: Int):String{
        val sb = StringBuilder("<html>")
        val clrstr = str.split(":", limit = 2)
        val hex = String.format("#%06X", 0xFFFFFF and colorRgb)
        sb.append("<font color=$hex>")
        sb.append(clrstr[0])
        sb.append("</font>:")
        sb.append(clrstr[1])
        sb.append("</html>")
        return  sb.toString()
    }

    override fun parse(data: String) {
        data.split(":", limit = 2).let {
            when (it[0]) {
                "INTR" -> {
                    writeInChat("Представьтесь", SYS)
                }

                "REINTR" -> {
                    writeInChat("Имя занято, выберите другое: ", SYS)
                }

                "NAMEOK" -> {
                    writeInChat("Вы успешно вошли в чат", SYS)
                }

                "URMSG"->{

                    writeInChat(it[1], URS)
                }
                "MSG" -> {
                    val split2 = it[1].split(":",limit = 2)
                    writeInChat(split2[1], MSG,split2[0].toInt())
                }

                "NEW" -> {
                    writeInChat("Пользователь ${it[1]} вошёл в чат", SYS)
                }

                "EXIT" -> {
                    writeInChat("Пользователь ${it[1]} покинул чат", SYS)
                }

                else -> {
                    writeInChat("Получена неизвестная команда: "+it[0], SYS)
                }
            }
        }
    }
}