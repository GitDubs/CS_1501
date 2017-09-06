import java.math.BigInteger;
import java.util.Random;

public class Tester {
    public static void main(String[] args){
        Random rnd = new Random();
        byte[] test1 = {88,-88};                    //0101 1000, 1010 1000 (22,696)
        byte[] test2 = {-113,96};                   //1000 1111, 0110 0000 (36,704)
        byte[] test3 = {-128, 0};                   //1000 0000, 0000 0000 (32,768)
        byte[] test4 = {5, -120};                   //0000 0101, 1000 1000 (1,416)
        byte[] test5 = {2, 24};                     //0000 0010, 0001 1000 (536)
        byte[] test6 = {127, 56};                   //0111 1111, 0011 1000 (32,568)
        byte[] sub;
        MyBigInteger one = new MyBigInteger(test1);
        MyBigInteger two = new MyBigInteger(test2);
        MyBigInteger three = new MyBigInteger(test3);
        MyBigInteger four = new MyBigInteger(test4);
        MyBigInteger five = new MyBigInteger(test5);

        
        //sub = two.substract(test1);
        //sub = three.substract(test6);
        sub = three.GCD(three, four);
        printByteArray(sub);
        
    }
    
    public static void printByteArray(byte[] a){
        for(byte b : a)
            System.out.print(" " + b + " ");
    }
    
//    public static void testAdd(){
//        byte[] test1 = {-113,96};
//        byte[] test2 = {-1};
//        byte[] result = MyBigInteger.add(test1, test2);
//        
//        for(int i = 0; i < result.length; i++)
//            System.out.print(" " + result[i] + " ");
//    }
//    
//    public static void testSomeShiftStuff(){
//        byte toShift = -48;
//        byte savedData;
//        byte shiftAMT = 3;
//        byte toAND = 0;
//        
//        savedData = (byte)(toShift >> (8 - shiftAMT));
//        toAND = getToAND(shiftAMT);
//        System.out.println("Saved Data: " + (savedData & toAND));
//        toShift = (byte)(toShift << shiftAMT);
//        System.out.println("toShift: " + toShift);
//        
//        byte[] test1 = {-113,96};
//        byte[] test2 = {88, -88};
//        byte[] result = MyBigInteger.shift(test2, 5);
//        
//        for(byte b : result)
//            System.out.print(" " + b + " ");
//        
//    }
//    
//    public static byte getToAND(int shiftAMT){
//        if(shiftAMT == 1)
//            return 1;
//        else if(shiftAMT == 2)
//            return 3;
//        else if(shiftAMT == 3)
//            return 7;
//        else if(shiftAMT == 4)
//            return 15;
//        else if(shiftAMT == 5)
//            return 31;
//        else if(shiftAMT == 6)
//            return 63;
//        else if(shiftAMT == 7)
//            return 127;
//        return 0;
//    }
//    
//    public static void testMultiply(){
//        byte[] test1 = {-113,96};
//        byte[] test2 = {88,-88};
//        byte[] result;
//        
//        result = MyBigInteger.multiply(test1, test2);
//        for(byte b : result)
//            System.out.print(" " + b + " ");
//        
//    }
}
