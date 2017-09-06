import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FindPasswords {
    DLB dictionary;
    File log;
    BufferedWriter bw;
    FileWriter fw;
    Long startTime;
    int count;
    
    public FindPasswords(){
        dictionary = new DLB();
        readInDictionary();
	count = 0;
    }

   public void crackerWrapper(String str){
	try{
            log = new File("all_passwords.txt");
            //if(!log.exists())
            //    log.createNewFile();

            fw = new FileWriter(log,true);
            bw = new BufferedWriter(fw);
        }catch(IOException e){
                e.printStackTrace();
        }
        
        startTime = System.currentTimeMillis();
	cracker(str);
	try{
            bw.close();
            fw.close();
	}catch(IOException e){
            e.printStackTrace();
	}
	
   }
    
    private void cracker(String str){
        boolean validPrefix = isValidPrefix(str);
        if(validPrefix && str.length() == 5){
	    count++;
            try {
                bw.write(str + "," + (double)((System.currentTimeMillis()- startTime)) / 1000 + "\n");
                if(count > 500000){
                    bw.flush();
                    count = 0;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        else if(validPrefix){
            //b to h
            for(int i = 98; i <= 104; i++)
                cracker(str + (char)i);
            //j to z
            for(int i = 106; i <= 122; i++)
                cracker(str + (char)i);
            //0
            cracker(str + '0');
            //2 to 9
            for(int i = 50; i <= 57; i++)
                cracker(str + (char)i);
            cracker(str + '!');
            cracker(str + '@');
            cracker(str + '$');
            cracker(str + '^');
            cracker(str + '_');
            cracker(str + '*');
        }
    }
    
    public int getCount(){
	return count;
    }

    //--------------------------------------------------------------------------
    //isValidPrefix: takes a string and determines if it is the prefix to a 
    //valid password. It essentially makes sure the string is not already
    //violating any of the rules for the potential passwords
    //--------------------------------------------------------------------------
    public boolean isValidPrefix(String str){
        if(str.length() > 5)
            return false;
        //Simple check to make sure the new character is valid
        if(str.length() != 0)
            if(!isValidChar(str.charAt(str.length()-1)))
                return false;
        
        //Enforcing the limits on the number of symbols, letter, and digits
        if(str.length() == 1 || str.length() == 0)
            return true;
        else if(str.length() == 3){
            if(numSymbols(str) > 2)
                return false;
            if(numDigits(str) > 2)
                return false;
        }else if(str.length() == 4){
            if(numSymbols(str) > 2)
                return false;
            if(numDigits(str) > 2)
                return false;
            if(numLetters(str) > 3)
                return false;
        }else if(str.length() == 5){
            if(numSymbols(str) > 2)
                return false;
            if(numDigits(str) > 2)
                return false;
            if(numLetters(str) > 3)
                return false;
            if(numLetters(str) == 0)
                return false;
            if(numSymbols(str) == 0)
                return false;
            if(numDigits(str) == 0)
                return false;
        }
            
        
        //Replacing all of the possible symbols with their coresponding letter
        str = replaceNumbers(str);   
            for(int i = 0; i <str.length(); i++){
                boolean val = dictionary.keyExists(str.substring(i));
                if(val == true)
                    return false;
            }
        return true;
    }
    
    //--------------------------------------------------------------------------
    //isValideCharacter: given a character c it determines if that character can
    //exist in a valid password
    //--------------------------------------------------------------------------
    public static boolean isValidChar(char c){
        int ascii = (int)c;
    
        //note that a and i are excluded from being valid characters this is
        //because a and i are both in the dictionary.txt doc and therefor cannot
        //appear anywhere in a legal password subsequently 1 can stand in for
        //i so it is also an invalid character, 4 can stand in for a so it is also
        //an invalid character
        
        //b to h
        if(ascii >= 'b' && ascii <= 'h')
            return true;
        //j to z
        else if(ascii >= 'j' && ascii <='z')
            return true;
        //0
        else if(ascii == '0')
            return true;
        //2 to 3
        else if(ascii >= '2' && ascii <= '3')
            return true;
        //5 to 9
        else if(ascii >= '5' && ascii <= '9')
            return true;
        //!
        else if(ascii == '!')
            return true;
        //@
        else if(ascii == '@')
            return true;
        //$
        else if(ascii == '$')
            return true;
        //^
        else if(ascii == '^')
            return true;
        //_
        else if(ascii == '_')
            return true;
        //*
        else if(ascii == '*')
            return true;
        //The character is not legal 
        else
            return false;
    }
    
    //--------------------------------------------------------------------------
    //numDigits: returns the number of ascii digits (0-9) in str
    //--------------------------------------------------------------------------
    private int numDigits(String str){
        int count = 0;
        for(int i = 0; i < str.length(); i++)
            if(str.charAt(i) >= 48 && str.charAt(i) <= 57)
                count++;
        return count;
    }
    
    //--------------------------------------------------------------------------
    //numLetters: returns the number of letters in the string, excluding a and i
    //--------------------------------------------------------------------------
    private int numLetters(String str){
        int count = 0;
        for(int i = 0; i < str.length(); i++)
            if((str.charAt(i) >= 106 && str.charAt(i) <= 122) || (str.charAt(i) >= 98 && str.charAt(i) <= 104))
                count++;
        return count;
    }
    
    //--------------------------------------------------------------------------
    //numSymbols: returns the number of symbols str (!, @, $, ^, _, *)
    //--------------------------------------------------------------------------
    private int numSymbols(String str){
        int count = 0;
        for(int i = 0; i < str.length(); i++)
            //!
            if(str.charAt(i) == 33)
                count++;
            //@
            else if(str.charAt(i) == 64)
                count++;
            //$
            else if(str.charAt(i) == 36)
                count++;
            //^
            else if(str.charAt(i) == 94)
                count++;
            //_
            else if(str.charAt(i) == 95)
                count++;
            //*
            else if(str.charAt(i) == 42)
                count++;
        return count;
    }
    
    //--------------------------------------------------------------------------
    //replaceNumbers: replaces each number or symbol with the letter that it 
    //represents in order to catch common words with some letters replaced with
    //numbers. Example: b3n is changed to ben
    //--------------------------------------------------------------------------
    private String replaceNumbers(String str){
        //StringBuilder newStr = new StringBuilder(str);
        str = str.replace('7', 't');
        str = str.replace('4', 'a');
        str = str.replace('0', 'o');
        str = str.replace('3', 'e');
        str = str.replace('$', 's');
        return str;
    }
    
    //--------------------------------------------------------------------------
    //countChar: counts the number of times a character (c) appears in a string
    //(str). The only reason this method exists is because of the corner case
    //in which 1 can represent i or l... such a pain in the ass
    //--------------------------------------------------------------------------
    private int countChars(char c, String str){
        int count = 0;
        for(int i = 0; i < str.length(); i++)
            if(str.charAt(i) == c)
                count++;
        return count;
    }
    
    //--------------------------------------------------------------------------
    //readInDictionary: inserts every word from dictionary.txt into a DLB as a 
    //new key
    //--------------------------------------------------------------------------
    private void readInDictionary(){
        String fileName = "dictionary.txt";
        String nextLine = null;
        
        try{
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            while((nextLine = bufferedReader.readLine()) != null){
                //No word of more than five letters can be represented in a
                //valid password, so I don't bother adding them to the DLB
                if(nextLine.length() <= 5)
                    dictionary.insertKey(nextLine.toLowerCase());
            }
        }catch(FileNotFoundException e){
            System.out.println("Unable to open file " + fileName);
        }catch(IOException ex){
            System.out.println("Error reading file: " + fileName);
        }
        
    }
}
