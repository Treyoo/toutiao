package com.cui.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.cui.toutiao.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * author:CuiWJ
 * Date:2018/11/8
 * Created with IDEA
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "QAj8PibLOKb01W88k0ZliyENLni5AwI6B90j3gO7";
    String SECRET_KEY = "cCbjou3bsPxGMvOq-LC-k7YMoY4lWMdGCDdNFs-T";
    //要上传的空间
    String bucketname = "toutiao";
    //七牛空间域名，这里是体验域名，30天有效期（18/11/8起）
    private static String QINIU_IMAGE_DOMAIN = "http://phvoddcl4.bkt.clouddn.com/";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象

    UploadManager uploadManager = new UploadManager();


    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //返回图片路径
            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常，状态码:" + e.code() + ",出错信息：" + e.response.error);
            return null;
        }
    }
}
