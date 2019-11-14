import java.util.ArrayList;

public class PreprocessBackTrackSolver extends SudokuSolver {

    private int solutionCount = 0;//Counting the number of solutions.


    public PreprocessBackTrackSolver(int boardSize) {
        this.boardSize = boardSize;
        gridSize = (int) Math.sqrt(boardSize);
    }

    public int[][] solve(int[][] board) {
        return backTrackSolve(preProcess(board));
    }

    public boolean hasUniqueSolution(int[][] board) {
        solutionCount = 0;
        countNumberofSolution(preProcess(board));
        return solutionCount == 1;
    }


    public int[][] backTrackSolve(GameInformatonContainer gic) {
        if (gic == null) {
            return null;
        }
        int i, j;
        int[] zeropos = findNextZero(gic.board);
        if (zeropos == null) {
            return gic.board;
        }
        i = zeropos[0];
        j = zeropos[1];
        for (int k = 1; k <= boardSize; k++) {
//            System.out.println(i + " " + j + " " + k + " " + gic.possibleVals[k][i][j]);
            if (gic.possibleVals[k][i][j] != 1) {
                gic.board[i][j] = k;
                if (checkConsistent(gic.board, i, j)) {
                    GameInformatonContainer next = inference(gic, new int[]{i, j});
                    if (next != null) {
                        int[][] nextStep = backTrackSolve(next);
                        if (nextStep != null) {
                            return nextStep;
                        }
                    }
                    gic.board[i][j] = 0;
                } else {
                    gic.board[i][j] = 0;
                }
            }
        }
        return null;
    }

    public int countNumberofSolution(GameInformatonContainer gic) {
        if (solutionCount > 1) {
            return -1;
        }
        if (gic == null) {
            return 0;
        }
        int i, j;
        int[] zeropos = findNextZero(gic.board);
        if (zeropos == null) {
            solutionCount += 1;
            return 1;
        }
        i = zeropos[0];
        j = zeropos[1];
        for (int k = 1; k <= boardSize; k++) {
            if (gic.possibleVals[k][i][j] != 1) {
                gic.board[i][j] = k;
                if (checkConsistent(gic.board, i, j)) {
                    GameInformatonContainer next = inference(gic, new int[]{i, j});
                    if (next != null) {
                        countNumberofSolution(next);
                        if (solutionCount > 1) {
                            return -1;
                        }
                    }
                    gic.board[i][j] = 0;
                } else {
                    gic.board[i][j] = 0;
                }
            }
        }
        return 0;
    }
}
