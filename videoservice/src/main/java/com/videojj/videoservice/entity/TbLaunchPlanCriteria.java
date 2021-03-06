package com.videojj.videoservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TbLaunchPlanCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TbLaunchPlanCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public  Criteria andCreativeIdEquals(Integer creativeId){

            addCriterion("creative_id =", creativeId, "creativeId");
            return (Criteria) this;

        }

        public Criteria andLaunchPlanNameIsNull() {
            addCriterion("launch_plan_name is null");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameIsNotNull() {
            addCriterion("launch_plan_name is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameEqualTo(String value) {
            addCriterion("launch_plan_name =", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameNotEqualTo(String value) {
            addCriterion("launch_plan_name <>", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameGreaterThan(String value) {
            addCriterion("launch_plan_name >", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameGreaterThanOrEqualTo(String value) {
            addCriterion("launch_plan_name >=", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameLessThan(String value) {
            addCriterion("launch_plan_name <", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameLessThanOrEqualTo(String value) {
            addCriterion("launch_plan_name <=", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameLike(String value) {
            addCriterion("launch_plan_name like", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameNotLike(String value) {
            addCriterion("launch_plan_name not like", value, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameIn(List<String> values) {
            addCriterion("launch_plan_name in", values, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameNotIn(List<String> values) {
            addCriterion("launch_plan_name not in", values, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameBetween(String value1, String value2) {
            addCriterion("launch_plan_name between", value1, value2, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andLaunchPlanNameNotBetween(String value1, String value2) {
            addCriterion("launch_plan_name not between", value1, value2, "launchPlanName");
            return (Criteria) this;
        }

        public Criteria andInteractionIdEqualTo(Integer value) {
            addCriterion("interaction_id =", value, "interactionId");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }


        public Criteria andLaunchVideoIdIsNull() {
            addCriterion("launch_video_id is null");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdIsNotNull() {
            addCriterion("launch_video_id is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdEqualTo(String value) {
            addCriterion("launch_video_id =", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdNotEqualTo(String value) {
            addCriterion("launch_video_id <>", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdGreaterThan(String value) {
            addCriterion("launch_video_id >", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdGreaterThanOrEqualTo(String value) {
            addCriterion("launch_video_id >=", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdLessThan(String value) {
            addCriterion("launch_video_id <", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdLessThanOrEqualTo(String value) {
            addCriterion("launch_video_id <=", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdLike(String value) {
            addCriterion("launch_video_id like", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdNotLike(String value) {
            addCriterion("launch_video_id not like", value, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdIn(List<String> values) {
            addCriterion("launch_video_id in", values, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdNotIn(List<String> values) {
            addCriterion("launch_video_id not in", values, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdBetween(String value1, String value2) {
            addCriterion("launch_video_id between", value1, value2, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchVideoIdNotBetween(String value1, String value2) {
            addCriterion("launch_video_id not between", value1, value2, "launchVideoId");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeIsNull() {
            addCriterion("launch_time_type is null");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeIsNotNull() {
            addCriterion("launch_time_type is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeEqualTo(Byte value) {
            addCriterion("launch_time_type =", value, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeNotEqualTo(Byte value) {
            addCriterion("launch_time_type <>", value, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeGreaterThan(Byte value) {
            addCriterion("launch_time_type >", value, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("launch_time_type >=", value, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeLessThan(Byte value) {
            addCriterion("launch_time_type <", value, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeLessThanOrEqualTo(Byte value) {
            addCriterion("launch_time_type <=", value, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeIn(List<Byte> values) {
            addCriterion("launch_time_type in", values, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeNotIn(List<Byte> values) {
            addCriterion("launch_time_type not in", values, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeBetween(Byte value1, Byte value2) {
            addCriterion("launch_time_type between", value1, value2, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("launch_time_type not between", value1, value2, "launchTimeType");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartIsNull() {
            addCriterion("launch_date_start is null");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartIsNotNull() {
            addCriterion("launch_date_start is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartEqualTo(Date value) {
            addCriterion("launch_date_start =", value, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartNotEqualTo(Date value) {
            addCriterion("launch_date_start <>", value, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartGreaterThan(Date value) {
            addCriterion("launch_date_start >", value, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartGreaterThanOrEqualTo(Date value) {
            addCriterion("launch_date_start >=", value, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartLessThan(Date value) {
            addCriterion("launch_date_start <", value, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartLessThanOrEqualTo(Date value) {
            addCriterion("launch_date_start <=", value, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartIn(List<Date> values) {
            addCriterion("launch_date_start in", values, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartNotIn(List<Date> values) {
            addCriterion("launch_date_start not in", values, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartBetween(Date value1, Date value2) {
            addCriterion("launch_date_start between", value1, value2, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateStartNotBetween(Date value1, Date value2) {
            addCriterion("launch_date_start not between", value1, value2, "launchDateStart");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndIsNull() {
            addCriterion("launch_date_end is null");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndIsNotNull() {
            addCriterion("launch_date_end is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndEqualTo(Date value) {
            addCriterion("launch_date_end =", value, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndNotEqualTo(Date value) {
            addCriterion("launch_date_end <>", value, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndGreaterThan(Date value) {
            addCriterion("launch_date_end >", value, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndGreaterThanOrEqualTo(Date value) {
            addCriterion("launch_date_end >=", value, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndLessThan(Date value) {
            addCriterion("launch_date_end <", value, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndLessThanOrEqualTo(Date value) {
            addCriterion("launch_date_end <=", value, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndIn(List<Date> values) {
            addCriterion("launch_date_end in", values, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndNotIn(List<Date> values) {
            addCriterion("launch_date_end not in", values, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndBetween(Date value1, Date value2) {
            addCriterion("launch_date_end between", value1, value2, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchDateEndNotBetween(Date value1, Date value2) {
            addCriterion("launch_date_end not between", value1, value2, "launchDateEnd");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeIsNull() {
            addCriterion("launch_time is null");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeIsNotNull() {
            addCriterion("launch_time is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeEqualTo(String value) {
            addCriterion("launch_time =", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeNotEqualTo(String value) {
            addCriterion("launch_time <>", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeGreaterThan(String value) {
            addCriterion("launch_time >", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeGreaterThanOrEqualTo(String value) {
            addCriterion("launch_time >=", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeLessThan(String value) {
            addCriterion("launch_time <", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeLessThanOrEqualTo(String value) {
            addCriterion("launch_time <=", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeLike(String value) {
            addCriterion("launch_time like", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeNotLike(String value) {
            addCriterion("launch_time not like", value, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeIn(List<String> values) {
            addCriterion("launch_time in", values, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeNotIn(List<String> values) {
            addCriterion("launch_time not in", values, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeBetween(String value1, String value2) {
            addCriterion("launch_time between", value1, value2, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchTimeNotBetween(String value1, String value2) {
            addCriterion("launch_time not between", value1, value2, "launchTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeIsNull() {
            addCriterion("launch_len_time is null");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeIsNotNull() {
            addCriterion("launch_len_time is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeEqualTo(String value) {
            addCriterion("launch_len_time =", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeNotEqualTo(String value) {
            addCriterion("launch_len_time <>", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeGreaterThan(String value) {
            addCriterion("launch_len_time >", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeGreaterThanOrEqualTo(String value) {
            addCriterion("launch_len_time >=", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeLessThan(String value) {
            addCriterion("launch_len_time <", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeLessThanOrEqualTo(String value) {
            addCriterion("launch_len_time <=", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeLike(String value) {
            addCriterion("launch_len_time like", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeNotLike(String value) {
            addCriterion("launch_len_time not like", value, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeIn(List<String> values) {
            addCriterion("launch_len_time in", values, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeNotIn(List<String> values) {
            addCriterion("launch_len_time not in", values, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeBetween(String value1, String value2) {
            addCriterion("launch_len_time between", value1, value2, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andLaunchLenTimeNotBetween(String value1, String value2) {
            addCriterion("launch_len_time not between", value1, value2, "launchLenTime");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andExtraInfoIsNull() {
            addCriterion("extra_info is null");
            return (Criteria) this;
        }

        public Criteria andExtraInfoIsNotNull() {
            addCriterion("extra_info is not null");
            return (Criteria) this;
        }

        public Criteria andExtraInfoEqualTo(String value) {
            addCriterion("extra_info =", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotEqualTo(String value) {
            addCriterion("extra_info <>", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoGreaterThan(String value) {
            addCriterion("extra_info >", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoGreaterThanOrEqualTo(String value) {
            addCriterion("extra_info >=", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoLessThan(String value) {
            addCriterion("extra_info <", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoLessThanOrEqualTo(String value) {
            addCriterion("extra_info <=", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoLike(String value) {
            addCriterion("extra_info like", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotLike(String value) {
            addCriterion("extra_info not like", value, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoIn(List<String> values) {
            addCriterion("extra_info in", values, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotIn(List<String> values) {
            addCriterion("extra_info not in", values, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoBetween(String value1, String value2) {
            addCriterion("extra_info between", value1, value2, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andExtraInfoNotBetween(String value1, String value2) {
            addCriterion("extra_info not between", value1, value2, "extraInfo");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNull() {
            addCriterion("creator is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNotNull() {
            addCriterion("creator is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorEqualTo(String value) {
            addCriterion("creator =", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotEqualTo(String value) {
            addCriterion("creator <>", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThan(String value) {
            addCriterion("creator >", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThanOrEqualTo(String value) {
            addCriterion("creator >=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThan(String value) {
            addCriterion("creator <", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThanOrEqualTo(String value) {
            addCriterion("creator <=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLike(String value) {
            addCriterion("creator like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotLike(String value) {
            addCriterion("creator not like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorIn(List<String> values) {
            addCriterion("creator in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotIn(List<String> values) {
            addCriterion("creator not in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorBetween(String value1, String value2) {
            addCriterion("creator between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotBetween(String value1, String value2) {
            addCriterion("creator not between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedIsNull() {
            addCriterion("gmt_created is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedIsNotNull() {
            addCriterion("gmt_created is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedEqualTo(Date value) {
            addCriterion("gmt_created =", value, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedNotEqualTo(Date value) {
            addCriterion("gmt_created <>", value, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedGreaterThan(Date value) {
            addCriterion("gmt_created >", value, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_created >=", value, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedLessThan(Date value) {
            addCriterion("gmt_created <", value, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedLessThanOrEqualTo(Date value) {
            addCriterion("gmt_created <=", value, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedIn(List<Date> values) {
            addCriterion("gmt_created in", values, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedNotIn(List<Date> values) {
            addCriterion("gmt_created not in", values, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedBetween(Date value1, Date value2) {
            addCriterion("gmt_created between", value1, value2, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andGmtCreatedNotBetween(Date value1, Date value2) {
            addCriterion("gmt_created not between", value1, value2, "gmtCreated");
            return (Criteria) this;
        }

        public Criteria andModifierIsNull() {
            addCriterion("MODIFIER is null");
            return (Criteria) this;
        }

        public Criteria andModifierIsNotNull() {
            addCriterion("MODIFIER is not null");
            return (Criteria) this;
        }

        public Criteria andModifierEqualTo(String value) {
            addCriterion("MODIFIER =", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotEqualTo(String value) {
            addCriterion("MODIFIER <>", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierGreaterThan(String value) {
            addCriterion("MODIFIER >", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierGreaterThanOrEqualTo(String value) {
            addCriterion("MODIFIER >=", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierLessThan(String value) {
            addCriterion("MODIFIER <", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierLessThanOrEqualTo(String value) {
            addCriterion("MODIFIER <=", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierLike(String value) {
            addCriterion("MODIFIER like", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotLike(String value) {
            addCriterion("MODIFIER not like", value, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierIn(List<String> values) {
            addCriterion("MODIFIER in", values, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotIn(List<String> values) {
            addCriterion("MODIFIER not in", values, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierBetween(String value1, String value2) {
            addCriterion("MODIFIER between", value1, value2, "modifier");
            return (Criteria) this;
        }

        public Criteria andModifierNotBetween(String value1, String value2) {
            addCriterion("MODIFIER not between", value1, value2, "modifier");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNull() {
            addCriterion("gmt_modified is null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNotNull() {
            addCriterion("gmt_modified is not null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedEqualTo(Date value) {
            addCriterion("gmt_modified =", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotEqualTo(Date value) {
            addCriterion("gmt_modified <>", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThan(Date value) {
            addCriterion("gmt_modified >", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_modified >=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThan(Date value) {
            addCriterion("gmt_modified <", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThanOrEqualTo(Date value) {
            addCriterion("gmt_modified <=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIn(List<Date> values) {
            addCriterion("gmt_modified in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotIn(List<Date> values) {
            addCriterion("gmt_modified not in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedBetween(Date value1, Date value2) {
            addCriterion("gmt_modified between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotBetween(Date value1, Date value2) {
            addCriterion("gmt_modified not between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNull() {
            addCriterion("is_deleted is null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIsNotNull() {
            addCriterion("is_deleted is not null");
            return (Criteria) this;
        }

        public Criteria andIsDeletedEqualTo(String value) {
            addCriterion("is_deleted =", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotEqualTo(String value) {
            addCriterion("is_deleted <>", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThan(String value) {
            addCriterion("is_deleted >", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedGreaterThanOrEqualTo(String value) {
            addCriterion("is_deleted >=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThan(String value) {
            addCriterion("is_deleted <", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLessThanOrEqualTo(String value) {
            addCriterion("is_deleted <=", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedLike(String value) {
            addCriterion("is_deleted like", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotLike(String value) {
            addCriterion("is_deleted not like", value, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedIn(List<String> values) {
            addCriterion("is_deleted in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotIn(List<String> values) {
            addCriterion("is_deleted not in", values, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedBetween(String value1, String value2) {
            addCriterion("is_deleted between", value1, value2, "isDeleted");
            return (Criteria) this;
        }

        public Criteria andIsDeletedNotBetween(String value1, String value2) {
            addCriterion("is_deleted not between", value1, value2, "isDeleted");
            return (Criteria) this;
        }


    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}