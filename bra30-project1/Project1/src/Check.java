import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class Check {
    DLB passwords;
    
    public Check() {
        passwords = new DLB();
    }
    //--------------------------------------------------------------------------
    //readInDictionary: inserts every word from dictionary.txt into a DLB as a 
    //new key
    //--------------------------------------------------------------------------
    public void readInPasswords(){
        String fileName = "all_passwords.txt";
        String nextLine = null;
        String [] temp;

        try{
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            while((nextLine = bufferedReader.readLine()) != null){
                temp = nextLine.split(",");
                if(temp[0].length() == 5)
                    passwords.insertKey(temp[0], Double.parseDouble(temp[1]));
            }
        }catch(FileNotFoundException e){
            System.out.println("Unable to open file " + fileName);
        }catch(IOException ex){
            System.out.println("Error reading file: " + fileName);
        }
        
    }
    
    
    
    public String exists(String key){
        double time = passwords.getTime(key);
        if(time != -1)
            return key + ": " + Double.toString(time);
        return "Invalid Password \n" + sharedPrefix(passwords.longestPrefix(key));
    }
    
    public String sharedPrefix(String key){
        String toReturn = "";
        String [] strArray;
        strArray = passwords.getTenPasswords(key);
        for(String str : strArray){
            toReturn += str + "\n";
        }
        return toReturn;
    }
    
    public String toString(){
        return passwords.toString();
    }
}
