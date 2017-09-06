import java.io.Serializable;

public class MyBigInteger implements Serializable{
    private final static long LONG_MASK = 0xffffffffL;
    byte[] a;
    
    //Constructor that takes a byte[] and removs the sign bit if necessary
    public MyBigInteger(byte[] b){
        a = b;
        if(a[0] == 0){
            //System.out.println("size: " + a.length);
            byte[] temp = new byte[a.length - 1];
            System.arraycopy(a, 1, temp, 0, a.length - 1);
            a = temp;
            //System.out.println("size: " + a.length);
        }
    }
    
    //Constructor that takes a byte and makes an array of size 1 out of it
    public MyBigInteger(byte num){
        a = new byte[1];
        a[0] = num;
    }
    
    //--------------------------------------------------------------------------
    //Given another byte[] determine if they are equal
    //--------------------------------------------------------------------------
    public boolean equal(byte[] b){
        if(a.length != b.length)
            return false;
        for(int i = 0; i < a.length; i++)
            if(a[i] != b[i])
                return false;
        return true;
    }
    
    public boolean equalsZero(byte[] a){
        if(a.length == 1 && a[0] == 0)
            return true;
        return false;
    }
    
    //--------------------------------------------------------------------------
    //Wrapper for compare to that allows another MyBigInteger to be taken as an
    //argument
    //--------------------------------------------------------------------------
    public int compareTo(MyBigInteger b){
        return compareTo(b.getArray());
    }
    
    //--------------------------------------------------------------------------
    //Returns 1 if this MyBigInteger is larger the byte[] passed to it, -1 if 
    //the byte[] is larger, and zero if equal
    //--------------------------------------------------------------------------
    public int compareTo(byte[] b){
        if(a.length > b.length)
            return 1;
        if(b.length > a.length)
            return -1;
        
        for(int i = 0; i < a.length; i++){
            if((a[i] & 0xFF) > (b[i] & 0xFF))
                return 1;
            if((b[i] & 0xFF) > ((a[i]) & 0xFF))
                return -1;
        }
        
        return 0;
    }
    
    //--------------------------------------------------------------------------
    //adds a given byte[] array x and the byte[] of this MyBigInteger
    //--------------------------------------------------------------------------
    public byte[] add(byte[] x){
        return add(a, x);
    }
    
    //--------------------------------------------------------------------------
    //adds any two given byte[]'s
    //--------------------------------------------------------------------------
    private byte[] add(byte[]x, byte[]y){
        if (x.length < y.length) {
            byte[] tmp = new byte[y.length];
            System.arraycopy(x, 0, tmp, (y.length - x.length), x.length);
            x = tmp;
        }else if(y.length < x.length){
            byte[] tmp = new byte[x.length];
            System.arraycopy(y, 0, tmp, (x.length - y.length), y.length);
            y = tmp;
        }
            
        int xIndex = x.length;
        int yIndex = y.length;
        byte result[] = new byte[xIndex];
        int sum = 0;
        int carryBit = 0;
        
        // Add common parts of both numbers
        while(yIndex > 0) {
            sum = (x[--xIndex] & 0xFF) + (y[--yIndex] & 0xFF) + carryBit;
            if(sum > 255)
                carryBit = 1;
            else
                carryBit = 0;
            result[xIndex] = (byte)sum;
        }
        
        if(carryBit == 1){
            byte[] tmp = new byte[result.length];
            for(int i = 0; i < result.length; i++)
                tmp[i] = result[i];
            
            result = new byte[result.length + 1];
            result[0] = 1;
            for(int i = 1; i < result.length; i++)
                result[i] = tmp[i - 1];
        }
        return result;
    }
    
    //--------------------------------------------------------------------------
    //Wrapper for the multiply function
    //--------------------------------------------------------------------------
    public byte[] multiply(MyBigInteger b){
        return multiply(b.getArray());
    }
    
    //--------------------------------------------------------------------------
    //multiplies this MyBigInteger with the byte[] y using the gradeschool 
    //algorithm
    //--------------------------------------------------------------------------
    public byte[] multiply(byte[] y){
        if (a.length < y.length) {
            byte[] tmp = a;
            a = y;
            y = tmp;
        }
        
        byte[] result = new byte[y.length];
        int xIndex = a.length - 1;
        int shiftCounter = 0;
        
        while(xIndex >= 0){
            for(int i = 0; i <= 7; i++){
                if(((a[xIndex] >> i)  & 0x01) == 1){
                    result = add(result, shift(y, shiftCounter));
                }
                shiftCounter++;
            }
            xIndex--;
        }
        return result;
    }
    
    //--------------------------------------------------------------------------
    //Wrapper for shift(byte[], int shiftAMT), it is used to implement the 
    //multiply method that uses the gradeschool algorithm
    //--------------------------------------------------------------------------
    public byte[] shift(int shiftAMT){
        return shift(a, shiftAMT);
    }
    
    //--------------------------------------------------------------------------
    //shift: shifts the bytes in the to the left array by shiftAMT and places 0's in 
    //their places
    //--------------------------------------------------------------------------
    private byte[] shift(byte[] array, int shiftAMT){
        if(shiftAMT == 0)
            return array;
        byte[] result;
        
        int indexesToAdd = shiftAMT / 8;
        
        result = smallShift(array, shiftAMT % 8);
        if(indexesToAdd != 0){
            byte[] temp = new byte[result.length + indexesToAdd];
            System.arraycopy(result, 0, temp, 0, result.length);
            result = temp;
        }
        return result;
    }
    
    //--------------------------------------------------------------------------
    //smallShift: Does shift amounts less than 8 bits
    //--------------------------------------------------------------------------
    private byte[] smallShift(byte[] array, int shiftAMT){
        byte newByte = 0;
        byte oldByte = 0;
        byte [] result = new byte[array.length];
        
        for(int i = array.length - 1; i >= 0; i--){
            if(i == 0){
                newByte = (byte)(array[i] >> (8 - shiftAMT));
                newByte = (byte)(newByte & getToAND(shiftAMT));
                result[i] = (byte)(array[i] << shiftAMT);
                result[i] = (byte)(result[i] | oldByte);
                if(newByte != 0){
                    byte[] temp = new byte[array.length + 1];
                    System.arraycopy(result, 0, temp, 1, result.length);
                    result = temp;
                    result[0] = newByte;
                }                    
            }else{
                newByte = (byte)(array[i] >> (8 - shiftAMT));
                newByte = (byte)(newByte & getToAND(shiftAMT));
                result[i] = (byte)(array[i] << shiftAMT);
                result[i] = (byte)(result[i] | oldByte);
                oldByte = newByte;
            }
        }
        
        return result;
    }
    
    //--------------------------------------------------------------------------
    //subtractOne: subtracts only one for when calculating phi(n), I could  
    //use the normal subtract method
    //--------------------------------------------------------------------------
    public void subtractOne(){
        for(int i = a.length - 1; i >= 0; i--){
            if(a[i] == 0)
                a[i] = (byte)(a[i] | 0xFF);
            else{
                a[i] = (byte)((a[i] & 0xFF) - 1);
                break;
            }
        }
        a[a.length - 1] = (byte)(a[a.length - 1] & 0xFE);    
    }
    
    //--------------------------------------------------------------------------
    //getArray: Returns the array that represents the integer, which is NOT kept in two's
    //complement form
    //--------------------------------------------------------------------------
    public byte[] getArray(){
        return a;
    }
    
    //--------------------------------------------------------------------------
    //getBigIntArray: Returns the array that represents the integer,but with a sign bit if 
    //necessary. This allows the byte[] to be used in a BigInteger because I did
    //not successfully implement all of the necessary math funcitons
    //Not currently needed
    //--------------------------------------------------------------------------
    public byte[] getBigIntArray(){
        if(a[0] < 0){
            byte[] temp = new byte[a.length + 1];
            System.arraycopy(a, 0, temp, 1, a.length);
            return temp;
        }
        return a;
    }
    
    public byte[] mod(MyBigInteger b){
        return mod(b.getArray());
    }
    
    //--------------------------------------------------------------------------
    //mod: Not currently working
    //--------------------------------------------------------------------------
    public byte[] mod(byte[] b){
        byte[] result = {0}, last = b;                               

        while(true){
            result = add(result,b);;
            if(this.compareTo(result) == -1){
                return substract(last);
            }
            last = result;
        }
    }
    
    //--------------------------------------------------------------------------
    //subtract: subtracts b from this.a Ths method only works if the result is
    //non-negative, but RSA never deals with negative values so I didn't bother
    //implementing it to do negative values.
    //--------------------------------------------------------------------------
    public byte[] substract(byte[] b){
        byte[] result = new byte[a.length];
        byte[] temp = new byte[a.length];
        System.arraycopy(a, 0, result, 0, a.length);
        int resultIndex = result.length - 1;
        int bIndex = b.length - 1;
        
        while(resultIndex >= 0 && bIndex >= 0){
            if(((result[resultIndex] & 0xFF) - (b[bIndex] & 0xFF)) < 0){
                for(int i = resultIndex; i > 0; i--){
                    if(result[resultIndex - 1] != 0){
                        result[i - 1] = (byte)((result[i - 1] & 0xFF) - 1);
                        break;
                    }else
                        result[i - 1] = (byte)(result[i - 1] | 0xFF);
                }
                result[resultIndex] = (byte)((result[resultIndex] & 0xFF) - (b[bIndex] & 0xFF));
            }else{
                result[resultIndex] = (byte)((result[resultIndex] & 0xFF) - (b[bIndex] & 0xFF));
            }
            resultIndex--;
            bIndex--;
        }
        
        return result;
    }
    
    public byte[] GCD(MyBigInteger a, MyBigInteger b){
            if(equalsZero((a.mod(b.getArray())))){
                return b.getArray();
            }else{
                MyBigInteger aModB = new MyBigInteger(a.mod(b.getArray()));
                return GCD(b, aModB);
            }
        }
    
    //--------------------------------------------------------------------------
    //This method is a helper for the smallShift method allowing the "borrowing"
    //process of the operation to work correctly. 
    //--------------------------------------------------------------------------
    private byte getToAND(int shiftAMT){
        if(shiftAMT == 1)
            return 1;
        else if(shiftAMT == 2)
            return 3;
        else if(shiftAMT == 3)
            return 7;
        else if(shiftAMT == 4)
            return 15;
        else if(shiftAMT == 5)
            return 31;
        else if(shiftAMT == 6)
            return 63;
        else if(shiftAMT == 7)
            return 127;
        return 0;
    }
    
    public void printByteArray(){
        printByteArray(a);
    }
    public void printByteArray(byte [] a){
        for(byte b : a)
            System.out.print(b + "  ");
        System.out.println();
    }
}
