package com.talenton.lsg.server.bean.school;

import java.util.ArrayList;

/**
 * @author zjh
 * @date 2016/4/22
 */
public class RspClassDetail {
    public static final int FREE = 0; //免费
    public static final int CHAGER = 1; //付费

    private long aid; //课程ID
    private String pic; //课程图片地址
    private String title; //课程标题
    private float starcount; //星数
    private int age; //年龄
    private int joincount; //参加人数
    private int ispay; //是否付费
    private double price; //价格
    private String lastupdate; //最后更新内容
    private int totalcatalog; //总节数
    private int iscomplete; //是否已经更新完成
    private int commentcount; //评论数
    private int studycount;  //学习人数
    private int likecount;  //点赞数
    private String content; //简介
    private int islike; //是否点赞 0=否,1=是
    private int iscollection; //是否收藏 0=否,1=是
    private int isjoin; //是否参加了该课程 0=否,1=是
    private String share_url; //分享地址
    private ArrayList<CatalogData> cataloglist;
    private ArrayList<ClassEvaluateData> commentlist;
    private ArrayList<SupportToolData> aidslist;
    private ArrayList<AdVideoData> adlist; //广告

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getStarcount() {
        return starcount;
    }

    public void setStarcount(float starcount) {
        this.starcount = starcount;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getJoincount() {
        return joincount;
    }

    public void setJoincount(int joincount) {
        this.joincount = joincount;
    }

    public int getIspay() {
        return ispay;
    }

    public void setIspay(int ispay) {
        this.ispay = ispay;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public int getTotalcatalog() {
        return totalcatalog;
    }

    public void setTotalcatalog(int totalcatalog) {
        this.totalcatalog = totalcatalog;
    }

    public int getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(int iscomplete) {
        this.iscomplete = iscomplete;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getStudycount() {
        return studycount;
    }

    public void setStudycount(int studycount) {
        this.studycount = studycount;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<CatalogData> getCataloglist() {
        return cataloglist;
    }

    public void setCataloglist(ArrayList<CatalogData> cataloglist) {
        this.cataloglist = cataloglist;
    }

    public ArrayList<ClassEvaluateData> getCommentlist() {
        return commentlist;
    }

    public void setCommentlist(ArrayList<ClassEvaluateData> commentlist) {
        this.commentlist = commentlist;
    }

    public ArrayList<SupportToolData> getAidslist() {
        return aidslist;
    }

    public void setAidslist(ArrayList<SupportToolData> aidslist) {
        this.aidslist = aidslist;
    }

    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public int getIscollection() {
        return iscollection;
    }

    public void setIscollection(int iscollection) {
        this.iscollection = iscollection;
    }

    public int getIsjoin() {
        return isjoin;
    }

    public void setIsjoin(int isjoin) {
        this.isjoin = isjoin;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public ArrayList<AdVideoData> getAdlist() {
        return adlist;
    }

    public void setAdlist(ArrayList<AdVideoData> adlist) {
        this.adlist = adlist;
    }
}
