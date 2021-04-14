package archive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.GameControl;
import core.Board;
import enumerations.CommonReturnType;
import enumerations.GridType;
import enumerations.Status;
import core.Player;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 存档管理类
 *
 * @author Raymond Wong
 */
public class ArchiveManager {

    public static final String BOARD = "board";
    public static final String CURRENT_PLAYER = "current_player";
    public static final String STATUS = "status";

    private static final String PATH = "./save.json";
    private static Board board= Board.getInstance();

    public static JSONObject getJsonObject(GameControl gameControl) throws RuntimeException {
        JSONObject object = new JSONObject();


        //gameBoard;
        int[][] gameBoard = board.getGameBoard();
        if (gameBoard == null) {
            throw new RuntimeException("Internal info error");
        }
        if (gameBoard.length != board.getROW()) {
            throw new RuntimeException("Internal info error");
        }
        if (gameBoard.length != 0 && gameBoard[0].length != board.getCOL()) {
            throw new RuntimeException("Internal info error");
        }
        String boardJsonString = JSON.toJSONString(board.getGameBoard());
        object.put(BOARD, boardJsonString);

        // current player
        Player player = gameControl.getCurrPlayer();
        if (player == null) {
            throw new RuntimeException("Internal info error");
        }
        object.put(CURRENT_PLAYER, player.value());

        // game status
        Status status = gameControl.getGameStatus();
        if (status == null) {
            throw new RuntimeException("Internal info error");
        }
        object.put(STATUS, status.value());
        return object;
    }

    public static void loadJsonObject(JSONObject object, GameControl gameControl) throws RuntimeException {
        if (gameControl == null || object == null) {
            throw new RuntimeException("内部错误");
        }
        int[][] savedBoard = new int[board.getROW()][board.getCOL()];
        JSONArray array = object.getJSONArray(BOARD);
        if (array.isEmpty()) {
            throw new RuntimeException("存档信息不正确，无法读取");
        }
        if (array.size() !=board.getROW()) {
            throw new RuntimeException("存档信息不正确，无法读取");
        }
        for (int i = 0; i < array.size(); i++) {
            JSONArray arr = array.getJSONArray(i);
            if (arr.size() != board.getCOL()) {
                throw new RuntimeException("存档信息不正确，无法读取");
            }
            for (int j = 0; j < arr.size(); j++) {
                int type = arr.getInteger(j);
                if (type != GridType.EMPTY.value() && type != GridType.PLAYER_1.value() && type != GridType.PLAYER_2.value()) {
                    throw new RuntimeException("存档信息不正确，无法读取");
                }
                savedBoard[i][j] = arr.getInteger(j);
            }
        }

        int playerNum = object.getInteger(CURRENT_PLAYER);
        if (playerNum != Player.PLAYER_1.value() && playerNum != Player.PLAYER_2.value()) {
            throw new RuntimeException("存档信息不正确，无法读取");
        }

        int status = object.getInteger(STATUS);
        if (status != Status.WIN.value() && status != Status.FAIL.value() && status != Status.CONTINUE.value()) {
            throw new RuntimeException("存档信息不正确，无法读取");
        }

        gameControl.setCurrPlayer(Player.of(playerNum));
        gameControl.setGameState(Status.of(status));
        board.setGameBoard(savedBoard);
    }

    private static String file2String(String path) throws RuntimeException {
        StringBuilder builder = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return builder.toString();
    }

    public static CommonReturnType loadArchive(GameControl gameControl) {
        return loadArchive(gameControl, PATH);
    }

    public static CommonReturnType loadArchive(GameControl gameControl, String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new CommonReturnType(CommonReturnType.FAIL, "没有找到存档文件");
        }
        try {
            JSONObject obj = JSON.parseObject(file2String(path));
            loadJsonObject(obj, gameControl);
        } catch (RuntimeException e) {
            return new CommonReturnType(CommonReturnType.FAIL, e.getMessage());
        }
        return new CommonReturnType(CommonReturnType.SUCCESS, "加载成功");
    }

    public static CommonReturnType saveArchive(GameControl gameControl) {
        return saveArchive(gameControl, PATH);
    }

    public static CommonReturnType saveArchive(GameControl gameControl, String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
                return new CommonReturnType(CommonReturnType.FAIL, e.getMessage());
            }
        }
        try {
            JSONObject obj = getJsonObject(gameControl);
            byte[] bytes = obj.toString().getBytes(StandardCharsets.UTF_8);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        } catch (RuntimeException | IOException e) {
            return new CommonReturnType(CommonReturnType.FAIL, e.getMessage());
        }
        return new CommonReturnType(CommonReturnType.SUCCESS, "Successfully saved");
    }
}
