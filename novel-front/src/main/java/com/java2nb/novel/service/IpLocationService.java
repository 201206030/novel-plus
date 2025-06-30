package com.java2nb.novel.service;

/**
 * IP 地址定位服务类
 *
 * <p>该服务用于实现 IP 地址到地理位置的查询功能，
 * 包括国家、省份、城市等信息。</p>
 *
 * <p>此类设计为 Spring 管理的 Service Bean，支持在 Controller 或其他 Service 中注入使用。</p>
 *
 * @author xiongxiaoyang
 * @date 2025/6/30
 */
public interface IpLocationService {

    /**
     * 根据 IP 地址查询地理位置信息
     *
     * @param ip 待查询的 IP 地址（IPv4）
     * @return 如果是中国 IP，返回省份；否则返回国家
     */
    String getLocation(String ip);

}
