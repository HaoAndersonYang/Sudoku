import java.util.ArrayList;

public class PreprocessBackTrackSolver extends SudokuSolver {


    public PreprocessBackTrackSolver(int boardSize) {
        this.boardSize = boardSize;
        gridSize = (int) Math.sqrt(boardSize);
    }

    public int[][] solve(int[][] board) {
        return backTrackSolve(preProcess(board));
    }

    private GameInformatonContainer preProcess(int[][] board) {
        // PossibleVals[0] will not be used.
        // 0 means possible, 1 means impossible
        int[][][] possibleVals = new int[boardSize + 1][boardSize][boardSize];
        // 0 is not checked
        int[][] checked = new int[boardSize][boardSize];
        // valCount is the number of impossible vals of a cell
        int[][] valCount = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int val = board[i][j];
                if (val != 0) {
                    for (int k = 1; k <= boardSize; k++) {
                        if (k != val) {
                            possibleVals[k][i][j] = 1;
                            valCount[i][j]++;
                        }
                    }
                }
            }
        }

//        for (int i = 1; i <= boardSize; i++) {
//            System.out.println(i);
//            for (int j = 0; j < boardSize; j++) {
//                for (int k = 0; k < boardSize; k++) {
//                    int val = possibleVals[i][j][k];
//                    System.out.print(val + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//            System.out.println();
//        }

        GameInformatonContainer gic = new GameInformatonContainer(board, checked, valCount, possibleVals);
        gic = inference(gic, new int[]{0, 0});
        System.out.println("Finished PreProcessing");
        return gic;
    }


    public void printArray(int[][] array) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int val = array[i][j];
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    private class GameInformatonContainer {
        int[][] board;
        int[][][] possibleVals;
        int[][] valCount;
        int[][] checked;

        public GameInformatonContainer(int[][] board, int[][] checked, int[][] valCount, int[][][] possibleVals) {
            this.board = board;
            this.possibleVals = possibleVals;
            this.valCount = valCount;
            this.checked = checked;
        }

        public GameInformatonContainer clone() {
            int[][] nb = new int[boardSize][boardSize];

            int[][][] np = new int[boardSize + 1][boardSize][boardSize];
            // 0 is not checked
            int[][] nc = new int[boardSize][boardSize];
            // valCount is the number of impossible vals of a cell
            int[][] nv = new int[boardSize][boardSize];
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    nb[i][j] = board[i][j];
                    nc[i][j] = checked[i][j];
                    nv[i][j] = valCount[i][j];
                    for (int k = 1; k <= boardSize; k++) {
                        np[k][i][j] = possibleVals[k][i][j];
                    }
                }
            }
            return new GameInformatonContainer(nb, nc, nv, np);
        }
    }


    private GameInformatonContainer inference(GameInformatonContainer input, int[] pos) {
        GameInformatonContainer gic = input.clone();
        int i = pos[0];
        int j = pos[1];
        int val = input.board[i][j];
        if (val != 0) {
            gic.checked[i][j] = 1;
            for (int m = 0; m < boardSize; m++) {
                //Check row
                if (m != i) {
                    gic.possibleVals[val][m][j] = 1;
                    gic.valCount[m][j] = 0;
                    for (int r = 1; r <= boardSize; r++) {
                        gic.valCount[m][j] += gic.possibleVals[r][m][j];
                    }
                }
                //Check column
                if (m != j) {
                    gic.possibleVals[val][i][m] = 1;
                    gic.valCount[i][m] = 0;
                    for (int r = 1; r <= boardSize; r++) {
                        gic.valCount[i][m] += gic.possibleVals[r][i][m];
                    }
                }
            }
            // Check subgrid
            int gridx = (i / gridSize) * gridSize;
            int gridy = (j / gridSize) * gridSize;
            for (int m = 0; m < gridSize; m++) {
                for (int n = 0; n < gridSize; n++) {
                    int row = gridx + m;
                    int col = gridy + n;
                    if (row != i || col != j) {
                        gic.possibleVals[val][row][col] = 1;
                        gic.valCount[row][col] = 0;
                        for (int r = 1; r <= boardSize; r++) {
                            gic.valCount[row][col] += gic.possibleVals[r][row][col];
                        }
                    }
                }
            }
        }
        // Find next cell that has a certain value
        for (int p = 0; p < boardSize; p++) {
            for (int q = 0; q < boardSize; q++) {
                if (gic.checked[p][q] == 0) {
                    if (gic.valCount[p][q] == boardSize - 1) {
                        if (gic.board[p][q] != 0) {
                            return inference(gic, new int[]{p, q});
                        } else {
                            for (int r = 1; r <= boardSize; r++) {
                                if (gic.possibleVals[r][p][q] == 0) {
                                    gic.board[p][q] = r;
                                    return inference(gic, new int[]{p, q});
                                }
                            }
                        }
                    }
                }
                if (gic.valCount[p][q] == boardSize) {
//                    System.out.println("FAIL AT " + p + " " + q);
                    return null;
                }
            }
        }
        return gic;
    }


//    private int[] findNextNonZero(GameInformatonContainer gic) {
//        for (int i = 0; i < boardSize; i++) {
//            for (int j = 0; j < boardSize; j++) {
//                if (gic.board[i][j] != 0 && gic.checked[i][j] == 0) {
//                    gic.checked[i][j] = 1;
//                    return new int[]{i, j};
//                }
//            }
//        }
//        return null;
//    }


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

    public int findUniqueSolution(GameInformatonContainer gic, int solutionCount) {
        if (gic == null) {
            return 0;
        }
        int i, j;
        int[] zeropos = findNextZero(gic.board);
        if (zeropos == null) {
            return 0;
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
                        int nextStep = findUniqueSolution(next, solutionCount);
                        if (nextStep != 0) {
                            return solutionCount + 1;
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
