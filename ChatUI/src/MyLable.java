import javax.swing.*;
import java.awt.*;

class MyLabel extends JLabel {

    public MyLabel() {
        setPreferredSize(new Dimension(200,50));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        char[] text = getText().toCharArray();
        int baseline = getHeight() - 5;
        int x = 5;
        for (int i = 0; i < text.length; i++) {
            if (i % 4 == 3) {
                graphics.setColor(Color.RED);
            } else {
                graphics.setColor(Color.BLACK);
            }
            graphics.drawChars(text, i, 1, x, baseline);
            x += 9;
        }
    }
}
