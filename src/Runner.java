public class Runner {

    public static void main(String[] args) {
        InputOutput io = new InputOutput(args[0]);
        int[][] board = io.getBoard();
        // Basic Back track solver
        SudokuSolver BBTS = new BasicBackTrackSolver(board.length);
        SudokuSolver PBTS = new PreprocessBackTrackSolver(board.length);
        io.outputSolution(solveSudoku(board, PBTS));
    }

    public static int[][] solveSudoku(int[][] puzzle, SudokuSolver solver) {
        long startTime = System.currentTimeMillis();
        int[][] solution = solver.solve(puzzle);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time in Milliseconds: " + (endTime - startTime));
        return solution;
    }

}
