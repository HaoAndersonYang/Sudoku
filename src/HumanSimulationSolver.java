import java.util.ArrayList;

public class HumanSimulationSolver extends SudokuSolver {

    private int nakedSingleCount = 0;
    private int hiddenSingleCount = 0;
    private int lockedCandicateCount = 0;

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
//        System.out.println("NAKED SINGLE");
        for (int p = 0; p < boardSize; p++) {
            for (int q = 0; q < boardSize; q++) {
                if (gic.checked[p][q] == 0) {
                    if (gic.impossibleValCount[p][q] == boardSize - 1) {
                        if (gic.board[p][q] != 0) {
                            gic.update(new int[]{p, q});
                        } else {
                            nakedSingleCount++;
                            for (int r = 1; r <= boardSize; r++) {
                                if (gic.possibleVals[r][p][q] == 0) {
                                    gic.board[p][q] = r;
                                    gic.update(new int[]{p, q});
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hiddenSingle() {
//        System.out.println("HIDDEN SINGLE");
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
                    if (gic.checked[row][colPos] == 0) {
                        if (gic.board[row][colPos] != 0) {
                            gic.update(new int[]{row, colPos});
                        } else {
                            hiddenSingleCount++;
                            gic.board[row][colPos] = val;
                            gic.update(new int[]{row, colPos});
                            return true;
                        }
                    }
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
                    if (gic.checked[rowPos][col] == 0) {
                        if (gic.board[rowPos][col] != 0) {
                            gic.update(new int[]{rowPos, col});
                        } else {
                            hiddenSingleCount++;
                            gic.board[rowPos][col] = val;
                            gic.update(new int[]{rowPos, col});
                            return true;
                        }
                    }
                }
            }
        }

        //Grid
        int gridCount = boardSize / gridSize;
        for (int i = 0; i < gridCount; i++) {
            for (int j = 0; j < gridCount; j++) {
                int initialRow = i * 3;
                int initialCol = j * 3;
                int colPos = 0;
                int rowPos = 0;
                for (int val = 1; val <= boardSize; val++) {
                    int count = 0;
                    for (int row = initialRow; row < gridSize; row++) {
                        for (int col = initialCol; col < gridSize; col++) {
                            if (gic.possibleVals[val][row][col] == 0) {
                                count++;
                                colPos = col;
                                rowPos = row;
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
                        if (gic.checked[rowPos][colPos] == 0) {
                            if (gic.board[rowPos][colPos] != 0) {
                                gic.update(new int[]{rowPos, colPos});
                            } else {
                                hiddenSingleCount++;
                                gic.board[rowPos][colPos] = val;
                                gic.update(new int[]{rowPos, colPos});
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    private boolean lockedCandicate() {
        int gridCount = boardSize / gridSize;
        for (int i = 0; i < gridCount; i++) {
            for (int j = 0; j < gridCount; j++) {
                int initialRow = i * gridSize;
                int initialCol = j * gridSize;
                int colPos = 0;
                int rowPos = 0;
                for (int val = 1; val <= boardSize; val++) {
                    int rowcount = 0;
                    int rowindex = -1;
                    int colcount = 0;
                    int colindex = -1;
                    for (int row = initialRow; row < gridSize; row++) {
                        for (int col = initialCol; col < gridSize; col++) {
                            if (gic.possibleVals[val][row][col] == 0) {
                                if (rowcount == 0) {
                                    rowcount++;
                                    colcount++;
                                    colindex = col;
                                    rowindex = row;
                                } else if (row != rowindex) {
                                    rowcount++;
                                } else if (col != colindex) {
                                    colcount++;
                                }
                            }
                        }

                    }
                    if (rowcount == 1) {
                        for (int k = 0; k < boardSize; k++) {
                            if (k < initialCol || k >= initialCol + gridSize) {
                                gic.possibleVals[val][rowindex][k] = 1;
                            }
                        }
                        lockedCandicateCount++;
                        return true;
                    } else if (colcount == 1) {
                        for (int k = 0; k < boardSize; k++) {
                            if (k < initialRow || k >= initialRow + gridSize) {
                                gic.possibleVals[val][k][colindex] = 1;
                            }
                        }
                        lockedCandicateCount++;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int[][] humanSolve() {

        while (applyStrategy(1)) {
        }
        if (findNextZero(gic.board) == null) {
            System.out.println(hiddenSingleCount);
            System.out.println(nakedSingleCount);
            System.out.println("Solved by using upto LEVEL 1 Strategies");
            return gic.board;
        }
        System.out.println("LEVEL 1 Strategies cannot solve");
        while (applyStrategy(2)) {
        }
        if (findNextZero(gic.board) == null) {
            System.out.println(hiddenSingleCount);
            System.out.println(nakedSingleCount);
            System.out.println(lockedCandicateCount);
            System.out.println("Solved by using upto LEVEL 2 Strategies");
            return gic.board;
        }
        System.out.println("LEVEL 2 Strategies cannot solve");
        if (findNextZero(gic.board) == null) {
            return gic.board;
        }
        return null;
    }

    public boolean printAttemptResults(int level) {
        switch (level) {
            case 3:
            case 2:
                System.out.println(lockedCandicateCount);
            case 1:
                System.out.println(hiddenSingleCount);
                System.out.println(nakedSingleCount);
            default:
                return findNextZero(gic.board) == null;
        }
    }


    public boolean applyStrategy(int level) {
        ArrayList<Boolean> resultList = new ArrayList<>();
        switch (level) {
            case 3:
            case 2:
                resultList.add(lockedCandicate());
            case 1:
                resultList.add(hiddenSingle());
                resultList.add(nakedSingle());
            default:
                return resultList.contains(true);
        }
    }

}
