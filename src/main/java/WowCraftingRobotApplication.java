import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WowCraftingRobotApplication {

    // Params to change
    private static final double CRAFTING_TIME = 3; // sec, used time to craft 1 item
    private static final int NB_SLOTS = 1; // sec, #empty slots
    private static final int NB_MAX_SLOTS = 148; // sec, #empty slots


    // it depends where is your selling button
    private static final int CRAFT_ALL_POSITION_X_MIN = 721;
    private static final int CRAFT_ALL_POSITION_X_MAX = 790;
    private static final int CRAFT_ALL_POSITION_Y_MIN = 591;
    private static final int CRAFT_ALL_POSITION_Y_MAX = 600;
    private static final int SELL_ALL_POSITION_X_MIN = 700;
    private static final int SELL_ALL_POSITION_X_MAX = 813;
    private static final int SELL_ALL_POSITION_Y_MIN = 616;
    private static final int SELL_ALL_POSITION_Y_MAX = 631;
    private static final int TIME_MOUSE_CLICK_INTERVAL = 400;
    private static final int NB_ITEMS_SOLD_BY_SEC = 4; // sec
    private static final double CRAFTING_WAIT = CRAFTING_TIME * NB_SLOTS; // sec
    private static final int SELLING_WAIT = NB_SLOTS / NB_ITEMS_SOLD_BY_SEC; // sec
    private static final int RANDOM_WAIT = 10; // sec
    private static final int SMOOTH_MOVE_NB_STEPS = 1000; // sec
    private static final int SMOOTH_MOVE_MAX_SECONDS_TRAVEL = 3; // sec

    private int nbAdd = 0;

    private void crafting(final Robot robot, final double craftingTime, final int nbSlots) throws InterruptedException {
        // Crafting
        final var random = new Random();
        final var baseX = MouseInfo.getPointerInfo().getLocation().x;
        final var baseY = MouseInfo.getPointerInfo().getLocation().y;
        final var x = new Random().nextInt(CRAFT_ALL_POSITION_X_MAX - CRAFT_ALL_POSITION_X_MIN) + CRAFT_ALL_POSITION_X_MIN;
        final var y = new Random().nextInt(CRAFT_ALL_POSITION_Y_MAX - CRAFT_ALL_POSITION_Y_MIN) + CRAFT_ALL_POSITION_Y_MIN;
        final var mouseIntervalClick = new Random().nextInt(200) + TIME_MOUSE_CLICK_INTERVAL;

        mouseGlide(baseX, baseY, x, y, (int) (random.nextFloat() * SMOOTH_MOVE_MAX_SECONDS_TRAVEL), SMOOTH_MOVE_NB_STEPS);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(mouseIntervalClick);

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        mouseGlide(x, y, baseX, baseY, (int) (random.nextFloat() * SMOOTH_MOVE_MAX_SECONDS_TRAVEL), SMOOTH_MOVE_NB_STEPS);
        System.out.println(new Date() + " - Start crafting...");
        final double craftingWait = craftingTime * nbSlots;
        TimeUnit.SECONDS.sleep((long) (craftingWait + SMOOTH_MOVE_MAX_SECONDS_TRAVEL + (nbAdd * craftingTime)));
        System.out.println(new Date() + " - Crafting DONE");
    }

    private void selling(final Robot robot, final int nbSlots) throws InterruptedException {
        // Selling
        final var random = new Random();
        final var baseX = MouseInfo.getPointerInfo().getLocation().x;
        final var baseY = MouseInfo.getPointerInfo().getLocation().y;
        final var x = new Random().nextInt(SELL_ALL_POSITION_X_MAX - SELL_ALL_POSITION_X_MIN) + SELL_ALL_POSITION_X_MIN;
        final var y = new Random().nextInt(SELL_ALL_POSITION_Y_MAX - SELL_ALL_POSITION_Y_MIN) + SELL_ALL_POSITION_Y_MIN;
        final var mouseIntervalClick = new Random().nextInt(200) + TIME_MOUSE_CLICK_INTERVAL;

        mouseGlide(baseX, baseY, x, y, (int) (random.nextFloat() * SMOOTH_MOVE_MAX_SECONDS_TRAVEL), SMOOTH_MOVE_NB_STEPS);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(mouseIntervalClick);

        final int sellingWait = nbSlots / NB_ITEMS_SOLD_BY_SEC;

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        mouseGlide(x, y, baseX, baseY, (int) (random.nextFloat() * SMOOTH_MOVE_MAX_SECONDS_TRAVEL), SMOOTH_MOVE_NB_STEPS);
        System.out.println(new Date() + " - Start selling...");
        TimeUnit.SECONDS.sleep(sellingWait + SMOOTH_MOVE_MAX_SECONDS_TRAVEL + nbAdd / NB_ITEMS_SOLD_BY_SEC);
        System.out.println(new Date() + " - Selling DONE");
    }

    public void start(Double craftingTime, Integer nbSlots, Integer nbMaxAvailableSlots) throws AWTException, InterruptedException {
        final var random = new Random();
        final double crafting = craftingTime != null ? craftingTime : CRAFTING_TIME;
        final int minSlots = nbSlots != null ? nbSlots : NB_SLOTS;
        final int nbMaxSlots = nbMaxAvailableSlots != null ? nbMaxAvailableSlots : NB_MAX_SLOTS;

        do {
            this.crafting(new Robot(), crafting, minSlots);
            var sleepTime = random.nextInt(RANDOM_WAIT);
            System.out.println(new Date() + " - sleep time : " + sleepTime);

            this.selling(new Robot(), nbSlots);
            sleepTime = random.nextInt(RANDOM_WAIT);
            System.out.println(new Date() + " - sleep time : " + sleepTime);
            TimeUnit.SECONDS.sleep(sleepTime);
            nbAdd += 1;
            if (nbAdd >= nbMaxSlots) nbAdd = nbMaxSlots;
        } while (true);
    }

    public void showPointerInfo() {
        System.out.println(MouseInfo.getPointerInfo().getLocation());
    }

    private void mouseGlide(int x1, int y1, int x2, int y2, int t, int n) {
        try {
            Robot r = new Robot();
            double dx = (x2 - x1) / ((double) n);
            double dy = (y2 - y1) / ((double) n);
            double dt = t / ((double) n);
            for (int step = 1; step <= n; step++) {
                Thread.sleep((int) dt);
                r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws AWTException, InterruptedException {
        if (args.length != 3) {
            System.out.println("Il faut 3 paramètres séparés par un espace pour lancer l'application :");
            System.out.println("1) le temps de craft en second");
            System.out.println("2) Nb d'espaces libres");
            System.out.println("3) Nb max d'espaces libres");
            System.out.println("exemple : java -jar WowCraftingRobot-1.0.jar 2 1 148");
        }

        try {
            final Double craftingTime = Double.parseDouble(args[0]);
            final Integer nbSlots = Integer.parseInt(args[1]);
            final Integer nbMaxSlots = Integer.parseInt(args[2]);

            final var robot = new WowCraftingRobotApplication();
            robot.start(craftingTime, nbSlots, nbMaxSlots);
//        robot.showPointerInfo();
        } catch (NumberFormatException nfe) {
            System.out.println("Erreur durant l'application" + nfe.getLocalizedMessage());
        }
    }
}
