package net.minecraft.server;

import com.google.common.base.Objects;

public class BaseBlockPosition implements Comparable<BaseBlockPosition> {

    // PaperSpigot start - Make mutable and protected for MutableBlockPos
    protected int a;
    protected int c;
    protected int d;
    // PaperSpigot end

    public BaseBlockPosition(final int i, final int j, final int k) {
        this.a = i;
        this.c = j;
        this.d = k;
    }

    public BaseBlockPosition(final double d0, final double d1, final double d2) {
        this(MathHelper.floor(d0), (int) Math.floor(d1), (int) Math.floor(d2));
    }

    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof BaseBlockPosition)) {
            return false;
        } else {
            final BaseBlockPosition baseblockposition = (BaseBlockPosition) object;

            return this.getX() == baseblockposition.getX() && (this.getY() == baseblockposition.getY() && this.getZ() == baseblockposition.getZ());
        }
    }

    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public int g(final BaseBlockPosition baseblockposition) {
        return this.getY() == baseblockposition.getY() ? (this.getZ() == baseblockposition.getZ() ? this.getX() - baseblockposition.getX() : this.getZ() - baseblockposition.getZ()) : this.getY() - baseblockposition.getY();
    }

    // PaperSpigot start - Only allow one implementation of these methods (make final)
    public final int getX() {
        return this.a;
    }

    public final int getY() {
        return this.c;
    }

    public final int getZ() {
        return this.d;
    }
    // PaperSpigot end

    public BaseBlockPosition d(final BaseBlockPosition baseblockposition) {
        return new BaseBlockPosition(this.getY() * baseblockposition.getZ() - this.getZ() * baseblockposition.getY(), this.getZ() * baseblockposition.getX() - this.getX() * baseblockposition.getZ(), this.getX() * baseblockposition.getY() - this.getY() * baseblockposition.getX());
    }

    public double c(final double d0, final double d1, final double d2) {
        final double d3 = (double) this.getX() - d0;
        final double d4 = (double) this.getY() - d1;
        final double d5 = (double) this.getZ() - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double d(final double d0, final double d1, final double d2) {
        final double d3 = (double) this.getX() + 0.5D - d0;
        final double d4 = (double) this.getY() + 0.5D - d1;
        final double d5 = (double) this.getZ() + 0.5D - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double i(final BaseBlockPosition baseblockposition) {
        return this.c(baseblockposition.getX(), baseblockposition.getY(), baseblockposition.getZ());
    }

    public String toString() {
        return Objects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }

    // Paperspigot - Signature change, Object -> BaseBlockPosition
    public int compareTo(final BaseBlockPosition object) {
        return this.g(object);
    }
}
