import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HumanSimulationSolver extends SudokuSolver {

    private int nakedSingleCount = 0;
    private int hiddenSingleCount = 0;
    private int lockedCandidateCount = 0;
    private int nakedDoubleCount = 0;
    private int hiddenDoubleCount = 0;
    private int nakedTripleCount = 0;
    private int hiddenTripleCount = 0;

    private int levelUsed = 4;

    private int[][] lockedCandidateChecked;//grid, val
    private int[][][] nakedDoubleChecked;//val, (row/col/grid), pos
    private int[][][] hiddenDoubleChecked;//val, (row/col/grid), pos
    private int[][][] nakedTripleChecked;//val, (row/col/grid), pos
    private int[][][] hiddenTripleChecked;//val, (row/col/grid), pos
    private GameInformatonContainer gic;

    public HumanSimulationSolver(int boardSize) {
        this.boardSize = boardSize;
        gridSize = (int) Math.sqrt(boardSize);
        lockedCandidateChecked = new int[boardSize + 1][boardSize + 1];
        nakedDoubleChecked = new int[boardSize + 1][3][boardSize + 1];
        hiddenDoubleChecked = new int[boardSize + 1][3][boardSize + 1];
        nakedTripleChecked = new int[boardSize + 1][3][boardSize + 1];
        hiddenTripleChecked = new int[boardSize + 1][3][boardSize + 1];
    }

    public int[][] solve(int[][] board) {
        gic = preProcess(board);
        return humanSolve();
    }

    public int difficultyLevel(int[][] board) {
        solve(board);
        return levelUsed;
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
                        if (count > 1) {
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
                        if (count > 1) {
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
                    for (int row = initialRow; row < initialRow + gridSize; row++) {
                        for (int col = initialCol; col < initialCol + gridSize; col++) {
                            if (gic.possibleVals[val][row][col] == 0) {
                                count++;
                                colPos = col;
                                rowPos = row;
                                if (count > 1) {
                                    break;
                                }
                            }
                        }
                        if (count > 1) {
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

    private boolean nakedDouble() {
//        System.out.println("NAKED DOUBLE");
        //Row
        for (int row = 0; row < boardSize; row++) {
            HashMap<String, Integer> candidatemap = new HashMap<>();
            for (int col = 0; col < boardSize; col++) {
                if (gic.impossibleValCount[row][col] == boardSize - 2) {
                    String candidate = "";
                    for (int val = 1; val <= boardSize; val++) {
                        if (gic.possibleVals[val][row][col] == 0) {
                            candidate += val;
                        }
                    }
                    if (candidatemap.containsKey(candidate)) {
                        int val1 = candidate.charAt(0) - '0';
                        int val2 = candidate.charAt(1) - '0';
                        if (nakedDoubleChecked[val1][0][row] == 0) {
                            nakedDoubleChecked[val1][0][row] = 1;
                            nakedDoubleChecked[val2][0][row] = 1;
                            for (int eliminateCol = 0; eliminateCol < boardSize; eliminateCol++) {
                                if (eliminateCol != col && eliminateCol != candidatemap.get(candidate)) {
                                    gic.possibleVals[val1][row][eliminateCol] = 1;
                                    gic.possibleVals[val2][row][eliminateCol] = 1;
                                    gic.countImpossibleVals(row, eliminateCol);
                                }
                            }
                            nakedDoubleCount++;
                            return true;
                        }
                    } else {
                        candidatemap.put(candidate, col);
                    }
                }
            }
        }
        //Col
        for (int col = 0; col < boardSize; col++) {
            HashMap<String, Integer> candidatemap = new HashMap<>();
            for (int row = 0; row < boardSize; row++) {
                if (gic.impossibleValCount[row][col] == boardSize - 2) {
                    String candidate = "";
                    for (int val = 1; val <= boardSize; val++) {
                        if (gic.possibleVals[val][row][col] == 0) {
                            candidate += val;
                        }
                    }
                    if (candidatemap.containsKey(candidate)) {
                        int val1 = candidate.charAt(0) - '0';
                        int val2 = candidate.charAt(1) - '0';
                        if (nakedDoubleChecked[val1][1][col] == 0) {
                            nakedDoubleChecked[val1][1][col] = 1;
                            nakedDoubleChecked[val2][1][col] = 1;
                            for (int eliminateRow = 0; eliminateRow < boardSize; eliminateRow++) {
                                if (eliminateRow != row && eliminateRow != candidatemap.get(candidate)) {
                                    gic.possibleVals[val1][eliminateRow][col] = 1;
                                    gic.possibleVals[val2][eliminateRow][col] = 1;
                                    gic.countImpossibleVals(eliminateRow, col);
                                }
                            }
                            nakedDoubleCount++;
                            return true;
                        }
                    } else {
                        candidatemap.put(candidate, row);
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
                HashMap<String, String> candidatemap = new HashMap<>();
                for (int row = initialRow; row < initialRow + gridSize; row++) {
                    for (int col = initialCol; col < initialCol + gridSize; col++) {
                        if (gic.impossibleValCount[row][col] == boardSize - 2) {
                            String candidate = "";
                            for (int val = 1; val <= boardSize; val++) {
                                if (gic.possibleVals[val][row][col] == 0) {
                                    candidate += val;
                                }
                            }
                            if (candidatemap.containsKey(candidate)) {
                                int val1 = candidate.charAt(0) - '0';
                                int val2 = candidate.charAt(1) - '0';
                                int rowpos = candidatemap.get(candidate).charAt(0) - '0';
                                int colpos = candidatemap.get(candidate).charAt(1) - '0';
                                if (nakedDoubleChecked[val1][2][i * 3 + j] == 0) {
                                    nakedDoubleChecked[val1][2][i * 3 + j] = 1;
                                    nakedDoubleChecked[val2][2][i * 3 + j] = 1;
                                    for (int eliminateRow = initialRow; eliminateRow < initialRow + gridSize; eliminateRow++) {
                                        for (int eliminateCol = initialCol; eliminateCol < initialCol + gridSize; eliminateCol++) {
                                            if ((eliminateRow != row && eliminateRow != rowpos) ||
                                                    (eliminateCol != col && eliminateCol != colpos)) {
                                                gic.possibleVals[val1][eliminateRow][eliminateCol] = 1;
                                                gic.possibleVals[val2][eliminateRow][eliminateCol] = 1;
                                                gic.countImpossibleVals(eliminateRow, eliminateCol);
                                            }
                                        }
                                    }
                                    nakedDoubleCount++;
                                    return true;
                                }
                            } else {
                                candidatemap.put(candidate, row + "" + col);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hiddenDouble() {
//        System.out.println("HIDDEN DOUBLE");
        //Row
        for (int row = 0; row < boardSize; row++) {
            int[] appearCount = new int[boardSize + 1];
            ArrayList<Integer> possible = new ArrayList<>();
            for (int val = 1; val <= boardSize; val++) {
                if (hiddenDoubleChecked[val][0][row] == 1) {
                    continue;
                }
                for (int col = 0; col < boardSize; col++) {
                    appearCount[val] += 1 - gic.possibleVals[val][row][col];
                }
                if (appearCount[val] == 2) {
                    possible.add(val);
                }
            }
            for (int val1 : possible) {
                for (int val2 : possible) {
                    if (val1 != val2) {
                        int col1 = -1;
                        int col2 = -1;
                        for (int col = 0; col < boardSize; col++) {
                            if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                if (col1 == -1) {
                                    col1 = col;
                                } else {
                                    col2 = col;
                                }
                            }
                        }
                        if (col2 != -1) {
                            for (int val = 1; val <= boardSize; val++) {
                                if (val != val1 && val != val2) {
                                    gic.possibleVals[val][row][col1] = 1;
                                    gic.possibleVals[val][row][col2] = 1;
                                }
                            }
                            gic.countImpossibleVals(row, col1);
                            gic.countImpossibleVals(row, col2);
                            hiddenDoubleChecked[val1][0][row] = 1;
                            hiddenDoubleChecked[val2][0][row] = 1;
                            hiddenDoubleCount++;
                            return true;
                        }
                    }
                }
            }
        }
        //Col
        for (int col = 0; col < boardSize; col++) {
            int[] appearCount = new int[boardSize + 1];
            ArrayList<Integer> possible = new ArrayList<>();
            for (int val = 1; val <= boardSize; val++) {
                if (hiddenDoubleChecked[val][1][col] == 1) {
                    continue;
                }
                for (int row = 0; row < boardSize; row++) {
                    appearCount[val] += 1 - gic.possibleVals[val][row][col];
                }
                if (appearCount[val] == 2) {
                    possible.add(val);
                }
            }
            for (int val1 : possible) {
                for (int val2 : possible) {
                    if (val1 != val2) {
                        int row1 = -1;
                        int row2 = -1;
                        for (int row = 0; row < boardSize; row++) {
                            if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                if (row1 == -1) {
                                    row1 = row;
                                } else {
                                    row2 = row;
                                }
                            }
                        }
                        if (row2 != -1) {
                            for (int val = 1; val <= boardSize; val++) {
                                if (val != val1 && val != val2) {
                                    gic.possibleVals[val][row1][col] = 1;
                                    gic.possibleVals[val][row2][col] = 1;
                                }
                            }
                            gic.countImpossibleVals(row1, col);
                            gic.countImpossibleVals(row2, col);
                            hiddenDoubleChecked[val1][1][col] = 1;
                            hiddenDoubleChecked[val2][1][col] = 1;
                            hiddenDoubleCount++;
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
                int[] appearCount = new int[boardSize + 1];
                ArrayList<Integer> possible = new ArrayList<>();
                for (int val = 1; val <= boardSize; val++) {
                    if (hiddenDoubleChecked[val][2][i * 3 + j] == 1) {
                        continue;
                    }
                    for (int row = initialRow; row < initialRow + gridSize; row++) {
                        for (int col = initialCol; col < initialCol + gridSize; col++) {
                            appearCount[val] += 1 - gic.possibleVals[val][row][col];
                        }
                    }
                    if (appearCount[val] == 2) {
                        possible.add(val);
                    }
                }
                for (int val1 : possible) {
                    for (int val2 : possible) {
                        if (val1 != val2) {
                            int row1 = -1;
                            int row2 = -1;
                            int col1 = -1;
                            int col2 = -1;
                            for (int row = initialRow; row < initialRow + gridSize; row++) {
                                for (int col = initialCol; col < initialCol + gridSize; col++) {
                                    if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                        if (col1 == -1) {
                                            row1 = row;
                                            col1 = col;
                                        } else {
                                            row2 = row;
                                            col2 = col;
                                        }
                                    }
                                }
                            }
                            if (col2 != -1) {
                                for (int val = 1; val <= boardSize; val++) {
                                    if (val != val1 && val != val2) {
                                        gic.possibleVals[val][row1][col1] = 1;
                                        gic.possibleVals[val][row2][col2] = 1;
                                    }
                                }
                                gic.countImpossibleVals(row1, col1);
                                gic.countImpossibleVals(row2, col2);
                                hiddenDoubleChecked[val1][2][i * 3 + j] = 1;
                                hiddenDoubleChecked[val2][2][i * 3 + j] = 1;
                                hiddenDoubleCount++;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private int[] setToArray(Set<Integer> input) {
        int[] res = new int[input.size()];
        int index = 0;
        for (int i : input) {
            res[index] = i;
            index++;
        }
        return res;
    }

    private boolean nakedTriple() {
//        System.out.println("NAKED TRIPLE");
        //Row
        for (int row = 0; row < boardSize; row++) {
            HashMap<Integer, String> candidatemap = new HashMap<>();
            for (int col = 0; col < boardSize; col++) {
                if (gic.impossibleValCount[row][col] == boardSize - 2 || gic.impossibleValCount[row][col] == boardSize - 3) {
                    String candidate = "";
                    for (int val = 1; val <= boardSize; val++) {
                        if (nakedTripleChecked[val][0][row] == 1) {
                            continue;
                        }
                        if (gic.possibleVals[val][row][col] == 0) {
                            candidate += val;
                        }
                    }
                    candidatemap.put(col, candidate);
                }
            }
            int[] colArray = setToArray(candidatemap.keySet());
            if (colArray.length < 3) {
                continue;
            }
            for (int i = 0; i < colArray.length; i++) {
                for (int j = i + 1; j < colArray.length; j++) {
                    for (int k = j + 1; k < colArray.length; k++) {
                        String candidates = candidatemap.get(colArray[i]);
                        candidates += candidatemap.get(colArray[j]);
                        candidates += candidatemap.get(colArray[k]);
                        Set<Integer> candidateSet = new HashSet<>();
                        for (char c : candidates.toCharArray()) {
                            candidateSet.add(c - '0');
                        }
                        if (candidateSet.size() == 3) {
                            for (int eliminateCol = 0; eliminateCol < boardSize; eliminateCol++) {
                                if (eliminateCol != colArray[i] && eliminateCol != colArray[j] && eliminateCol != colArray[k]) {
                                    for (int val : candidateSet) {
                                        gic.possibleVals[val][row][eliminateCol] = 1;
                                    }
                                    gic.countImpossibleVals(row, eliminateCol);
                                }
                            }
                            for (int val : candidateSet) {
                                nakedTripleChecked[val][0][row] = 1;
                            }
                            nakedTripleCount++;
                            return true;
                        }
                    }
                }
            }
        }

        //Col
        for (int col = 0; col < boardSize; col++) {
            HashMap<Integer, String> candidatemap = new HashMap<>();
            for (int row = 0; row < boardSize; row++) {
                if (gic.impossibleValCount[row][col] == boardSize - 2 || gic.impossibleValCount[row][col] == boardSize - 3) {
                    String candidate = "";
                    for (int val = 1; val <= boardSize; val++) {
                        if (nakedTripleChecked[val][1][col] == 1) {
                            continue;
                        }
                        if (gic.possibleVals[val][row][col] == 0) {
                            candidate += val;
                        }
                    }
                    candidatemap.put(row, candidate);
                }
            }
            int[] rowArray = setToArray(candidatemap.keySet());
            if (rowArray.length < 3) {
                continue;
            }
            for (int i = 0; i < rowArray.length; i++) {
                for (int j = i + 1; j < rowArray.length; j++) {
                    for (int k = j + 1; k < rowArray.length; k++) {
                        String candidates = candidatemap.get(rowArray[i]);
                        candidates += candidatemap.get(rowArray[j]);
                        candidates += candidatemap.get(rowArray[k]);
                        Set<Integer> candidateSet = new HashSet<>();
                        for (char c : candidates.toCharArray()) {
                            candidateSet.add(c - '0');
                        }
                        if (candidateSet.size() == 3) {
                            for (int eliminateRow = 0; eliminateRow < boardSize; eliminateRow++) {
                                if (eliminateRow != rowArray[i] && eliminateRow != rowArray[j] && eliminateRow != rowArray[k]) {
                                    for (int val : candidateSet) {
                                        gic.possibleVals[val][eliminateRow][col] = 1;
                                    }
                                    gic.countImpossibleVals(eliminateRow, col);
                                }
                            }
                            for (int val : candidateSet) {
                                nakedTripleChecked[val][1][col] = 1;
                            }
                            nakedTripleCount++;
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
                HashMap<String, String> candidatemap = new HashMap<>();
                for (int row = initialRow; row < initialRow + gridSize; row++) {
                    for (int col = initialCol; col < initialCol + gridSize; col++) {
                        if (gic.impossibleValCount[row][col] == boardSize - 2 || gic.impossibleValCount[row][col] == boardSize - 3) {
                            String candidate = "";
                            for (int val = 1; val <= boardSize; val++) {
                                if (nakedTripleChecked[val][2][i * 3 + j] == 1) {
                                    continue;
                                }
                                if (gic.possibleVals[val][row][col] == 0) {
                                    candidate += val;
                                }
                            }
                            candidatemap.put(row + "" + col, candidate);
                        }
                    }
                }
                String[] indexArray = new String[candidatemap.keySet().size()];
                int index = 0;
                for (String str : candidatemap.keySet()) {
                    indexArray[index] = str;
                    index++;
                }
                if (indexArray.length < 3) {
                    continue;
                }
                for (int p = 0; p < indexArray.length; p++) {
                    for (int q = p + 1; q < indexArray.length; q++) {
                        for (int k = q + 1; k < indexArray.length; k++) {
                            String candidates = candidatemap.get(indexArray[p]);
                            candidates += candidatemap.get(indexArray[q]);
                            candidates += candidatemap.get(indexArray[k]);
                            Set<Integer> candidateSet = new HashSet<>();
                            for (char c : candidates.toCharArray()) {
                                candidateSet.add(c - '0');
                            }
                            if (candidateSet.size() == 3) {
                                int rowpos1 = indexArray[p].charAt(0) - '0';
                                int rowpos2 = indexArray[q].charAt(0) - '0';
                                int rowpos3 = indexArray[k].charAt(0) - '0';
                                int colpos1 = indexArray[p].charAt(1) - '0';
                                int colpos2 = indexArray[q].charAt(1) - '0';
                                int colpos3 = indexArray[k].charAt(1) - '0';

                                for (int eliminateRow = initialRow; eliminateRow < initialRow + gridSize; eliminateRow++) {
                                    for (int eliminateCol = initialCol; eliminateCol < initialCol + gridSize; eliminateCol++) {
                                        if ((eliminateRow != rowpos1 && eliminateRow != rowpos2 && eliminateRow != rowpos3) ||
                                                (eliminateCol != colpos1 && eliminateCol != colpos2 && eliminateCol != colpos3)) {
                                            for (int val : candidateSet) {
                                                gic.possibleVals[val][eliminateRow][eliminateCol] = 1;
                                            }
                                            gic.countImpossibleVals(eliminateRow, eliminateCol);
                                        }
                                    }
                                }
                                for (int val : candidateSet) {
                                    nakedTripleChecked[val][2][i * 3 + j] = 1;
                                }
                                nakedTripleCount++;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hiddenTriple() {
//        System.out.println("HIDDEN TRIPLE");
        //Row
        for (int row = 0; row < boardSize; row++) {
            int[] appearCount = new int[boardSize + 1];
            ArrayList<Integer> possible = new ArrayList<>();
            for (int val = 1; val <= boardSize; val++) {
                if (hiddenTripleChecked[val][0][row] == 1) {
                    continue;
                }
                for (int col = 0; col < boardSize; col++) {
                    appearCount[val] += 1 - gic.possibleVals[val][row][col];
                }
                if (appearCount[val] == 2 || appearCount[val] == 3) {
                    possible.add(val);
                }
            }
            for (int val1 : possible) {
                for (int val2 : possible) {
                    for (int val3 : possible) {
                        if (val1 != val2 && val1 != val3 && val3 != val2) {
                            HashSet<Integer> colSet = new HashSet<>();
                            for (int col = 0; col < boardSize; col++) {
                                if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                    colSet.add(col);
                                }
                                if (gic.possibleVals[val3][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                    colSet.add(col);
                                }
                                if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val3][row][col] == 0) {
                                    colSet.add(col);
                                }
                            }
                            if (colSet.size() == 3) {
                                for (int val = 1; val <= boardSize; val++) {
                                    if (val != val1 && val != val2 && val != val3) {
                                        for (int colindex : colSet) {
                                            gic.possibleVals[val][row][colindex] = 1;
                                        }
                                    }
                                }
                                for (int colindex : colSet) {
                                    gic.countImpossibleVals(row, colindex);
                                }
                                hiddenTripleChecked[val1][0][row] = 1;
                                hiddenTripleChecked[val2][0][row] = 1;
                                hiddenTripleChecked[val3][0][row] = 1;
                                hiddenTripleCount++;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        //Col
        for (int col = 0; col < boardSize; col++) {
            int[] appearCount = new int[boardSize + 1];
            ArrayList<Integer> possible = new ArrayList<>();
            for (int val = 1; val <= boardSize; val++) {
                if (hiddenTripleChecked[val][1][col] == 1) {
                    continue;
                }
                for (int row = 0; row < boardSize; row++) {
                    appearCount[val] += 1 - gic.possibleVals[val][row][col];
                }
                if (appearCount[val] == 2 || appearCount[val] == 3) {
                    possible.add(val);
                }
            }
            for (int val1 : possible) {
                for (int val2 : possible) {
                    for (int val3 : possible) {
                        if (val1 != val2 && val1 != val3 && val3 != val2) {
                            HashSet<Integer> rowSet = new HashSet<>();
                            for (int row = 0; row < boardSize; row++) {
                                if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                    rowSet.add(row);
                                }
                                if (gic.possibleVals[val3][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                    rowSet.add(row);
                                }
                                if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val3][row][col] == 0) {
                                    rowSet.add(row);
                                }
                            }
                            if (rowSet.size() == 3) {
                                for (int val = 1; val <= boardSize; val++) {
                                    if (val != val1 && val != val2 && val != val3) {
                                        for (int rowindex : rowSet) {
                                            gic.possibleVals[val][rowindex][col] = 1;
                                        }
                                    }
                                }
                                for (int rowindex : rowSet) {
                                    gic.countImpossibleVals(rowindex, col);
                                }
                                hiddenTripleChecked[val1][1][col] = 1;
                                hiddenTripleChecked[val2][1][col] = 1;
                                hiddenTripleChecked[val3][1][col] = 1;
                                hiddenTripleCount++;
                                return true;
                            }
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
                int[] appearCount = new int[boardSize + 1];
                ArrayList<Integer> possible = new ArrayList<>();
                for (int val = 1; val <= boardSize; val++) {
                    if (hiddenTripleChecked[val][2][i * 3 + j] == 1) {
                        continue;
                    }
                    for (int row = initialRow; row < initialRow + gridSize; row++) {
                        for (int col = initialCol; col < initialCol + gridSize; col++) {
                            appearCount[val] += 1 - gic.possibleVals[val][row][col];
                        }
                    }
                    if (appearCount[val] == 2 || appearCount[val] == 3) {
                        possible.add(val);
                    }
                }
                for (int val1 : possible) {
                    for (int val2 : possible) {
                        for (int val3 : possible) {
                            if (val1 != val2 && val1 != val3 && val3 != val2) {
                                HashSet<String> indexSet = new HashSet<>();
                                for (int row = initialRow; row < initialRow + gridSize; row++) {
                                    for (int col = initialCol; col < initialCol + gridSize; col++) {
                                        if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                            indexSet.add(row + "" + col);
                                        }
                                        if (gic.possibleVals[val3][row][col] == 0 && gic.possibleVals[val2][row][col] == 0) {
                                            indexSet.add(row + "" + col);
                                        }
                                        if (gic.possibleVals[val1][row][col] == 0 && gic.possibleVals[val3][row][col] == 0) {
                                            indexSet.add(row + "" + col);
                                        }
                                    }
                                }
                                if (indexSet.size() == 3) {
                                    for (int val = 1; val <= boardSize; val++) {
                                        if (val != val1 && val != val2 && val != val3) {
                                            for (String index : indexSet) {
                                                gic.possibleVals[val][index.charAt(0) - '0'][index.charAt(1) - '0'] = 1;
                                            }
                                        }
                                    }
                                    for (String index : indexSet) {
                                        gic.countImpossibleVals(index.charAt(0) - '0', index.charAt(1) - '0');
                                    }
                                    hiddenTripleChecked[val1][2][i * 3 + j] = 1;
                                    hiddenTripleChecked[val2][2][i * 3 + j] = 1;
                                    hiddenTripleChecked[val3][2][i * 3 + j] = 1;
                                    hiddenTripleCount++;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean lockedCandidate() {
//        System.out.println("LOCKED CANDIDATE");
        int gridCount = boardSize / gridSize;
        for (int i = 0; i < gridCount; i++) {
            for (int j = 0; j < gridCount; j++) {
                int initialRow = i * gridSize;
                int initialCol = j * gridSize;
                for (int val = 1; val <= boardSize; val++) {
                    if (lockedCandidateChecked[i * 3 + j][val] == 1) {
                        continue;
                    }
                    int rowcount = 0;
                    int rowindex = -1;
                    int colcount = 0;
                    int colindex = -1;
                    for (int row = initialRow; row < initialRow + gridSize; row++) {
                        for (int col = initialCol; col < initialCol + gridSize; col++) {
                            if (gic.checked[row][col] == 1) {
                                continue;
                            }
                            if (gic.possibleVals[val][row][col] == 0) {
                                if (rowcount == 0) {
                                    rowcount++;
                                    colcount++;
                                    colindex = col;
                                    rowindex = row;
                                } else {
                                    if (row != rowindex) {
                                        rowcount++;
                                    }
                                    if (col != colindex) {
                                        colcount++;
                                    }
                                }
                            }
                        }
                    }
                    if (rowcount == 1 && colcount != 1) {
                        for (int k = 0; k < boardSize; k++) {
                            if (k < initialCol || k >= initialCol + gridSize) {
                                if (gic.board[rowindex][k] == 0) {
                                    gic.possibleVals[val][rowindex][k] = 1;
                                    gic.countImpossibleVals(rowindex, k);
                                }
                            }
                        }
                        lockedCandidateChecked[i * 3 + j][val] = 1;
                        lockedCandidateCount++;
                        return true;
                    }
                    if (colcount == 1 && rowcount != 1) {
                        for (int k = 0; k < boardSize; k++) {
                            if (k < initialRow || k >= initialRow + gridSize) {
                                if (gic.board[k][colindex] == 0) {
                                    gic.possibleVals[val][k][colindex] = 1;
                                    gic.countImpossibleVals(k, colindex);
                                }
                            }
                        }
                        lockedCandidateChecked[i * 3 + j][val] = 1;
                        lockedCandidateCount++;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static String get2DArrayPrint(int[][] matrix) {
        String output = new String();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    output = output + ("-" + "\t");
                } else {
                    output = output + (matrix[i][j] + "\t");
                }
            }
            output = output + "\n";
        }
        return output;
    }


    public int[][] humanSolve() {
        if (tryStrategy(1))
            return gic.board;
        if (tryStrategy(2))
            return gic.board;
        if (tryStrategy(3))
            return gic.board;
        return null;
    }

    public boolean checkAttemptResults(int level) {
        StringBuilder sb = new StringBuilder();
        switch (level) {
            case 3:
                sb.append("Hidden Triple Strategy Count: ").append(hiddenTripleCount).append("\n");
                sb.append("Naked Triple Strategy Count: ").append(nakedTripleCount).append("\n");
            case 2:
                sb.append("Hidden Double Strategy Count: ").append(hiddenDoubleCount).append("\n");
                sb.append("Naked Double Strategy Count: ").append(nakedDoubleCount).append("\n");
                sb.append("Locked Candidate Strategy Count: ").append(lockedCandidateCount).append("\n");
            case 1:
                sb.append("Hidden Single Strategy Count: ").append(hiddenSingleCount).append("\n");
                sb.append("Naked Single Strategy Count: ").append(nakedSingleCount).append("\n");
            default:
                boolean res = findNextZero(gic.board) == null;
                System.out.print(sb.toString());
                if (res) {
                    System.out.println("Solved by using up to LEVEL " + level + " Strategies.");
                    levelUsed = level;
                } else {
                    System.out.println("LEVEL " + level + " Strategies cannot solve the puzzle.");
//                    printArray(gic.board);
                }
                System.out.println("**************************************");
                return res;
        }
    }

    public boolean tryStrategy(int level) {
        while (applyStrategy(level)) {
        }
        return checkAttemptResults(level);
    }

    public boolean applyStrategy(int level) {
        ArrayList<Boolean> resultList = new ArrayList<>();
        switch (level) {
            case 3:
                resultList.add(hiddenTriple());
                resultList.add(nakedTriple());
            case 2:
                resultList.add(hiddenDouble());
                resultList.add(nakedDouble());
                resultList.add(lockedCandidate());
            case 1:
                resultList.add(hiddenSingle());
                resultList.add(nakedSingle());
            default:
                return resultList.contains(true);
        }
    }

}
