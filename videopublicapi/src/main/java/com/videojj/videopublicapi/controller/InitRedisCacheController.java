package com.videojj.videopublicapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.dto.base.BaseResponseDTO;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import com.videojj.videoservice.enums.LaunchTimeTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/12/4 下午7:05.
 * @Description:
 */
@Controller
public class InitRedisCacheController {

    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Resource
    private RedisManager redisManager;

    private static Logger log = LoggerFactory.getLogger("InitRedisCacheController");

    @RequestMapping("/videoos-api/initRedis")
    public @ResponseBody
    BaseResponseDTO initRedis(){

        BaseResponseDTO res = null;

        Date endDate = DateUtil.getEndDate(new Date());

        long dif = DateUtil.getDifferenceMillis(new Date(),endDate);

        /**查询今天的所有的数据*/
        List<TbLaunchPlanApiInfoExt> launchPlanApiList =  tbLaunchPlanMapper.selectByVideoId(null,null, DateUtil.getStartDate(new Date()), LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        if(CollectionUtils.isEmpty(launchPlanApiList)){

            res = new BaseResponseDTO();

            log.info("InitRedisCacheController.initRedis==> there is no data need to load to cache");

            res.setResCode(Constants.SUCESSCODE);

            res.setResMsg(Constants.COMMONSUCCESSMSG);

            return res;
        }

        /**初始化是加载当天的*/
        String pre = DateUtil.toShortDateString(new Date());

        Map<String,List<TbLaunchPlanApiInfoExt>> resultMap = new HashMap<>();

        launchPlanApiList.stream().forEach(k->{

            if(CollectionUtils.isEmpty(resultMap.get(k.getLaunchVideoId()))){

                List<TbLaunchPlanApiInfoExt> launchPlanList = new ArrayList<>();

                launchPlanList.add(k);

                resultMap.put(k.getLaunchVideoId(),launchPlanList);
            }else{

                resultMap.get(k.getLaunchVideoId()).add(k);
            }

        });

        Set<String> keyset = resultMap.keySet();

        Iterator<String> iter = keyset.iterator();

        while(iter.hasNext()){

            String key = iter.next();

            redisManager.set(pre.concat(key).getBytes(),JSONArray.toJSONString(resultMap.get(key)).getBytes(),(int) dif);
        }

        res = new BaseResponseDTO();

        res.setResCode(Constants.SUCESSCODE);

        res.setResMsg(Constants.COMMONSUCCESSMSG);

        return res;
    }


}
