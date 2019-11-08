public class GameInformatonContainer {
    int[][] board;
    int[][][] possibleVals;
    int[][] impossibleValCount;
    int[][] checked;
    int boardSize;
    int gridSize;

    public GameInformatonContainer(int[][] board, int[][] checked, int[][] impossibleValCount, int[][][] possibleVals) {
        boardSize = board.length;
        gridSize = (int) Math.sqrt(boardSize);
        this.board = board;
        this.possibleVals = possibleVals;
        this.impossibleValCount = impossibleValCount;
        this.checked = checked;
    }

    public GameInformatonContainer clone() {
        int[][] nb = new int[boardSize][boardSize];

        int[][][] np = new int[boardSize + 1][boardSize][boardSize];
        // 0 is not checked
        int[][] nc = new int[boardSize][boardSize];
        // impossibleValCount is the number of impossible vals of a cell
        int[][] nv = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                nb[i][j] = board[i][j];
                nc[i][j] = checked[i][j];
                nv[i][j] = impossibleValCount[i][j];
                for (int k = 1; k <= boardSize; k++) {
                    np[k][i][j] = possibleVals[k][i][j];
                }
            }
        }
        return new GameInformatonContainer(nb, nc, nv, np);
    }


    public void update(int[] pos) {
        int i = pos[0];
        int j = pos[1];
        int val = board[i][j];
        if (val != 0) {
            checked[i][j] = 1;
            for (int m = 0; m < boardSize; m++) {
                //Check row
                if (m != i) {
                    setImpossibleVals(val, m, j);
                }
                //Check column
                if (m != j) {
                    setImpossibleVals(val, i, m);
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
                        setImpossibleVals(val, row, col);
                    }
                }
            }
        }
    }

    public void setImpossibleVals(int val, int row, int col) {
        possibleVals[val][row][col] = 1;
        impossibleValCount[row][col] = 0;
        for (int r = 1; r <= boardSize; r++) {
            impossibleValCount[row][col] += possibleVals[r][row][col];
        }
    }
}