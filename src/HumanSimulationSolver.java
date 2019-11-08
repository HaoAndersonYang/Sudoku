public class HumanSimulationSolver extends SudokuSolver {
    private int solutionCount = 0;//Counting the number of solutions.

    private int nakedSingleCount = 0;
    private int hiddenSingleCount = 0;


    private GameInformatonContainer gic;

    public HumanSimulationSolver(int boardSize) {
        this.boardSize = boardSize;
        gridSize = (int) Math.sqrt(boardSize);
    }

    public int[][] solve(int[][] board) {
        gic = preProcess(board);
        return humanSolve();
    }


    private boolean nakedSingle() {
        boolean found;
        do {
            found = false;
            for (int p = 0; p < boardSize; p++) {
                for (int q = 0; q < boardSize; q++) {
                    if (gic.checked[p][q] == 0) {
                        if (gic.impossibleValCount[p][q] == boardSize - 1) {
                            if (gic.board[p][q] != 0) {
                                gic = inference(gic, new int[]{p, q});
                            } else {
                                nakedSingleCount++;
                                found = true;
                                gic = putCertainValue(gic, p, q);
                            }
                        }
                    }
                }
            }
        } while (found);
        return false;
    }

    private boolean hiddenSingle() {
        boolean found;
        do {
            found = false;
            //Row
            for (int row = 0; row < boardSize; row++) {
                for (int val = 1; val <= boardSize; val++) {
                    int colPos = 0;
                    int count = 0;
                    for (int col = 0; col < boardSize; col++) {
                        if (gic.possibleVals[val][row][col] == 0) {
                            count++;
                            colPos = col;
                            if (count >= 1) {
                                break;
                            }
                        }
                    }
                    if (count == 1) {
                        hiddenSingleCount++;
                        found = true;
                        gic.board[row][colPos] = val;
                        gic.update(new int[]{row, colPos});
                    }
                }
            }
            //Col
            for (int col = 0; col < boardSize; col++) {
                for (int val = 1; val <= boardSize; val++) {
                    int rowPos = 0;
                    int count = 0;
                    for (int row = 0; row < boardSize; row++) {
                        if (gic.possibleVals[val][row][col] == 0) {
                            count++;
                            rowPos = row;
                            if (count >= 1) {
                                break;
                            }
                        }
                    }
                    if (count == 1) {
                        hiddenSingleCount++;
                        found = true;
                        gic.board[rowPos][col] = val;
                        gic.update(new int[]{rowPos, col});
                    }
                }
            }

            //Grid
            int gridCount = boardSize / gridSize;
            for (int i = 0; i < gridCount; i++) {
                for (int j = 0; j < gridCount; j++) {
                    int initialRow = i * 3;
                    int initialCol = j * 3;
                    int colIndex = 0;
                    int rowIndex = 0;
                    for (int val = 1; val <= boardSize; val++) {
                        int count = 0;
                        for (int row = initialRow; row < gridSize; row++) {
                            for (int col = initialCol; col < gridSize; col++) {
                                if (gic.possibleVals[val][row][col] == 0) {
                                    count++;
                                    colIndex = col;
                                    rowIndex = row;
                                    if (count >= 1) {
                                        break;
                                    }
                                }
                            }
                            if (count >= 1) {
                                break;
                            }
                        }
                        if (count == 1) {
                            hiddenSingleCount++;
                            found = true;
                            gic.board[rowIndex][colIndex] = val;
                            gic.update(new int[]{rowIndex, colIndex});
                        }
                    }


                }
            }
        } while (found);
        return false;
    }


    public int[][] humanSolve() {

        while (true) {
            boolean hiddenSingleFound = hiddenSingle();
            boolean nakedSingleFound = nakedSingle();
            if (!(hiddenSingleFound || nakedSingleFound)) {
                break;
            }
        }
        if (findNextZero(gic.board) == null) {
            return gic.board;
        }
        return null;
    }

}
