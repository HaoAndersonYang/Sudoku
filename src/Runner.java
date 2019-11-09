import java.io.File;
import java.util.Scanner;

public class Runner {

    private final static String commandLinePrompt = "Enter \"s\" for solving a sudoku puzzle, \"g\" for generating a new 9-by-9 Sudoku puzzle, \"q\" for quit.";
    private final static String enterFileName = "Enter the filename for the puzzle, enter \"q\" for quit.";
    private final static String filenotExist = "File not exist!";
    private final static String invalidCommand = "Invalid command!";

    public static void main(String[] args) {
        System.out.println(commandLinePrompt);
        Scanner s = new Scanner(System.in);
        while (true) {
            switch (s.next()) {
                case "s":
                    System.out.println(enterFileName);
                    String fileName = s.next();
                    while (true) {
                        if (!checkFileExist(fileName)) {
                            if (fileName.equals("q")) {
                                return;
                            }
                            System.out.println(filenotExist);
                            System.out.println(enterFileName);
                            fileName = s.next();
                        } else {
                            break;
                        }
                    }
                    InputOutput io = new InputOutput(fileName);
                    int[][] board = io.getBoard();

                    PreprocessBackTrackSolver PBTS = new PreprocessBackTrackSolver(board.length);

                    HumanSimulationSolver HSS = new HumanSimulationSolver(board.length);
                    io.outputSolution(solveSudoku(board, HSS));
                    System.out.println();
                    System.out.println(commandLinePrompt);
                    break;
                case "g":
                    System.out.println(enterFileName);
                    fileName = s.next();
                    PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
                    io = new InputOutput(fileName, 9);
                    io.outputPuzzle(puzzleGenerator.getPuzzle());
                    io.outputSolution(puzzleGenerator.getSolution());
                    System.out.println();
                    System.out.println(commandLinePrompt);
                    break;
                case "q":
                    return;
                default:
                    System.out.println(invalidCommand);
                    System.out.println(commandLinePrompt);
                    break;
            }
        }
    }


    public static boolean checkFileExist(String fileName) {
        String path = System.getProperty("user.dir") + "\\" + fileName;
        File temp = new File(path);
        return temp.exists();
    }

    public static int[][] solveSudoku(int[][] puzzle, SudokuSolver solver) {
        long startTime = System.currentTimeMillis();
        int[][] solution = solver.solve(puzzle);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time in Milliseconds: " + (endTime - startTime));
        return solution;
    }

}
