package algorithm;

public class 有序二维数组查找 {
    public static void main(String[] args) {
        /**
         * [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]]
         * 5
         */
        int[] row0 = new int[]{1, 4, 7, 11, 15};
        int[] row1 = new int[]{2, 5, 8, 12, 19};
        int[] row2 = new int[]{3, 6, 9, 16, 22};
        int[] row3 = new int[]{10, 13, 14, 17, 24};
        int[] row4 = new int[]{18, 21, 23, 26, 30};

        int[][] matrix = new int[5][];
        matrix[0] = row0;
        matrix[1] = row1;
        matrix[2] = row2;
        matrix[3] = row3;
        matrix[4] = row4;

        System.out.println(findNumberIn2DArray(matrix, 10));

    }

    public static boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int rows = matrix.length, columns = matrix[0].length;
        int row = 0, column = columns - 1;
        while (row < rows && column >= 0) {
            int num = matrix[row][column];
            if (num == target) {
                return true;
            } else if (num > target) {
                column--;
            } else {
                row++;
            }
        }
        return false;
    }

}
