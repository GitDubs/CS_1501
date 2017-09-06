
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
            insertKey(key, "AAAAAAAAAAAAAAAA");
        }
        
        //----------------------------------------------------------------------
        //insertKey: inserts a new key into the DLB
        //----------------------------------------------------------------------
        public void insertKey(String key, String vin){
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
                            currentNode.addVin(vin);
                        }else if(inserted == keyArray.length) {
                            currentNode.setEndOfValidKey(true);
                            currentNode.addVin(vin);
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
        
        public String[] getVins(String key){
            char[] keyArray = key.toCharArray();
            Node currentNode = head;
            
            //For loop traverses down the trie
            for(int i = 0; i < keyArray.length; i++){
                if(currentNode == null)
                    return createVinArray(currentNode);
                //While loop traverses across each level
                while(keyArray[i] != currentNode.getChar())
                    if(currentNode.nextSiblingNode() == null)
                        return createVinArray(currentNode);
                    else
                        currentNode = currentNode.nextSiblingNode();
                if(i != keyArray.length - 1)
                    currentNode = currentNode.getChild();
            }
            if(currentNode.getEndOfValidKey())
                return createVinArray(currentNode);
            return null;
        }
        
        private String[] createVinArray(Node node){
            String[] toReturn = new String[node.numVins];
            for(int i = 0; i < toReturn.length; i++)
                toReturn[i] = node.vins[i];
            for(String str : toReturn)
                System.out.println(str);
            return toReturn;
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
            private char data;
            private Node next;
            private Node child;
            private boolean endOfValidKey;
            private String[] vins;
            private int numVins = 0;
            public Node(char data){
                this.data = data;
                child = null;
                next = null;
                endOfValidKey = false;
                vins = null;
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
            
            public Node nextSiblingNode(){
                return next;
            }
            
            public Node getChild(){
                return child;
            }
            
            public void addVin(String vin){
                if(vins == null)
                    vins = new String[10];
                else if(numVins - 1 == vins.length)
                    resize();
                vins[numVins++] = vin;
            }
            
            public void resize(){
                String [] temp = new String[vins.length * 2];
                for(int i = 0; i < vins.length; i++)
                    temp[i] = vins[i];
                vins = temp;
            }
        
        }
}
