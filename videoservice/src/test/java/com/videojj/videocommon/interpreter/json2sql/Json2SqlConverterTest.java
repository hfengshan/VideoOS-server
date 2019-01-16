package com.videojj.videocommon.interpreter.json2sql;

import com.videojj.videocommon.exception.ServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Json2SqlConverterTest {

    /**
     * 测试异常：缺失action
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_1() throws Exception {
        String dataString = "{\n" +
                "\"condition\": [{\n" +
                "\"key\": \"key1\",\n" +
                "\"value\": \"value1\",\n" +
                "\"operator\": \"equal\"\n" +
                "}]\n" +
                "}";

        String eMessage = null;
        try{
            Json2SqlConverter.convert4TbMobileDataDetail(dataString);
        }catch (ServiceException e){
            eMessage = e.getMessage();
        }
        Assert.assertEquals(eMessage,"后端校验失败，JSON数据格式不正确，请检查action字段！");
    }

    /**
     * 测试异常：缺失action
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_2() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"\",\n" +
                "\"condition\": [{\n" +
                "\"key\": \"key1\",\n" +
                "\"value\": \"value1\",\n" +
                "\"operator\": \"equal\"\n" +
                "}]\n" +
                "}";

        String eMessage = null;
        try{
            Json2SqlConverter.convert4TbMobileDataDetail(dataString);
        }catch (ServiceException e){
            eMessage = e.getMessage();
        }
        Assert.assertEquals(eMessage,"后端校验失败，JSON数据格式不正确，请检查action字段！");
    }

    /**
     * 测试正常：缺失condition，查询全部
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_3() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"count\"\n" +
                "}";
        Assert.assertEquals(Json2SqlConverter.convert4TbMobileDataDetail(dataString),
                "SELECT COUNT(*) FROM tb_mobile_data_detail");
    }

    /**
     * 测试正常：condition为空，查询全部
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_4() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"count\",\n" +
                "\"condition\": []\n" +
                "}";
        Assert.assertEquals(Json2SqlConverter.convert4TbMobileDataDetail(dataString),
                "SELECT COUNT(*) FROM tb_mobile_data_detail");
    }

    /**
     * 测试异常：condition的operator缺失
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_5() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"count\",\n" +
                "\"condition\": [{\n" +
                "\"key\": \"key1\",\n" +
                "\"value\": \"value1\"\n" +
                "}]\n" +
                "}";

        String eMessage = null;
        try{
            Json2SqlConverter.convert4TbMobileDataDetail(dataString);
        }catch (ServiceException e){
            eMessage = e.getMessage();
        }
        Assert.assertEquals(eMessage,"后端校验失败，JSON数据格式不正确，请检查operator字段！");
    }

    /**
     * 测试异常：condition的key缺失
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_6() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"count\",\n" +
                "\"condition\": [{\n" +
                "\"value\": \"value1\",\n" +
                "\"operator\": \"equal\"\n" +
                "}]\n" +
                "}";

        String eMessage = null;
        try{
            Json2SqlConverter.convert4TbMobileDataDetail(dataString);
        }catch (ServiceException e){
            eMessage = e.getMessage();
        }
        Assert.assertEquals(eMessage,"后端校验失败，JSON数据格式不正确，请检查key字段！");
    }

    /**
     * 测试异常：condition的value缺失
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_7() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"count\",\n" +
                "\"condition\": [{\n" +
                "\"key\": \"key1\",\n" +
                "\"operator\": \"equal\"\n" +
                "}]\n" +
                "}";

        String eMessage = null;
        try{
            Json2SqlConverter.convert4TbMobileDataDetail(dataString);
        }catch (ServiceException e){
            eMessage = e.getMessage();
        }
        Assert.assertEquals(eMessage,"后端校验失败，JSON数据格式不正确，请检查value字段！");
    }

    /**
     * 测试正常：condition有1条
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_8() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"count\",\n" +
                "\"condition\": [{\n" +
                "\"key\": \"key1\",\n" +
                "\"value\": \"value1\",\n" +
                "\"operator\": \"equal\"\n" +
                "}]\n" +
                "}";

          Assert.assertEquals(  Json2SqlConverter.convert4TbMobileDataDetail(dataString),"SELECT COUNT(*) FROM tb_mobile_data_detail WHERE (data_key = 'key1' AND data_value = 'value1')");
    }

    /**
     * 测试正常：condition有3条
     *
     * @throws Exception
     */
    @Test
    public void testConvert4TbMobileDataDetail_9() throws Exception {
        String dataString = "{\n" +
                "\"action\": \"count\",\n" +
                "\"condition\": [{\n" +
                "\"key\": \"key1\",\n" +
                "\"value\": \"value1\",\n" +
                "\"operator\": \"equal\"\n" +
                "},{\n" +
                "\"key\": \"key2\",\n" +
                "\"value\": \"value2\",\n" +
                "\"operator\": \"equal\"\n" +
                "},{\n" +
                "\"key\": \"key3\",\n" +
                "\"value\": \"value3\",\n" +
                "\"operator\": \"equal\"\n" +
                "}]\n" +
                "}";

        Assert.assertEquals(  Json2SqlConverter.convert4TbMobileDataDetail(dataString),"SELECT COUNT(*) FROM tb_mobile_data_detail WHERE (((data_key = 'key1' AND data_value = 'value1') OR (data_key = 'key2' AND data_value = 'value2')) OR (data_key = 'key3' AND data_value = 'value3'))");
    }

}
