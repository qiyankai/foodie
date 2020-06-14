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

        findNumberIn2DArray(matrix, 5);

    }

    public static boolean findNumberIn2DArray2(int[][] matrix, int target) {
        int width = matrix[0].length;
        int height = matrix[0].length;



        return false;
    }


    public static boolean findNumberIn2DArray(int[][] matrix, int target) {
        for (int i = 0; i < matrix[0].length; i++) {


        }
        // 二分法找到哪行
        int left = 0, right = matrix[0].length, mid = 0;
        while (left <= right) {
            mid = (left + right) / 2;
            if (target == matrix[mid][0]) {
                return true;
            } else if (target > matrix[mid][0]) {
                left = mid + 1;
            } else if (target < matrix[mid][0]) {
                right = mid - 1;
            }

            System.out.println("left--" + left);
            System.out.println("mid--" + mid);
            System.out.println("right--" + right);
            System.out.println("------------------------------");

        }

        left = 1;
        right = matrix[mid].length;
        int[] rowArr = matrix[mid];
        while (left <= right) {
            mid = (left + right) / 2;
            if (target == rowArr[mid]) {
                return true;
            } else if (target > rowArr[0]) {
                left = mid + 1;
            } else if (target < rowArr[0]) {
                right = mid - 1;
            }

            System.out.println("left--" + left);
            System.out.println("mid--" + mid);
            System.out.println("right--" + right);

        }

        return false;
    }

}
