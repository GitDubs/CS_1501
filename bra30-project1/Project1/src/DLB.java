
public class CarDLB{
        private Node head;
        
        public CarDLB(){
            head = new Node('b');
        }
        
        //Constructor
        //Initializes the data structure and places the first key in it
        public CarDLB(String key){
            insertKey(key);
        }
        
        //----------------------------------------------------------------------
        //insertKey: inserts a new key into the DLB
        //----------------------------------------------------------------------
        public void insertKey(String key){
            insertKey(key, -1);
        }
        
        //----------------------------------------------------------------------
        //insertKey: inserts a new key into the DLB
        //----------------------------------------------------------------------
        public void insertKey(String key, double time){
            char[] keyArray = key.toCharArray();
            Node currentNode = head;
            int inserted = 0;
            boolean createdChild = false;

            if(head == null){
                head = new Node(keyArray[0]);
            }
            
            //System.out.println(key);
            currentNode = head;

            while(inserted < keyArray.length){
                //System.out.println("loop");
                        while(keyArray[inserted] != currentNode.getChar() && !createdChild){
                            if(currentNode.nextSiblingNode() == null){
                                currentNode.setNext(new Node(keyArray[inserted]));
                                currentNode = currentNode.nextSiblingNode();
                                //System.out.println("created sibling: " + currentNode.getChar());
                            }else{
                                currentNode = currentNode.nextSiblingNode();
                            }
                        }
                        if(keyArray[inserted] == currentNode.getChar() && !createdChild)
                            inserted++;
                        
                        if(currentNode.getChild() == null && inserted != keyArray.length){
                            currentNode.setChild(new Node(keyArray[inserted]));
                            inserted++;
                            createdChild = true;
                            //System.out.println("created child: " + currentNode.getChild().getChar());
                        }
                        if(inserted == keyArray.length && createdChild){
                            currentNode = currentNode.getChild();
                            currentNode.setEndOfValidKey(true);
                            currentNode.setTime(time);
                        }else if(inserted == keyArray.length) {
                            currentNode.setEndOfValidKey(true);
                            currentNode.setTime(time);
                        }
                        
                        currentNode = currentNode.getChild();
            }
            
        }
        
        //----------------------------------------------------------------------
        //keyExists: given a key determine if it exists in the DLB
        //----------------------------------------------------------------------
        public boolean keyExists(String key){
            char[] keyArray = key.toCharArray();
            Node currentNode = head;
            
            //For loop traverses down the trie
            for(int i = 0; i < keyArray.length; i++){
                if(currentNode == null)
                    return false;
                //While loop traverses across each level
                while(keyArray[i] != currentNode.getChar())
                    if(currentNode.nextSiblingNode() == null)
                        return false;
                    else
                        currentNode = currentNode.nextSiblingNode();
                if(i != keyArray.length - 1)
                    currentNode = currentNode.getChild();
            }
            if(currentNode.getEndOfValidKey())
                return true;
            return false;
        }
        
        //----------------------------------------------------------------------
        //keyExists: given a key determine if it exists in the DLB
        //----------------------------------------------------------------------
        public String longestPrefix(String key){
            char[] keyArray = key.toCharArray();
            Node currentNode = head;
            String temp = "";
            
            //For loop traverses down the trie
            for(int i = 0; i < keyArray.length; i++){
                if(currentNode == null)
                    return temp;
                //While loop traverses across each level
                while(keyArray[i] != currentNode.getChar()){
                    if(currentNode.nextSiblingNode() == null)
                        return temp;
                    else
                        currentNode = currentNode.nextSiblingNode();
                }
                temp += currentNode.getChar();
                if(i != keyArray.length - 1)
                    currentNode = currentNode.getChild();
            }
            return temp;
        }
        
        public double getTime(String key){
            char[] keyArray = key.toCharArray();
            Node currentNode = head;
            
            //For loop traverses down the trie
            for(int i = 0; i < keyArray.length; i++){
                if(currentNode == null)
                    return -1;
                //While loop traverses across each level
                while(keyArray[i] != currentNode.getChar())
                    if(currentNode.nextSiblingNode() == null)
                        return -1;
                    else
                        currentNode = currentNode.nextSiblingNode();
                if(i != keyArray.length - 1)
                    currentNode = currentNode.getChild();
            }
            if(currentNode.getEndOfValidKey())
                return currentNode.getTime();
            return -1;
        }
        
        public String[] getTenPasswords(String key){
            char[] keyArray = key.toCharArray();
            Node currentNode = head;
            Node parentNode = null;
            String [] toReturn = new String[10];
            String temp = key;
            boolean flag = false;
            if(key.compareTo("") == 0){
                key = "b";
                keyArray = key.toCharArray();
                temp = key;
            }
            //For loop traverses down the trie
            for(int i = 0; i < keyArray.length; i++){
                //While loop traverses across each level
                while(keyArray[i] != currentNode.getChar()){
                    currentNode = currentNode.nextSiblingNode();
                }
                if(i != keyArray.length - 1)
                    currentNode = currentNode.getChild();
            }
            String parentString = temp;
            parentNode = currentNode;
            for(int i = 0; i < 10; i++){
                while(temp.length() < 5){
                    if(flag == true)
                        temp += currentNode.getChar();
                    else
                        flag = true;
                    if(temp.length() != 5){
                        parentNode = currentNode;
                        currentNode = currentNode.getChild();
                    }
                }
                
                toReturn[i] = temp + ", " + currentNode.getTime();
                if(currentNode.nextSiblingNode() != null){
                    currentNode = currentNode.nextSiblingNode();
                    while(currentNode.nextSiblingNode() != null && i < 9){
                        toReturn[++i] = temp.substring(0, temp.length() - 1) + currentNode.getChar() 
                        + ", " + currentNode.getTime();
                        currentNode = currentNode.nextSiblingNode();
                    }
                }
                currentNode = parentNode.nextSiblingNode();
                parentNode = parentNode.nextSiblingNode();
                temp = parentString;
            }
            return toReturn;
        }
        
        
        public String toString(){
            String toReturn = null;
            Node currentNode = head.nextSiblingNode().nextSiblingNode();
            Node tempNode;
            while(currentNode != null){
                System.out.println(currentNode.getChar());
                tempNode = currentNode;
                System.out.println("New Child");
                while(currentNode.nextSiblingNode() != null){
                    System.out.println(currentNode.getChar());
                    currentNode = currentNode.nextSiblingNode();
                }
                currentNode = tempNode.getChild();
            }
            return toReturn;
        }
        
        //Node class used to build the DLB
        private class Node{
            double time;
            private char data;
            private Node next;
            private Node child;
            private boolean endOfValidKey;
            
            public Node(char data){
                this.data = data;
                child = null;
                next = null;
                endOfValidKey = false;
                time = -1;
            }
            
            public void setNext(Node next){
                this.next = next;                   
            }
            
            public void setChild(Node child){
                this.child = child;
            }
            
            public void setEndOfValidKey(boolean val){
                endOfValidKey = val;
            }
            
            public boolean getEndOfValidKey(){
                return endOfValidKey;
            }
            
            public char getChar(){
                return data;
            }
            
            public double getTime(){
                return time;
            }
            
            public void setTime(double time){
                this.time = time;
            }
            
            public Node nextSiblingNode(){
                return next;
            }
            
            public Node getChild(){
                return child;
            }
        
        }
}
