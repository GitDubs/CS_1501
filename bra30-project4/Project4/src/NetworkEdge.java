
public class NetworkEdge extends FlowEdge{
    private static final int OPTICAL_SPEED = 200000000; // meters per second for optical
    private static final int COPPER_SPEED =  230000000; // meters per second for copper
    private double flow;
    private int length;
    private char type;      //Specifies optical or copper connection ('o' or 'c')
    
    public NetworkEdge(int capacity, int to, int from, char type, int length){
        super(from, to, capacity);
        this.type = type;
        this.length = length;
    }

    /**
     * @return the flow
     */
    public double getFlow() {
        return flow;
    }

    /**
     * @return the type
     */
    public char getType() {
        return type;
    }
    
    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }
    
    public double weight(){
        if(type == 'o')
            return ((double)length) / OPTICAL_SPEED;
        else
            return ((double)length) / COPPER_SPEED;
    }
    
    public int either(){
        return super.from();
    }
    
    @Override
    public String toString(){
        return super.from() + " " + super.to() + " " + type + " " + super.capacity() + " " + getLength();
    }

    
}
