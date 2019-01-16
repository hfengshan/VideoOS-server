package com.videojj.videoservice.fileserver;

import com.videojj.videoservice.config.FileServerConfig;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SftpUtilTest {

    private static final String TEST_FILE_NAME = SftpUtilTest.class.getSimpleName() + ".class";

    private SftpUtil sftpUtil;

    private FileServerConfig sftpConfig = new FileServerConfig();

    private InputStream testFileInputStream = this.getClass().getResourceAsStream(TEST_FILE_NAME);

    @Before
    public void setUp() {
        //初始化sftp dev环境的配置信息
        Yaml yaml = new Yaml();
        LinkedHashMap sftpYamlMap = ((LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) yaml.load(this.getClass()
                .getResourceAsStream("/application-dev.yml"))).get("video")).get("sftp"));
        sftpConfig.setUsername(sftpYamlMap.get("username").toString());
        sftpConfig.setPassword(sftpYamlMap.get("password").toString());
        sftpConfig.setHost(sftpYamlMap.get("host").toString());
        sftpConfig.setPort(Integer.parseInt(sftpYamlMap.get("port").toString()));
        sftpConfig.setRootPath(sftpYamlMap.get("rootPath").toString());
        this.sftpUtil = new SftpUtil(this.sftpConfig);
    }

    /**
     * 测试上传
     *
     * @throws Exception
     */
    @Test
    public void test001() throws Exception {
        sftpUtil.upload(TEST_FILE_NAME, testFileInputStream);
    }

    /**
     * 测试下载
     *
     * @throws Exception
     */
    @Test
    public void test002() throws Exception {
        System.out.println(IOUtils.toString(sftpUtil.download(TEST_FILE_NAME), Charset.defaultCharset()));
    }

    /**
     * 测试删除
     *
     * @throws Exception
     */
    @Test
    public void test003() throws Exception {
        sftpUtil.delete(TEST_FILE_NAME);
        String result = null;
        try {
            sftpUtil.download(TEST_FILE_NAME);
        } catch (Exception e) {
            result = e.getMessage();
        }
        Assert.assertEquals(result, "No such file");
    }
}
