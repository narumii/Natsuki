package pw.narumi.object;

//NWM XD
public class Timer
{
    private long time;
    private final long l;

    public Timer() {
        this.l = 1000000L;
        this.time = System.nanoTime() / this.l;
    }

    public boolean hasTimeElapsed(final long delay) {
        if (this.getTime() >= delay) {
            this.time = System.nanoTime() / this.l;
            return true;
        }
        return false;
    }

    private long getTime() {
        return System.nanoTime() / this.l - this.time;
    }
}
