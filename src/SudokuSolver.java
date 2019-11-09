public class SudokuSolver {
    int boardSize;
    int gridSize;

    int[][] solve(int[][] board) {
        return null;
    }

    boolean checkConsistent(int[][] solution, int x, int y) {
//        System.out.println("x:" + x + " y:" + y + " val:" + solution[x][y]);
        for (int i = 0; i < boardSize; i++) {
//            System.out.println(x + " " + i);
//            System.out.println(i + " " + y);
            if ((solution[x][i] == solution[x][y] && i != y) || (solution[i][y] == solution[x][y] && i != x)) {
                return false;
            }
        }
        int gridx = x / gridSize;
        int gridy = y / gridSize;
        gridx *= gridSize;
        gridy *= gridSize;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
//                System.out.println((gridx + i) + " " + (gridy + j));
                if (solution[gridx + i][gridy + j] == solution[x][y] && (x != gridx + i || y != gridy + j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[] findNextZero(int[][] board) {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (board[x][y] == 0) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }


    //The following code is used for preprocess backtrack solver and HumanSimulationSolvers

    public GameInformatonContainer preProcess(int[][] board) {
        // PossibleVals[0] will not be used.
        // 0 means possible, 1 means impossible
        int[][][] possibleVals = new int[boardSize + 1][boardSize][boardSize];
        // 0 is not checked
        int[][] checked = new int[boardSize][boardSize];
        // impossibleValCount is the number of impossible vals of a cell
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
        gic.initialize();
        //        System.out.println("Finished PreProcessing");
        return gic;
    }


    public GameInformatonContainer inference(GameInformatonContainer input, int[] pos) {
        GameInformatonContainer gic = input.clone();
        gic.update(pos);
        // Find next cell that has a certain value
        for (int p = 0; p < boardSize; p++) {
            for (int q = 0; q < boardSize; q++) {
                if (gic.checked[p][q] == 0) {
                    if (gic.impossibleValCount[p][q] == boardSize - 1) {
                        if (gic.board[p][q] != 0) {
                            return inference(gic, new int[]{p, q});
                        } else {
                            return putCertainValue(gic, p, q);
                        }
                    }
                }
                if (gic.impossibleValCount[p][q] == boardSize) {
//                    System.out.println("FAIL AT " + p + " " + q);
                    return null;
                }
            }
        }
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


    public GameInformatonContainer putCertainValue(GameInformatonContainer gic, int row, int col) {
        for (int r = 1; r <= boardSize; r++) {
            if (gic.possibleVals[r][row][col] == 0) {
                gic.board[row][col] = r;
                return inference(gic, new int[]{row, col});
            }
        }
        return null;
    }
}
