package ui;


import archive.ArchiveManager;
import core.Board;
import core.GameControl;
import core.Player;
import enumerations.CommonReturnType;
import enumerations.Status;
import ui.components.*;

import javax.swing.*;

/**
 * @author Fimon
 */
public class MainWindow extends JFrame {

    /**
     * 窗口大小参数
     */
    static final int WINDOW_WIDTH = 850;
    static final int WINDOW_HEIGHT = 670;

    /**
     * 窗口四周间隙参数
     */
    static final int GAP_HORIZONTAL = 5;
    static final int GAP_VERTICAL = 4;



    private JPanel panelMain;
    private BoardPanel panelChessBoardPanel;
    private PlayerPanel[] players;
    private CountdownTimer timeDisplay;
    private BtnGroup panelButtons;
    private MenuBar menuBar;

    public MainWindow() {
        super("Connect4");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initEventHandler();

        //开始计时
        timeDisplay.restartCountdown();
    }

    /**
     * 初始化各组件
     */
    private void initComponents() {
        //主体选用SpringLayout布局
        panelMain = new JPanel(new SpringLayout());
        this.setContentPane(panelMain);

        // 棋盘组件
        panelChessBoardPanel = new BoardPanel();
        panelChessBoardPanel.setGameControl(new GameControl());

        // 玩家信息组件
        players = new PlayerPanel[2];
        players[0] = new PlayerPanel("Player 1");
        players[1] = new PlayerPanel("Player 2");
        //玩家1先手
        players[0].switchStatus();

        //计时器组件
        timeDisplay = new CountdownTimer();

        // 按键组
        panelButtons = new BtnGroup(Board.getInstance().getCOL());

        // 添加菜单栏
        menuBar = new MenuBar();
        this.setJMenuBar(menuBar);
    }

    /**
     * 设置各组件位置
     */
    private void initLayout() {
        SpringLayout springLayout = (SpringLayout) panelMain.getLayout();
        //将棋盘加到panelMain中并设置其格式
        panelMain.add(panelChessBoardPanel);
        SpringLayout.Constraints panelChessBoardCons = springLayout.getConstraints(panelChessBoardPanel);
        panelChessBoardCons.setX(Spring.constant(GAP_HORIZONTAL));
        panelChessBoardCons.setY(Spring.constant(GAP_VERTICAL));

        // 将按钮组加到panelMain并设置其格式
        panelMain.add(panelButtons);
        SpringLayout.Constraints panelButtonsCons = springLayout.getConstraints(panelButtons);
        panelButtonsCons.setX(Spring.constant(GAP_HORIZONTAL));
        panelButtonsCons.setY(
                // padding-top: 20
                Spring.sum(
                        panelChessBoardCons.getConstraint(SpringLayout.SOUTH),
                        Spring.constant(20))
        );

        //将计时器组件加入到panelMain
        panelMain.add(timeDisplay);
        SpringLayout.Constraints timeDisplayCons = springLayout.getConstraints(timeDisplay);
        timeDisplayCons.setX(
                Spring.sum(
                        panelChessBoardCons.getConstraint(SpringLayout.EAST),
                        Spring.constant(80)
                )
        );
        timeDisplayCons.setY(Spring.constant(50));

        // 将玩家信息组加到panelMain并设置其格式
        panelMain.add(players[0]);
        SpringLayout.Constraints player1Cons = springLayout.getConstraints(players[0]);
        player1Cons.setX(
                Spring.sum(
                        panelChessBoardCons.getConstraint(SpringLayout.EAST),
                        Spring.constant(10)
                )
        );
        player1Cons.setY(
                Spring.sum(
                        timeDisplayCons.getConstraint(SpringLayout.SOUTH),
                        Spring.constant(50)
                )
        );

        panelMain.add(players[1]);
        SpringLayout.Constraints player2Cons = springLayout.getConstraints(players[1]);
        player2Cons.setX(
                Spring.sum(
                        panelChessBoardCons.getConstraint(SpringLayout.EAST),
                        Spring.constant(10)
                )
        );
        player2Cons.setY(
                Spring.sum(
                        player1Cons.getConstraint(SpringLayout.SOUTH),
                        Spring.constant(20)
                )
        );
    }

    /**
     * 设置各组件的事件处理方法
     */
    private void initEventHandler() {
        timeDisplay.setTimeoutCallback(() -> {
                    JOptionPane.showMessageDialog(null,
                            panelChessBoardPanel.getCore().getCurrPlayer() + " 超时了!");
                    panelChessBoardPanel.getCore().setGameState(Status.FAIL);
                }
        );

        menuBar.setHandler(new MenuBar.MenuBarEvent() {
            @Override
            public void newGame() {
                panelChessBoardPanel.getCore().reset();
                panelChessBoardPanel.repaint();
                panelButtons.enableBtns();
                players[0].reset();
                players[1].reset();
                players[0].switchStatus();
                timeDisplay.restartCountdown();
            }

            @Override
            public void saveGame() {
                CommonReturnType result = ArchiveManager.saveArchive(panelChessBoardPanel.getCore());
                if (result.getStatus() == CommonReturnType.FAIL) {
                    JOptionPane.showMessageDialog(null, result.getMessage(), "save", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, result.getMessage());
                }
            }

            @Override
            public void loadArchive() {
                CommonReturnType result = ArchiveManager.loadArchive(panelChessBoardPanel.getCore());
                if (result.getStatus() == CommonReturnType.FAIL) {
                    JOptionPane.showMessageDialog(null, result.getMessage(), "load", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, result.getMessage());
                    timeDisplay.restartCountdown();
                    panelChessBoardPanel.repaint();
                    if (panelChessBoardPanel.getCore().getGameStatus() == Status.CONTINUE) {
                        panelButtons.enableBtns();
                    }
                    players[0].reset();
                    players[1].reset();
                    players[panelChessBoardPanel.getCore().getCurrPlayer() == Player.PLAYER_1 ? 0 : 1].switchStatus();
                }
            }

            @Override
            public void exit() {
                System.exit(0);
            }
        });

        panelButtons.setHandler(i -> {
            if (panelChessBoardPanel.getCore().getGameStatus() != Status.CONTINUE) {
                return;
            }
            panelChessBoardPanel.getCore().dropAt(i);
            panelChessBoardPanel.repaint();

            switch (panelChessBoardPanel.getCore().getGameStatus()) {
                case WIN:
                    timeDisplay.stopCountdown();
                    JOptionPane.showMessageDialog(null, panelChessBoardPanel.getCore().getCurrPlayer() + " wins!");
                    panelButtons.disableBtns();
                    break;
                case FAIL:
                    timeDisplay.stopCountdown();
                    JOptionPane.showMessageDialog(null, "It's a Draw!");
                    panelButtons.disableBtns();
                    break;
                case CONTINUE:
                    //交换玩家
                    panelChessBoardPanel.getCore().switchPlayer();
                    players[0].switchStatus();
                    players[1].switchStatus();
                    timeDisplay.restartCountdown();
                    break;
                default:
                    break;
            }
        });
    }


    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}