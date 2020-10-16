package pers.byx.parser.sample;

import pers.byx.parser.core.Lazy;
import pers.byx.parser.core.ParseResult;
import pers.byx.parser.core.Parser;

import java.util.Stack;
import java.util.function.Consumer;

import static pers.byx.parser.core.Parsers.*;

public class BinaryTreeParsing
{
    public static class TreeNode
    {
        private int val;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(int val)
        {
            this.val = val;
            this.left = null;
            this.right = null;
        }

        public TreeNode(int val, TreeNode left, TreeNode right)
        {
            this.val = val;
            this.left = left;
            this.right = right;
        }

        public int getVal()
        {
            return val;
        }

        public void setVal(int val)
        {
            this.val = val;
        }

        public TreeNode getLeft()
        {
            return left;
        }

        public void setLeft(TreeNode left)
        {
            this.left = left;
        }

        public TreeNode getRight()
        {
            return right;
        }

        public void setRight(TreeNode right)
        {
            this.right = right;
        }
    }

    public static TreeNode buildTree(String input)
    {
        Stack<TreeNode> stack = new Stack<>();

        Consumer<ParseResult> pushNull = r -> stack.push(null);

        Consumer<ParseResult> pushInteger = r ->
        {
            int val = Integer.parseInt(r.recognized().toString());
            stack.push(new TreeNode(val));
        };

        Consumer<ParseResult> combine = r ->
        {
            TreeNode right = stack.pop();
            TreeNode left = stack.pop();
            TreeNode root = stack.pop();
            root.setLeft(left);
            root.setRight(right);
            stack.push(root);
        };

        Parser digit = range('0', '9');
        Parser lp = ch('(');
        Parser rp = ch(')');
        Parser comma = ch(',');

        Parser integer = digit.oneOrMore();
        Lazy expr = lazy();
        expr.set(or(integer.callback(pushInteger).concat(lp).concat(expr).concat(comma).concat(expr).concat(rp).callback(combine),
                    integer.callback(pushInteger),
                    empty.callback(pushNull)));
        Parser p = expr.end();

        ParseResult result = p.parse(input);
        if (!result.isSuccess()) return null;

        return stack.pop();
    }

    public static void main(String[] args)
    {
        TreeNode t1 = buildTree("25");
        TreeNode t2 = buildTree("3(,)");
        TreeNode t3 = buildTree("12(34,56)");
        TreeNode t4 = buildTree("12(,56)");
        TreeNode t5 = buildTree("12(34,)");
        TreeNode t6 = buildTree("1(2(4,5),3(6,7))");
        TreeNode t7 = buildTree("1(,2(,3(,4)))");
    }
}
