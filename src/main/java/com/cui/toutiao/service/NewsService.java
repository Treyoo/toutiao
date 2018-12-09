package com.cui.toutiao.service;

import com.cui.toutiao.dao.CommentDAO;
import com.cui.toutiao.dao.NewsDAO;
import com.cui.toutiao.model.News;
import com.cui.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * author:CuiWJ
 * Date:2018/11/7
 * Created with IDEA
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(),
                new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }


    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    /**
     * 根据id获取资讯对象
     */
    public News getById(int id) {
        return newsDAO.getById(id);
    }

    public void updateCommentCount(int entityId, int count) {
        newsDAO.updateCommentCount(entityId, count);
    }

    public void updateLikeCount(int newsId,int count){
        newsDAO.updateLikeCount(newsId,count);
    }
}
