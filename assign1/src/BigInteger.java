import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";

    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("(?<s1>[-+]?)(?<opd1>\\d+)(?<o>[-+*])(?<s2>[-+]?)(?<opd2>\\d+)$");

    private char[] number;
    private boolean sign = true; // is true when positive

    public BigInteger(int i)
    {
        if (i < 0){
            sign = false;
            i *= -1;
        }
        number = Integer.toString(i).toCharArray();
    }

    // Assume that only positive numbers come in
    public BigInteger(int[] num1)
    {
        number = new char[num1.length];
        for (int i=0; i<num1.length; i++){
            number[i] = (char) (48+num1[i]);
        }
    }

    public BigInteger(String s)
    {
        int k = 0;
        if (s.charAt(0)=='-' || s.charAt(0)=='+'){
            sign = s.charAt(0)=='+';
            k = 1;
//          remove leading zeros
            while (k<s.length() && s.charAt(k)=='0'){
                k+=1;
            }
            if (k == s.length())
                number = "0".toCharArray();
            else
                number = s.substring(k, s.length()).toCharArray();
        }
        // sign not specified
        else{
            sign = true;
            // remove leading zeros
            while (k<s.length() && s.charAt(k)=='0'){
                k+=1;
            }
            if (k == s.length())
                number = "0".toCharArray();
            else
                number = s.substring(k, s.length()).toCharArray();
        }
    }

    public BigInteger add(BigInteger big)
    {
        String result = "";

        //same sign
        if (big.sign == this.sign) {
            int cur = 1;
            int len1 = this.number.length;
            int len2 = big.number.length;
            int carry = 0, sum;
            // 각 자리수마다 더하기 연산을해 result의 왼쪽에 추가한다.
            while (cur<=len1 || cur<=len2){
                if (cur<=len1 && cur<=len2)
                    sum = this.number[len1-cur]+big.number[len2-cur] - 48 - 48 + carry;

                else if (cur<=len1)
                    sum = this.number[len1-cur] - 48 + carry;

                else
                    sum = big.number[len2-cur] - 48 + carry;

                if (sum >= 10){
                    carry = 1;
                    result = sum%10 + result;
                }
                else {
                    carry = 0;
                    result  = sum + result;
                }
                cur += 1;
            }
            if (carry != 0)
                result = "1" + result;
            result = (big.sign ? "+":"-") + result;
            return new BigInteger(result);
        }
        else{
            if (this.sign) {
                BigInteger new_big = new BigInteger(big.toString());
                new_big.sign = true;
                return this.subtract(new_big);
            }
            else{
                BigInteger new_big = new BigInteger(this.toString());
                this.sign = true;
                return big.subtract(this);
            }
        }
    }

    public BigInteger subtract(BigInteger big)
    {
        if (this.sign == big.sign){
            String result = "";
            int cur = 1;
            int len1 = this.number.length;
            int len2 = big.number.length;
            int carry = 0, sub;
            if (this.compareToIgnoreSign(big) == 0)
                return new BigInteger("0");
            else if (this.compareToIgnoreSign(big) > 0){
                while(cur<=len1 || cur<=len2){
                    if(cur<=len1 && cur<=len2)
                        sub = this.number[len1-cur] - big.number[len2-cur] - carry;
                    else
                        sub = this.number[len1-cur] - 48 - carry;

                    if (sub>=0){
                        result = sub + result;
                        carry = 0;
                    }
                    else {
                        result = sub + 10 + result;
                        carry = 1;
                    }
                    cur += 1;
                }
                while (result.startsWith("0"))
                    result = result.substring(1, result.length());
                result = (this.sign ? "+":"-") + result;
                return new BigInteger(result);
            }
            else{
                BigInteger new_big = new BigInteger(new String(big.number));
                BigInteger result_big = new_big.subtract(new BigInteger(new String(this.number)));
                result_big.sign = !this.sign;
                return result_big;
            }

        }
        else{
            if (big.sign){
                BigInteger new_big = new BigInteger(big.toString());
                new_big.sign = false;
                return this.add(new_big);
            }
            else{
                BigInteger new_big = new BigInteger(big.toString());
                new_big.sign = true;
                return this.add(new_big);
            }
        }
    }

    public BigInteger multiply(BigInteger big)
    {
        BigInteger bigResult = new BigInteger("0");
        int multi;
        int carry = 0;
        for (int i=big.number.length-1; i>=0; i--) {
            String result = "";
            for (int j = this.number.length - 1; j >= 0; j--) {
//                System.out.println((big.number[i]-48) + " * " + (this.number[j]-48));
                multi = (big.number[i] - 48) * (this.number[j] - 48) + carry;
                if (multi >= 10) {
                    carry = multi / 10;
                    result = (multi % 10) + result;
                } else {
                    carry = 0;
                    result = multi + result;
                }
            }
            if (carry > 0){
                result = carry + result;
                carry = 0;
            }
            if (!result.equals("0")){
                for (int k = 0; k < (big.number.length - i - 1); k++) {
                    result += "0";
                }
            }
//            System.out.println((new BigInteger(result)).toString());
            bigResult = bigResult.add(new BigInteger(result));
        }
        bigResult.sign = this.sign == big.sign;
        return bigResult;
    }

    @Override
    public String toString()
    {
        if (sign || (number.length == 1 && number[0] == '0'))
            return new String(number);
        else{
            return "-" + new String(number);
        }
    }
    //절대값 비교
    public int compareToIgnoreSign(BigInteger big)
    {
        if (this.number.length != big.number.length)
            return this.number.length - big.number.length;
        int k = 0;
        while (k<this.number.length && (this.number[k]==big.number[k])){
            k+=1;
        }
        if (k == this.number.length )
            return 0;
        return this.number[k]-big.number[k];
    }

    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        String trimmed_input = input.replaceAll("\\s",""); // replace white space with ""
        Matcher matcher = EXPRESSION_PATTERN.matcher(trimmed_input);
        //does not match
        if (!matcher.matches()){
            throw new IllegalArgumentException();
        }

        String s1 = matcher.group("s1"); // sign of the first operand
        String opd1 = matcher.group("opd1");  //first operand
        String s2 = matcher.group("s2");
        String opd2 = matcher.group("opd2");
        String operator = matcher.group("o");

        BigInteger num1 = new BigInteger(s1 + opd1);
        BigInteger num2 = new BigInteger(s2 + opd2);

        BigInteger result;
        switch (operator){
            case  "+":
                return num1.add(num2);

            case "-":
                return num1.subtract(num2);

            case "*":
                return num1.multiply(num2);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();

                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);

        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());

            return false;
        }
    }

    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}