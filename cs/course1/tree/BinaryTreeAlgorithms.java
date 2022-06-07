package ru.vsu.cs.course1.tree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTreeAlgorithms {

    @FunctionalInterface
    public interface Visitor<T> {
        /**
         * "Посещение" значения
         *
         * @param value Значение, которое "посещаем"
         * @param level Уровень дерева/поддерева, на котором находится данное значение
         */
        void visit(T value, int level);
    }


    /**
     * ссанный обход этой мерзости (дерева) с поиском минимального левого потомка ( май версион)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     */
    public static BinaryTree.TreeNode<Integer> preOrderVisit(BinaryTree.TreeNode<Integer> treeNode) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 3-мя параметрами)
        class Inner {
            BinaryTree.TreeNode<Integer> preOrderVisit(BinaryTree.TreeNode<Integer> node, BinaryTree.TreeNode<Integer> min) {
                if (node == null) {
                    return min;
                }
                if (node.getLeft() != null && compareTreeNodes(min, node.getLeft()) > 0) {
                    min = node.getLeft();
                }
                BinaryTree.TreeNode<Integer> leftMin = preOrderVisit(node.getLeft(), min);
                BinaryTree.TreeNode<Integer> rightMin = preOrderVisit(node.getRight(), min);
                int n = compareTreeNodes(leftMin, rightMin);
                if (n > 0) {
                    return rightMin;
                } else return leftMin;
            }
        }
        if (treeNode != null) {
            if (treeNode.getLeft() != null) {
                return new Inner().preOrderVisit(treeNode, treeNode.getLeft());
            }
        }
        return null;
    }

    public static int compareTreeNodes(BinaryTree.TreeNode<Integer> first, BinaryTree.TreeNode<Integer> second) {
        return first.getValue() - second.getValue();
    }

    /**
     * Обход поддерева с вершиной в данном узле в виде итератора в
     * симметричном/поперечном/центрированном/LNR порядке (предполагается, что в
     * процессе обхода дерево не меняется)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @return Итератор
     */
    public static <T> Iterable<T> inOrderValues(BinaryTree.TreeNode<T> treeNode) {
        return () -> {
            Stack<BinaryTree.TreeNode<T>> stack = new Stack<>();
            BinaryTree.TreeNode<T> node = treeNode;
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }

            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T next() {
                    BinaryTree.TreeNode<T> node = stack.pop();
                    T result = node.getValue();
                    if (node.getRight() != null) {
                        node = node.getRight();
                        while (node != null) {
                            stack.push(node);
                            node = node.getLeft();
                        }
                    }
                    return result;
                }
            };
        };
    }

    /**
     * Представление дерева в виде строки в скобочной нотации
     *
     * @param treeNode Узел поддерева, которое требуется представить в виже скобочной нотации
     * @return дерево в виде строки
     */
    public static <T> String toBracketStr(BinaryTree.TreeNode<T> treeNode) {
        // данный класс нужен только для того, чтобы "спрятать" его метод (c 2-мя параметрами)
        class Inner {
            void printTo(BinaryTree.TreeNode<T> node, StringBuilder sb) {
                if (node == null) {
                    return;
                }
                sb.append(node.getValue());
                if (node.getLeft() != null || node.getRight() != null) {
                    sb.append(" (");
                    printTo(node.getLeft(), sb);
                    if (node.getRight() != null) {
                        sb.append(", ");
                        printTo(node.getRight(), sb);
                    }
                    sb.append(")");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        // класс приходится создавать, т.к. статические методы в таких класс не поддерживаются
        new Inner().printTo(treeNode, sb);

        return sb.toString();
    }
}
