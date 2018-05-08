import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Token implements Comparable<Token> {
//  연산기호, 숫자를 표현하는 자료형
    char oper;
    boolean isNum;
    long val;

    Token(char oper) {
        this.oper = oper;
        this.isNum = false;
        this.val = 0;
    }

    Token(long val) {
        this.oper = 0;
        this.isNum = true;
        this.val = val;
    }

//  우선순위에 따라 대소 비교
    @Override
    public int compareTo(Token o) {
        return this.getMagni() - o.getMagni();
    }

    public int getMagni() {
        if (oper == '(' || oper == ')')
            return -1;
        else if (oper == '^')
            return 3;
        else if (oper == '~')
            return 2;
        else if (oper == '*' || oper == '%' || oper == '/')
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        if (isNum)
            return ""+val;
        else
            return ""+oper;
    }

//  String을 token 단위로 끊어서 postfix 순서로 리스트에 담아 반환
    public static ArrayList<Token> stringTokenizer(String input) throws Exception {
//      정의되지않은 문자 처리
        Pattern errorPattern = Pattern.compile("[^+\\-*/%^()0-9\\s]");
        if (errorPattern.matcher(input).find())
            throw new Exception();
//      infix -> postfix 순서로 arraylist에 저장
        ArrayList<Token> tokens = new ArrayList<>();
        Stack<Token> operators = new Stack<>();
        Pattern pattern = Pattern.compile("[+\\-*/%^()]|[0-9]+");
        Matcher matcher = pattern.matcher(input);

        boolean isNumTurn = true;
//      더 이상 매칭되는 문자열이 없을 때 까지
        while (matcher.find()) {
//          현재 매칭된 문자열
            String cur = matcher.group();
//          - (unary) 인 경우
            if (cur.equals("-")) {
                if (isNumTurn) {
                    cur = "~";
                    Token curToken = new Token('~');
                    while(!operators.empty()){
                        Token top = operators.peek();
                        if (top.compareTo(curToken) <= 0)
                            break;
                        tokens.add(operators.pop());
                    }
                    operators.push(curToken);
                }
            }
//           연산자인 경우
            if ("+-*/%^".contains(cur)) {
//              숫자가 와야하는데 연산자
                if (isNumTurn)
                    throw new Exception();
//              다음엔 숫자가 와야함
                isNumTurn = true;
                Token curToken = new Token(cur.charAt(0));

                while(!operators.empty()){
                    Token top = operators.peek();
                    if (top.compareTo(curToken) < 0)
                        break;
//                  ^는 right associative
                    if (top.oper == '^' && top.compareTo(curToken) == 0)
                        break;
                    tokens.add(operators.pop());
                }
                operators.push(curToken);
            } else if (cur.equals(")")) {
//              숫자가 올차례였다면 에러
                if (isNumTurn)
                    throw new Exception();
                Token curToken = new Token(cur.charAt(0));
                boolean isPaired = false;
                while (!operators.empty()){
                    Token top = operators.peek();
//                  짝을 찾은경우 stop
                    if (top.oper == '(') {
                        isPaired = true;
                        operators.pop();
                        break;
                    }
                    tokens.add(operators.pop());
                }
//              짝이 안맞는 경우 에러
                if (!isPaired)
                    throw new Exception();
            } else if (cur.equals("(")) {
                if (!isNumTurn)
                    throw new Exception();
                Token curToken = new Token(cur.charAt(0));
                operators.push(curToken);
            }
//          숫자인 경우
            else if (!cur.equals("~")) {
                if (!isNumTurn)
                    throw new Exception();
                tokens.add(new Token(Long.parseLong(cur)));
                isNumTurn = false;
            }
        }
        if (isNumTurn)
            throw new Exception();
        while (!operators.empty()) {
            if (operators.peek().oper == '(')
                throw new Exception();
            tokens.add(operators.pop());
        }
        return tokens;
    }
}


public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) throws Exception {
		ArrayList<Token> tokens = Token.stringTokenizer(input);
        long evaluatedVal = eval(tokens);
        printTokens(tokens);
		System.out.println(evaluatedVal);
	}

//  postfix 로 된 수식 계산해주는 함수
	private static long eval(ArrayList<Token> tokens) throws Exception {
	    Stack<Long> nums = new Stack<>();
	    for (Token token : tokens){
	        if (token.isNum)
	            nums.push(token.val);
	        else if (token.oper == '~'){
	            nums.push(nums.pop()*-1);
            }
            else{
	            long operand2 = nums.pop();
	            long operand1 = nums.pop();
	            long result = 0l;
//	            연산자에 알맞은 연산 수행
	            switch (token.oper){
                    case '+':
                        result = operand1 + operand2;
                        break;
                    case '-':
                        result = operand1 - operand2;
                        break;
                    case '*':
                        result = operand1 * operand2;
                        break;
                    case '/':
                        result = operand1 / operand2;
                        break;
                    case '%':
                        result = operand1 % operand2;
                        break;
                    case '^':
                        if (operand1 == 0 && operand2<0)
                            throw new Exception();
                        result = (long) (Math.pow(operand1, operand2));
                        break;
	            }
	            nums.push(result);
            }
        }
	    long result = nums.pop();
//	    아직 스택에 계산되지 않은 값이 남아 있다면 에러
	    if (!nums.empty())
	        throw new Exception();
	    return result;
    }

    private static void printTokens(ArrayList<Token> tokens){
	    String print="";
	    for (Token token : tokens){
	        print += token.toString() + " ";
        }
//      마지막 공백 제거후 출력
        System.out.println(print.trim());
    }
}
