package com.videojj.videopublicapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.videojj.videocommon.constant.Constants;
import com.videojj.videoservice.encry.CommonRSAService;
import com.videojj.videoservice.service.ApiService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
public class MobileDataControllerTest {

    @Bean
    @Primary
    public ApiService apiService() throws Exception {
        ApiService apiService = Mockito.mock(ApiService.class);

        Mockito.when(apiService.mobileQuery(any(),any(),any())).thenReturn(null);
        JSONObject jsonRes = new JSONObject();
        jsonRes.put("resCode",Constants.SUCESSCODE);
        jsonRes.put("resMsg",Constants.COMMONSUCCESSMSG);
        Mockito.when(apiService.queryInfoByCondition(any(),any())).thenReturn(jsonRes);

        return apiService;
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CommonRSAService commonRSAService;

    private final static String TEXT_PLAIN_UTF_8 = "text/plain;charset=UTF-8";

//    @Autowired
//    private MockHttpSession session;// 注入模拟的http session
//
//    @Autowired
//    private MockHttpServletRequest request;// 注入模拟的http request\

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    }

    /**
     * 测试 异常情况
     */
    @Test
    public void testMobileModify_1() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", null);

        MvcResult result = mockMvc.perform(post("/videoos-api/api/mobileModify").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        JSONObject jsonObject = JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8"));
        Assert.assertEquals(jsonObject.getString("resCode"), Constants.FAILCODE);
        Assert.assertEquals(jsonObject.getString("resMsg"), "data 为空");
    }

    /**
     * 测试 异常情况
     */
    @Test
    public void testMobileModify_2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", commonRSAService.encrypt("{\"commonParam\":{\"VERSION\":\"1.0\"}}"));

        MvcResult result = mockMvc.perform(post("/videoos-api/api/mobileModify").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_PLAIN_UTF_8))
                .andReturn();
        JSONObject jsonObject = JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8"));
        Assert.assertEquals(jsonObject.getString("resCode"), Constants.FAILCODE);
        Assert.assertEquals(jsonObject.getString("resMsg"), "JSON字符串格式有误!");
    }

    /**
     * 测试 正常情况
     */
    @Test
    public void testMobileModify_3() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", commonRSAService.encrypt("{\"businessParam\":{\"userId\":\"userId1\",\"creativeId\":1,\"businessInfo\":\"businessInfoyyyyy\"},\"commonParam\":{\"VERSION\":\"1.0\"}}"));

        MvcResult result = mockMvc.perform(post("/videoos-api/api/mobileModify").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_PLAIN_UTF_8))
                .andReturn();
        Assert.assertEquals(JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8")).getString("resCode"), Constants.SUCESSCODE);
    }

    /**
     * 测试 异常情况
     */
    @Test
    public void testMobileQuery_1() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", null);

        MvcResult result = mockMvc.perform(post("/videoos-api/api/mobileQuery").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        JSONObject jsonObject = JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8"));
        Assert.assertEquals(jsonObject.getString("resCode"), Constants.FAILCODE);
        Assert.assertEquals(jsonObject.getString("resMsg"), "data 为空");
    }

    /**
     * 测试 异常情况
     */
    @Test
    public void testMobileQuery_2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", commonRSAService.encrypt("{\"commonParam\":{\"VERSION\":\"1.0\"}}"));

        MvcResult result = mockMvc.perform(post("/videoos-api/api/mobileQuery").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_PLAIN_UTF_8))
                .andReturn();
        JSONObject jsonObject = JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8"));
        Assert.assertEquals(jsonObject.getString("resCode"), Constants.FAILCODE);
        Assert.assertEquals(jsonObject.getString("resMsg"), "JSON字符串格式有误!");
    }

    /**
     * 测试 正常情况
     */
    @Test
    public void testMobileQuery_3() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", commonRSAService.encrypt("{\"businessParam\":{\"userId\":\"userId1\",\"creativeId\":1,\"businessInfo\":\"businessInfoyyyyy\"},\"commonParam\":{\"VERSION\":\"1.0\"}}"));

        MvcResult result = mockMvc.perform(post("/videoos-api/api/mobileQuery").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_PLAIN_UTF_8))
                .andReturn();
        Assert.assertEquals(JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8")).getString("resCode"), Constants.SUCESSCODE);
    }

    /**
     * 测试 异常情况
     */
    @Test
    public void testCommonQuery_1() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", null);

        MvcResult result = mockMvc.perform(post("/videoos-api/api/commonQuery").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();
        JSONObject jsonObject = JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8"));
        Assert.assertEquals(jsonObject.getString("resCode"), Constants.FAILCODE);
        Assert.assertEquals(jsonObject.getString("resMsg"), "data 为空");
    }

    /**
     * 测试 正常情况
     */
    @Test
    public void testCommonQuery_2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("data", commonRSAService.encrypt("{\"businessParam\":{\"userId\":\"userId1\",\"creativeId\":1,\"businessInfo\":\"businessInfoyyyyy\"},\"commonParam\":{\"VERSION\":\"1.0\"}}"));

        MvcResult result = mockMvc.perform(post("/videoos-api/api/commonQuery").content(JSONObject.toJSONString(map)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_PLAIN_UTF_8))
                .andReturn();
        Assert.assertEquals(JSONObject.parseObject(commonRSAService.publicDecrypt(JSONObject.parseObject(result.getResponse().getContentAsString()).getString("encryptData"),"utf-8")).getString("resCode"), Constants.SUCESSCODE);
    }
}
