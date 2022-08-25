package com.ugo.todosapp;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

public class FloatTest {

    @Test
    void infinityTest(){

    }
    @Test
    void floatTest(){

        double b = 0.5*2;

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ;  i < 100000000; i++) {

            if(b == 1) {
                System.out.println("소수점 " + i + "번째에서 끝남");
                return;
            }
            if ((int) b == 1) {
                b = b - 1;
                builder.append(1);
            } else {
                builder.append(0);
            }
            if(i%30 == 0){
                System.out.println();
            }
            String format = decimalFormat.format(b);
            b= Double.parseDouble(format);
            b = b * 2;
        }
        System.out.println("binary value = " + builder.toString());
    }

}
