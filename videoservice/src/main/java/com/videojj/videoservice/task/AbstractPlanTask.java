package com.videojj.videoservice.task;

import com.videojj.videoservice.bo.LaunchPlanTaskBo;
import com.videojj.videoservice.dao.TbLaunchPlanMapper;
import com.videojj.videoservice.entity.TbLaunchPlan;
import com.videojj.videoservice.handler.SendEmqService;
import com.videojj.videoservice.util.JsonUtil;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/31 下午5:22.
 * @Description:
 */
public abstract class AbstractPlanTask {

    @Autowired
    protected TbLaunchPlanMapper tbLaunchPlanMapper;

    @Autowired
    protected SendEmqService sendEmqService;

    @Resource
    protected HandleTimeAndSendEmqHelper helper;

    private static org.slf4j.Logger log = LoggerFactory.getLogger("AbstractPlanTask");

    public void run() {

        Date date = getDate();

        // 1、查询出所有的投放日期范围内的
        List<TbLaunchPlan> planList = getAllData(date);

        // 2、将今天的日期，去掉时分秒，拼凑上投放时间（投放时间是逗号分割的多个），就是实际的投放时间
        addQuartz(planList, date);

    }

    protected abstract Date getDate();

    public List<TbLaunchPlan> getAllData(Date date) {
        return tbLaunchPlanMapper.selectBjTimeByDate(date);
    }

    public void addQuartz(List<TbLaunchPlan> launchPlanList, Date today) {

        if (CollectionUtils.isEmpty(launchPlanList)) {

            return;
        }

        List<LaunchPlanTaskBo> launchPlanTasks = new ArrayList<>();

        /**因为每个计划有多个投放时间 先循环计划，再循环投放时间*/
        for (TbLaunchPlan tbLaunchPlan : launchPlanList) {
            //TODO 此处要更改，拆分时间

            List<String> launchTimeList = JsonUtil.parseLaunchTimeString(tbLaunchPlan.getLaunchTime());

            helper.handle(today, launchTimeList, tbLaunchPlan);

        }

    }


}
