public class Barber extends Thread {
    
	private static int id_s = 1;
    private int id = -1;
    private States state = States.SLEEPING;
    private WaitingRoom watitingRoomReference;
    private CustomerCounter customerCounterReference;

    public Barber(WaitingRoom waitRoomRef) {
        id = id_s++;
        this.watitingRoomReference = waitRoomRef; // Hvilket rum, en barber befinder sig i
        this.customerCounterReference = CustomerCounter.getInstance();
        sleep(); // Barber sætter sig og sover.

    }

    public void doHaircut(Customer c) throws InterruptedException {
        c.acquire(); // Sikrer sig at der ikke klippes flere kunder på samme tid af samme barber.
        state = States.WORKING; // Barber vågner og påbegynder sit arbejde.
        System.out.println("Barber " + getID() + " starts shaving customer " + c.getID());
        Thread.sleep((long) (2000 * Math.random())); // Barber skal også lige have en chance for at vågne og finde sin
                                                     // saks.
        c.shave(); // En kunde bliver klippet, barberet og sat pænt i stand.
        c.release(); // Kunden bliver frigivet fra barber.
        customerCounterReference.increment(); // Tæller antallet af nåede kunder op med 1.
    }

    public boolean isOccupied() {
        return state == States.WORKING;
    }

    public void wakeBarber() {
        // isSleeping = false;
        state = States.WORKING;
        System.out.println("The barber " + getID() + " was awakened to do his job.");
    }

    public void sleep() {
        // isSleeping = true;
        state = States.SLEEPING;
        System.out.println("The barber " + getID() + " went to sleep.");

    }

    public int getID() {
        return id;
    }

    @Override
    public void run() {
        while (customerCounterReference.customerCount() < 50) {
            Customer customer = watitingRoomReference.unseatCustomer(); // Tager kunden fra køen
            if (customer != null) {
                if (state == States.SLEEPING) {
                    wakeBarber(); // Vækker frisøren
                }

                try {
                    doHaircut(customer); // Foretager arbejdet
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else { // Hvis der ikke er nogle kunder, lægger frisøren sig til at sove
                if (state == States.WORKING) {
                    state = States.SLEEPING;
                    System.out.println("The barber is going to sleep...");
                }
            }
        }

        System.out.println("DONE! :O");
    }

    public enum States {
        SLEEPING, WORKING
    }
}