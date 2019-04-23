package com.nuc.admin.dao;

import com.nuc.admin.domain.Shops;

import java.util.List;

public interface IShopsDao {

	public abstract int addShops(Shops shops);

	public abstract int delShops(String shops_id);

	public abstract int delShopss(String[] shops_ids);

	public abstract int updateShops(Shops shops);

	public abstract Shops getShopss(Shops shops);

	public abstract List<Shops>  listShopss(Shops shops);

	public abstract int listShopssCount(Shops shops);

}
