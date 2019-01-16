package com.videojj.videoservice.handler;

import com.videojj.videocommon.util.DateUtil;
import com.videojj.videoservice.task.AbstractPlanTask;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/31 下午5:47.
 * @Description:
 */
@Service
public class TimingService extends AbstractPlanTask{


    @Override
    protected Date getDate() {

        Date nextday = DateUtil.addDays(DateUtil.getStartDate(new Date()),1);

        return nextday;
    }
}
