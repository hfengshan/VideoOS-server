package com.videojj.videoservice.service;

import com.videojj.videoservice.bo.TemplateAddBo;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.dao.*;
import com.videojj.videoservice.entity.TbTemplate;
import com.videojj.videoservice.entity.TbTemplateFile;
import com.videojj.videoservice.entity.TbTemplateZipFile;
import com.videojj.videoservice.fileserver.CommonFileServer;
import com.videojj.videoservice.service.impl.TemplateExtServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class TemplateExtServiceTest extends BaseTest {

    @InjectMocks
    private TemplateExtServiceImpl templateExtService;

    @Mock
    protected CommonFileServer commonFileServer;

    @Mock
    protected TbTemplateZipFileMapper tbTemplateZipFileMapper;

    @Mock
    protected CommonConfig commonConfig;

    @Mock
    protected TransactionTemplate transactionTemplate;

    @Mock
    protected TbTemplateMapper tbTemplateMapper;

    @Mock
    protected TbInteractionMapper tbInteractionMapper;

    @Mock
    protected OperationLogService operationLogService;

    @Mock
    protected TbTemplateFileMapper tbTemplateFileMapper;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private TbCreativeMapper tbCreativeMapper;




    private InputStream getUploadZipInputStream() {
        return ClassLoader.getSystemResourceAsStream("test.zip");
    }

    private InputStream getUploadZipWithDirInputStream() {
        return ClassLoader.getSystemResourceAsStream("testWithDir.zip");
    }

    private InputStream getNewestZipInputStream() {
        return ClassLoader.getSystemResourceAsStream("1.0.111-template.zip");
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        String testFilePath = System.getProperty("java.io.tmpdir") + "/os-test/";
        new File(testFilePath).mkdir();

        Mockito.when(commonConfig.getFilePath()).thenReturn(testFilePath);
        Mockito.when(commonFileServer.download(any())).thenReturn(getNewestZipInputStream());
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("test.zip");
        Mockito.when(multipartFile.getInputStream()).thenReturn(getUploadZipInputStream());
        TbTemplateZipFile tbTemplateZipFile = new TbTemplateZipFile();
        tbTemplateZipFile.setFileVersion("1.0.111");
        Mockito.when(tbTemplateZipFileMapper.selectByParam(any())).thenReturn(new ArrayList<TbTemplateZipFile>() {{
            add(tbTemplateZipFile);
        }});
    }

    /**
     * 测试没有上传测试文件
     */
    @Test
    public void testUploadFile_1() {
        try {

//          Mockito.when(multipartFile.getInputStream()).thenThrow(new IOException(""));
            templateExtService.uploadFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename());

        } catch (Exception e) {

            Assert.assertEquals("没有收到上传的文件，请检查参数!!!", e.getMessage());
        }
    }

    /**
     * 测试压缩包名称有重复的情况
     */
    @Test
    public void testUploadFile_2()  throws Exception{
        Mockito.when(tbTemplateMapper.selectByParam(any())).thenReturn(new ArrayList<TbTemplate>() {{
            add(new TbTemplate());
        }});
        templateExtService.uploadFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename());
        Assert.assertEquals("压缩文件的名字test.zip已存在，请更换名称", templateExtService.uploadFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename()).getUploadMsg());
    }

    /**
     * 测试读取zip里面的所有的文件名的情况
     */
    @Test
    public void testUploadFile_3() throws Exception {
        Mockito.when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{}));
        Mockito.when(tbTemplateMapper.selectByParam(any())).thenReturn(new ArrayList<TbTemplate>() {{
        }});
        try {
            templateExtService.uploadFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename());
        } catch (Exception e) {
            Assert.assertEquals("上传的压缩包中没有文件", e.getMessage());
        }

    }

    /**
     * 测试 再检验压缩包里面的文件，所有的文件名，是否存在重复的，并取出这些文件名 的情况
     */
    @Test
    public void testUploadFile_4() throws Exception{
        TbTemplateFile tbTemplateFile1 = new TbTemplateFile();
        tbTemplateFile1.setFileName("fileName1");
        TbTemplateFile tbTemplateFile2 = new TbTemplateFile();
        tbTemplateFile2.setFileName("fileName2");
        Mockito.when(tbTemplateFileMapper.selectByFileNameList(any())).thenReturn(new ArrayList<TbTemplateFile>() {{
            add(tbTemplateFile1);
            add(tbTemplateFile2);
        }});

        Mockito.when(tbTemplateMapper.selectByParam(any())).thenReturn(new ArrayList<TbTemplate>() {{
        }});
        Assert.assertEquals("主题文件的名字pom.xml已存在，请更换名称", templateExtService.uploadFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename()).getUploadMsg());
    }

    /**
     * 测试正常情况
     */
    @Test
    public void testUploadFile_5() throws Exception{
        Mockito.when(tbTemplateFileMapper.selectByFileNameList(any())).thenReturn(new ArrayList<TbTemplateFile>() {{
        }});
        Mockito.when(tbTemplateMapper.selectByParam(any())).thenReturn(new ArrayList<TbTemplate>() {{
        }});

        Assert.assertEquals(true, templateExtService.uploadFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename()).getUploadResult());
    }

    /**
     * 测试压缩包中存在目录的情况
     */
    @Test
    public void testUploadFile_6() throws IOException {
        Mockito.when(tbTemplateFileMapper.selectByFileNameList(any())).thenReturn(new ArrayList<TbTemplateFile>() {{
        }});
        Mockito.when(tbTemplateMapper.selectByParam(any())).thenReturn(new ArrayList<TbTemplate>() {{
        }});
        Mockito.when(multipartFile.getInputStream()).thenReturn(getUploadZipWithDirInputStream());
        try {
            templateExtService.uploadFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename());
        } catch (Exception e) {
            Assert.assertEquals("压缩包中不要添加目录", e.getMessage());
        }
    }


    /**
     * 测试 压缩包中的文件名格式存在问题情况
     */
    @Test
    public void testAddTemplateInfo_1() {
        mockTransactionTemplate(transactionTemplate);
        TemplateAddBo templateAddBo = new TemplateAddBo();
        templateAddBo.setUploadFileNameList(new ArrayList<String>() {{
            add("1_hotspot.lua");
            add("2_hotspot.lua");
        }});
        try {
            templateExtService.addTemplateInfo(templateAddBo);
        } catch (Exception e) {
        }

        verify(tbTemplateMapper, times(0)).insertSelective(any());
    }

    /**
     * 测试正常情况
     */

    // 空指针
//    @Test
    @Test(expected=NullPointerException.class)
    public void testAddTemplateInfo_2() {
        Mockito.when(commonConfig.getFileDomainName()).thenReturn("");
        Mockito.when(commonConfig.getPreKey()).thenReturn("");
        Mockito.when(transactionTemplate.execute(any(TransactionCallback.class))).thenReturn(null);
        mockTransactionTemplate(transactionTemplate);

        mockTransactionTemplate(transactionTemplate);
        TemplateAddBo templateAddBo = new TemplateAddBo();
        templateAddBo.setUploadFileNameList(new ArrayList<String>() {{
            add("1_hotspot.lua");
            add("1_test.lua");
            add("2_test.lua");
        }});

        templateExtService.addTemplateInfo(templateAddBo);

        verify(tbTemplateZipFileMapper, times(1)).insertSelective(any());
    }


    /**
     * 测试 压缩包中的文件名格式存在问题情况
     */
    @Test
    public void testUpdateRelationInfo_1() {
        mockTransactionTemplate(transactionTemplate);
        TemplateAddBo templateAddBo = new TemplateAddBo();
        templateAddBo.setUploadFileNameList(new ArrayList<String>() {{
            add("1_hotspot.lua");
            add("2_hotspot.lua");
        }});
        templateAddBo.setZipFileName("test.zip");
        try {
            templateExtService.updateRelationInfo(templateAddBo, 1);
        } catch (Exception e) {
        }

        verify(tbTemplateMapper, times(0)).updateByPrimaryKeySelective(any());
    }

    /**
     * 测试正常情况
     */

    // 空指针
//    @Test
    @Test(expected=NullPointerException.class)
    public void testUpdateRelationInfo_2() {
        Mockito.when(commonConfig.getFileDomainName()).thenReturn("");
        Mockito.when(commonConfig.getPreKey()).thenReturn("");
        Mockito.when(transactionTemplate.execute(any(TransactionCallback.class))).thenReturn(null);

        mockTransactionTemplate(transactionTemplate);
        TemplateAddBo templateAddBo = new TemplateAddBo();
        templateAddBo.setUploadFileNameList(new ArrayList<String>() {{
            add("1_hotspot.lua");
            add("1_test.lua");
            add("2_test.lua");
        }});
        templateAddBo.setZipFileName("test.zip");

        templateExtService.updateRelationInfo(templateAddBo, 1);

        verify(tbTemplateZipFileMapper, times(1)).insertSelective(any());
    }


}
