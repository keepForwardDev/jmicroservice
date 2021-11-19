package com.jcloud.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/3/23
 */
@ApiModel(value = "公共分页", description = "常用于列表请求，列表的数据返回，列表请求一般只用传递{currentPage} {pageSize} 即可，列表返回的数据在list中")
public class CommonPage<T> {


    /**
     * 总条数
     */
    @ApiModelProperty(value = "总数")
    private long totalCount=0 ;

    /**
     *总页数
     */
    @ApiModelProperty(value = "总页数")
    private int pageCount=0;

    /**
     *每页条数
     */
    @ApiModelProperty(value = "每页条数")
    private int pageSize=20;

    /**
     * 当前页数 从第一页开始
     */
    @ApiModelProperty(value = "当前页")
    private int currentPage=1;

    /**
     * 数据
     */
    @ApiModelProperty(value = "返回的数据列表")
    private List<T> list=new ArrayList<>() ;

    public CommonPage() {

    }

    public CommonPage(int currentPage, int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }


    public CommonPage(long totalCount, int pageCount, int pageSize, int currentPage, List<T> list) {
        super();
        this.totalCount = totalCount;
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.list = list;
    }

    public CommonPage(long totalCount, int currentPage, int pageSize, List<T> list){
        super();
        this.totalCount=totalCount;
        //计算总页数
        int pageCount = (int) (totalCount / pageSize);
        if (totalCount % pageSize > 0) {
            pageCount++;
        }
        this.pageCount=pageCount;
        this.pageSize=pageSize;
        this.currentPage=currentPage;
        this.list=list;

    }

    public long getTotalCount() {
        if(this.totalCount<0){
            return 0;
        }
        if(this.totalCount>Long.MAX_VALUE){
            return Long.MAX_VALUE;
        }
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        //计算总页数
        int pageCount = (int) (totalCount / pageSize);
        if (totalCount % pageSize > 0) {
            pageCount++;
        }
        this.pageCount=pageCount;
        this.totalCount = totalCount;

    }

    public int getPageCount() {
        if(this.pageCount<0){
            this.pageCount=0;
        }
        if(this.pageCount>Integer.MAX_VALUE){
            this.pageCount=Integer.MAX_VALUE;
        }
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        if(this.pageSize<0){
            this.pageSize=0;
        }
        if(this.pageSize>Integer.MAX_VALUE){
            this.pageSize=Integer.MAX_VALUE;
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
		/*if(this.pageCount>0&&this.currentPage>this.pageCount){
			return this.pageCount;
		}*/
        //jpa从0开始的 前端是从1开始的,所以这里 如 >=1 默认 -1
        if(this.currentPage<=0||this.currentPage>Integer.MAX_VALUE){
            this.currentPage=1;
        }
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
    @Override
    public String toString() {
        return "CommonPage [totalCount=" + totalCount + ", pageCount=" + pageCount + ", pageSize=" + pageSize
                + ", currentPage=" + currentPage + ", list=" + list + "]";
    }
}
