package com.videojj.videoservice.bo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Tree<T> {
    /**
     * 节点ID
     */
    private String nodeId;
    /**
     * 图标
     */
    private String icon;
    /**
     * url
     */
    private String path;
    /**
     * 显示节点文本
     */
    private String name;

    private String parentId;

    private Boolean hasParent;

    private String external;

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    /**
     * 是否有子节点
     */
    private boolean hasChildren = false;

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Boolean getHasParent() {
        return hasParent;
    }

    public void setHasParent(Boolean hasParent) {
        this.hasParent = hasParent;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 节点的子节点
     */
    private List<Tree<T>> children = new ArrayList<Tree<T>>();

    private List<Tree<T>> items = new ArrayList<Tree<T>>();

    public List<Tree<T>> getItems() {
        return items;
    }

    public void setItems(List<Tree<T>> items) {
        this.items = items;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Tree() {
        super();
    }

    @Override
    public String toString() {

        return JSON.toJSONString(this);
    }

}