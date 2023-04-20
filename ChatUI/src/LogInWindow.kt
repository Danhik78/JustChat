package ru.danhik.net.bank.client.gui

import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*

class LogInWindow : JFrame() {

    companion object {
        const val GROW = GroupLayout.DEFAULT_SIZE
        const val SHRINK = GroupLayout.PREFERRED_SIZE
    }


    val minSize = Dimension(230, 300)
    val btn = JButton()
    val regbtn = JButton("Зарегестрироваться")
    val tf = JTextField().apply {

    }
    val pf = JPasswordField()
    val lblRes = JLabel()
    val logLabel = JLabel("Логин:")
    val passLabel = JLabel("Пароль:")

    init {
        setLocation(400,200)
        isVisible = true
        title = "Вход"
        size = minSize
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false
        btn.text = "Войти"
        lblRes.text = "Войти по номеру карты..."
        val gl = GroupLayout(this.contentPane)
        layout = gl

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(logLabel)
                .addComponent(tf, SHRINK, SHRINK, SHRINK)
                .addComponent(passLabel)
                .addComponent(pf, SHRINK, SHRINK, SHRINK)
                .addGap(8)
                .addComponent(btn, SHRINK, SHRINK, SHRINK)
                .addGap(8, 8, Int.MAX_VALUE)
                .addComponent(regbtn,SHRINK, SHRINK, SHRINK)
                .addGap(8)
                /*.addComponent(lblRes,GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(5)*/
        )


        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(2, 2, 20)
                .addGroup(gl.createParallelGroup()
                        .addComponent(tf)
                        .addComponent(logLabel)
                        .addComponent(pf)
                        .addComponent(passLabel)
                        .addGroup(gl.createSequentialGroup()
                                .addGap(8, 8, Int.MAX_VALUE)
                                .addComponent(btn, SHRINK, SHRINK, SHRINK)
                                .addGap(8, 8, Int.MAX_VALUE)
                        )
                        .addGroup(gl.createSequentialGroup()
                                .addGap(8, 8, Int.MAX_VALUE)
                                .addComponent(regbtn,SHRINK, SHRINK, SHRINK)
                                .addGap(8, 8, Int.MAX_VALUE)
                        )
                        /*.addGroup(gl.createSequentialGroup()
                            .addGap(2,2,Int.MAX_VALUE)
                            .addComponent(lblRes, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(2,2,Int.MAX_VALUE)
                        )*/
                )
                .addGap(2, 2, 20))

    }
}