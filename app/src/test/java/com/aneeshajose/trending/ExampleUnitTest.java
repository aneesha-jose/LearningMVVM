package com.aneeshajose.trending;

import com.google.common.truth.Truth;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIsAnagram() {
        Truth.assertThat(isAnAnagram("funeral", "reAlfun")).isTrue();
    }

    boolean isAnAnagram(String a, String b) {
        if (a.length() != b.length())
            return false;

        Map<String, Integer> map = new HashMap();

        for (int i = 0; i < a.length(); i++) {
            String cur = String.valueOf(a.charAt(i));
            if (b.contains(cur)) {
                int lastKnownIndex = 0;
                if (map.containsKey(cur)) {
                    lastKnownIndex = b.indexOf(cur, map.get(cur) + 1);
                } else lastKnownIndex = b.indexOf(cur);
                if (lastKnownIndex == -1)
                    return false;
                map.put(cur, lastKnownIndex);
            } else
                return false;
        }

        return true;
    }

    @Test
    public void trialSpace() {
//        numUniqueEmails(new String[]{"testemail@leetcode.com","testemail1@leetcode.com","testemail+david@lee.tcode.com"});
        Truth.assertThat(oddEvenJumps(new int[]{1, 2, 3, 2, 1, 4, 4, 5})).isEqualTo(6);
    }

    public int numUniqueEmails(String[] emails) {
        List<String> uniqueFinalAddrs = new ArrayList<>();

        for (String current : emails) {
            String[] parts = current.split("@");
            String addr = simplifiedLocalName(parts[0]) + "@" + parts[1];
            if (!uniqueFinalAddrs.contains(addr))
                uniqueFinalAddrs.add(addr);
        }

        return uniqueFinalAddrs.size();
    }

    private String simplifiedLocalName(String input) {
        if (input.contains("+")) {
            input = input.substring(0, input.indexOf("+"));
        }
        return input.replaceAll("\\.", "");
    }

    private int oddEvenJumps(int[] A) {

        HashMap<Integer, NumJumpData> trackerMap = new HashMap<>();
        int successJump = 1;

        for (int i = 0; i < (A.length - 1); i++) {
            successJump += isProperStartPoint(A, i, 1, trackerMap) ? 1 : 0;
        }

        return successJump;
    }

    private boolean isProperStartPoint(int[] array, int index, int nextJumpCount, HashMap<Integer, NumJumpData> trackerMap) {
        if (index == array.length - 1)
            return true;

        boolean isEven = (nextJumpCount % 2) == 0;

        NumJumpData data = trackerMap.get(index);

        if (data == null) {
            data = new NumJumpData(index);
            trackerMap.put(index, data);
            data.setJump(isEven, findNextJumpIndex(array, index, isEven));
        } else if (!data.isJumpValid(isEven)) {
            data.setJump(isEven, findNextJumpIndex(array, index, isEven));
        }

        if (data.getJump(isEven) < 0)
            return false;

        return isProperStartPoint(array, data.getJump(isEven), ++nextJumpCount, trackerMap);
    }

    private int findNextJumpIndex(int[] array, int index, boolean isEven) {
        int compIndex = -1;
        for (int i = index + 1; i < array.length; i++) {
            if (isValidJumpCriteria(isEven, array[index], compIndex > 0 ? array[compIndex] : null, array[i])) {
                compIndex = i;
            }
        }
        return compIndex;
    }

    private boolean isValidJumpCriteria(boolean isEven, int value, Integer comparator, int input) {
        if (isEven) {
            return value >= input && (comparator == null || comparator < input);
        } else {
            return value <= input && (comparator == null || input < comparator);
        }
    }

    class NumJumpData {
        int index;
        int oddJump = 0;
        int evenJump = 0;

        NumJumpData(int indx) {
            this.index = indx;
        }

        public boolean isJumpValid(boolean isEven) {
            return isEven ? (evenJump != 0) : (oddJump != 0);
        }

        public int getJump(boolean isEven) {
            return isEven ? evenJump : oddJump;
        }

        public void setJump(boolean isEven, int value) {
            if (isEven)
                evenJump = value;
            else oddJump = value;
        }

    }

    @Test
    public void trialSpace2() {
        int n = 4;

        List<String> result = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            StringBuilder currentRegex = new StringBuilder();
            for (int j = i; j > 0; j--)
                currentRegex.append("(");
            String valid;
            boolean divisibility = true;
            while (true) {
                valid = getValidStrings(n, i, 0, 0, 0, divisibility, currentRegex.toString());
                if (result.contains(valid))
                    break;
                result.add(valid);
                System.out.println(valid);
                divisibility = false;
            }
        }
    }

    private String getValidStrings(int n, int iteration, int validBrackets, int openParenCounter, int closedParenCounter, boolean divisibility, String regex) {
        if (validBrackets >= n)
            return "";
        if (openParenCounter == 0 || (divisibility && validBrackets == openParenCounter && (iteration * 2) < n))
            return regex + getValidStrings(n, iteration, validBrackets, openParenCounter + iteration, closedParenCounter, divisibility, regex);
        else if (openParenCounter > closedParenCounter) {
            StringBuilder currentRegex = new StringBuilder();
            do {
                currentRegex.append(")");
                closedParenCounter++;
                validBrackets++;
            } while (!divisibility && closedParenCounter < openParenCounter);
            if(divisibility && openParenCounter < n){
                currentRegex.append("(");
                openParenCounter++;
            }
            return currentRegex.toString() + getValidStrings(n, iteration, validBrackets, openParenCounter, closedParenCounter, divisibility, regex);
        } else if (openParenCounter < n) {
            StringBuilder currentRegex = new StringBuilder();
            int counter = (n - openParenCounter);
            while (counter > 0) {
                currentRegex.append("(");
                counter--;
            }
            return currentRegex.toString() + getValidStrings(n, iteration, openParenCounter, (openParenCounter + (n - openParenCounter)), closedParenCounter, divisibility, regex);
        }
        return "";
    }

    @Test
    public void trialMethod(){
//        Truth.assertThat(licenseKeyFormatting("5F3Z-2e-9-w",4)).isEqualTo("5F3Z-2E9W");
        Truth.assertThat(totalFruit(new int[]{0,1,2,2})).isEqualTo(3);
    }

    private String licenseKeyFormatting(String S, int K) {
        String input = S.replaceAll("-","").toUpperCase();
        int groupLength = input.length() % K;
        if(groupLength == 0){
            groupLength = K;
        }
        StringBuilder output = new StringBuilder("");
        for(int i = 0; i < input.length(); i++){
            if(i == groupLength)
                output.append("-");
            output.append(input.charAt(i));
            groupLength += K;
        }
        return output.toString();
    }

    private int totalFruit(int[] tree) {
        int firstIndex = 0;
        int fruitCounter = 0;
        int secondIndex = 0;

        int max = 0;

        while(firstIndex < (tree.length - 1)){
            for(int i=firstIndex; i <tree.length; i++){
                if(tree[i] == tree[firstIndex])
                    fruitCounter++;
                else if(tree[firstIndex] == tree[secondIndex]){
                    secondIndex = i;
                    fruitCounter++;
                } else if(tree[i] == tree[secondIndex])
                    fruitCounter++;
                else
                    break;
            }
            if(fruitCounter > max)
                max = fruitCounter;
            if(firstIndex == secondIndex)
                break;
            firstIndex = secondIndex;
            fruitCounter = 0;
        }
        return max;
    }
}