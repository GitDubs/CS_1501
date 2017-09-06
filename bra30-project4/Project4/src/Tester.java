import java.io.File;

public class Tester {
        static MyGraph graph;
    public static void main(String args[]){
        graph = new MyGraph(new File("network_data1.txt"));
        testPrim();
    }
    
    public static void testFF(){
        MyFordFulkerson ff = new MyFordFulkerson(graph, 0, 1);
        System.out.println("Max flow: " + ff.value());
    }
    
    public static void testDJ(){
        MyDijkstra dj = new MyDijkstra(graph, 2);
        System.out.println("Lowest latency path to 1: " + dj.distTo(3) );
        for(NetworkEdge e : dj.pathTo(3))
            System.out.println(e);
    }
    
    public static void testUF(){
        UF uf = new UF(graph.V());
        
        for(NetworkEdge e : graph.edges())
            //if(e.getType() != 'o')
                uf.union(e.from(), e.to());
        
        System.out.println("Are 4 and 0 connected: " + uf.connected(0, 4));
        System.out.println("All connected: " + uf.allConnected());
    }
    
    public static void testPrim(){
        MyEagerPrim ep = new MyEagerPrim(graph);
        for(NetworkEdge e : ep.edges())
            System.out.println(e);
    }
}
