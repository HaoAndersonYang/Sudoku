public class Runner {

    public static void main(String[] args) {
        PuzzleGenerator pg = new PuzzleGenerator();
        pg.generate();










//
//        InputOutput io = new InputOutput(args[0]);
//        int[][] board = io.getBoard();
//        // Basic Back track solver
//        SudokuSolver BBTS = new BasicBackTrackSolver(board.length);
//        SudokuSolver PBTS = new PreprocessBackTrackSolver(board.length);
//        long startTime = System.currentTimeMillis();
//        System.out.println(((PreprocessBackTrackSolver) PBTS).hasUniqueSolution(board));
//        long endTime = System.currentTimeMillis();
//        System.out.println("Unique Solution execution time in Milliseconds: " + (endTime - startTime));
//        io.outputSolution(solveSudoku(board, PBTS));
    }

    public static int[][] solveSudoku(int[][] puzzle, SudokuSolver solver) {
        long startTime = System.currentTimeMillis();
        int[][] solution = solver.solve(puzzle);
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time in Milliseconds: " + (endTime - startTime));
        return solution;
    }

}
