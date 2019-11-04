import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Generate 9-by-9 puzzle
 */
public class PuzzleGenerator {


    public PuzzleGenerator() {
    }

    public int[][] generate() {
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
        printArray(board);
        PreprocessBackTrackSolver PBTS = new PreprocessBackTrackSolver(9);
        board = PBTS.solve(board);
        System.out.println("solution");
        printArray(board);

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

    private ArrayList<Integer> generateRandomSequence() {
        ArrayList<Integer> randomSequence = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            randomSequence.add(i);
        }
        Collections.shuffle(randomSequence);
        return randomSequence;
    }


}
