import java.util.Scanner;
import java.awt.Color;
/*
*
*/
public class CarTracker {
    
    
    public static void main(String [] args){
        TrieST<Integer> mileageTrie = new TrieST();
        TrieST<Integer> priceTrie = new TrieST();
        MinPQ pricePriority = new MinPQ('p');
        MinPQ mileagePriority = new MinPQ('m');
        CarDLB dlb = new CarDLB();
        Scanner input = new Scanner(System.in);
        int userChoice = -1;
        
        CarMenuUI.mainMenu();
        //Main Menu loop
        while(userChoice != 8){
            //Force the user to enter valid input
            do{
                System.out.print("Enter choice: ");
                userChoice = input.nextInt();
                if(userChoice < 0 || userChoice > 8)
                    System.out.println("Please enter a valid menu option\nEnter choice");
            }while(userChoice < 0 || userChoice > 8);
            
            if(userChoice == 0)
                CarMenuUI.mainMenu();
            else if(userChoice == 1)
                addACar(dlb, mileageTrie, priceTrie, pricePriority, mileagePriority);
            else if (userChoice == 2)
                update(dlb, mileageTrie, priceTrie, pricePriority, mileagePriority);
            else if(userChoice == 3)
                remove(dlb, mileageTrie, priceTrie, pricePriority, mileagePriority);
            else if(userChoice == 4)
                System.out.println(pricePriority.min());
            else if(userChoice == 5)
                System.out.println(mileagePriority.min());
            else if(userChoice == 6)
                lowestSpecificMakeMode(dlb, priceTrie, pricePriority, 'p');
            else if(userChoice == 7)
                lowestSpecificMakeMode(dlb, mileageTrie, mileagePriority, 'm');    
            else
                CarMenuUI.goodbyeMessage();
        }
    }
    
    //--------------------------------------------------------------------------
    // addACar creates the terminal interface for allowing the user to add a car
    //--------------------------------------------------------------------------
    public static void addACar(CarDLB dlb, TrieST mileageTrie, TrieST priceTrie, MinPQ pricePriority, MinPQ mileagePriority){
        Car toAdd;
        String make, model, vin, color;
        int price, mileage;
        Scanner input = new Scanner(System.in);
        
        //Get vin from user
        do{
            System.out.print("Enter the VIN: ");
            vin = input.next();
            if(vin == null || vin.equals(""))
                System.out.println("VIN cannot be null or empty");
            else if(dlb.keyExists(vin))
                System.out.println("VIN already exists");
        }while(vin == null || vin.equals("") || dlb.keyExists(vin));
        
        //Get make from user
        do{
            System.out.print("Enter the make: ");
            make = input.next();
            if(make == null || make.equals(""))
                System.out.println("VIN cannot be null or empty");
        }while(make == null || make.equals(""));

        //Get model from user
        do{
            System.out.print("Enter the model: ");
            model = input.next();
            if(model == null || model.equals(""))
                System.out.println("Model cannot be null or empty");
        }while(model == null || model.equals(""));
            
        //Get price from user
        do{
            System.out.print("Enter the price (In US $): ");
            price = input.nextInt();
            if(price < 0)
                System.out.println("Price cannot be negative");
        }while(price < 0);
        
        //Get mileage from user
        do{
            System.out.print("Enter the mileage: ");
            mileage = input.nextInt();
            if(mileage < 0)
                System.out.println("Mileage cannot be negative");
        }while(mileage < 0);
        
        //Get color from user
        do{
            System.out.print("Enter the color of the car: ");
            color = input.next();
            if(color == null || color.equals(""))
                System.out.println("Color cannot be null or empty");
        }while(color == null || color.equals(""));
        
        //Add the new Car to the Car DLB, price PQ, and mileagePQ
        toAdd = new Car(vin, make, model, price, mileage, color);
        dlb.insertKey(make + model, vin);
        pricePriority.insert(toAdd, priceTrie);
        mileagePriority.insert(toAdd, mileageTrie);
        
        //Print the a success message 
        CarMenuUI.carAdded(toAdd);
    }
    
    //--------------------------------------------------------------------------
    //update: Provides a terminal interface for updating a car
    //--------------------------------------------------------------------------
    public static void update(CarDLB dlb, TrieST<Integer> mileageTrie, TrieST<Integer> priceTrie, MinPQ pricePriority, MinPQ mileagePriority){
        Car toUpdate;
        int i, price, mileage;
        String vin, color;
        Scanner input = new Scanner(System.in);
        
        //Get VIN
        do{
            System.out.println("Enter the vin of the car to update: ");
            vin = input.next();
            if(!priceTrie.contains(vin))
                System.out.println("Car with VIN: " + vin + " does not exist in the system");
        }while(!priceTrie.contains(vin));
        
        //Grab the Car and display its current attributes
        i = priceTrie.get(vin);
        toUpdate = pricePriority.getCarAtIndex(i);
        
        System.out.println(toUpdate + "\n");
        
        //Get new price
        do{
            System.out.print("Enter the new price (In US $): ");
            price = input.nextInt();
            toUpdate.setPrice(price);
            if(price < 0)
                System.out.println("Price cannot be negative");
        }while(price < 0);
        
        //Get new mileage
        do{
            System.out.print("Enter the new mileage: ");
            mileage = input.nextInt();
            toUpdate.setMileage(mileage);
            if(mileage < 0)
                System.out.println("Mileage cannot be negative");
        }while(mileage < 0);
        
        //Get new color
        do{
            System.out.print("Enter new the color of the car: ");
            color = input.next();
            toUpdate.setColor(color);
            if(color == null || color.equals(""))
                System.out.println("Color cannot be null or empty");
        }while(color == null || color.equals(""));
        
        //Check that the heap invariant is not violated and correct it if it is
        
        pricePriority.restoreHeapInvariant(i, priceTrie);
        mileagePriority.restoreHeapInvariant(i, mileageTrie);
        
        //Print updated car stats
        CarMenuUI.carUpdated(toUpdate);
    }
    
    //--------------------------------------------------------------------------
    //lowestPrice: provides a terminal interface for finding the lowest price
    //or lowest mileage of a car with a specific make and model
    //--------------------------------------------------------------------------
    public static void lowestSpecificMakeMode(CarDLB dlb, TrieST<Integer> trie, MinPQ pq, char type){
        String make, model;
        String[] vins;
        int indexOfCar;
        Car[] cars;
        Scanner input = new Scanner(System.in);
        
        //Get make
        do{
            System.out.print("Enter the make: ");
            make = input.next();
            if(make == null || make.equals(""))
                System.out.println("VIN cannot be null or empty");
        }while(make == null || make.equals(""));
        
        //Get model
        do{
            System.out.print("Enter the model: ");
            model = input.next();
            if(model == null || model.equals(""))
                System.out.println("Model cannot be null or empty");
        }while(model == null || model.equals(""));
        
        //Get an array of the VINs of all cars matching the requested make and model
        vins = dlb.getVins(make + model);
        
        //For each VIN in the array we get its corresponding car object
        //by looking up the index of car from the DLB and then using 
        //that index to get the car object from the priority queue
        cars = new Car[vins.length];
        for(int i = 0; i < vins.length; i++){
            indexOfCar = trie.get(vins[i]);
            cars[i] = pq.getCarAtIndex(indexOfCar);
        }
        
        //Add all of the cars into a temperary heap and then get the min,
        //esentially its a heapsort implementation
        MinPQ temp = new MinPQ(cars, trie, type);
        
        CarMenuUI.lowestSpecific(temp.min(), type);
    }
    
    //--------------------------------------------------------------------------
    //remove:
    //--------------------------------------------------------------------------
    public static void remove(CarDLB dlb, TrieST<Integer> mileageTrie, TrieST<Integer> priceTrie, MinPQ pricePriority, MinPQ mileagePriority){
        Car toRemove;
        int i;
        String vin;
        Scanner input = new Scanner(System.in);

        do{
            System.out.println("Enter the vin of the car to remove: ");
            vin = input.next();
            if(!priceTrie.contains(vin))
                System.out.println("Car with VIN: " + vin + " does not exist in the system");
        }while(!priceTrie.contains(vin));
        
        i = priceTrie.get(vin);
        toRemove = pricePriority.getCarAtIndex(i);
        CarMenuUI.carRemoved(toRemove);
        toRemove.setPrice(-1);
        toRemove.setMileage(-1);
        
        pricePriority.restoreHeapInvariant(i, priceTrie);
        mileagePriority.restoreHeapInvariant(i, mileageTrie);
        
        pricePriority.delMin(priceTrie);
        mileagePriority.delMin(mileageTrie);
        
        CarMenuUI.carRemoved(toRemove);
    }
}

