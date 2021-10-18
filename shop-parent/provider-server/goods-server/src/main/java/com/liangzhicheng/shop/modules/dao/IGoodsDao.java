package com.liangzhicheng.shop.modules.dao;

import com.liangzhicheng.shop.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Mapper
public interface IGoodsDao {

//    @Select("SELECT * FROM t_goods WHERE id IN ('','',...)")
    @SelectProvider(type = GoodsSelectProvider.class, method = "getByGoodsIds")
    List<Goods> getByGoodsIds(@Param("goodsIds") Set<Long> goodsIds);

    /**
     * @description 获取商品列表的供应者
     */
    class GoodsSelectProvider{
        public String getByGoodsIds(@Param("goodsIds") Set<Long> goodsIds){
            if(CollectionUtils.isEmpty(goodsIds)){
                return null;
            }
            //初始化大小
            StringBuilder splice = new StringBuilder(100)
                    .append("SELECT * FROM t_goods WHERE id IN (");
            for(Iterator<Long> it = goodsIds.iterator(); it.hasNext();){
                splice.append(it.next())
                        .append(",");
            }
            splice.deleteCharAt(splice.length() - 1);
            splice.append(")");
            return splice.toString();
        }
    }

}
