package algorithm;

public class 替换空格 {
    public static void main(String[] args) {

        System.out.println(replaceSpace("We are happy."));
    }
    public static String replaceSpace(String s) {

        int len = s.length(), size = 0;
        char[] resultArr = new char[len * 3];

        for (int i = 0; i < s.length(); i++) {
            char currChar = s.charAt(i);
            if (' '==(currChar)) {
                resultArr[size++] = '%';
                resultArr[size++] = '2';
                resultArr[size++] = '0';
            } else {
                resultArr[size++] = currChar;
            }
        }

        return new String(resultArr, 0, size);
    }
}
