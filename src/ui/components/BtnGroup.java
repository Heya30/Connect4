package ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * @author Fimon
 */
public class BtnGroup extends JPanel {

    private static final int HEIGHT = 30;

    BtnEvent handler;

    private final JButton[] btns;

    public void setHandler(BtnEvent handler) {
        this.handler = handler;
    }

    public interface BtnEvent {
        /**
         * 按键点击事件
         * @param i 按钮序号
         */
        void buttonClick(int i);
    }

    public BtnGroup(int nums) {

        //按键采用GridLayout布局
        GridLayout gridLayoutForButtons = new GridLayout(1, nums, BoardPanel.GAP, 0);
        this.setLayout(gridLayoutForButtons);
        this.setPreferredSize(new Dimension(BoardPanel.WIDTH, HEIGHT));
        btns = new JButton[nums];
        for (int i = 0; i < nums; i++) {
            btns[i] = new JButton("↓");
            btns[i].setSize(BoardPanel.SQUARE_SIZE, HEIGHT);
            this.add(btns[i]);

            final int column = i;
            btns[i].addActionListener(e -> handler.buttonClick(column));
        }
    }

    public void disableBtns() {
        for (JButton button : btns) {
            button.setText("");
            button.setEnabled(false);
        }
    }

    public void enableBtns() {
        for (JButton button : btns) {
            button.setText("↓");
            button.setEnabled(true);
        }
    }
}
