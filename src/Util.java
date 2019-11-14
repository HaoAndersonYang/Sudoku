public class Util {
    public static int[][] arrayCopy(int[][] input) {
        int[][] res = new int[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                res[i][j] = input[i][j];
            }
        }
        return res;
    }


    public static int[][] rotation(int[][] input) {
        int[][] res = new int[input.length][input.length];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[j][res.length - i - 1] = input[i][j];
            }
        }
        return res;
    }

    public static int[][] mirror(int[][] input) {
        int[][] res = Util.arrayCopy(input);
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[res.length - i - 1][j] = input[i][j];
            }
        }
        return res;
    }

    public static int[][] transposition(int[][] input) {
        int[][] res = Util.arrayCopy(input);
        int r1 = (int) (Math.random() * 9);
        int r2 = 0;
        switch (r1) {
            case 0:
            case 3:
            case 6:
                r2 = (int) (r1 + Math.floor(Math.random() * 2) + 1);
                break;
            case 1:
            case 4:
            case 7:
                if ((int) (Math.random()) == 0) {
                    r2 = r1 + 1;
                } else {
                    r2 = r1 - 1;
                }
                break;
            case 2:
            case 5:
            case 8:
                r2 = (int) (r1 - Math.floor(Math.random() * 2) - 1);
        }
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                if (i == r1) {
                    res[i][j] = input[r2][j];
                }
                if (i == r2) {
                    res[i][j] = input[r1][j];
                }
            }
        }
        return res;
    }

    public static int[][] ciphering(int[][] input) {
        int[][] res = Util.arrayCopy(input);
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                res[j][res.length - i - 1] = input[i][j];
            }
        }
        return res;
    }
}
