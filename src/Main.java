import archive.ArchiveManager;
import enumerations.CommonReturnType;
import core.Board;
import core.Player;
import core.GameControl;
import java.util.InputMismatchException;
import java.util.Scanner;

import enumerations.Status;
public class Main {
    static Scanner reader = new Scanner(System.in);  // Reading from System.in
    public static void main(String[] args) {
        Status gameState;
        GameControl gameControl = new GameControl();
        System.out.println("开始游戏");
        chooseLoad(gameControl);
        while (true){
            int pos = getUserInput(gameControl.getCurrPlayer());
            while(pos == -1){
                pos =  getUserInput(gameControl.getCurrPlayer());
            }
            gameState = gameControl.dropAt(pos);
            if(gameState == Status.WIN){
                System.out.println(gameControl.getCurrPlayer().toString() + "获胜！");
                break;
            }else if(gameState == Status.FAIL){
                System.out.println("平局！");
                break;
            }else if(gameState == Status.FULL)
                System.out.println("该列已满，请重新输入");
            else
                gameControl.switchPlayer();
            //每一步自动存档
            ArchiveManager.saveArchive(gameControl);
        }
    }

    private static int getUserInput(Player player){
        System.out.println("请"+player.toString()+"输入所要下的位置(0-6)：");
        int pos;
        try{
            pos = reader.nextInt();
        }catch (InputMismatchException e){
            reader.next();
            System.out.println("输入不合法");
            return -1;
        }
        if(pos >6 || pos < 0){
            System.out.println("请输入0-6的整数");
            return -1;
        }
        return pos;
    }

    private static void chooseLoad(GameControl gameControl){
        System.out.println("是否要选择读档，输入Y/y选择读档，输入其他重新开始新游戏");
        String s = reader.nextLine();
        CommonReturnType commonReturnType;
        if(s.equals("Y") || s.equals("y")){
            commonReturnType = ArchiveManager.loadArchive(gameControl);
            if(commonReturnType.getStatus() == CommonReturnType.SUCCESS) {
                System.out.println("读档成功，请继续游戏");
                Board.getInstance().printBoard();
            }
            else
                System.out.println("读档失败，请开始新游戏");
        }
    }
}
