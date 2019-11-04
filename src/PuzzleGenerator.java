import java.util.ArrayList;
import java.util.Collections;

/**
 * Generate 9-by-9 puzzle
 */
public class PuzzleGenerator {


    public PuzzleGenerator() {
    }

    public int[][] generatePuzzle() {
        int[][] board = generateCompleteBoard();
        return generatePuzzlefromBoard(board);
    }


    public int[][] generateCompleteBoard() {
        int[][] board = new int[9][9];
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> randomSequence = generateRandomSequence();
            for (int j = 3 * i; j < 3 + 3 * i; j++) {
                for (int k = 3 * i; k < 3 + 3 * i; k++) {
                    board[j][k] = randomSequence.get(0);
                    randomSequence.remove(0);
                }
            }
        }
        PreprocessBackTrackSolver PBTS = new PreprocessBackTrackSolver(9);
        board = PBTS.solve(board);
        return board;
    }

    public void printArray(int[][] array) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int val = array[i][j];
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }


    public int[][] generatePuzzlefromBoard(int[][] board) {
        PreprocessBackTrackSolver PBTS = new PreprocessBackTrackSolver(9);
        int temp, x, y;
        do {
            x = (int) (Math.random() * 9);
            y = (int) (Math.random() * 9);
            temp = board[x][y];
            board[x][y] = 0;
        } while (PBTS.hasUniqueSolution(board));
//        System.out.println("*************PUZZLE*********");
        board[x][y] = temp;
        return board;
    }


    private ArrayList<Integer> generateRandomSequence() {
        ArrayList<Integer> randomSequence = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            randomSequence.add(i);
        }
        Collections.shuffle(randomSequence);
        return randomSequence;
    }


}
