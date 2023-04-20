import java.awt.Dimension
import javax.swing.*

class RegWindow : JFrame() {

    companion object{
        const val GROW = GroupLayout.DEFAULT_SIZE
        const val SHRINK = GroupLayout.PREFERRED_SIZE
    }



    val minSize = Dimension(230,300)
    val btn = JButton()
    val tf = JTextField().apply{

    }
    val pf = JPasswordField()
    val pf2 = JPasswordField()
    val lblRes = JLabel()
    val logLabel = JLabel("Логин:")
    val passLabel = JLabel("Пароль:")
    val passLabel2 = JLabel("Повторите пароль:")

    init{
        setLocation(400,200)
        isVisible = true
        title = "Регистрация"
        size = minSize
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false
        btn.text = "Зарегистрировать"
        btn.addActionListener{ }
        lblRes.text = "Войти по номеру карты..."
        val gl = GroupLayout(this.contentPane)
        layout = gl

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(10,10,10)
                .addComponent(logLabel)
                .addComponent(tf,SHRINK, SHRINK, SHRINK)
                .addComponent(passLabel)
                .addComponent(pf,SHRINK, SHRINK, SHRINK)
                .addComponent(passLabel2)
                .addComponent(pf2,SHRINK, SHRINK, SHRINK)
                .addGap(8)
                .addComponent(btn,SHRINK, SHRINK, SHRINK)
                .addGap(8,8,Int.MAX_VALUE)
                /*.addComponent(lblRes,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)*/
        )


        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(2,2, 20)
                .addGroup(gl.createParallelGroup()
                        .addComponent(tf)
                        .addComponent(logLabel)
                        .addComponent(pf)
                        .addComponent(passLabel)
                        .addComponent(pf2)
                        .addComponent(passLabel2)
                        .addGroup(gl.createSequentialGroup()
                                .addGap(8,8,Int.MAX_VALUE)
                                .addComponent(btn,SHRINK, SHRINK, SHRINK)
                                .addGap(8,8,Int.MAX_VALUE)
                        )
                        /*.addGroup(gl.createSequentialGroup()
                            .addGap(2,2,Int.MAX_VALUE)
                            .addComponent(lblRes, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(2,2,Int.MAX_VALUE)
                        )*/
                )
                .addGap(2,2, 20))

    }
}