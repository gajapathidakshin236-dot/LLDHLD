package com.company.patterns.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * INTERPRETER — represent a tiny language's grammar as a class hierarchy;
 * evaluating = walking the expression tree.
 *
 * Rarely hand-rolled at work (you'd use a parser library), but the SHAPE —
 * an Abstract Syntax Tree of Expression nodes each knowing how to evaluate
 * itself — underlies SQL engines, regex, Spring Expression Language, and
 * every compiler you'll touch. Composite applied to grammars.
 */
public class InterpreterDemo {
    public static void main(String[] args) {
        // AST for: (5 + 3) - (2 + 1)   built manually
        Expression expr = new Subtract(
                new Add(new NumberLiteral(5), new NumberLiteral(3)),
                new Add(new NumberLiteral(2), new NumberLiteral(1)));
        System.out.println("(5+3)-(2+1) = " + expr.interpret());

        // Same, parsed from postfix (RPN): "5 3 + 2 1 + -"
        Expression parsed = parsePostfix("5 3 + 2 1 + -");
        System.out.println("parsed RPN  = " + parsed.interpret());
    }

    /** Mini-parser: shows how source text becomes the tree. */
    static Expression parsePostfix(String src) {
        Deque<Expression> stack = new ArrayDeque<>();
        for (String tok : src.split("\\s+")) {
            switch (tok) {
                case "+": {
                    Expression r = stack.pop(), l = stack.pop();
                    stack.push(new Add(l, r));
                    break;
                }
                case "-": {
                    Expression r = stack.pop(), l = stack.pop();
                    stack.push(new Subtract(l, r));
                    break;
                }
                default:
                    stack.push(new NumberLiteral(Integer.parseInt(tok)));
            }
        }
        return stack.pop();
    }
}

/** ABSTRACT EXPRESSION */
interface Expression {
    int interpret();
}

/** TERMINAL expression — a leaf of the AST. */
class NumberLiteral implements Expression {
    private final int value;

    NumberLiteral(int value) { this.value = value; }

    @Override public int interpret() { return value; }
}

/** NON-TERMINAL expressions — inner nodes that combine children. */
class Add implements Expression {
    private final Expression left, right;

    Add(Expression left, Expression right) { this.left = left; this.right = right; }

    @Override public int interpret() { return left.interpret() + right.interpret(); }
}

class Subtract implements Expression {
    private final Expression left, right;

    Subtract(Expression left, Expression right) { this.left = left; this.right = right; }

    @Override public int interpret() { return left.interpret() - right.interpret(); }
}
