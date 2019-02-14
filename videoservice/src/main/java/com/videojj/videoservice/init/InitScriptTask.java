package com.videojj.videoservice.init;

import com.videojj.videocommon.constant.Constants;
import com.videojj.videocommon.exception.ServiceException;
import com.videojj.videoservice.bo.TemplateAddBo;
import com.videojj.videoservice.config.CommonConfig;
import com.videojj.videoservice.dao.TbInteractionMapper;
import com.videojj.videoservice.entity.TbInteraction;
import com.videojj.videoservice.enums.IsDeletedEnum;
import com.videojj.videoservice.enums.IsSystemEnum;
import com.videojj.videoservice.fileserver.CommonFileServer;
import com.videojj.videoservice.service.TemplateExtService;
import com.videojj.videoservice.util.PermissionUtil;
import jodd.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @Author @videopls.com
 * Created by  on 2018/8/30 下午8:28.
 * @Description:
 */
@Component
public class InitScriptTask implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TbInteractionMapper tbInteractionMapper;

    @Resource
    protected CommonFileServer commonFileServer;

    @Resource
    private TemplateExtService templateExtService;

    @Resource
    private CommonConfig commonConfig;

    @Resource
    private TransactionTemplate transactionTemplate;

    private static final String INIT_DIR_URL = "init/";

    private static final String INTERACTION_DIR_URL = INIT_DIR_URL + "interaction/";

    private static final String DATA_FILE_URL = INIT_DIR_URL + "data.yml";

    private static final String TEST_CONNECTION_FILE_URL = INIT_DIR_URL + "test_connection";

    private static final String INTERACTION_JSON_DIR_URL = INTERACTION_DIR_URL + "json/";

    private static final String INTERACTION_IMG_DIR_URL = INTERACTION_DIR_URL + "img/";

    private static final String USER_NAME = "system";

    private static final String INTERACTION_DEFAULT_JPG_URL = INTERACTION_DIR_URL + Constants.INTERACTION_DEFAULT_JPG;

    private static final String TEMPLATE_DIR_URL = INIT_DIR_URL + "template/";

    private static final String TEMPLATE_ZIP_DIR_URL = TEMPLATE_DIR_URL + "zip/";

    private InputStream getResourceAsStream(String localUrl) throws IOException {
        URL url = this.getClass().getClassLoader().getResource(localUrl);
        logger.info("InitScriptTask.getResourceAsStream() ----> File url : {}",url.getFile());
        if(url.getFile().contains(".jar!")){
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            return jarURLConnection.getInputStream();
        }else {
            return new FileInputStream(url.getFile());
        }
    }

    @Override
    public void afterPropertiesSet() {
        transactionTemplate.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus transactionStatus) {
                try {
                    PermissionUtil.setCurrentUsername(USER_NAME);
                    Map<String, TbInteraction> interactionTypeAndTbInteractionMap = new LinkedHashMap<>();
                    Map<String, List<LinkedHashMap>> interactionTypeAndTemplateListMap = new HashMap<>();

                    //测试文件服务
                    commonFileServer.upload(commonConfig.getPreKey() + "test_connection", getResourceAsStream(TEST_CONNECTION_FILE_URL));
                    //上传应用默认图片
                    commonFileServer.upload(commonConfig.getPreKey() + Constants.INTERACTION_DEFAULT_JPG, getResourceAsStream(INTERACTION_DEFAULT_JPG_URL));
                    //文件服务开放访问 基础URL
                    final String EXTERNAL_ACCESS_FILE_BASE_URL = commonConfig.getFileDomainName().concat(commonConfig.getPreKey());
                    Yaml yaml = new Yaml();
                    //1.判断tb_interaction表，如果为空，则开始初始化数据。
                    if (tbInteractionMapper.selectCount(new TbInteraction()) == 0) {

                        for (Map.Entry typeEntry : ((Set<Map.Entry>) yaml.loadAs(getResourceAsStream(DATA_FILE_URL), LinkedHashMap.class).entrySet())) {

                            //2.初始化互动应用的官方数据，根据yaml文件的配置
                            LinkedHashMap fields = (LinkedHashMap) typeEntry.getValue();
                            final String interactionType = (String) typeEntry.getKey();
                            final String interactionTypeName = (String) fields.get("interactionTypeName");
                            final String fileName = interactionType + ".json";
                            final String content = FileUtil.readUTFString(getResourceAsStream(INTERACTION_JSON_DIR_URL + fileName));

                            TbInteraction tbInteraction = new TbInteraction();
                            tbInteraction.setInteractionTypeName(interactionTypeName);
                            tbInteraction.setFileName(fileName);
                            tbInteraction.setContent(content);
                            tbInteraction.setCreator(USER_NAME);
                            tbInteraction.setModifier(USER_NAME);
                            tbInteraction.setIsDeleted(IsDeletedEnum.NO.getValue());
                            tbInteraction.setGmtCreated(new Date());
                            tbInteraction.setIsSystem(IsSystemEnum.YES.getValue());

                            //3.上传应用图片
                            final String imgName = interactionType + ".jpg";
                            commonFileServer.upload(commonConfig.getPreKey() + imgName, getResourceAsStream(INTERACTION_IMG_DIR_URL + imgName));
                            tbInteraction.setImgUrl(EXTERNAL_ACCESS_FILE_BASE_URL.concat(imgName));

                            tbInteractionMapper.insertSelective(tbInteraction);
                            interactionTypeAndTbInteractionMap.put(interactionType, tbInteraction);
                            interactionTypeAndTemplateListMap.put(interactionType, (List) fields.get("templateList"));
                        }

                        //4.生成官方模版数据
                        for (Map.Entry<String, TbInteraction> interactionTypeAndTbInteractionEntry : interactionTypeAndTbInteractionMap.entrySet()) {

                            for (Map templateMap : interactionTypeAndTemplateListMap.get(interactionTypeAndTbInteractionEntry.getKey())) {

                                String zipFileName = (String) templateMap.get("zipFileName");
                                TemplateAddBo templateAddBo = templateExtService.uploadFile(getResourceAsStream(TEMPLATE_ZIP_DIR_URL + zipFileName), zipFileName);
                                templateAddBo.setInteractionTypeId(interactionTypeAndTbInteractionEntry.getValue().getId());
                                templateAddBo.setInteractionTypeName(interactionTypeAndTbInteractionEntry.getValue().getInteractionTypeName());
                                templateAddBo.setTemplateName((String) templateMap.get("templateName"));
                                templateAddBo.setUsername(USER_NAME);
                                templateAddBo.setZipFileName(zipFileName);

                                templateExtService.addTemplateInfo(templateAddBo);
                            }
                        }

                    }
                } catch (Exception e) {
                    logger.error("初始化脚本失败！", e);
                    throw new ServiceException("初始化脚本失败！");
                }

                return true;
            }
        });
    }
}

