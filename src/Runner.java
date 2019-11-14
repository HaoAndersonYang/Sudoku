import java.io.File;
import java.util.Scanner;

public class Runner {

    private final static String commandLinePrompt = "Enter \"s\" for solving a sudoku puzzle, \"g\" for generating a new 9-by-9 Sudoku puzzle, \"q\" for quit.";
    private final static String enterFileName = "Enter the filename for the puzzle, enter \"q\" for quit.";
    private final static String filenotExist = "File not exist!";
    private final static String invalidCommand = "Invalid command!";
    private final static String inputDifficulty = "Enter difficulty (from 1 to 4)";

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
//                    BasicBackTrackSolver BBTS = new BasicBackTrackSolver(board.length);

                    HumanSimulationSolver HSS = new HumanSimulationSolver(board.length);
                    io.outputSolution(solveSudoku(board, HSS));
                    System.out.println();
                    System.out.println(commandLinePrompt);
                    break;
                case "g":
                    System.out.println(enterFileName);
                    fileName = s.next();
                    System.out.println(inputDifficulty);
                    int targetDiff = s.nextInt();
                    PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
                    puzzleGenerator.generatePuzzle();
                    HSS = new HumanSimulationSolver(puzzleGenerator.getPuzzle().length);
                    int[][] puzzle = puzzleGenerator.getPuzzle();
                    int diff = HSS.difficultyLevel(Util.arrayCopy(puzzle));
                    while (diff != targetDiff) {
                        HSS = new HumanSimulationSolver(9);
                        puzzleGenerator = new PuzzleGenerator();
                        puzzleGenerator.generatePuzzle();
                        puzzle = puzzleGenerator.getPuzzle();
                        diff = HSS.difficultyLevel(Util.arrayCopy(puzzle));
                    }
                    io = new InputOutput(fileName, 9);
                    io.outputPuzzle(puzzleGenerator.getPuzzle());
                    io.outputSolution(puzzleGenerator.getSolution());
                    System.out.println();
                    System.out.println(commandLinePrompt);
                    break;
                case "g1000":
                    int[] difficultyCount = new int[4];
                    for (int i = 0; i < 1000; i++) {
                        HSS = new HumanSimulationSolver(9);
                        puzzleGenerator = new PuzzleGenerator();
                        puzzleGenerator.generatePuzzle();
                        puzzle = puzzleGenerator.getPuzzle();
                        int[][] modpuzzle = Util.rotation(puzzle);
                        diff = HSS.difficultyLevel(Util.arrayCopy(puzzle));
                        HSS = new HumanSimulationSolver(9);
                        int diff2 = HSS.difficultyLevel(modpuzzle);
                        if (diff2 != diff) {
                            difficultyCount[0]++;
                        }
                    }
                    for (int i = 0; i < 1000; i++) {
                        HSS = new HumanSimulationSolver(9);
                        puzzleGenerator = new PuzzleGenerator();
                        puzzleGenerator.generatePuzzle();
                        puzzle = puzzleGenerator.getPuzzle();
                        int[][] modpuzzle = Util.mirror(puzzle);
                        diff = HSS.difficultyLevel(Util.arrayCopy(puzzle));
                        HSS = new HumanSimulationSolver(9);
                        int diff2 = HSS.difficultyLevel(modpuzzle);
                        if (diff2 != diff) {
                            difficultyCount[0]++;
                        }
                    }
                    for (int i = 0; i < 1000; i++) {
                        HSS = new HumanSimulationSolver(9);
                        puzzleGenerator = new PuzzleGenerator();
                        puzzleGenerator.generatePuzzle();
                        puzzle = puzzleGenerator.getPuzzle();
                        int[][] modpuzzle = Util.transposition(puzzle);
                        diff = HSS.difficultyLevel(Util.arrayCopy(puzzle));
                        HSS = new HumanSimulationSolver(9);
                        int diff2 = HSS.difficultyLevel(modpuzzle);
                        if (diff2 != diff) {
                            difficultyCount[0]++;
                        }
                    }
                    for (int i = 0; i < 1000; i++) {
                        HSS = new HumanSimulationSolver(9);
                        puzzleGenerator = new PuzzleGenerator();
                        puzzleGenerator.generatePuzzle();
                        puzzle = puzzleGenerator.getPuzzle();
                        int[][] modpuzzle = Util.ciphering(puzzle);
                        diff = HSS.difficultyLevel(Util.arrayCopy(puzzle));
                        HSS = new HumanSimulationSolver(9);
                        int diff2 = HSS.difficultyLevel(modpuzzle);
                        if (diff2 != diff) {
                            difficultyCount[0]++;
                        }
                    }
                    System.out.println(difficultyCount[0]);
                    System.out.println(difficultyCount[1]);
                    System.out.println(difficultyCount[2]);
                    System.out.println(difficultyCount[3]);

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

    public static void printArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                int val = array[i][j];
                if (val == 0) {
                    System.out.print("- ");
                } else {
                    System.out.print(val + " ");
                }
            }
            System.out.println();
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
