import java.util.ArrayList; 
import java.util.Scanner; 

public class Rover implements Contract{

    public String name; // name of rover 
    public int year; // year currently   
    private ArrayList<String> inventory; // what rover currenty contains 
    private Destination destination; // where are you sending it?
    private int Fuel; 
    private boolean launchSuccess; 
    private boolean landSuccess; 
    public Scanner userInput; 
    private boolean on; 
    
    public Rover(String name, int launchYear, Destination destination, int maxFuel){

        this.name = name; 
        this.year = launchYear; // starts out at year launched 
        this.Fuel = maxFuel; // when initilize, at max fuel 
        this.destination = destination; 
        this.inventory = new ArrayList<>(); // empty list to fill up with things! 
        this.launchSuccess = false; 
        this.landSuccess = false; 
        this.userInput = new Scanner(System.in); 
        this.on = false; 

        System.out.println("");
    }

    public boolean fly(int x, int y){

           // tests if rocket was successful! x and y give launch angle -> want to make sure is 
        if (x/y < 0.5){ // if y is bigger than 0.5 x launch is successful 
            System.out.println("Launch successful!");

            System.out.println("Your mission destination: "+this.destination);

            if (this.destination == Destination.MARS || this.destination == Destination.VENUS){
                System.out.println("Trip duration: 1 yr");
                this.year += 1; 
            }

            if (this.destination == Destination.EUROPA){
                System.out.println("Trip duration: 5 yr");
                this.year += 5; 
            }

            if (this.destination == Destination.PLUTO){
                System.out.println("Trip duration: 40 yr");
                this.year += 40; 
            }
            this.launchSuccess = true; 
            this.on = true; 
        }
        else{
            System.out.println("Rocket launch failure! Try a larger vertical velocity relative to horizontal");
        }

        return this.launchSuccess; // if rocket successfully launched 
    }


    public String drop(String item){ // where to land -> item gives name of location to drop onto 
        if (this.launchSuccess && this.on) { // if successfully launched 
            String firstLetter = item.substring(0, 1); // first character as string 

            if (firstLetter.equals("P") || firstLetter.equals("C")  ||  firstLetter.equals("H") ||  firstLetter.equals("M")){                      // random condition to make sure the item location input makes sense 
                System.out.println("You have successfully landed on "+this.destination+"'s surface on "+item);
                this.landSuccess = true; 
            }
            else{
                System.out.println(item + " could not be landed on. " + this.name +" has crashed!");
                System.out.println("Mission failure!");
                this.landSuccess = false; 
            }
        }

        else{ // if launch was not a success 
            throw new RuntimeException("Cannot land on "+this.destination+ "because rocket launch failed or rover is not turned on."); 
        }
        
        return item; 
    }

    public boolean walk(String direction){
        if (this.landSuccess && this.on){ // if landed ok 
            this.Fuel -= 1; // use one fuel unit  
            if (this.Fuel <= 0){
                System.out.println("Can't move! No fuel!");
            }
            else{
                System.out.println("Moving " + direction + ". Current fuel level: " + this.Fuel);
            }
        }
        else{
            throw new RuntimeException("Cannot travel " + direction + " because rocket launch or landing was not successful, or rover is not turned on."); 
        }
        return true; 
    }

    public void examine(String item){
        if (this.landSuccess && this.on){
            System.out.println(this.name + " hasy found a " + item + "!");
            System.out.println("Do you wish to look closer? (y/n)");
            String input = this.userInput.nextLine(); 

            if (input.equals("y")){
                if (item.length() >= 5){
                    System.out.println("Object is small and gray and smooth.");
                } 
                else{
                    System.out.println("Object is large, rough, and a little dusty.");
                }
            }

            else{
                System.out.println("Ok, nevermind");
            }
        }
        else{
            throw new RuntimeException("Cannot examine item " + item+ ". Launch or landing was not successful, or rover is not turned on!"); 
        }
    }

    public void grab(String item){
        if (this.landSuccess && this.on){
            System.out.println("Adding " + item + " to inventory. ");
            this.inventory.add(item); 
            System.out.println("Do you wish to view inventory? (y/n)");

            String input = this.userInput.nextLine(); 

            if (input.equals("y")){
            System.out.println("Inventory contents: " + this.inventory); } 

            else{
                System.out.println("Ok - but trust me, its there!");
            }
        }
        else{
            throw new RuntimeException("Cannot grab item "+ item + ". Launch or landing was not successful, or rover is not turned on!"); 
        }

    }

    public void use(String item){

    }

    public Number shrink(){
        if (this.landSuccess && this.on){
            System.out.println("Dropping one inventory item... ");
            System.out.println("Which item do you want to remove?");
            System.out.println(this.inventory);

            String item = this.userInput.nextLine(); 
            
            if (this.inventory.contains(item)){
                this.inventory.remove(item); 
            }
            else{
                System.out.println("Cannot remove " + item + " because it is not present.");
            }
            return 1; 
        }

        else{
            throw new RuntimeException("Cannot drop inventory items. Rover has not landed successfully, or rover is not turned on."); 
        }
    }

    public Number grow(){
        if (this.landSuccess && this.on){

        System.out.println("1 year has passed. The year is now " + this.year + ".");
        System.out.println("...");
        System.out.println("Happy birthday to you!");
        System.out.println("Happy birthday to you!");
        System.out.println("Happy birthday dear " + this.name + ",");
        System.out.println("Happy birthday to you!");

        this.year += 1; 
        this.Fuel -= 10; // use fuel up over time 

        }
        else{
            System.out.println("1 year has passed since the launch of " + this.name + ".");
            System.out.println("");
        }
        return this.year; 
    }

    public void rest(){
        if (this.landSuccess && this.on){
        System.out.println("Even rovers need some sleep.");
        System.out.println("Powering down...");
        System.out.println("Rover is powered down. To access contols again, run the 'undo' method.");
        System.out.println("...");
        this.on = false; 
    }
        else{
            throw new RuntimeException("Cannot rest. Rover has not landed successfully, or rover is not turned on."); 
        }
    }

    public void undo(){
        if (this.landSuccess){
            System.out.println("Powering on..."); 
            this.on = true; 
            System.out.println(this.name + " has been turned back on. Methods are once again accessable."); 
            }
            else{
                throw new RuntimeException("Cannot power off. Rover has not landed successfully"); 
        }
    }

        // getter for current fuel level 
     public int getCurrentFuel(){
            return this.Fuel; 
        }

public static void main(String[] args){
    // Rover curiosity = new Rover("Curiosity", 2000, Destination.MARS, 100); 
    // curiosity.fly(4,10 ); 
    // curiosity.drop("Crater"); 
    // curiosity.walk("West"); 
    // curiosity.examine("rock");
    // curiosity.grab("rock"); 
    // curiosity.shrink(); 
    // curiosity.grow(); 
    // curiosity.rest(); 
    // curiosity.undo(); 

    System.out.println(".....");
    System.out.println("New rover!");
    System.out.println(".....");

    Rover plutorover = new Rover("plutoRover", 2025, Destination.PLUTO, 3); 
    plutorover.fly(1, 10); 
    plutorover.drop("Trench");


    System.out.println(".....");
    System.out.println("New rover!");
    System.out.println(".....");


    Rover icymoon = new Rover("Icymoon", 2025, Destination.EUROPA, 3); 
    icymoon.fly(1, 10); 
    icymoon.drop("Highlands");
    icymoon.examine("ice");
    icymoon.grab("rock");
    icymoon.grab("ice");
    icymoon.shrink(); 
    icymoon.rest(); 
    icymoon.undo(); 
    icymoon.walk("south"); 
    icymoon.walk("west"); 
    icymoon.walk("north"); 
    icymoon.walk("west"); 


}

}
