package com.example.myapplication.util;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
    private int sumCount;
    private int indexPage;
    private int sumPage;
    private int row;
    private List<T> getListLimit ;

    public int getSumCount() {
        return sumCount;
    }

    public void setSumCount(int sumCount) {
        this.sumCount = sumCount;
        this.sumPage = this.sumCount%this.row==0?this.sumCount/this.row:this.sumCount/this.row+1;
    }

    public int getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(int indexPage) {
        this.indexPage = indexPage;
    }

    public int getSumPage() {
        return sumPage;
    }

    public void setSumPage(int sumPage) {
        this.sumPage = sumPage;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public List<T> getGetListLimit() {
        return getListLimit;
    }

    public void setGetListLimit(List<T> getListLimit) {
        this.getListLimit = getListLimit;
    }
}
