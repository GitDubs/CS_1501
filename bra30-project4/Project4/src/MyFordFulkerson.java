public class MyFordFulkerson {
    private static final double FLOATING_POINT_EPSILON = 1E-11;
    private final int V;
    private boolean[] marked;
    private NetworkEdge[] edgeTo;
    private double value;
    
    public MyFordFulkerson(MyGraph G, int s, int t){
        V = G.V();
        validate(s);
        validate(t);
        if(s == t) throw new IllegalArgumentException("Source is sink");
        
        value = excess(G, t);
        while(hasAugmentingPath(G, s, t)){
            
            //compute bottleneck capacity
            double bottle = Double.POSITIVE_INFINITY;
            for(int v = t; v != s; v = edgeTo[v].other(v)){
                bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));
            }
            
            // augment flow
            for (int v = t; v != s; v = edgeTo[v].other(v)) {
                edgeTo[v].addResidualFlowTo(v, bottle); 
            }
            
            value += bottle;
        }
        
        assert check(G, s ,t);
    }
    
    public double value(){
        return value;
    }
    
    private void validate(int v)  {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
    
    // return excess flow at vertex v
    private double excess(MyGraph G, int v) {
        double excess = 0.0;
        for (NetworkEdge e : G.adj(v)) {
            if (v == e.from()) excess -= e.flow();
            else               excess += e.flow();
        }
        return excess;
    }
    
    private boolean hasAugmentingPath(MyGraph G, int s, int t){
        edgeTo = new NetworkEdge[G.V()];
        marked = new boolean[G.V()];
        
        Queue<Integer> queue = new Queue<Integer>();
        queue.enqueue(s);
        marked[s] = true;
        while(!queue.isEmpty() && !marked[t]){
            int v = queue.dequeue();
            
            for(NetworkEdge e : G.adj(v)){
                int w = e.other(v);
                
                // if residual capacity from v to w
                if(e.residualCapacityTo(w) > 0){
                    if(!marked[w]){
                        edgeTo[w] = e;
                        marked[w] = true;
                        queue.enqueue(w);
                    }
                }
            }
        }
        
        return marked[t];
    }
    
    
    
     // return excess flow at vertex v
    private boolean isFeasible(MyGraph G, int s, int t) {

        // check that capacity constraints are satisfied
        for (int v = 0; v < G.V(); v++) {
            for (NetworkEdge e : G.adj(v)) {
                if (e.flow() < -FLOATING_POINT_EPSILON || e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
                    System.err.println("Edge does not satisfy capacity constraints: " + e);
                    return false;
                }
            }
        }

        // check that net flow into a vertex equals zero, except at source and sink
        if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at source = " + excess(G, s));
            System.err.println("Max flow         = " + value);
            return false;
        }
        if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at sink   = " + excess(G, t));
            System.err.println("Max flow         = " + value);
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s || v == t) continue;
            else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
                System.err.println("Net flow out of " + v + " doesn't equal zero");
                return false;
            }
        }
        return true;
    }
    
    // check optimality conditions
    private boolean check(MyGraph G, int s, int t) {

        // check that flow is feasible
        if (!isFeasible(G, s, t)) {
            System.err.println("Flow is infeasible");
            return false;
        }

        // check that s is on the source side of min cut and that t is not on source side
        if (!inCut(s)) {
            System.err.println("source " + s + " is not on source side of min cut");
            return false;
        }
        if (inCut(t)) {
            System.err.println("sink " + t + " is on source side of min cut");
            return false;
        }

        // check that value of min cut = value of max flow
        double mincutValue = 0.0;
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.adj(v)) {
                if ((v == e.from()) && inCut(e.from()) && !inCut(e.to()))
                    mincutValue += e.capacity();
            }
        }

        if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
            System.err.println("Max flow value = " + value + ", min cut value = " + mincutValue);
            return false;
        }

        return true;
    }
    
        public boolean inCut(int v)  {
        validate(v);
        return marked[v];
    }
}
