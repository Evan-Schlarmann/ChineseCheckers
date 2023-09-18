package com.example.buttonwithswitch;

public class Counter {
        private static int count = 0;
        public Counter(){
            count = 0;
        }
        public static int getCount(){
            return count;
        }
        public static void incrementCount(int increment){
            count += increment;
        }
        public static void resetCount(){
            count = 0;
        }
}
