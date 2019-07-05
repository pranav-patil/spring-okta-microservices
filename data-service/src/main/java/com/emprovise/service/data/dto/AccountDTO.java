package com.emprovise.service.data.dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

public class AccountDTO implements Serializable {

    @QuerySqlField(index = true)
    private Integer id;

    @QuerySqlField(index = true)
    private String accountHolder;

    @QuerySqlField(index = true)
    private String stockId;

    @QuerySqlField(index = true)
    private String stockName;

    @QuerySqlField(index = true)
    private Integer totalStocks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Integer getTotalStocks() {
        return totalStocks;
    }

    public void setTotalStocks(Integer totalStocks) {
        this.totalStocks = totalStocks;
    }
}
