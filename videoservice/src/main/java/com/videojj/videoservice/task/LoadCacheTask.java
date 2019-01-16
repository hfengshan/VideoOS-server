package com.videojj.videoservice.task;

import com.alibaba.fastjson.JSONArray;
import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.entity.TbLaunchPlanApiInfoExt;
import com.videojj.videoservice.enums.LaunchTimeTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.crazycake.shiro.RedisManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/12/4 下午6:43.
 * @Description:
 */
@Component
public class LoadCacheTask {


    @Resource
    private TbLaunchPlanMapper tbLaunchPlanMapper;

    @Resource
    private RedisManager redisManager;

    private final int expireTime = 90000;

    public void run() {

        List<TbLaunchPlanApiInfoExt> launchPlanApiList =  tbLaunchPlanMapper.selectByVideoId(null,null, DateUtil.getStartDate(DateUtil.addDays(new Date(),1)), LaunchTimeTypeEnum.VIDIO_TIME.getValue());

        if(CollectionUtils.isEmpty(launchPlanApiList)){

            return;
        }
        new Thread() {

            public void run() {

                String pre = DateUtil.toShortDateString(DateUtil.addDays(new Date(),1));

                Map<String,List<TbLaunchPlanApiInfoExt>> resultMap = new HashMap<>();

                //根据vidoId进行分组，然后再放入到缓存中
                launchPlanApiList.stream().forEach(k->{

                    if(CollectionUtils.isEmpty(resultMap.get(k.getLaunchVideoId()))){

                        List<TbLaunchPlanApiInfoExt> launchPlanList = new ArrayList<TbLaunchPlanApiInfoExt>();

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

                    redisManager.set(pre.concat(key).getBytes(),JSONArray.toJSONString(resultMap.get(key)).getBytes(),expireTime);
                }

            }
        }.start();

    }
}
