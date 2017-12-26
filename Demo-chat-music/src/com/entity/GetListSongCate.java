package com.entity;

import java.util.List;

public class GetListSongCate {

    int page;
    int limit;
    int totalPage;
    int totalItem;
    List<SongCate> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public List<SongCate> getData() {
        return data;
    }

    public void setData(List<SongCate> data) {
        this.data = data;
    }
}
