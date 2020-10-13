package pers.byx.parser.sample;

import pers.byx.parser.core.Lazy;
import pers.byx.parser.core.Parser;

import static pers.byx.parser.core.Parsers.*;

class TreeNode
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

public class BinaryTreeParsing
{
    public static TreeNode buildTree(String input)
    {
        Parser digit = range('0', '9');
        Parser lp = ch('(');
        Parser rp = ch(')');
        Parser comma = ch(',');
        Parser nul = literal("null");

        Parser integer = digit.oneOrMore();
        Lazy expr = lazy();
        expr.set(or(nul,
                    lp.concat(integer).concat(comma).concat(expr).concat(comma).concat(expr).concat(rp)));
        Parser p = expr.end();

        return null;
    }

    public static void main(String[] args)
    {

    }
}
