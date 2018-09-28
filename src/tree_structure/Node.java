/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tree_structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author trinhhaison
 */
public abstract class Node<T> implements Serializable{
    
    protected T data;
    protected Node<T> parent;
    protected List<Node<T>> children;
    protected static final long serialVersionUID = 3429535029278912108L;

    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
    }
    
    public abstract boolean isLeaf();
    
    public abstract List<Node<T>> listChildren(Set<T> existNode);
    
    public T getData() {
        return data;
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Node<T>> children) {
        this.children = children;
    }
    
    
}
