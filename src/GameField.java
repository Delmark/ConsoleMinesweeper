import java.util.*;

public class GameField {

    private Cell[][] field;
    private final int FIELD_SIZE;
    private final int BOMB_COUNT;



    public GameField(int FIELD_SIZE, int BOMB_COUNT) {
        if (FIELD_SIZE <= 1) {
            System.out.print("Размер поля должен быть корректным!");
            this.FIELD_SIZE = 3;
        }
        else this.FIELD_SIZE = FIELD_SIZE;
        if (BOMB_COUNT >= FIELD_SIZE*FIELD_SIZE) {
            System.out.print("Количество бомб должно быть меньше чем игровое поле");
            this.BOMB_COUNT = (FIELD_SIZE*FIELD_SIZE - 1) / 2;
        }
        else this.BOMB_COUNT = BOMB_COUNT;
    }

    public void initField() {
        field = new Cell[FIELD_SIZE][FIELD_SIZE];
        ArrayList<Integer[]> bombsCoords = getBombsCoords(BOMB_COUNT);

        //Инициализация бомб
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                Integer[] fieldCords = new Integer[] {i,j};
                if (compareCoords(bombsCoords, fieldCords)) {
                    field[i][j] = new Cell(true);
                }
            }
        }

        //Инициализация остальных клеток
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (field[i][j] == null) {
                    int bombsNearby = 0;
                    //Проверка соседних полей на наличие бомб
                    for (int k = -1; k <= 1; k++) {
                        if ((i + k >= 0 && i + k < FIELD_SIZE && j + k >= 0 && j + k < FIELD_SIZE) && field[i+k][j+k] != null && field[i+k][j+k].isHasBomb()) bombsNearby += 1;
                        if ((i + k >= 0 && i + k < FIELD_SIZE && j - k >= 0 && j - k < FIELD_SIZE) && field[i+k][j-k] != null && field[i+k][j-k].isHasBomb()) bombsNearby += 1;
                        if ((j + k >= 0 && k + j < FIELD_SIZE) && field[i][j+k] != null && field[i][j+k].isHasBomb()) bombsNearby += 1;
                        if ((i + k >= 0 && i + k < FIELD_SIZE) && field[i+k][j] != null && field[i+k][j].isHasBomb()) bombsNearby += 1;
                    }

                    field[i][j] = new Cell(false);
                    if (bombsNearby > 0) {
                        field[i][j].setBombsNearby(bombsNearby);
                    }
                }
            }
        }
    }

    public void printField() {
        for (int i = 0; i <= FIELD_SIZE; i++) {
            System.out.print(i + " ");
        }

        System.out.println();

        for (int i = 0; i <= FIELD_SIZE; i++) {
            System.out.print("- ");
        }

        System.out.println();

        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print((i+1) + "|");
            for (int j = 0; j < FIELD_SIZE; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void openEmptyCells(int i, int j) {
        if (i < 0 || j < 0 || i >= FIELD_SIZE || j >= FIELD_SIZE || field[i][j].isOpened()) {
            return;
        }

        field[i][j].openCell();

        if (!field[i][j].isEmpty()) {
            return;
        }

        openEmptyCells(i - 1, j - 1);
        openEmptyCells(i - 1, j);
        openEmptyCells(i - 1, j + 1);
        openEmptyCells(i, j - 1);
        openEmptyCells(i, j + 1);
        openEmptyCells(i + 1, j - 1);
        openEmptyCells(i + 1, j);
        openEmptyCells(i + 1, j + 1);
    }


   public void fieldAction(String action, int row, int column){
        if (action.equals("O")){
            if (!field[row][column].isOpened()) {

                if (field[row][column].isHasBomb()) {
                    field[row][column].openCell();
                    printField();
                    for (int i = 0; i < 2; i++) {
                        System.out.println();
                    }
                    System.out.println("Вы попали в бомбу! Какая неудача...");
                    MainGame.inGame = false;
                }
                else openEmptyCells(row, column);
            }
        }
        else {
            field[row][column].setFlagged(!field[row][column].isFlagged());
        }
   }

    public boolean checkVictory(){
        int flagsCount = 0;
        int flaggedBombs = 0;

        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (field[i][j].isFlagged() && field[i][j].isHasBomb()) {
                    flaggedBombs += 1;
                    flagsCount += 1;
                }
                else if (field[i][j].isFlagged()) flagsCount += 1;
            }
        }

        return flaggedBombs == BOMB_COUNT && flaggedBombs == flagsCount;
    }

    public ArrayList<Integer[]> getBombsCoords(int BOMB_COUNT) {
        Random random = new Random();
        ArrayList<Integer[]> bombCoords = new ArrayList<>(BOMB_COUNT);

        for (int i = 0; i < BOMB_COUNT; i++) {
            Integer[] bombPos = {random.nextInt(FIELD_SIZE),random.nextInt(FIELD_SIZE)};

            while (compareCoords(bombCoords, bombPos)) {
                bombPos[0] = random.nextInt(FIELD_SIZE);
                bombPos[1] = random.nextInt(FIELD_SIZE);
            }

            bombCoords.add(bombPos);
        }

        return bombCoords;
    }

    public int getFieldSize() {
        return FIELD_SIZE;
    }

    public boolean compareCoords(List<Integer[]> list, Integer[] pos) {
        for (Integer[] listPos : list) {
            if (Arrays.equals(listPos, pos)) {
                return true;
            }
        }
        return false;
    }
}
