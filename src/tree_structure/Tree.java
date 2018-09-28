/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree_structure;

import tree_structure.Node;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author trinhhaison
 */
public class Tree<T> implements Serializable{
    
    protected Node<T> root;
    protected int depth;
    protected List<Node<T>> bottomNodes;
    protected Set<T> existData;
    protected static final long serialVersionUID = 6967278513397430722L;

    public Tree(Node<T> root) {
        this.root = root;
        depth = 1;
        bottomNodes = new ArrayList<>();
        bottomNodes.add(root);
        existData = new HashSet<>();
        existData.add(root.getData());
    }
    
    protected void expandNode(Node<T> node, int extraDepth, List<Node<T>> leaves, List<Node<T>> bottomNodes, Set<T> existData){
        if(extraDepth != 0){
            List<Node<T>> children = node.listChildren(existData);
            node.setChildren(children);
            for(Node child : children){
                System.out.println("Expand: " + child.getData().toString());
                if(child.isLeaf() == true){
                    leaves.add(child);
                }
                else{
                    expandNode(child, extraDepth - 1, leaves, bottomNodes, existData);
                }
            }
        }
        else{
            bottomNodes.add(node);
        }
    }
    
    public void expand(int extraDepth, List<Node<T>> leaves){
        List<Node<T>> copiedNodes = new ArrayList<>();
        copiedNodes.addAll(bottomNodes);
        bottomNodes = new ArrayList<>();
        for(Node<T> node : copiedNodes){
            expandNode(node, extraDepth, leaves, bottomNodes, existData);
        }
        depth += extraDepth;
    }

    public Node<T> getRoot() {
        return root;
    }

    public int getDepth() {
        return depth;
    }

    public List<Node<T>> getBottomNodes() {
        return bottomNodes;
    }

    public Set<T> getExistNodes() {
        return existData;
    }
    
    
}
