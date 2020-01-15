/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Lisanne Nijenhuis & Romano Braxhoofden
 * @version 07-01-2020
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room toilet, gangV21, lokaalA232, lokaalA218, gangV22, lift, kantine, trapV21, trapV22, ontvangsthal, helpdesk, hoofdingang, gangBG1, gangBG2, docentenkamers, kelder, ventilatieschacht;

        // create the rooms
        toilet = new Room("het toilet");
        gangV21 = new Room("gang van vleugel A op de 2e verdieping 1/2");
        lokaalA232 = new Room("lokaal A2.32");
        lokaalA218 = new Room("lokaal A.218");
        gangV22 = new Room("gang van vleugel A op de 2e verdieping 2/2");
        lift = new Room("de lift");
        kantine = new Room("de kantine");
        trapV21 = new Room("de trap bij de lift");
        trapV22 = new Room("de trap bij de kantine");
        ontvangsthal = new Room("ontvangsthal op begane grond");
        helpdesk = new Room("de helpdesk");
        hoofdingang = new Room("de hoofdingang");
        gangBG1 = new Room("gang op de begane grond 1/2");
        gangBG2 = new Room("gang op de begane grond 2/2");
        docentenkamers = new Room("de docentenkamers");
        kelder = new Room("de kelder");
        ventilatieschacht = new Room("de ventilatieschacht");

        
        // initialise room exits
        toilet.setExit("north", gangV21);

        gangV21.setExit("north", lokaalA232);
        gangV21.setExit("east", gangV22);
        gangV21.setExit("south", lokaalA218);
        gangV21.setExit("west", toilet);

        lokaalA232.setExit("south", gangV21);
        
        lokaalA218.setExit("north", gangV21);
        
        gangV22.setExit("north", trapV21);
        gangV22.setExit("east", kantine);
        gangV22.setExit("south", gangV21);
        gangV22.setExit("west", lift);
        
        //trap moet met up-down
        trapV21.setExit("south", ontvangsthal);//down
        trapV21.setExit("north", gangV22);//up
        
        kantine.setExit("west", gangV22);
        kantine.setExit("north", trapV22);
        
        //trap moet met up-down
        trapV22.setExit("south", gangBG1);//down
        trapV22.setExit("north", kantine);//up
        
        ontvangsthal.setExit("north", trapV21);
        ontvangsthal.setExit("east", helpdesk);
        ontvangsthal.setExit("south", hoofdingang);
        ontvangsthal.setExit("west",gangBG1);
        
        helpdesk.setExit("west", ontvangsthal);
        
        hoofdingang.setExit("north", ontvangsthal);
        
        gangBG1.setExit("north", ontvangsthal);
        gangBG1.setExit("east", trapV22);
        gangBG1.setExit("south", gangBG2);
        
        gangBG2.setExit("north", gangBG1);
        gangBG2.setExit("south", docentenkamers);
        gangBG2.setExit("west", kelder);
        
        kelder.setExit("east", ventilatieschacht);
        kelder.setExit("west", gangBG2);
        
        ventilatieschacht.setExit("east", kelder);

        currentRoom = toilet;  // start game op toilet
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
            System.out.println("I don't know what you mean...");
            break;

            case HELP:
            printHelp();
            break;

            case GO:
            goRoom(command);
            break;

            case QUIT:
            wantToQuit = quit(command);
            break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
