import java.awt.Color;

public class Test {
    public static void main(String args[]){
        //testTrieMinPQ();
        testCarDLB();
    }
    
    public static void testTrieMinPQ(){
        TrieST mileageTrie = new TrieST();
        TrieST priceTrie = new TrieST();
        MinPQ pricePriority;
        MinPQ mileagePriority;
        
        Car[] cars = new Car[4];
        cars[0] = new Car("A", "Ford", "F-150", 30000, 5000, "Blue");
        cars[1] = new Car("BB", "Toyota", "Avalon", 26000, 9000, "Black");
        cars[2] = new Car("C", "Ford", "Penis", 10000, 12000, "Green");
        cars[3] = new Car("D", "Lamborghini", "murcielago", 42000, 400, "Red");
        
        pricePriority = new MinPQ(cars, priceTrie, 'p');
        mileagePriority = new MinPQ(cars, mileageTrie, 'm');
        
        System.out.println("The car with the lowest price is: " + pricePriority.min());
        System.out.println("The car wit the lowest milage is: " + mileagePriority.min());
        pricePriority.pukeOutArray();
        System.out.println();
        mileagePriority.pukeOutArray();
        
        System.out.println(mileageTrie.contains("A"));
        System.out.println(mileageTrie.get("A"));
    }
    
    public static void testCarDLB(){
        CarDLB dlb = new CarDLB();
        String[] vins;
        Car[] cars = new Car[5];
        cars[0] = new Car("A", "Ford", "F-150", 30000, 5000, "Blue");
        cars[1] = new Car("BB", "Toyota", "Avalon", 26000, 9000, "Black");
        cars[2] = new Car("C", "Ford", "Penis", 10000, 12000, "Green");
        cars[3] = new Car("D", "Lamborghini", "murcielago", 42000, 400, "Red");
        cars[4] = new Car("EE", "Toyota", "Avalon", 26000, 3000, "Black");
        
        dlb.insertKey(cars[0].getMake() + cars[0].getModel(), cars[0].getVIN());
        dlb.insertKey(cars[1].getMake() + cars[1].getModel(), cars[1].getVIN());
        dlb.insertKey(cars[2].getMake() + cars[2].getModel(), cars[2].getVIN());
        dlb.insertKey(cars[3].getMake() + cars[3].getModel(), cars[3].getVIN());
        dlb.insertKey(cars[4].getMake() + cars[4].getModel(), cars[4].getVIN());
        
        vins = dlb.getVins("ToyotaAvalon");
        for(String str : vins)
            System.out.println(str);
    }
}
