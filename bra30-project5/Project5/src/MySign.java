import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileSystemNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MySign {
    public static void main(String[] args){
        char mode;
        
        //Error checking the arguments
        if(args.length == 0){
            System.out.println("No parameters entered... Terminating");
            System.exit(0);
        }
        if(args.length == 1){
            System.out.println("Too few parameters entered, missing "
                    + "either a mode or a file name... Terminating");
            System.exit(0);
        }
        if(args.length > 2){
            System.out.println("too many parameters entered... Terminating");
            System.exit(0);
        }
        if(args[0].length() > 1){
            System.out.println("Enter the mode as a single character, s for"
                    + " signing and v for verifying... Terminating");
            System.exit(0);
        }
        
        args[0].toLowerCase();
        mode = args[0].charAt(0);
        
        //Determining which mode we are in
        if(mode != 's' && mode != 'v'){
            System.out.println("Invalid argument");
            System.exit(0);
        }else if(mode == 's'){
            signFile(args[1]);
        }else if(mode == 'v'){
            verifyFile(args[1]);
        }
        
    }
    
    public static void signFile(String fileName){
       
        try{
            MyBigInteger n;
            BigInteger d, decrypted;
            byte[] data, hashedData;
            
            Path filepath = Paths.get(fileName);                                                //Sort of copy pasted thisstuff from the provided HashEx.java file				
            data = Files.readAllBytes(filepath);
            
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");			// Hashes the contents using SHA 256
            sha256.update(data);
            hashedData = sha256.digest();
            
            FileInputStream keyFile = new FileInputStream("privkey.rsa");			// Opens privkey.rsa
            ObjectInputStream reader = new ObjectInputStream(keyFile);
            
            d = (BigInteger)reader.readObject();                                                //Reading in d and n 
            n = (MyBigInteger)reader.readObject();
            reader.close();
            
            decrypted = new BigInteger(1, hashedData).modPow(d, new BigInteger(1, n.getArray()));
            
            writeFile(fileName, data, decrypted);                                               
            
        }catch(Exception exc){
            System.out.println("From sign file");
            System.err.println(exc);
        }
    }
    
    public static void writeFile(String fileName, byte[] data, BigInteger decrypted){
        try{
            fileName = fileName.concat(".signed");                                              //Using ObjectOutputStreams for ease of use
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream writer = new ObjectOutputStream(file);
            
            writer.writeObject(data);
            writer.writeObject(decrypted);
            writer.close();
            
            System.out.println("Successfully signed file");
        }catch(IOException e){
            System.out.println("From write file");
            System.err.println(e);
        }
    }
    
    public static void verifyFile(String fileName){
        try {
                byte[] original;
                BigInteger decrypted;				

                FileInputStream file = new FileInputStream(fileName);
                ObjectInputStream signedReader = new ObjectInputStream(file);

                original = (byte[])signedReader.readObject();	
                decrypted = (BigInteger)signedReader.readObject();
                signedReader.close();

                readPublicKey(original, decrypted, fileName);
        }catch (Exception e) {
                System.out.println("from verify file");
                System.err.println(e);
        }
    }
    
    public static void readPublicKey(byte[] data, BigInteger decrypted, String fileName){
        byte[] hashed;
        BigInteger e, encrypted, originalHash;
        MyBigInteger n;
        try{
            
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(data);
            hashed = sha256.digest();
            originalHash = new BigInteger(1, hashed);
            
            FileInputStream file = new FileInputStream("pubkey.rsa");
            ObjectInputStream reader = new ObjectInputStream(file);
            
            e = (BigInteger)reader.readObject();
            n = (MyBigInteger)reader.readObject();
            reader.close();
            encrypted = decrypted.modPow(e, new BigInteger(1, n.getBigIntArray()));
            if(encrypted.equals(originalHash))
                System.out.println("Successfully validated file");
            else
                System.out.println("File is not valid");
        }catch(Exception ex){
            System.out.println("from read public key");
            System.err.println(ex);
        }
    }
}
