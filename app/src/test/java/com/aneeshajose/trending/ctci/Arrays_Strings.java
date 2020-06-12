package com.aneeshajose.trending.ctci;

import com.google.common.truth.Truth;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.min;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;

/**
 * Local unit test, which will execute on the development machine (host) for running solution code for CTCI Arrays & Strings chapter
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Arrays_Strings {

    @Test
    public void string_isUnique() {
        assertEquals(hasAllUniqueCharacters("Document"), true);
    }

    @Test
    public void string_isNotUnique() {
        assertEquals(hasAllUniqueCharacters("documentation"), false);
    }


    public static boolean hasAllUniqueCharacters(String input) {
        String inputInLowerCase = input.toLowerCase();
        int[] alphabets = new int[26];
        int subtractionConstant = ((int) 'a');
        for (int i = 0; i < inputInLowerCase.length(); i++) {
            int index = inputInLowerCase.charAt(i) - subtractionConstant;
            if (alphabets[index] > 0) return false;
            alphabets[index]++;
        }
        return true;
    }

    @Test
    public void string_isPermutation() {
        assertEquals(isPermutation("tear", "tare"), true);
    }

    @Test
    public void string_isNotPermutation() {
        assertEquals(isPermutation("rest", "trees"), false);
    }

    public static boolean isPermutation(String givenString, String possPerm) {
        if (givenString.length() != possPerm.length()) return false;
        StringBuilder copyOfGivenStr = new StringBuilder(givenString);
        int index;
        for (char ch : possPerm.toCharArray()) {
            index = copyOfGivenStr.indexOf(String.valueOf(ch));
            if (index == -1)
                return false;
            copyOfGivenStr.deleteCharAt(index);
        }
        return copyOfGivenStr.length() == 0;
    }

    @Test
    public void string_isOneAway() {
        assertEquals(isOneAway("pale", "ple"), true);
        assertEquals(isOneAway("pales", "pale"), true);
        assertEquals(isOneAway("pale", "bale"), true);
        assertEquals(isOneAway("pale", "pale"), true);
    }

    @Test
    public void string_isNotOneAway() {
        assertEquals(isOneAway("pale", "bake"), false);
        assertEquals(isOneAway("pale", "pe"), false);
    }

    public static boolean isOneAway(String a, String b) {
        if (a.length() != b.length())
            return requiresOnlyOneAddition(a, b);
        else
            return requiresMaxOneReplacement(a, b);
    }

    private static boolean requiresOnlyOneAddition(String a, String b) {
        String largerString = a.length() > b.length() ? a : b;
        StringBuilder mutableString = new StringBuilder(a.length() > b.length() ? b : a);
        if (largerString.length() - mutableString.length() != 1)
            return false;
        for (int i = 0; i < largerString.length(); i++) {
            if (i >= mutableString.length()) {
                mutableString.append(largerString.charAt(i));
                break;
            }
            if (largerString.charAt(i) != mutableString.charAt(i)) {
                mutableString.insert(i, largerString.charAt(i));
                break;
            }
        }
        return largerString.equals(mutableString.toString());
    }

    private static boolean requiresMaxOneReplacement(String a, String b) {
        if (a.equals(b)) return true;
        StringBuilder mutableString = new StringBuilder(b);
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != mutableString.charAt(i)) {
                mutableString.setCharAt(i, a.charAt(i));
                break;
            }
        }
        return a.equals(mutableString.toString());
    }

    @Test
    public void test_maxSet() {
        ArrayList<Integer> input = new ArrayList<>();
//        input.add(756898537);
        input.add(-1973594324);
        input.add(-2038664370);
        input.add(-184803526);
//        input.add(1424268980);
        String result = maxset(input).toString();
        System.out.println(result);
        assertEquals(result, "[]");
    }

    public ArrayList<Integer> maxset(ArrayList<Integer> A) {
        long sum = 0;
        long maxSum = -1;
        int startIndex = -1;
        int endIndex = -1;
        int maxStartIndex = -1;
        int maxEndIndex = -1;
        if (!(A == null || A.isEmpty())) {
            int i = 0;
            while (i < A.size()) {
                for (; i < A.size(); i++) {
                    if (A.get(i) >= 0) {
                        if (startIndex < 0) startIndex = i;
                        endIndex = i;
                        sum += A.get(i);
                    } else break;
                }
                if (startIndex >= 0 && endIndex >= 0 && sum >= 0) {
                    if (sum > maxSum) {
                        maxStartIndex = startIndex;
                        maxEndIndex = endIndex;
                        maxSum = sum;
                    } else if (sum == maxSum) {
                        int maxIndexDiff = maxEndIndex - maxStartIndex;
                        int indexDiff = endIndex - startIndex;
                        if (indexDiff > maxIndexDiff || (indexDiff == maxIndexDiff && startIndex < maxStartIndex)) {
                            maxStartIndex = startIndex;
                            maxEndIndex = endIndex;
                        }
                    }
                }
                startIndex = i + 1;
                endIndex = i + 1;
                sum = 0;
                i++;
            }
        }
        return maxStartIndex < 0 || maxEndIndex < 0 ? new ArrayList<>() : new ArrayList<>(A.subList(maxStartIndex, maxEndIndex + 1));
    }

    @Test
    public void test_pascal() {
        System.out.println(solve(5));
    }

    public ArrayList<ArrayList<Integer>> solve(int A) {
        ArrayList<ArrayList<Integer>> rslt = new ArrayList<>();
        if (A > 0) {
            int row = 1;
            for (; row <= A; row++) {
                ArrayList<Integer> patternRow = new ArrayList<>();
                patternRow.add(1);
                for (int i = 1; i <= row / 2; i++) {
                    ArrayList<Integer> prevRow = rslt.get(row - 2);
                    if (prevRow.size() >= 2 && i <= prevRow.size()) {
                        patternRow.add((prevRow.get(i) + prevRow.get(i - 1)));
                    }
                }
                for (int j = ((row - patternRow.size()) - 1); j >= 0; j--) {
                    patternRow.add(patternRow.get(j));
                }
                rslt.add(patternRow);
            }
        }
        return rslt;
    }

    @Test
    public void test_pascalRow() {
        System.out.println(getRow(4));
    }

    public ArrayList<Integer> getRow(int A) {
        ArrayList<Integer> row = new ArrayList<>();
        row.add(1);
        if (A > 1) {
            row.add(1);// 1 1
            for (int i = 2; i <= A; i++) {
                int prev = row.get(0);
                for (int j = 1; j < i; j++) {
                    int sum = prev + row.get(j);
                    prev = row.get(j);
                    row.set(j, sum);
                }
                row.add(1);
            }
        }
        return row;
    }

    @Test
    public void test_wave() {
        ArrayList<Integer> input = new ArrayList<>();
        input.add(6);
        input.add(17);
        input.add(15);
        input.add(13);
        System.out.println(wave(input));
    }

    public ArrayList<Integer> wave(ArrayList<Integer> A) {
        ArrayList<Integer> result = new ArrayList<>();
        if (!(A == null || A.isEmpty())) {
            Collections.sort(A);
            result.add(A.get(0));
            boolean checkLarger = true;
            for (int i = 0, j = 1; i < j && j < A.size(); i++, j++) {
                boolean swap = (checkLarger && result.get(i) < A.get(j)) || (!checkLarger && result.get(i) > A.get(j));
                if (swap) result.add(i, A.get(j));
                else result.add(A.get(j));
                checkLarger = !checkLarger;
            }
        }
        return result;
    }

    @Test
    public void test_rotate() {
        ArrayList<List<Integer>> input = new ArrayList<>();
        input.add(Arrays.asList(27, 35, 36, 47, 94, 133, 163, 164, 235, 253, 280, 310, 339, 352));
        input.add(Arrays.asList(46, 72, 77, 95, 144, 149, 158, 174, 242, 243, 317, 371, 378, 386));
        input.add(Arrays.asList(13, 14, 80, 83, 121, 157, 158, 163, 215, 220, 308, 325, 388, 397));
        input.add(Arrays.asList(11, 38, 45, 84, 105, 132, 134, 145, 184, 219, 282, 298, 380, 381));
        input.add(Arrays.asList(23, 27, 42, 118, 120, 139, 168, 225, 243, 271, 274, 349, 393, 395));
        input.add(Arrays.asList(22, 27, 49, 85, 103, 167, 175, 234, 241, 258, 283, 296, 352, 385));
        input.add(Arrays.asList(24, 78, 117, 119, 137, 147, 173, 189, 193, 216, 281, 304, 332, 358));
        input.add(Arrays.asList(27, 71, 108, 109, 112, 133, 137, 145, 150, 171, 195, 225, 260, 336));
        input.add(Arrays.asList(5, 56, 65, 114, 123, 200, 220, 222, 248, 264, 285, 317, 350, 367));
        input.add(Arrays.asList(2, 20, 60, 72, 75, 130, 136, 149, 189, 254, 264, 295, 315, 349));
        input.add(Arrays.asList(23, 35, 68, 77, 104, 129, 153, 165, 248, 253, 290, 316, 321, 394));
        input.add(Arrays.asList(34, 127, 129, 154, 186, 202, 203, 210, 235, 269, 331, 344, 376, 387));
        input.add(Arrays.asList(11, 98, 99, 118, 119, 183, 250, 252, 277, 280, 291, 307, 360, 368));
        input.add(Arrays.asList(42, 74, 93, 119, 178, 186, 198, 221, 234, 295, 296, 319, 322, 335));
        for (List<Integer> lst : input) {
            System.out.println(lst);
        }
        System.out.println();
        rotate(input);
    }

    private void rotate(ArrayList<List<Integer>> a) {
        if (a == null || a.isEmpty()) return;

        int m = a.size();
        int n = a.get(0).size();
        int swappedElements = m * n - 1;
        int spRow = 0;
        int spCol = 0;
        int rpRow = 0;
        int rpCol = n - 1;
        int offset = 0;
        int swpCount = 0;
        int colOffset = 0;

        while (spRow < m / 2) {
            //Swap
            int temp = a.get(spRow).get(spCol);
            if (spRow == 14 || spCol == 14 || rpRow == 14 || rpCol == 14) {
                System.out.println("Shit");
            }
            a.get(spRow).set(spCol, a.get(rpRow).get(rpCol));
            a.get(rpRow).set(rpCol, temp);
            swappedElements--;
            swpCount = ++swpCount % 3;
            //increment swap points
            switch (swpCount) {
                case 1:
                    rpRow = m - 1 - offset;
                    rpCol = rpCol - colOffset;
                    break;
                case 2:
                    rpCol = offset;
                    rpRow = rpRow - colOffset;
                    break;
                case 0:
                    colOffset++;
                    if (swappedElements <= 0 || colOffset >= (n - 1 - (2 * offset))) {
                        colOffset = 0;
                        offset++;
                        spRow = offset;
                        spCol = spRow;
                    } else {
                        spCol = colOffset + offset;
                    }
                    rpRow = spRow + colOffset;
                    rpCol = n - 1 - offset;
                    swappedElements--;
                    break;
            }
        }

        for (List<Integer> lst : a) {
            System.out.println(lst);
        }
    }

    @Test
    public void test_SingleNumber() {
        Truth.assertThat(singleNumber(Arrays.asList(1, 2, 4, 3, 3, 2, 2, 3, 1, 1))).isEqualTo(4);
    }

    public int singleNumber(final List<Integer> A) {
        long num = 0;
        for (int i = 0; i < 32; i++) {
            long ones = 0;
            for (int j = 0; j < A.size(); j++) {
                if ((A.get(j) & (1 << i)) != 0) ones++;
            }
            if ((ones % 3) == 1) num += (1 << i);
        }
        return (int) num;
    }

    @Test
    public void test_intersect() {
        List<Integer> A = Collections.singletonList(10000000);
        List<Integer> B = Collections.singletonList(10000000);
        Truth.assertThat(intersect(A, B).toString()).isEqualTo("[10000000]");
    }

    public ArrayList<Integer> intersect(final List<Integer> A, final List<Integer> B) {
        ArrayList<Integer> result = new ArrayList<>(A.size() < B.size() ? A.size() : B.size());

        int i = 0, j = 0;

        while (i < A.size() && j < B.size()) {
            if (A.get(i).equals(B.get(j))) {
                result.add(A.get(i));
                i++;
                j++;
            } else {
                if (A.get(i) < B.get(j)) {
                    i++;
                    while (A.get(i) < B.get(j)) i++;
                } else {
                    j++;
                    while (B.get(j) < A.get(i)) j++;
                }
            }
        }
        return result;
    }

    @Test
    public void test_threeSum() {
        ArrayList<Integer> A = new ArrayList<>(Arrays.asList(1, -4, 0, 0, 5, -5, 1, 0, -2, 4, -4, 1, -1, -4, 3, 4, -1, -1, -3));
        Truth.assertThat(threeSum(A)).hasSize(11);
    }

    public ArrayList<ArrayList<Integer>> threeSum(ArrayList<Integer> A) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        if (A.size() > 2) {
            int a = 0;
            Collections.sort(A);
            ArrayList<String> cache = new ArrayList<>();
            ArrayList<Integer> row = new ArrayList<>();
            while (a < (A.size() - 3)) {
                int b = a + 1, c = A.size() - 1;
                while (c > b) {
                    if (A.get(a) + A.get(b) > 0) break;
                    long sum = A.get(a) + A.get(b) + A.get(c);
                    if (sum == 0) {
                        String key = a+"-"+b+"-"+c;
                        if(!cache.contains(key)) {
                            row.add(A.get(a));
                            row.add(A.get(b));
                            row.add(A.get(c));
                            result.add(row);
                            row = new ArrayList<>();
                            cache.add(key);
                        }
                        b++;
                        c--;
                    } else if (sum > 0) {
                        c--;
                    } else {
                        b++;
                    }
                }
                a++;
            }
        }
        return result;
    }

    @Test
    public void test_removeDuplicates() {
        ArrayList<Integer> A = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3));
        Truth.assertThat(removeDuplicates(A)).isEqualTo(4);
    }

    public int removeDuplicates(ArrayList<Integer> a) {
        if(a.size() > 1){
            int i = 0,j = 1;
            for(;i< a.size() && j < a.size();i++){
                while(i != j && a.get(i).equals(a.get(j))) {
                    a.remove(j);
                    if(j >= a.size()) break;
                }
                j++;

            }
        }
        return a.size();
    }

    @Test
    public void test_countNodes() {
        ArrayList<Integer> A = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3));
        Truth.assertThat(removeDuplicates(A)).isEqualTo(4);
    }

    public int countNodes(TreeNode root) {
        int count = root == null ? 0 : 1;
        if(count > 0){
            int level = 0;
            TreeNode rightMostChild = root;
            while(rightMostChild.right != null) {
                rightMostChild = rightMostChild.right;
                level++;
                count += Math.pow(2,level);
            }
            if(rightMostChild.left != null) {
                count += (1 << level) - 1;
            }
        }
        return count;
    }

    static boolean isMatch(String text, String pattern) {
        if(text.isEmpty()) return pattern.isEmpty();
        if(pattern.isEmpty()) return true;
        Stack<Character> stack = new Stack();
        for(char c : text.toCharArray()) stack.push(c);
        for(int i= pattern.length() - 1;i>=0;i--){
            char c = pattern.charAt(i);
            switch(c){
                case '.':
                    stack.pop();
                    break;
                case '*':{
                    if(i == 0) return true;
                    char temp =  pattern.charAt(i-1);
                    while(stack.peek() == temp) stack.pop();
                    i--;
                }
                break;
                default:
                    if(stack.pop() != c) return false;
            }
        }
        return stack.empty();
    }

    @Test
    public void test_sqrt() {
        Truth.assertThat(sqrt(5)).isEqualTo(2);
    }

    private int sqrt(int A) {
        int sqrt = A == 0 ? 0 : 1;
        if(A > 3){
            sqrt = A;
            while((sqrt/2 > 1) && Math.pow(sqrt/2,2) <= A ){
                sqrt = sqrt/2;
                if(sqrt * sqrt == A) return sqrt;
            }
            while(Math.pow(sqrt + 1,2) < A){
                sqrt++;
            }

        }
        return sqrt;
    }

    @Test
    public void test_pow() {
        Truth.assertThat(pow(71045970,41535484,64735492)).isEqualTo(2);
    }

    public int pow(int x, int n, int d) {
        long ans = findPow(x,n, new HashMap<>());
        if(ans < 0) ans = (ans + d) % d;
        return (int)ans;
    }

    private long findPow(int x,int n, HashMap<Integer,Long> memo){
        if(n <= 0) return 1;
        if(n == 1) return x;
        if(memo.containsKey(n)) return memo.get(n);
        long ans = findPow(x, n/2, memo) * findPow(x, n/2, memo) * ( n % 2 == 0 ? 1 : n);
        memo.put(n, ans);
        return ans;
    }

    @Test
    public void test_paint() {
        List<Integer> test = Arrays.asList(640, 435, 647, 352, 8, 90, 960, 329, 859);
        Truth.assertThat(paint(3,10, test)).isEqualTo(17220);
    }

    public int paint(int A, int B, List<Integer> C) {
        long min = 0;
        if(A >= C.size()){
            for(int i : C) min = Math.max(min, i);
        } else {
            for (int i : C) min = (min + i);
            if (A > 1) {
                min = findMin(0, (int) min, A, C);
            }
        }
        return (int)((min * B) % 10000003);
    }

    private long findMin(long start, long end, int A, List<Integer> C){
        long mid = end;
        while(start < end){
            mid = (start + end)/2;
            if(isPossible(A,mid,C)){
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return mid;
    }

    private boolean isPossible(int left, long min, List<Integer> C){
        long sum = 0;
        left--;
        for(int i : C){
            if(i > min) return false;
            sum = (sum + i);
            if(sum > min){
                if(left < 0) return false;
                sum = i;
                left--;
            }
        }
        return left >= 0;
    }

    @Test
    public void test_splitArray() {
        Truth.assertThat(splitArray(new int[]{2,3,1,2,4,3},5)).isEqualTo(4);
    }

    public int splitArray(int[] nums, int m) {
        long threshold = 0;
        if(m >= nums.length){
            Arrays.sort(nums);
            threshold = nums[nums.length - 1];
        } else {
            for(int i : nums) threshold+=i;
            if(m > 1){
                threshold = findMinThreshold(nums,m,threshold);
            }
        }
        return (int)threshold;
    }

    private long findMinThreshold(int[] nums, int m, long max){
        long start = 0, end = max;
        long mid = end;
        while(start <= end){
            mid = (start + end)/2;
            if(isPossible(nums,m,mid)){
                end = mid-1;
            } else{
                start = mid+1;
            }
        }
        return mid;
    }

    private boolean isPossible(int[] nums,int m, long threshold){
        m--;
        int sum = 0;
        for(int i : nums){
            if(i > threshold) return false;
            sum += i;
            if(sum > threshold){
                if(m < 0) return false;
                sum = i;
                m--;
            }
        }
        return m >= 0;
    }

}