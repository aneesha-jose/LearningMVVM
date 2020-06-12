package com.aneeshajose.trending.interviewbit;

import com.google.common.truth.Truth;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aneesha Jose on 2020-05-22.
 */
public class MathProb {

    @Test
    public void test_convertToTitle() {
        Truth.assertThat(convertToTitle(52)).isEqualTo("AZ");
    }

    private String convertToTitle(int A) {
        StringBuilder title = new StringBuilder();
        int div = A;
        int rem;
        while (div > 26) {
            rem = div % 26;
            title.append(getCorrespondingChar(rem));
            div = div / 26;
            if (rem == 0) div--;
        }
        title.append(getCorrespondingChar(div));
        return title.reverse().toString();
    }

    private char getCorrespondingChar(int addition) {
        if (addition == 0) return 'Z';
        return (char) ('A' + (addition - 1));
    }

    @Test
    public void test_reverse() {
//        Truth.assertThat(reverse(-1146467285)).isEqualTo();
    }

    public int reverse(int A) {
        boolean isNeg = A < 0;
        int abs = Math.abs(A);
        long reverse = 0;
        if(abs < 10){
            reverse = abs;
        } else {
            long multiplier = (long) Math.pow(10, String.valueOf(abs).length() - 1);
            int divider = 10;
            while(multiplier > 0){
                reverse += (abs % divider) * multiplier;
                multiplier /= 10;
                abs /= divider;
            }
        }
        if(reverse < Integer.MIN_VALUE || reverse > Integer.MAX_VALUE) reverse = 0;
        return (int)(isNeg ? (-1 * reverse) : reverse);
    }

    @Test
    public void test_maxArea() {
        Truth.assertThat(maxArea(new int[]{1,8,6,2,5,4,8,3,7})).isEqualTo(49);
    }

    public int maxArea(int[] height) {
        long maxArea = 0;
        if(height != null && height.length > 0){
            int i = 0, j = height.length -1;
            int prevLeft, prevRight, area;
            do{
                area = Math.min(height[i], height[j]) * (j-i);
                if(area > maxArea){
                    maxArea = area;
                }
                prevLeft = i;
                prevRight = j;
                i = height[i + 1] > height[i]? i + 1 : i;
                j = height[j - 1] > height[j]? j - 1 : j;
                if(i == prevLeft && j == prevRight){
                    i++;
                }
            }while(i < j);
        }
        return (int) maxArea;
    }

    @Test
    public void test_moves() {
        Truth.assertThat(removeStones(new int[][]{{0,0},{0,1},{1,0},{1,2},{2,1},{2,2}})).isEqualTo(5);
    }

    public int removeStones(int[][] stones) {
        int moves = 0;
        List<String> cache = new ArrayList<>();
        Arrays.sort(stones, (a, b) -> Integer.compare(a[0], b[0]));
        for(int i = 0; i < stones.length-1;i++){
            if(stones[i][0] == stones[i+1][0]){
                if(stones[i][1] == stones[i+1][1]){
                    stones[i][0] = -1;
                    stones[i][1] = -1;
                } else {
                    moves++;
                    cache.add(getStringRep(stones[i][0],stones[i][1],stones[i+1][0],stones[i+1][1]));
                }
            }
        }
        Arrays.sort(stones, (a,b) -> Integer.compare(a[1], b[1]));
        for(int i = 0; i < stones.length-1;i++){
            if(stones[i][1] != -1 && stones[i][1] == stones[i+1][1]){
                if(!cache.contains(getStringRep(stones[i][0],stones[i][1],stones[i+1][0],stones[i+1]                         [1]))) {
                    moves++;
                    cache.add(getStringRep(stones[i][0],stones[i][1],stones[i+1][0],stones[i+1][1]));
                }
            }
        }
        return moves;
    }

    private String getStringRep(int row1, int col1,int row2, int col2){
        return row1+":"+col1+" -> "+row2+":"+col2;
    }

}
