package org.ponking.bpeg.model;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author ponking
 * @Date 2021/4/9 19:57
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 买方
     */
    private Player buyer;


    /**
     * 卖方
     */
    private Player seller;

    private Material buyerMaterial;

    private Material sellerMaterial;

    private Integer buyerCount;

    private Integer sellerCount;

    private LocalDateTime createTime;

    private LocalDateTime makeTime;

    public Order(Player buyer, Player seller, Material buyerMaterial, Material sellerMaterial,
                 Integer buyerCount, Integer sellerCount, LocalDateTime createTime, LocalDateTime makeTime) {
        this.buyer = buyer;
        this.seller = seller;
        this.buyerMaterial = buyerMaterial;
        this.sellerMaterial = sellerMaterial;
        this.buyerCount = buyerCount;
        this.sellerCount = sellerCount;
        this.createTime = createTime;
        this.makeTime = makeTime;
    }

    public Player getBuyer() {
        return buyer;
    }

    public void setBuyer(Player buyer) {
        this.buyer = buyer;
    }

    public Player getSeller() {
        return seller;
    }

    public void setSeller(Player seller) {
        this.seller = seller;
    }

    public Material getBuyerMaterial() {
        return buyerMaterial;
    }

    public void setBuyerMaterial(Material buyerMaterial) {
        this.buyerMaterial = buyerMaterial;
    }

    public Material getSellerMaterial() {
        return sellerMaterial;
    }

    public void setSellerMaterial(Material sellerMaterial) {
        this.sellerMaterial = sellerMaterial;
    }

    public Integer getBuyerCount() {
        return buyerCount;
    }

    public void setBuyerCount(Integer buyerCount) {
        this.buyerCount = buyerCount;
    }

    public Integer getSellerCount() {
        return sellerCount;
    }

    public void setSellerCount(Integer sellerCount) {
        this.sellerCount = sellerCount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getMakeTime() {
        return makeTime;
    }

    public void setMakeTime(LocalDateTime makeTime) {
        this.makeTime = makeTime;
    }
}
