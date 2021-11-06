import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WowCraftingRobotApplication {

    // Params to change
    private static final double CRAFTING_TIME = 2; // sec, used time to craft 1 item
    private static final int NB_SLOTS_MAX = 148; // sec, #empty slots

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
    private static final double CRAFTING_WAIT = CRAFTING_TIME * NB_SLOTS_MAX; // sec
    private static final int SELLING_WAIT = NB_SLOTS_MAX / NB_ITEMS_SOLD_BY_SEC; // sec
    private static final int RANDOM_WAIT = 10; // sec
    private static final int SMOOTH_MOVE_NB_STEPS = 1000; // sec
    private static final int SMOOTH_MOVE_MAX_SECONDS_TRAVEL = 3; // sec

    private int nbAdd = 0;

    private void crafting(final Robot robot) throws InterruptedException {
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
        TimeUnit.SECONDS.sleep((long) (CRAFTING_WAIT + SMOOTH_MOVE_MAX_SECONDS_TRAVEL + (nbAdd * CRAFTING_TIME)));
        System.out.println(new Date() + " - Crafting DONE");
    }

    private void selling(final Robot robot) throws InterruptedException {
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

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        mouseGlide(x, y, baseX, baseY, (int) (random.nextFloat() * SMOOTH_MOVE_MAX_SECONDS_TRAVEL), SMOOTH_MOVE_NB_STEPS);
        System.out.println(new Date() + " - Start selling...");
        TimeUnit.SECONDS.sleep(SELLING_WAIT + SMOOTH_MOVE_MAX_SECONDS_TRAVEL + nbAdd / NB_ITEMS_SOLD_BY_SEC);
        System.out.println(new Date() + " - Selling DONE");
    }

    public void start() throws AWTException, InterruptedException {
        final var random = new Random();
        do {
            this.crafting(new Robot());
            var sleepTime = random.nextInt(RANDOM_WAIT);
            System.out.println(new Date() + " - sleep time : " + sleepTime);

            this.selling(new Robot());
            sleepTime = random.nextInt(RANDOM_WAIT);
            System.out.println(new Date() + " - sleep time : " + sleepTime);
            TimeUnit.SECONDS.sleep(sleepTime);
            nbAdd+=1;
            if(nbAdd >= 148) nbAdd = 148;
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
        final var robot = new WowCraftingRobotApplication();
        robot.start();
//        robot.showPointerInfo();
    }
}
