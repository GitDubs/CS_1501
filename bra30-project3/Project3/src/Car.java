
public class Car
{
    private String VIN;
    private String make;
    private String model;
    private int price;
    private int mileage;
    private String color;    
    
    public Car(String VIN, String make, String model, int price, int mileage, String color){
        this.VIN = VIN;
        this.make = make;
        this.model = model;
        this.price = price;
        this.mileage = mileage;
        this.color = color;
    }
    
    
    public boolean equals(Car toCompare){
        return toCompare.getVIN().equals(getVIN());
    }

    /**
     * @return the VIN
     */
    public String getVIN() {
        return VIN;
    }

    /**
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the mileage
     */
    public int getMileage() {
        return mileage;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @param mileage the mileage to set
     */
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }
    
    public int compareTo(Car car, char type){
        if(type == 'p'){
            if(price > car.price){
                return 1;
            }else if(price == car.price)
                return 0;
            else
                return -1;    
        }else if(type == 'm'){
            if(mileage > car.mileage){
                return 1;
            }else if(mileage == car.mileage)
                return 0;
            else
                return -1;    
        }
        return -1;
    }
    
    public String toString(){
        return color + " " + make + " " + model + " with " + mileage + " miles and costs $" + price; 
    }
}
