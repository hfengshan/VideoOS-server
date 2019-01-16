package com.videojj.videoservice.util;

import com.videojj.videoservice.bo.Tree;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils {
	/**这个方法的话，根节点是固定的。表里面并没有存储根节点，因此我们这边也不存储根节点。*/
	public static <T> Tree<T> build(List<Tree<T>> nodes) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<>();
		nodes.forEach(children -> {
			String pid = children.getParentId();
			if (pid == null || "0".equals(pid)) {
				topNodes.add(children);
				return;
			}
			for (Tree<T> parent : nodes) {
				String id = parent.getNodeId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setHasChildren(true);
					return;
				}
			}
		});

		Tree<T> root = new Tree<>();
		root.setNodeId("0");
//		root.setParentId();
//		root.setHasParent(false);
//		root.setHasChildren(true);
		root.setItems(topNodes);
//		root.setName("OS开源项目");

		return root;
	}

	public static <T> List<Tree<T>> buildList(List<Tree<T>> nodes, String idParam) {
		if (nodes == null) {
			return null;
		}
		List<Tree<T>> topNodes = new ArrayList<>();
		nodes.forEach(children -> {
			String pid = children.getParentId();
			if (pid == null || idParam.equals(pid)) {
				topNodes.add(children);
				return;
			}
			nodes.forEach(parent -> {
				String id = parent.getNodeId();
				if (id != null && id.equals(pid)) {
					parent.getChildren().add(children);
					children.setHasParent(true);
					parent.setHasChildren(true);
				}
			});
		});
		return topNodes;
	}
}