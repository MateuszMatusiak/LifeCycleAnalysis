package Main;

public class LeastSquaresLine {

    public static double calculateA(double[] x, double[] y) {
        double n = x.length;
        double s = n, sx = 0, sy = 0, sxx = 0, sxy = 0, syy = 0, d = 0;
        for (int i = 0; i < n; ++i) {
            sx += x[i];
            sy += y[i];
            sxx += x[i] * x[i];
            sxy += x[i] * y[i];
            syy += y[i] * y[i];
        }
        d = s * sxx - (sx * sx);

        return (s * sxy - sx * sy) / d;

    }
    public static double calculateB(double[] x, double[] y) {
        double n = x.length;
        double s = n, sx = 0, sy = 0, sxx = 0, sxy = 0, syy = 0, d = 0;
        for (int i = 0; i < n; ++i) {
            sx += x[i];
            sy += y[i];
            sxx += x[i] * x[i];
            sxy += x[i] * y[i];
            syy += y[i] * y[i];
        }
        d = s * sxx - (sx * sx);

        return (sxx * sy - sx * sxy) / d;

    }
}