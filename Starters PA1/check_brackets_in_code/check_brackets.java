import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;

class Bracket {
    Bracket(char type, int position) {
        this.type = type;
        this.position = position;
    }

    boolean Match(char c) {
        if (this.type == '[' && c == ']')
            return true;
        if (this.type == '{' && c == '}')
            return true;
        if (this.type == '(' && c == ')')
            return true;
        return false;
    }

    char type;
    int position;
}

class check_brackets {
    public static void main(String[] args) throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        String text = reader.readLine();

		boolean success = true;
		int position;
        Stack<Bracket> opening_brackets_stack = new Stack<Bracket>();
        for (position = 0; position < text.length(); ++position) {
            char next = text.charAt(position);

            if (next == '(' || next == '[' || next == '{') {
                // Process opening bracket, write your code here
				opening_brackets_stack.push(new Bracket(next, position));
            }

            if (next == ')' || next == ']' || next == '}') {
                // Process closing bracket, write your code here
				Bracket b;
				try {
					b = opening_brackets_stack.pop();
				} catch (Exception e){
					success = false;
					break;
				}
				if (!b.Match(next)) {
					success = false;
					break;
				}
            }
        }
		if (success){
			if (!opening_brackets_stack.empty()){
				success = false;
				position = opening_brackets_stack.pop().position;
			}
		}

        // Printing answer, write your code here
		
		if(success){
			System.out.println("Success");
		}else {
			System.out.println(String.valueOf(position + 1));
		}
    }
}
