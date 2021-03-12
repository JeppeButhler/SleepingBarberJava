public class Program {
    private static final int numberOfBarbers = 8; //Antallet af Barbers
    private Barber[] workers = new Barber[numberOfBarbers];
    private WaitingRoom waitingRoom; //Lokale med 4 stole (både til barbering og ventepladser).

    public Program() {
        waitingRoom = WaitingRoom.getInstance();
    }

    public static void main(String[] args) {
        Program instance = new Program();
        instance.startWorkDay(); //Work, work, work, work, work...
    }

    public void startWorkDay() {
        for (int i = 0; i < numberOfBarbers; i++) {
            workers[i] = new Barber(waitingRoom); //Opretter antallet af Barbers, der skal være
            workers[i].start(); //Starter Barber tr�dene
        }

        new Thread(new Runnable() { //Ny tråd oprettes
            @Override
            public void run() {
                int index = 0;
                while (index++ < 100 && CustomerCounter.getInstance().customerCount() < 50) { //Antallet af kunder oprettes i tråden, færdiggører igangværende kunder over lukketid.
                    try {
							waitingRoom.seatCustomer(new Customer()); //En kunde placeres i en stol, enten skal han vente, eller også har "the barber" tid med det samme
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
                    try {
                        Thread.sleep((long) (Math.random() * 1000)); //Lidt forsinkelse på instantiering og placering/afvisning af nye kunder
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}