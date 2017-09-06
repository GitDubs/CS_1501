//Ben Aston
//CS 1501, MW 9:30am - 10:45am
//Dr. Farnan

import java.util.Scanner;
import java.io.File;

public class NetworkAnalysis {
    public static void main(String[] args){
        int userChoice = 0;
        MyGraph graph = new MyGraph(new File(args[0]));
        Scanner input = new Scanner(System.in);
        
        System.out.println("Welcome to the Network Analysis Tool.");
        printMenu();
        do{
            System.out.print("Enter your selection: ");
            userChoice = input.nextInt();
            if(userChoice < 1 || userChoice > 7)
                System.out.println("Invalid choice, please enter a valid selection");
            else if(userChoice == 1)
                lowestLatency(graph);
            else if(userChoice == 2)
                copperConnected(graph);
            else if(userChoice == 3)
                maxFlow(graph);
            else if(userChoice == 4)
                spanningTree(graph);
            else if(userChoice == 5)
                twoFailedVertices(graph);
            else if(userChoice == 7)
                printMenu();
        }while(userChoice != 6);
    }
    
    //--------------------------------------------------------------------------
    //twoFailedVertices: checks for pairs of vertices that will cause a separation
    //of the graph
    //--------------------------------------------------------------------------
    public static void twoFailedVertices(MyGraph g){
        boolean pairs[][] = new boolean[g.V()][g.V()];
        UF uf = new UF(g.V());
        
        for(int i = 0; i < g.V(); i++){                    
            for(int j = 0; j < g.V(); j++){
                if(i != j){
                    for(NetworkEdge e : g.edges()){
                        if(e.from() != i && e.to() != i && e.from() != j && e.to() != j)
                            uf.union(e.from(), e.to());
                    }
                    if(!uf.allConnectedIgnore(i,j) && !pairs[i][j]){
                        System.out.println("Removing vetices " + i + " and " + j + " will cause the graph to fail");
                        pairs[i][j] = true;
                        pairs[j][i] = true;
                    }
                    uf = new UF(g.V());
                }
            }
        }
                    
    }   
    
    
    //--------------------------------------------------------------------------
    //spanningTree: prints out the edges that make up the lowest latency 
    //spanning tree
    //--------------------------------------------------------------------------
    public static void spanningTree(MyGraph g){
        MyEagerPrim ep = new MyEagerPrim(g);
        System.out.println("The spanning tree consists of the following edges: ");
        for(NetworkEdge e : ep.edges())
            System.out.println(e);
    }
    
    //--------------------------------------------------------------------------
    //maxFlow: Asks the user for two vertices and finds the max flow between 
    //them
    //--------------------------------------------------------------------------
    public static void maxFlow(MyGraph g){
        Scanner input = new Scanner(System.in);
        MyFordFulkerson ff;
        int source, sink;
        
        System.out.print("Enter the source vertex: ");
        source = input.nextInt();
        System.out.print("Enter the sink vertex: ");
        sink = input.nextInt();
        
        ff = new MyFordFulkerson(g, source, sink);
        System.out.println("The maximun flow from " + source + " to " + sink + " is: " + ff.value());
    }
    
    //--------------------------------------------------------------------------
    //lowestLatency: Asks for two vertices from the user and finds the path
    //with the lowest latency between them and that path's bandwidth
    //--------------------------------------------------------------------------
    public static void lowestLatency(MyGraph g){
        Scanner input = new Scanner(System.in);
        int source, sink;
        double minBandwidth = Double.MAX_VALUE;
        MyDijkstra dj;
        
        System.out.print("Enter the source vertex: ");
        source = input.nextInt();
        System.out.print("Enter the sink vertex: ");
        sink = input.nextInt();
        
        dj = new MyDijkstra(g, source);
        for(NetworkEdge e : dj.pathTo(sink)){
            System.out.println(e);
            if(e.capacity() < minBandwidth)
                minBandwidth = e.capacity();
        }
        System.out.println("The bandwidth available is: " + minBandwidth);
    }
    
    //--------------------------------------------------------------------------
    //copperConnected: Determines if a graph is still completely connected even
    //without the fibre connections
    //--------------------------------------------------------------------------
    public static void copperConnected(MyGraph g){
        UF uf = new UF(g.V());
        
        for(NetworkEdge e : g.edges())
            if(e.getType() != 'o')
                uf.union(e.to(), e.from());
        if(uf.allConnected())
            System.out.println("The graph is copper-only connected");
        else
            System.out.println("The graph is NOT copper-only connected");
    }
    
    //--------------------------------------------------------------------------
    //printMenu: prints all the functions of the NetworkAnalysis program
    //--------------------------------------------------------------------------
    public static void printMenu(){
        System.out.println("1) Find the lowest latency path between two vertices");
        System.out.println("2) Determine if the graph is \"copper-only connected\"");
        System.out.println("3) Find the Maximum amount of data that can be pushed from one vertex to another");
        System.out.println("4) Find the lowest average latency spanning tree");
        System.out.println("5) Determine if any two vertices areto fail if the graph will remain connected");
        System.out.println("6) Quit");
        System.out.println("7) Reprint Menu");
    }
}
