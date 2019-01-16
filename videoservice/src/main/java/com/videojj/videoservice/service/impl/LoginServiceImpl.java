package com.videojj.videoservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.bo.NodeBo;
import com.videojj.videoservice.bo.RoleAndNodeBo;
import com.videojj.videoservice.bo.Tree;
import com.videojj.videoservice.dao.TbNodeMapper;
import com.videojj.videoservice.dao.TbRoleMapper;
import com.videojj.videoservice.entity.*;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.service.LoginService;
import com.videojj.videoservice.util.TreeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author @videopls.com
 * Created by  on 2018/7/24 下午5:43.
 * @Description:
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static Logger log = LoggerFactory.getLogger("LoginServiceImpl");


    @Resource
    private TbRoleMapper tbRoleMapper;

    @Resource
    private TbNodeMapper tbNodeMapper;

    @Override
    public RoleAndNodeBo getAuthInfoByUsername(String username) {

        TbNode firstPageNode = new TbNode();
        firstPageNode.setNodeName("首页");
        firstPageNode.setPath("home");

        firstPageNode.setIcon("icon-home");

        firstPageNode.setParentId(0);

        firstPageNode.setExtraInfo("true");

        firstPageNode.setId(999999);

        RoleAndNodeBo roleAndNodeBo = new RoleAndNodeBo();

        TbRole tbRole = tbRoleMapper.selectRoleInfoByUsername(username);

        if(null == tbRole){

            throw new ServiceException("该用户或者角色已经不存在");
        }

        roleAndNodeBo.setRoleId(tbRole.getId());

        roleAndNodeBo.setRoleName(tbRole.getRoleName());

        TbNodeCriteria nodeParam = new TbNodeCriteria();

        TbNodeCriteria.Criteria nodeCriteria = nodeParam.createCriteria();

        nodeCriteria.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        /**如果权限里面是空，那么就是没有任何权限，那么就配置一下只能查看一些没有节点的菜单，当然，这种可能性比较小*/

        if(StringUtils.isNotEmpty(tbRole.getAuths())) {

            String[] leafNode = tbRole.getAuths().split(",");

            List<Integer> idList = new ArrayList<>();

            Arrays.asList(leafNode).stream().forEach(x -> idList.add(Integer.parseInt(x)));

            nodeCriteria.andIdIn(idList);
        }else{

            nodeCriteria.andIsLeafEqualTo((byte) 0);

        }

        List<TbNode> leafNodeList = tbNodeMapper.selectByParam(nodeParam);
        //将上面的路径全部都取出来
        String nodesStr = leafNodeList.stream().map(TbNode::getParentPath).collect(Collectors.joining(", "));

        List<Integer> nodeIdList = new ArrayList<>();

        Arrays.asList(nodesStr.replace(" ", "").split(",")).stream().forEach(x -> nodeIdList.add(Integer.parseInt(x.trim())));

        List<Integer> pathNodeIdList = nodeIdList.stream().distinct().collect(
                Collectors.toList());

        TbNodeCriteria fnodeParam = new TbNodeCriteria();

        TbNodeCriteria.Criteria fnodeCriteria = fnodeParam.createCriteria();

        fnodeCriteria.andIsDeletedEqualTo(IsDeletedEnum.NO.getValue());

        fnodeCriteria.andIdIn(pathNodeIdList);

        List<TbNode> pathNodeList = tbNodeMapper.selectByParam(fnodeParam);

        pathNodeList.addAll(leafNodeList);

        pathNodeList.add(firstPageNode);

        List<Tree<NodeBo>> trees = new ArrayList<>();

        buildTrees(trees, pathNodeList);

        Tree<NodeBo> treeNode = TreeUtils.build(trees);

        Gson gson = new Gson();

        String treeNodeStr = gson.toJson(treeNode);

        roleAndNodeBo.setNodeInfo(treeNodeStr);

        return roleAndNodeBo;
    }

    private void buildTrees(List<Tree<NodeBo>> trees, List<TbNode> pathNodeList) {

        pathNodeList.forEach(node -> {
            Tree<NodeBo> tree = new Tree<>();
            tree.setNodeId(node.getId().toString());
            tree.setParentId(node.getParentId().toString());
            tree.setName(node.getNodeName());
            tree.setIcon(node.getIcon());
            tree.setPath(node.getPath());
            tree.setExternal(node.getExtraInfo());

            trees.add(tree);
        });
    }
}
