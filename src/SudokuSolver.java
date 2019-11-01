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
}
