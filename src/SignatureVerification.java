import java.math.BigInteger;

public class SignatureVerification {
    private BigInteger p;
    private BigInteger h;
    private BigInteger g;
    private BigInteger H;
    private BigInteger S1;
    private BigInteger S2;

    public SignatureVerification(BigInteger p, BigInteger h, BigInteger g, BigInteger H, BigInteger S1, BigInteger S2) {
        this.p = p;
        this.h = h;
        this.g = g;
        this.H = H;
        this.S1 = S1;
        this.S2 = S2;
    }

    public boolean verify() {
        if (S1.compareTo(BigInteger.valueOf(0)) != 1 && S1.compareTo(p) != -1) {
            return false;
        }
        BigInteger v = h.modPow(S1, p).multiply(S1.modPow(S2, p));
        v = v.mod(p);

        if (g.modPow(H, p).compareTo(v) != 0) {
            return false;
        }
        return true;
    }
}
