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
}
