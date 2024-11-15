import java.util.ArrayList; 
import java.util.Scanner;


public class Rover implements Contract{

    public String name; // name of rover 
    public int year; // year currently   
    private ArrayList<String> inventory; // what rover currenty contains 
    private Destination destination; // where are you sending it?
    private int Fuel; // fuel level
    private boolean launchSuccess; // if rover launched successfully
    private boolean landSuccess; // if landed successfully 
    public Scanner userInput; // scanner to collect user input 
    private boolean on; // rover turned on/off?
    
    /**
     * Constructor for Rover class. Initilizes a Rover object with the following attributes
     * @param name: name of rover
     * @param launchYear: year launched 
     * @param destination: destination (from enum object)
     * @param maxFuel: fuel rover starts with 
     */
    public Rover(String name, int launchYear, Destination destination, int maxFuel){

        this.name = name; // name of rover
        this.year = launchYear; // year starts out at year launched 
        this.Fuel = maxFuel; // when initilize, at max fuel 
        this.destination = destination; // where going 
        this.inventory = new ArrayList<>(); // empty list to fill up with things! 
        this.launchSuccess = false; // has not launched yet, set to false 
        this.landSuccess = false; // has not landed yet, set to false 
        this.userInput = new Scanner(System.in); // initilizes scanner 
        this.on = false; // not on yet

        // tells user about newly created rover 
        System.out.println("You have succesfully built a rover, named " + this.name + " equipt to explore " + this.destination + ". Your rover is equipt with a camera, radio, and arm. The rover contains "+ this.Fuel+ " fuel units. The year is " + this.year);
    }
    
    /**
     * Method to fly rover to location. launch velocity (x^2 + y^2)^1/2 must be greater than Earth's escape velocity, 11.2 km/s. 
     * If launch is successful, sets this.launchSuccess variable to true, allowing the "drop" method to be accessed. 
     * @param x: initial x velocity of rocket, km/s
     * @param y: initial y velocity of rocket, km/s 
     * Returns: true if successfully flew, false if not
     */
    public boolean fly(int x, int y){

        if (this.landSuccess){ // makes sure rocket has not already landed bc cannot fly again 
            throw new RuntimeException("Rover has already flown to "+this.destination + " ."); 
        }
        else{

        // tests if rocket was successful! x and y give launch angle -> want to make sure is 
        if ( Math.pow(x*x + y*y, 2) > 11.2){ // if combined velocity is greater than Earth's escape velocity 
            System.out.println("Launch successful!");

            System.out.println("Your mission destination: "+this.destination);

            if (this.destination == Destination.Mars || this.destination == Destination.Venus){ // trip duration set by location ; venus and mars have same travel tiem
                System.out.println("Trip duration: 1 year");
                this.year += 1; 
            }

            if (this.destination == Destination.Europa){
                System.out.println("Trip duration: 5 year");
                this.year += 5; 
            }

            if (this.destination == Destination.Pluto){
                System.out.println("Trip duration: 40 years");
                this.year += 40; 
            }
            this.launchSuccess = true; // sets launch success to true if launched ok
            this.on = true; // rover turns on also 
        }
        else{
            System.out.println("Rocket launch failure! Combined velocity did not exceed Earth's escape velocity of 11.2 km/s");
        }
    }

        return this.launchSuccess; // if rocket successfully launched 
    }

    /**
     * Method to "drop" rover onto the planet. Must be run after fly to access remaining methods. If input drop location starts with a 
     * p, c, h, or m, the landing is a success. 
     * @param String item: location where to drop on the surface 
     * Returns: name of location where rover has dropped. 
     */
    public String drop(String item){ // where to land -> item gives name of location to drop onto 
        if (this.launchSuccess && this.on) { // if successfully launched 
            String firstLetter = item.substring(0, 1); // first character as string 
            // checks what first character is equal to -> determines if landing is successful or not 
            if (firstLetter.equals("P") || firstLetter.equals("C")  ||  firstLetter.equals("H") ||  firstLetter.equals("M")){                      // random condition to make sure the item location input makes sense 
                System.out.println("You have successfully landed on "+this.destination+"'s surface on "+item);
                this.landSuccess = true; // sets landSuccess to true, remaining methods are accessable. 
            }
            else{ // cannot land on item, rocket crashes + mission is a failure :(
                System.out.println(item + " could not be landed on. " + this.name +" has crashed!");
                System.out.println("Mission failure!"); // note land success is already set to false 
            }
        }

        else{ // if launch was not a success cannot access drop method 
            throw new RuntimeException("Cannot land on "+this.destination+ " because rocket launch failed or rover is not turned on."); 
        }
        
        return item; // return location 
    }

    /**
     * Method to "walk" (rover equivilant of walk) around on the surface of the body you landed on. Requires an input direction, and requires that
     * the rover has enough fuel to make a movement.
     * @param String direction: arbitraty direction to move in.
     * Returns: true if moved, false if not enough fuel to move 
     */
    public boolean walk(String direction){
        if (this.landSuccess && this.on){ // if landed ok 
            this.Fuel -= 1; // use one fuel unit  
            if (this.Fuel <= 0){
                System.out.println("Can't move! No fuel!");
                return false; 
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

    /**
     * Method to examine an item, asking user if they want to examine the item. if the item name is larger than 5, gets assigned one description. 
     * If shorter, gets assigned another. 
     * @param String item: arbitrary item to be examined. 
     * Returns: String name of item examined.
     */
    public void examine(String item){
        if (this.landSuccess && this.on){ // if rover has landed, launched, and is turned on 
            System.out.println(this.name + " hasy found a " + item + "!"); //tells user item found 
            System.out.println("Do you wish to look closer? (y/n)"); // asks user to look closer or not 
            String input = this.userInput.nextLine(); // y or n from user  

            if (input.equals("y")){ // if user says y 
                if (item.length() >= 5){ // if name of item is longer than 5
                    System.out.println("Object is small and gray and smooth."); // one description 
                } 
                else{ // if name is shorter than 5 
                    System.out.println("Object is large, rough, and a little dusty."); // other description 
                }
            }

            else{ // if user says n ( or anything else )
                System.out.println("Moving on"); // does not examine
            }
        }
        else{ // raises warning if rover has not launched, landed, or is not turned on. 
            throw new RuntimeException("Cannot examine item " + item+ ". Launch or landing was not successful, or rover is not turned on!"); 
        }
    }

    /**
     * Method to grab an item and add it to the rover inventory. user is also asked if they want to view the inventory. 
     * @param String item: name of item to add to inventory 
     */
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
    /**
     * Method to use one of the three tools on the rover, which are camera, radio, and arm (listed when rover was initilzied). If 
     * user input is not one of the items, user is notified that the rover does not have that item available, and is asked if they want 
     * to see what the available options are.
     * @param String item: item to use 
     */
    public void use(String item){
        if (this.landSuccess && this.on){ // if landed + on  
            if (item.toLowerCase().equals("camera")){ // checks if user input is camera 
                System.out.println("Snap! you have taken a picture"); // camera specific message 
            }

            if (item.toLowerCase().equals("radio")){ // same for radio
                System.out.println("Transmitting data....");
            }

            if (item.toLowerCase().equals("arm")){ // same for arm 
                System.out.println("Grabbing!");
            }

            else{ // if user input is not part of equiptment, tells user 
                System.out.println(this.name + " is not equipt with a "+ item);
                System.out.println("Do you wish to see available items? (y/n)"); // asks if user wants to see available items 

                String input = this.userInput.nextLine();  // y or n 

                if (input.equals("y")){
                    System.out.println("Available items: camera, radio, arm"); } // prints items available for use  

                else{
                    System.out.println("Ok, nevermind.");
                }
            }
        }

        else{ // cannot use method if landing / launch not succes or not on 
            throw new RuntimeException("Cannot use item "+ item + ". Launch or landing was not successful, or rover is not turned on!"); 
        }

    }

    /**
     * Method to remove an item from the Rover's inventory. Prints out inventory and then prompts user to enter an item. If entered item is not
     * present, tells user. 
     * Returns: length of inentory 
     */
    public Number shrink(){
        if (this.landSuccess && this.on){ 
            System.out.println("Dropping one inventory item... ");
            System.out.println("Which item do you want to remove?");
            System.out.println(this.inventory); // prints out inventory 

            String item = this.userInput.nextLine();  // promts user for y/n
            
            if (this.inventory.contains(item)){ // if item in inventory 
                this.inventory.remove(item); // removes item 
            }
            else{
                System.out.println("Cannot remove " + item + " because it is not present."); // if not present, cannot remove 
            }
            return this.inventory.size(); 
        }

        else{ // same as other methods
            throw new RuntimeException("Cannot drop inventory items. Rover has not landed successfully, or rover is not turned on."); 
        }
    }
/**
     * Method to run when a "year" has passed. Rover sings itself happy birthday and the year attribute is increased by one.  
     * Returns: current year
     */

    public Number grow(){
        if (this.landSuccess && this.on){

        System.out.println("1 year has passed. The year is now " + this.year + "."); // tells user current year 
        System.out.println("..."); // sings happy birthday :)
        System.out.println("Happy birthday to you!");
        System.out.println("Happy birthday to you!");
        System.out.println("Happy birthday dear " + this.name + ",");
        System.out.println("Happy birthday to you!");

        this.year += 1; // increase year counter 

        }
        else if (this.landSuccess && !this.on){ // if powered down 
            System.out.println("Rover has aged 1 year, but is not powered on, so cannot celebrate.");
        }

        else{ // if rover did not land / launch successfully, commemorate that 
            System.out.println("1 year has passed since the unsuccessful launch or landing of " + this.name + ".");
            System.out.println("");
        }
        return this.year; // returns current year 
    } 

    /**
     * Method to put rover to sleep, rendering methods unusuable until rover is turned back on using the "undo" method.
     */
    public void rest(){
        if (this.landSuccess && this.on){ // same as all
        System.out.println("Even rovers need some sleep."); // tells user powering down
        System.out.println("Powering down...");
        System.out.println("Rover is powered down. To access contols again, run the 'undo' method.");
        System.out.println("...");
        this.on = false; // turns rover off 
    }
        else{ // cannot rest if not landed + already powered down.
            throw new RuntimeException("Cannot rest. Rover has not landed successfully, or rover is not turned on."); 
        }
    }

    /**
     * Method to turn rover back on, only method that can be run after the "rest" method is run. 
     */
    public void undo(){
        if (this.on){ // if rover on, say it is already on 
            System.out.println("Rover is already turned on.");
        }
        if (this.landSuccess && !this.on){ // if rover landed + is turned off 
            System.out.println("Powering on..."); 
            this.on = true; // sets on to true 
            System.out.println(this.name + " has been turned back on. Methods are once again accessable."); 
            } // tells user back on 

        else{ // if not landed, cannot be turned on
                throw new RuntimeException("Cannot power off. Rover has not landed successfully"); 
        }
    }

        // getter for current fuel level 
     public int getCurrentFuel(){
            return this.Fuel; 
        }
        // getter for current year 
    public int getCurrentYear(){
        return this.year; 
    }

public static void main(String[] args){

    // makes Curiosity rover on Mars! tests basic functions 
    Rover curiosity = new Rover("Curiosity", 2000, Destination.Mars, 100); 
    try { // going to slow down the difference between printing out statements for readability... is there an easier way to do this 
        // stop for a second (so printing looks natural)
        Thread.sleep(1000); 
        curiosity.fly(1, 20 ); 
        Thread.sleep(1000); 
        curiosity.drop("Crater");
        Thread.sleep(1000);  
        curiosity.walk("West"); 
        Thread.sleep(1000); 
        curiosity.examine("rock");
        Thread.sleep(1000); 
        curiosity.grab("rock"); 
        Thread.sleep(1000); 
        curiosity.shrink(); 
        Thread.sleep(1000); 
        curiosity.grow(); 
        Thread.sleep(1000); 
        curiosity.rest();
        Thread.sleep(1000);  
        curiosity.undo(); 
        Thread.sleep(1000); 

        // make new rover 
        System.out.println(".....");
        System.out.println("New rover!");
        System.out.println(".....");
        Thread.sleep(1000); 

        // makes rover to go to pluto -> crashes 
        Rover plutorover = new Rover("plutoRover", 2025, Destination.Pluto, 3); 
        Thread.sleep(1000);  
        plutorover.fly(1, 10); 
        Thread.sleep(1000);  
        plutorover.drop("Trench");
        Thread.sleep(1000);  

        // new rover 
        System.out.println(".....");
        System.out.println("New rover!");
        System.out.println(".....");
        Thread.sleep(1000);  

        // makes rover to explore Europa + tests running out of fuel 
        Rover icymoon = new Rover("Icymoon", 2025, Destination.Europa, 3); 
        Thread.sleep(1000);  
        icymoon.fly(1, 10); 
        Thread.sleep(1000);  
        System.out.println("The year is: " + icymoon.getCurrentYear()); 
        Thread.sleep(1000);  
        icymoon.drop("Highlands");
        Thread.sleep(1000);  
        icymoon.examine("ice");
        Thread.sleep(1000);  
        icymoon.grab("rock");
        Thread.sleep(1000);  
        icymoon.grab("ice");
        Thread.sleep(1000);  
        icymoon.shrink(); 
        Thread.sleep(1000);  
        icymoon.rest(); 
        Thread.sleep(1000);  
        icymoon.undo(); 
        Thread.sleep(1000);  
        icymoon.walk("south"); 
        Thread.sleep(1000);  
        icymoon.walk("west"); 
        Thread.sleep(1000);  
        icymoon.walk("north"); 
        Thread.sleep(1000);  
        icymoon.walk("west"); 
        Thread.sleep(1000);  
        icymoon.grow(); 
        Thread.sleep(1000);  
        System.out.println("The year is: " + icymoon.getCurrentYear()); 

}   catch (InterruptedException e) {}
}

}
