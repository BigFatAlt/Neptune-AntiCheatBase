package cc.bigfatman.anticheat.util;

import cc.bigfatman.anticheat.util.fastmath.FastMath;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;

//from https://github.com/NikV2 anticheat base now deleted.
public final class MathUtils {

    public static final float FRICTION = .91F;
    public static final double WATER_FRICTION = .800000011920929D;
    public static final double SERVER_GROUND_DIVISOR = .015625D;
    public static final double MOTION_Y_FRICTION = .9800000190734863D;
    public static final double MAXIMUM_MOTION_Y = .41999998688697815D;

    public static final double EXPANDER = 1.6777216E7D;
    public static final long MINIMUM_ROTATION_DIVISOR = 131072L;


    public static long getAbsoluteGcd(final float current, final float last) {

        final long currentExpanded = (long) (current * EXPANDER);

        final long lastExpanded = (long) (last * EXPANDER);

        return getGcd(currentExpanded, lastExpanded);
    }

    private static long getGcd(final long current, final long last) {
        return (last <= 16384L) ? current : getGcd(last, current % last);
    }

    /**
     * Calculates the gcd of {@param a} and {@param b}
     * @return The gcd
     */
    public static double gcd(double a, double b) {
        if (a < b)
            return gcd(b, a);
        else if (Math.abs(b) < 0.001) // base case
            return a;
        else
            return gcd(b, a - Math.floor(a / b) * b);
    }

    public static double getAbsoluteDelta(final double one, final double two) {
        return Math.abs(Math.abs(one) - Math.abs(two));
    }

    public static float getAbsoluteDelta(final float one, final float two) {
        return Math.abs(Math.abs(one) - Math.abs(two));
    }

    public static double decimalRound(final double val, int scale) {
        return BigDecimal.valueOf(val).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static double angle(Vector a, Vector b) {
        double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1), 1);
        return Math.acos(dot);
    }

    /**
     * Calculates sqrt of all the values entries^2
     * @param values The number values
     * @return sqrt(values^2)
     */
    public static double hypot(double... values) {
        AtomicDouble squaredSum = new AtomicDouble(0D);

        Arrays.stream(values).forEach(value -> squaredSum.getAndAdd(Math.pow(value, 2D)));

        return Math.sqrt(squaredSum.get());
    }

    /**
     * Calculates the average (mean) of {@param values}
     * @param values The number values
     * @return The average (mean) of {@param values}
     */
    public static double getAverage(Collection<? extends Number> values) {
        return values.stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0D);
    }

    /**
     * Calculates the standard deviation of {@param values}
     * @param values The number values
     * @return The standard deviation of {@param values}
     */
    public static double getStandardDeviation(Collection<? extends Number> values) {
        double average = getAverage(values);

        AtomicDouble variance = new AtomicDouble(0D);

        values.forEach(delay -> variance.getAndAdd(Math.pow(delay.doubleValue() - average, 2D)));

        return Math.sqrt(variance.get() / values.size());
    }

    /**
     * Calculates the kurtosis of {@param values}
     * @param values The number values
     * @return The kurtosis of {@param values}
     */
    public static double getKurtosis(Collection<? extends Number> values) {
        double n = values.size();

        if (n < 3)
            return Double.NaN;

        double average = getAverage(values);
        double stDev = getStandardDeviation(values);

        AtomicDouble accum = new AtomicDouble(0D);

        values.forEach(delay -> accum.getAndAdd(Math.pow(delay.doubleValue() - average, 4D)));

        return n * (n + 1) / ((n - 1) * (n - 2) * (n - 3)) *
                (accum.get() / Math.pow(stDev, 4D)) - 3 *
                Math.pow(n - 1, 2D) / ((n - 2) * (n - 3));
    }

    public static Vector getDirection(final Location location) {

        Vector vector = new Vector();

        final double rotX = location.getYaw();
        final double rotY = location.getPitch();

        final double radiansRotY = FastMath.toRadians(rotY);

        vector.setY(-FastMath.sin(radiansRotY));

        final double xz = FastMath.cos(radiansRotY);

        final double radiansRotX = FastMath.toRadians(rotX);

        vector.setX(-xz * FastMath.sin(radiansRotX));
        vector.setZ(xz * FastMath.cos(radiansRotX));

        return vector;
    }

    public static Vector getDirection(final float yaw, final float pitch) {

        Vector vector = new Vector();

        final double radiansRotY = FastMath.toRadians(pitch);

        vector.setY(-FastMath.sin(radiansRotY));

        final double xz = FastMath.cos(radiansRotY);

        final double radiansRotX = FastMath.toRadians(yaw);

        vector.setX(-xz * FastMath.sin(radiansRotX));
        vector.setZ(xz * FastMath.cos(radiansRotX));

        return vector;
    }

    public static boolean isScientificNotation(final Number num) {
        return num.doubleValue() < .001D;
    }

    public static int millisToTicks(long millis) {
        return (int) millis / 50;
    }

    public static long nanosToMillis(final long nanoseconds) {
        return (nanoseconds / 1000000L);
    }

    public static long elapsed(final long millis) {
        return System.currentTimeMillis() - millis;
    }
}