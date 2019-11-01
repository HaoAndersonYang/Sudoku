public class BasicBackTrackSolver extends SudokuSolver {


    public BasicBackTrackSolver(int boardSize) {
        this.boardSize = boardSize;
        gridSize = (int) Math.sqrt(boardSize);
    }

    public int[][] solve(int[][] board) {
        int i, j;
        int[] zeropos = findNextZero(board);
        if (zeropos == null) {
            return board;
        }
        i = zeropos[0];
        j = zeropos[1];
        for (int k = 1; k <= boardSize; k++) {
            board[i][j] = k;
            if (checkConsistent(board, i, j)) {
                int[][] nextStep = solve(board);
                if (nextStep != null) {
                    return nextStep;
                }
            }
        }
        board[i][j] = 0;
        return null;
    }

//    private boolean checkCompleteness(int[][] solution) {
//        for (int i = 0; i < boardSize; i++) {
//            for (int j = 0; j < boardSize; j++) {
//                if (solution[i][j] == 0) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }


}
