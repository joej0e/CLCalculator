import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Calculator {
    public static void main(String[] args) throws Exception {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        String sIn;
		Pattern patternRom = Pattern.compile("[XVI]");
		Pattern patternArab = Pattern.compile("\\d+");

		
        try {
            System.out.println("Put numbers and operators here");
            sIn = d.readLine();	
			Matcher matcherRom = patternRom.matcher(sIn);
			Matcher matcherArab = patternArab.matcher(sIn);
			if(matcherArab.find() && matcherRom.find()) throw new Exception("Invalid arguments");
			if(matcherRom.find()) sIn = Transform.toArabic(sIn);
            sIn = OPNmaker.opn(sIn);
            System.out.println(Calculation.calculate(sIn));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
class Transform {

	 static String toArabic(String n) {
	    String[] roman = {"I", "II", "III","IV", "V", "VI", "VII", "VIII", "IX", "X"};
		String[] newString = n.split("[*/+-]");
		String[] oper = n.split("[XVI]");
		oper = String.join("", oper).split("");
		
		for(int i = 0; i < roman.length; i++){
			String tmp = roman[i];
			for(int j = 0; j < newString.length; j++) {
				if(tmp.equals(newString[j])) 
					newString[j] = Integer.toString(i+1);
			}
		}
		
		String[] nString = new String[oper.length+newString.length];
		int j = 0;
		 for (int i = 0; i < nString.length; i++) {
		if (i < newString.length) nString[j++] = newString[i]; 
		if (i < oper.length) nString[j++] = oper[i];
		}
		String result = String.join("", nString);
		return result;
	 }
}
    //reverse polish notation
class OPNmaker {
	static String opn(String sIn) throws Exception {
        StringBuilder sbStack = new StringBuilder(""), sbOut = new StringBuilder("");
        char cIn, cTmp;
		
        for (int i = 0; i < sIn.length(); i++) {
            cIn = sIn.charAt(i);
            if (Verific.isOp(cIn)) {
                while (sbStack.length() > 0) {
                    cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                    if (Verific.isOp(cTmp) && (Verific.opPrior(cIn) <= Verific.opPrior(cTmp))) {
                        sbOut.append(" ").append(cTmp).append(" ");
                        sbStack.setLength(sbStack.length()-1);
                    } else {
                        sbOut.append(" ");
                        break;
                    }
                }
                sbOut.append(" ");
                sbStack.append(cIn);
            } else if ('(' == cIn) {
                sbStack.append(cIn);
            } else if (')' == cIn) {
                cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                while ('(' != cTmp) {
                    if (sbStack.length() < 1) {
                        throw new Exception("Invalid expression");
                    }
                    sbOut.append(" ").append(cTmp);
                    sbStack.setLength(sbStack.length()-1);
                    cTmp = sbStack.substring(sbStack.length()-1).charAt(0);
                }
                sbStack.setLength(sbStack.length()-1);
            } else {
                
                sbOut.append(cIn);
            }
        }
       
        while (sbStack.length() > 0) {
            sbOut.append(" ").append(sbStack.substring(sbStack.length()-1));
            sbStack.setLength(sbStack.length()-1);
        }

        return  sbOut.toString();
    }
}
class Verific {
    static boolean isOp(char c) {
        switch (c) {
            case '-':
            case '+':
            case '*':
            case '/':
                return true;
        }
        return false;
    }

 
    static byte opPrior(char op) {
        switch (op) {
            case '*':
            case '/':
                return 2;
        }
        return 1; 
    }
}
class Calculation {
    static double calculate(String sIn) throws Exception {
        double dA = 0, dB = 0;
        String sTmp;
        Deque<Double> stack = new ArrayDeque<Double>();
        StringTokenizer st = new StringTokenizer(sIn);
        while(st.hasMoreTokens()) {
            try {
                sTmp = st.nextToken().trim();
                if (1 == sTmp.length() && Verific.isOp(sTmp.charAt(0))) {
                    if (stack.size() < 2) {
                        throw new Exception("Bad numbers of data" + sTmp);
                    }
                    dB = stack.pop();
                    dA = stack.pop();
                    switch (sTmp.charAt(0)) {
                        case '+':
                            dA += dB;
                            break;
                        case '-':
                            dA -= dB;
                            break;
                        case '/':
                            dA /= dB;
                            break;
                        case '*':
                            dA *= dB;
                            break;
                        default:
                            throw new Exception("Bad operation" + sTmp);
                    }
                    stack.push(dA);
                } else {
                    dA = Double.parseDouble(sTmp);
                    stack.push(dA);
                }
            } catch (Exception e) {
                throw new Exception("Bad symbol in expression");
            }
        }

        if (stack.size() > 1) {
            throw new Exception("number of operators aren't match number of data ");
        }

        return stack.pop();
    }
}