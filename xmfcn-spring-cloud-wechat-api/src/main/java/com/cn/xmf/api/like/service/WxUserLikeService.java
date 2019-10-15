package com.cn.xmf.api.like.service;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.model.wx.WxUserLike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * Service(微信点赞)
 * @author rufei.cn
 * @version 2019-10-15
 */
@SuppressWarnings("all")
@FeignClient(value = "${base-service.base-service}")// 配置远程服务名以及自定义权限验证配置
@RequestMapping("/server/like/")// 配置远程服务路径

public interface WxUserLikeService  {

    /**
     * list:(查询微信点赞 带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getList", consumes = MediaType.APPLICATION_JSON_VALUE)
    Partion getList(@RequestBody JSONObject map);

    /**
     * getWxUserLikeList:(查询微信点赞 不带分页数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWxUserLikeList", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<WxUserLike> getWxUserLikeList(@RequestBody WxUserLike wxUserLike);

     /**
     * getWxUserLike:(查询微信点赞单个实体数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "getWxUserLike", consumes = MediaType.APPLICATION_JSON_VALUE)
    public WxUserLike getWxUserLike(@RequestBody WxUserLike wxUserLike);

    /**
     * save:(保存微信点赞数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    WxUserLike save(@RequestBody WxUserLike wxUserLike);


    /**
     * delete:(删除微信点赞数据)
     * @Author rufei.cn
     * @return
     */
    @RequestMapping(value = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    boolean delete(@RequestParam("id") Long id);
	
}