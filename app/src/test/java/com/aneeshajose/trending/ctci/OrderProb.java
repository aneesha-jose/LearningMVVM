package com.aneeshajose.trending.ctci;

import com.google.common.truth.Truth;

import org.junit.Test;

/**
 * Created by Aneesha Jose on 2020-05-21.
 */
public class OrderProb {

    private final static String INC = "Increasing";
    private final static String INC_DEC = "Increasing than Decreasing";
    private final static String DEC = "Decreasing";
    private final static String DEC_INC = "Decreasing than Increasing";


    @Test
    public void test_orderType(){
        Truth.assertThat(getOrderType(new int[]{1, 2, 4, 3})).isEqualTo(INC_DEC);
    }

    public String getOrderType(int[] arr){
        String type = "";

        for(int i = 0; i< (arr.length - 1);i++){
            if(arr[i] == arr[i+1]) continue;
            if(arr[i] > arr[i+1]){
                if(type.isEmpty()) type = DEC;
                else if(type.equals(INC)) type = INC_DEC;
            } else {
                if(type.isEmpty()) type = INC;
                else if(type.equals(DEC)) type = DEC_INC;
            }
        }
        return type;
    }

    @Test
    public void test_orderTypeOPT(){
        Truth.assertThat(getOrderTypeOpt(new int[]{1, 1})).isEqualTo("");
    }

    public String getOrderTypeOpt(int[] arr){
        String type = "";

        int mid = (arr.length - 1)/2;

        for(int i = 0; i< mid;i++){
            type = getType(arr[i], arr[i+1], type);
            if(isOfBreakPointType(type)) break;
        }
        if(isOfBreakPointType(type)) return type;

        for(int i = mid; i < (arr.length - 1); i++){
            type = getType(arr[i], arr[i+1], type);
            if(isOfBreakPointType(type)) break;
        }
        return type;
    }

    private boolean isOfBreakPointType(String type){
        return type.equals(INC_DEC) || type.equals(DEC_INC);
    }

    private String getType(int cur, int next, String type) {
        if (cur != next) {
            if (cur > next) {
                if (type.isEmpty()) type = DEC;
                else if (type.equals(INC)) {
                    type = INC_DEC;
                }
            } else {
                if (type.isEmpty()) type = INC;
                else if (type.equals(DEC)) {
                    type = DEC_INC;
                }
            }
        }
        return type;
    }



}
