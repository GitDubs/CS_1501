public class CarMenuUI {
    public static void mainMenu(){
        System.out.println("Main Menu");
        System.out.println("0) Reprint Menu"
                + "\n1) Add a car"
                + "\n2) Update a car"
                + "\n3) Remove a car from consideration"
                + "\n4) Retrieve the lowest priced car"
                + "\n5) Retrieve the lowest mileage car"
                + "\n6) Retrieve the lowest price car by specific make and model"
                + "\n7) Retrieve the lowest mileage car by specific make and model"
                + "\n8) Quit");
    }
    
    public static void carAdded(Car car){
        System.out.println("Added Car:");
        System.out.println("\tMake: " + car.getMake());
        System.out.println("\tModel: " + car.getModel());
        System.out.println("\tMileage: " + car.getMileage());
        System.out.println("\tPrice: $" + car.getPrice());
        System.out.println("\tColor: " + car.getColor());
        System.out.println("\tVIN: " + car.getVIN());
    }
    
    public static void carUpdated(Car car){
        System.out.println("Updated car wit VIN: " + car.getVIN());
        System.out.println("\tMake: " + car.getMake());
        System.out.println("\tModel: " + car.getModel());
        System.out.println("\tMileage: " + car.getMileage());
        System.out.println("\tPrice: $" + car.getPrice());
        System.out.println("\tColor: " + car.getColor());
        System.out.println("\tVIN: " + car.getVIN());
    }
    
    public static void carRemoved(Car car){
        System.out.println(car + " has been removed from the system.");
    }
    
    public static void lowestPricedCar(Car car){
        System.out.println("The car with the lowest price is a " + car);
    }
    
    public static void lowestMileageCar(Car car){
        System.out.println("The car with the lowest mileage is a " + car);
    }
    
    public static void lowestSpecific(Car car, char type){
        if(type == 'p')
            System.out.println("The lowest priced " + car.getMake() + " " + car.getModel() + " is a " + car);
        else if(type == 'm')
            System.out.println("The lowest mileage " + car.getMake() + " " + car.getModel() + " is a " + car);
        else
            System.out.println("Error");
    }
    
    public static void goodbyeMessage(){
        System.out.println("Thank you for using Car Tracker");
    }
    
    
}
