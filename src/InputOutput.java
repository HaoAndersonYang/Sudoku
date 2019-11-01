import java.io.*;
import java.util.Scanner;

public class InputOutput {
    File inputFile;
    private final String outputFilename;
    private final String inputFilename;

    public InputOutput(String fileName) {
        inputFilename = System.getProperty("user.dir") + "\\" + fileName;
        System.out.println("Reading input from " + inputFilename);
        inputFile = new File(inputFilename);
        outputFilename = fileName.substring(0, fileName.length() - 4) + "Solution.txt";
//        System.out.println(outputFilename);

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
            System.out.println("Input file not found: " + inputFilename);
            System.exit(-1);
        }
        return null;
    }

    public void outputSolution(int[][] solution) {
        try {
            PrintWriter writer = new PrintWriter(outputFilename, "UTF-8");
            if (solution == null) {
                System.out.println("The sudoku is not solvable");
                writer.println("The sudoku is not solvable");
                writer.close();
                return;
            }
            writer.println(solution.length);
            for (int i = 0; i < solution.length; i++) {
                for (int j = 0; j < solution.length; j++) {
                    writer.print(solution[i][j] + " ");
                }
                writer.println();
            }
            System.out.println("Solution is stored in file: " + outputFilename);
            writer.close();
        } catch (Exception e) {
            System.out.println("Cannot create output file: " + outputFilename);
            System.exit(-1);
        }
    }

    public void invalidSodokuError() {
        System.out.println("Invalid sudoku game board");
        System.exit(-1);
    }

}
