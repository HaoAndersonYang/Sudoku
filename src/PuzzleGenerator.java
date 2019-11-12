import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Generate 9-by-9 puzzle
 */
public class PuzzleGenerator {

    private int[][] solution;
    private int[][] puzzle;

    public PuzzleGenerator() {
    }

    public int[][] generatePuzzle() {
        generateCompleteBoard();
        return generatePuzzlefromBoard();
    }


    public int[][] getSolution() {
        return solution;
    }

    public int[][] generateCompleteBoard() {
        solution = new int[9][9];
        for (int i = 0; i < 3; i++) {
            ArrayList<Integer> randomSequence = generateRandomSequence();
            for (int j = 3 * i; j < 3 + 3 * i; j++) {
                for (int k = 3 * i; k < 3 + 3 * i; k++) {
                    solution[j][k] = randomSequence.get(0);
                    randomSequence.remove(0);
                }
            }
        }
        PreprocessBackTrackSolver PBTS = new PreprocessBackTrackSolver(9);
        solution = PBTS.solve(solution);
        return solution;
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

    public int[][] getPuzzle() {
        return puzzle;
    }

    public int[][] copyArray(int[][] input) {
        int[][] copy = new int[input.length][];
        for (int i = 0; i < 9; i++) {
            copy[i] = Arrays.copyOf(input[i], input[i].length);
        }
        return copy;
    }

    public int[][] generatePuzzlefromBoard() {
        PreprocessBackTrackSolver PBTS = new PreprocessBackTrackSolver(9);
        puzzle = Util.arrayCopy(solution);
        int temp, x, y;
        do {
            x = (int) (Math.random() * 9);
            y = (int) (Math.random() * 9);
            temp = puzzle[x][y];
            puzzle[x][y] = 0;
            PBTS = new PreprocessBackTrackSolver(9);
        } while (PBTS.hasUniqueSolution(Util.arrayCopy(puzzle)));
        puzzle[x][y] = temp;
        return puzzle;
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
