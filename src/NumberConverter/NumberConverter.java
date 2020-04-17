package NumberConverter;


import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberConverter {

    final static Pattern p = Pattern.compile("\\d+");
    static Matcher m;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter base of a number (from 1 to 36): ");
        int base = getBase(in.nextLine());
        System.out.println("Enter number by that base (can be any): ");
        String s = in.nextLine().toLowerCase();

        String[] parts = s.split("\\.");
        int pow = parts.length == 2? -(parts[1].length()) : 0;
        double num = toDecimal(new StringBuilder(s), base, pow);

        System.out.println("Enter the base you want to convert that number to (from 1 to 36): ");
        int base2 = getBase(in.nextLine());
        System.out.println("Enter precision of calculations (for nums with fractions only): ");
        int precision = in.nextInt(); // nums after dot for fraction calculations (fixed)

        StringBuilder res = new StringBuilder().append(fromDecimal((long) num, base2));
        res.append(res.length() == 0 ? '0' : "");
        if (s.contains(".") && base2 != 1) {
            res.append('.').append(getFraction(num - (int)num, base2, precision));
        }
        System.out.printf("%nThe result of converting %s from base %d to base %d: %s", s, base, base2, res);
    }

    static int getBase(String line) {
        m = p.matcher(line);
        if (!m.matches()) {
            System.out.println("Base isn't a number error");
            System.exit(0);
        }
        int answ = Integer.parseInt(line);
        if (answ < 1 || answ > 36) {
            System.out.println("Base lower or higher than possible error");
            System.exit(0);
        }
        return answ;
    }

    /**
     * Convert num from any (1 to 36) base
     * to decimal (no fraction)
     *
     * @param num by any base
     * @param base of this num
     * @param pow trigger to start with
     * @return decimal representation of num (int)
     */
    static double toDecimal(StringBuilder num, int base, int pow) {
        double res = 0;
        int l = num.length() - 1;
        if (l < 0) {
            return res;
        }
        if (num.charAt(l) >= 48 && num.charAt(l) <= 57) {
            res += (num.charAt(l) - 48) * Math.pow(base, pow++);
        }
        else if (num.charAt(l) != '.') {
            int e = num.charAt(l) - 87; // decimal representation of literal (a-z)
            if (e > base - 1) {
                System.out.println("Letter by that base doesn't exist error");
                System.exit(0);
            }
            res += e * Math.pow(base, pow++);
        }
        return res + toDecimal(num.deleteCharAt(l), base, pow);
    }

    /**
     * Converts non-negative integers
     * From decimal to any(1 to 36) base number
     * Via recursion.
     *
     * @param decNum Decimal num (long)
     * @param base Base to convert num to
     * @return StringBuilder with number by that @base in it
     */
    static StringBuilder fromDecimal(long decNum, int base) {
        StringBuilder res = new StringBuilder();
        long n = decNum % base;
        if (decNum == 0 || base == 1) { // recursion exit and base 1 cheat
            if (base == 1) {
                while (decNum-- > 0) {
                    res.append('1');
                }
            }
            return res;
        }
        if (n > 9) {
            res.insert(0, (char) (n + 87));
        }
        else {
            res.insert(0, n);
        }
        return res.insert(0, fromDecimal(decNum / base, base));
    }

    /**
     * Takes num and converts its fraction
     * to specified base
     *
     * @param num (double) num, fraction part of which needs to be converted
     * @param base of that target num
     * @param precision result's precision (num of symbols after dot)
     * @return StringBuilder with converted fraction part
     */
    static StringBuilder getFraction(double num, int base, int precision) {
        StringBuilder res = new StringBuilder();
        if (precision < 1) {
            return res;
        }
        double temp = base * (num - (int) num);
        if ((int) temp > 9) {
            res.append((char) ((int) temp + 87));
        }
        else {
            res.append((int) temp);
        }
        return res.append(getFraction(temp, base, --precision));
    }
}