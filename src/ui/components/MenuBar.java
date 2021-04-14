package ui.components;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Raymond
 */
public class MenuBar extends JMenuBar {

    /**
     * 在此添加菜单事件
     */
    public interface MenuBarEvent {
        /**
         * 新游戏
         */
        void newGame();

        /**
         * 存档
         */
        void saveGame();

        /**
         * 读档
         */
        void loadArchive();

        /**
         * 退游
         */
        void exit();
    }

    MenuBarEvent handler;

    public void setHandler(MenuBarEvent handler) {
        this.handler = handler;
    }

    public MenuBar() {
        super();
        JMenu start = new JMenu("Start");
        addMenu(start, "New Game", e -> {
            int choice = JOptionPane.showOptionDialog(null,
                    "Start a new game?",
                    "Start",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);

            if (choice == JOptionPane.YES_OPTION) {
                handler.newGame();
            }
        });
        addMenu(start, "Save", e -> handler.saveGame());
        addMenu(start, "Load", e -> handler.loadArchive());
        addMenu(start, "Exit", e -> {
            int choice = JOptionPane.showOptionDialog(null,
                    "You really want to quit?",
                    "Quit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, null);

            if (choice == JOptionPane.YES_OPTION) {
                handler.exit();
            }
        });
        this.add(start);
    }

    /**
     * 对于添加菜单方法的简单封装
     *
     * @param handler  父菜单
     * @param name     菜单项名
     * @param callback 回调函数
     */
    void addMenu(JMenu handler, String name, ActionListener callback) {
        if (handler == null) {
            return;
        }
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(callback);
        handler.add(item);
    }
}
