package algorithm;


import java.util.HashSet;
import java.util.LinkedList;

public class 无重复字符的最长子串 {
    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring2("dvdf"));
    }

    public static int lengthOfLongestSubstring(String s) {
        int result = 0;

        HashSet<Character> charSet = new HashSet<>();

        LinkedList<Character> charQueue = new LinkedList<>();


        char[] charArray = s.toCharArray();

        for (int i = 0; i < charArray.length; i++) {

            char currChar = charArray[i];
            charQueue.addLast(currChar);

            if (!charSet.add(currChar)) {
                while (!charQueue.isEmpty()) {
                    Character dequeue = charQueue.removeFirst();
                    if (dequeue.equals(currChar)) {
                        break;
                    } else {
                        charSet.remove(dequeue);
                    }
                }
            }

            if (charSet.size() > result) {
                result = charSet.size();
            }

        }
        return result;
    }

    public static int lengthOfLongestSubstring2(String s) {
        int result = 0;
        LinkedList<Character> charQueue = new LinkedList<>();

        for (int i = 0; i < s.length(); i++) {
            char currChar = s.charAt(i);

            int indexOf = charQueue.indexOf(currChar);
            charQueue.addLast(currChar);

            if (indexOf != -1) {
                while (!charQueue.isEmpty()) {
                    Character dequeue = charQueue.removeFirst();
                    if (dequeue.equals(currChar)) {
                        break;
                    }
                }
            }
            result = charQueue.size() > result ? charQueue.size() : result;

        }
        return result;
    }

}
