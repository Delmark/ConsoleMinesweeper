import java.util.InputMismatchException;
import java.util.Scanner;

public class MainGame {
    public static boolean inGame;
    private static GameField gameField;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("Простая реализация игры в Сапёра.\nУ вас есть два действия, выбрать ячейку или поставить на неё флаг");
            System.out.println("Команда для выбора ячейки \"O {Строка} {Столбец}\"");
            System.out.println("Команда для постановки флага на ячейку \"F {Строка} {Столбец}\"");
            System.out.println("Победой считается если все бомбы в игре будут помечены флагом, поражением если будет открыта ячейка с бомбой.");
            System.out.println("Чтобы убрать флаг с ячейки, требуется снова ввести команду постановки флага на нужную ячейку.");

            for (int i = 0; i < 2; i++) {
                System.out.println();
            }

            int difficulty = -1;
            while (difficulty < 1 || difficulty > 4) {
                System.out.println("Выберите уровень сложности (введите номер) : \n1. Новичок \n2. Любитель \n3. Профессионал\n4. Своя сложность.");
                try {
                    difficulty = Integer.parseInt(scanner.nextLine());
                    if (difficulty < 1 || difficulty > 4) {
                        System.out.print("Вы должны выбрать число из списка!");
                    }
                    else break;
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Вы должны ввести число!");
                }
            }

            switch (difficulty) {
                case 1 -> gameField = new GameField(9, 10);
                case 2 -> gameField = new GameField(16, 40);
                case 3 -> gameField = new GameField(32, 60);
                case 4 -> {
                    try {
                        System.out.println("Введите два числа через пробел, {Размер поля} и {Количество бомб}.");
                        int[] playerDiff = new int[2];
                        int counter = 0;
                        for (String s : scanner.nextLine().split(" ")) {
                            playerDiff[counter++] = Integer.parseInt(s);
                        }
                        if (playerDiff[0] < 0 || playerDiff[1] < 0) System.out.println("Некорректный ввод");
                        else gameField = new GameField(playerDiff[0], playerDiff[1]);
                    } catch (InputMismatchException mismatchException) {
                        System.out.println("Вы должны ввести только числа!");
                    } catch (IndexOutOfBoundsException boundsException) {
                        System.out.println("Вы должны ввести только два числа: {Размер поля} и {Количество бомб}");
                    }
                }
            }

            if (gameField != null) startGame();
        }
    }

    public static void startGame() {
        inGame = true;
        gameField.initField();

        while (inGame) {
            for (int i = 0; i < 2; i++) {
                System.out.println();
            }
            gameField.printField();
            for (int i = 0; i < 2; i++) {
                System.out.println();
            }
            String[] playerMove = scanner.nextLine().split(" ");
            String action = playerMove[0];
            try {
                int row = Integer.parseInt(playerMove[1])-1, column = Integer.parseInt(playerMove[2])-1;

                if ((action.equals("F") || action.equals("O")) && (row >= 0 && row < gameField.getFieldSize()) && (column >= 0 && column <= gameField.getFieldSize())) {
                    gameField.fieldAction(action,row,column);
                    if (inGame && gameField.checkVictory()) {
                        System.out.println("Победа!");
                        break;
                    }
                    Thread.sleep(1000);
                }
                else {
                    System.out.println("Неверный ввод команды");
                    System.out.println("Команда для выбора ячейки \"O {Строка} {Столбец}\"");
                    System.out.println("Команда для постановки флага на ячейку \"F {Строка} {Столбец}\"");
                    Thread.sleep(1000);
                }

            }
            catch (NumberFormatException numberFormatException) {
                System.out.println("Неверный ввод команды!");
                System.out.println("Команда для выбора ячейки \"O {Строка} {Столбец}\"");
                System.out.println("Команда для постановки флага на ячейку \"F {Строка} {Столбец}\"");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
