import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyGraph{
    private int V;          //Number of vertices
    private int E;          //Number of edges
    private Bag<NetworkEdge>[] adjacencyList;
    
    public MyGraph(int V){
        this.V = V;
        E = 0;
        adjacencyList = (Bag<NetworkEdge>[]) new Bag[V];
    }
    
    public MyGraph(File graph){
        readInGraph(graph);
    }
    
    public int V(){
        return V;
    }
    
    private void readInGraph(File graph){
        Scanner inputGraph = null;
        int to, from, capacity, length;
        char type;
        String cableType;
        NetworkEdge edge;
        
        try{
            inputGraph = new Scanner(graph);
        }catch(FileNotFoundException e){
            System.out.println("File not found");   //Print error
            System.exit(0);                         //Exit
        }
        
        V = inputGraph.nextInt();
        inputGraph.nextLine();
        inputGraph.useDelimiter(" ");
        adjacencyList = (Bag<NetworkEdge>[]) new Bag[V]; 
        
        while(inputGraph.hasNext()){
            E++;                                   //Each time this loop runs we add an edge
            from = inputGraph.nextInt();
            to = inputGraph.nextInt();
            cableType = inputGraph.next();
            capacity = inputGraph.nextInt();
            length = Integer.parseInt(inputGraph.nextLine().trim());    //It wouldn't read in the int so I had to take it
                                                                        //as a String, trim the white space, and parse it 
                                                                        //as an integer, honestly this triggers me a little
           
            if(cableType.equals("optical"))
                type = 'o';
            else 
                type = 'c';
            if(adjacencyList[from] == null)
                adjacencyList[from] = new Bag();
            adjacencyList[from].add(edge = new NetworkEdge(capacity, to, from, type, length));
            if(adjacencyList[to] == null)
                adjacencyList[to] = new Bag();
            adjacencyList[to].add(new NetworkEdge(capacity, from, to, type, length));
        }
    }
    
    //Iterable 
    public Iterable<NetworkEdge> adj(int v){
            return adjacencyList[v];
    }
    
    // return list of all edges - excludes self loops
    public Iterable<NetworkEdge> edges() {
        Bag<NetworkEdge> list = new Bag<NetworkEdge>();
        for (int v = 0; v < V; v++)
            for (NetworkEdge e : adj(v)) {
                if (e.to() != v)
                    list.add(e);
            }
        return list;
    }
    
    public void printEdges(){
        for(Bag b : adjacencyList){
            if(b != null){
                    Iterator<NetworkEdge> I = b.iterator();
                for(;;I.hasNext())
                    try{
                        System.out.println(I.next());
                    }catch(NoSuchElementException e){ 
                        break;
                    }
            }
        }
    }
}
