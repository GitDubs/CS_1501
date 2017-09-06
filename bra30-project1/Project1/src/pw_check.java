//Ben Aston
//Project 1
//MW 9:30-10:45 
//Lasted Edited 2/7/2017

import java.util.Scanner;
import java.io.File;

public class pw_check {
    public static void main(String args[]){
        args = new String[1];
        args[0] = "-check";
        
        File allPasswords = new File("all_passwords.txt");
        
        if(args[0].compareTo("-find")== 0){
            FindPasswords test = new FindPasswords();
            test.crackerWrapper("");
            System.out.println("done");
        }else if(args[0].compareTo("-check")== 0){
            //If the all passwords file does not exist we creat it.
            if(!allPasswords.exists()){
                System.out.println("Creating a list of all possible passwords");
                FindPasswords test = new FindPasswords();
                test.crackerWrapper("");
                System.out.println("done");
            }
            
           
            Check check = new Check();
            System.out.println("Reading passwords into De La Briandais Trie");
            //Inserts the passwords into a DLB
            check.readInPasswords();
            System.out.println("finished reading");
            Scanner input = new Scanner(System.in);
            String toCheck = "";
            
            //Interacting with the user
            System.out.print("Enter a password (-1 to quit): ");
            toCheck = input.next();
            
            while (toCheck.compareTo("-1") != 0){
                System.out.println(check.exists(toCheck));
                System.out.print("Enter a password: ");
                toCheck = input.next();
            }
        }
    }  
}
