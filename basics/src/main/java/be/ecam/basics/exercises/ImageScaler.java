package be.ecam.basics.exercises;

public class ImageScaler {
    public static int[] scale(int width, int height, int num, int den) {
        if (width < 0 || height < 0) throw new IllegalArgumentException();
        if (den == 0) throw new IllegalArgumentException();

        int w = roundDiv(width, num, den);
        int h = roundDiv(height, num, den);

        if (w < 0) w = 0;
        if (h < 0) h = 0;
        return new int[]{w, h};
    }

    private static int roundDiv(int size, int num, int den) {
        long a = (long) size * (long) num; // éviter overflow
        long b = den;
        if (b < 0) { a = -a; b = -b; }     // normaliser le signe du dénominateur
        // arrondi à l’entier le plus proche (tie -> up)
        a = (a >= 0) ? (a + b / 2) : (a - b / 2);
        long r = a / b;
        if (r > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (r < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) r;
    }
}