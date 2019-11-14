import java.io.*;
import java.util.Scanner;

public class InputOutput {
    private File inputFile;
    private final String solutionFileName;
    private final String puzzleFileName;

    public InputOutput(String fileName) {
        puzzleFileName = System.getProperty("user.dir") + "\\" + fileName;
        System.out.println("Reading input from " + puzzleFileName);
        inputFile = new File(puzzleFileName);
        solutionFileName = fileName.substring(0, fileName.length() - 4) + "Solution.txt";
//        System.out.println(solutionFileName);
    }

    public InputOutput(String fileName, int size) {
        System.out.println("Creating puzzle file and solution file for Sudoku of size " + size + ".");
        puzzleFileName = System.getProperty("user.dir") + "\\" + fileName;
        solutionFileName = fileName.substring(0, fileName.length() - 4) + "Solution.txt";
    }

    public void writeBoard(int[][] board, PrintWriter writer) {
        writer.println(board.length);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                writer.print(board[i][j] + " ");
            }
            writer.println();
        }
    }

    public void outputPuzzle(int[][] puzzle) {
        try {
            PrintWriter writer = new PrintWriter(puzzleFileName, "UTF-8");
            writeBoard(puzzle, writer);
            System.out.println("Puzzle is stored in file: " + puzzleFileName);
            writer.close();
        } catch (Exception e) {
            System.out.println("Cannot create output file: " + puzzleFileName);
            System.exit(-1);
        }
    }

    public int[][] getBoard() {
        try {
            Scanner s = new Scanner(inputFile);
            int boardSize = s.nextInt();
            if (Math.floor(Math.sqrt((double) boardSize)) != Math.sqrt((double) boardSize)) {
                invalidSodokuError();
            }
            int[][] board = new int[boardSize][boardSize];
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (s.hasNext()) {
                        int next = s.nextInt();
                        if (next > boardSize || next < 0) {
                            invalidSodokuError();
                        }
                        board[i][j] = next;
                    } else {
                        invalidSodokuError();
                    }
                }
            }
            return board;

        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + puzzleFileName);
            System.exit(-1);
        }
        return null;
    }

    public void outputSolution(int[][] solution) {
        try {
            PrintWriter writer = new PrintWriter(solutionFileName, "UTF-8");
            if (solution == null) {
                System.out.println("The sudoku is not solvable");
                writer.println("The sudoku is not solvable");
                writer.close();
                return;
            }
            writeBoard(solution, writer);
            System.out.println("Solution is stored in file: " + solutionFileName);
            writer.close();
        } catch (Exception e) {
            System.out.println("Cannot create output file: " + solutionFileName);
            System.exit(-1);
        }
    }

    public void invalidSodokuError() {
        System.out.println("Invalid sudoku game board");
        System.exit(-1);
    }

}
