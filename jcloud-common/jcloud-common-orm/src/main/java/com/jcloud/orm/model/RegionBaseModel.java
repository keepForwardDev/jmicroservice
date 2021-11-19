package com.jcloud.orm.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jcloud.common.bean.LabelNode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 含有省市区街道,以及一些常用数据范围方法
 *
 * @param <T>
 */

@MappedSuperclass
public class RegionBaseModel<T extends RegionBaseModel<?>> extends BaseModel<T> {

    @Column(columnDefinition = "bigint(20) comment '省'")
    private Long provinceId = 0L;//省

    @Column(columnDefinition = "bigint(20) comment '市'")
    private Long cityId = 0L;//市

    @Column(columnDefinition = "bigint(20) comment '区'")
    private Long areaId = 0L;//区

    @Column(columnDefinition = "bigint(20) comment '街道'")
    private Long streetId = 0L;//街道

    /**
     * 省市区结合，不存入数据库,需要调用toArray有值
     */
    @TableField(exist = false)
    @Transient
    private String regionStr = "";

    public RegionBaseModel() {

    }

    /**
     * lgw
     * 通用 用来 拼接省市区街 jpa 拼接专用
     *
     * @param listsource
     * @param root
     * @param cb
     * @return
     */
    public List<Predicate> generatePredictListJpa(List<Predicate> listsource, Root<T> root, CriteriaBuilder cb) {
        List<Predicate> list = new ArrayList<Predicate>();
        if (this.getProvinceId() != null && this.getProvinceId().intValue() != 0) {
            list.add(cb.equal(root.get("provinceId"), this.getProvinceId()));
        }
        if (this.getCityId() != null && this.getCityId().intValue() != 0) {
            list.add(cb.equal(root.get("cityId"), this.getCityId()));
        }
        if (this.getAreaId() != null && this.getAreaId().intValue() != 0) {
            list.add(cb.equal(root.get("areaId"), this.getAreaId()));
        }
        if (this.getStreetId() != null && this.getStreetId().intValue() != 0) {
            list.add(cb.equal(root.get("streetId"), this.getStreetId()));
        }
        listsource.addAll(list);
        return listsource;
    }

    /**
     * lgw
     * 带过滤功能的
     * 原生sql 拼接省市区专用
     *
     * @param sql
     * @param alais 别名
     * @return
     */
    public String generatePredictListNativeSqlFilter(StringBuilder sql, String alais) {
        if (StringUtils.isNotBlank(alais)) {
            alais += ".";
        } else {
            alais = "";
        }
        if (sql == null) {
            sql = new StringBuilder();
        }
        if (!sql.toString().toUpperCase().contains("WHERE")) {
            sql.append(" where 1=1 ");
        }
        if (this.getProvinceId() != null && this.getProvinceId().intValue() != 0) {
            sql.append("  and " + alais + " province_id= " + this.getProvinceId() + " ");
        }
        if (this.getCityId() != null && this.getCityId().intValue() != 0) {
            sql.append("  and " + alais + "city_id= " + this.getCityId() + " ");
        }
        if (this.getAreaId() != null && this.getAreaId().intValue() != 0) {
            sql.append("  and " + alais + "area_id = " + this.getAreaId() + " ");
        }
        if (this.getStreetId() != null && this.getStreetId().intValue() != 0) {
            sql.append("  and " + alais + "street_id= " + this.getStreetId() + " ");
        }
        return sql.toString();
    }

    /**
     * lgw
     * 原生sql 拼接省市区专用
     *
     * @param sql
     * @param alais 别名
     * @return
     */
    public String generatePredictListNativeSql(StringBuilder sql, String alais) {
        if (StringUtils.isNotBlank(alais)) {
            alais += ".";
        } else {
            alais = "";
        }
        if (sql == null) {
            sql = new StringBuilder();
        }
        if (!sql.toString().toUpperCase().contains("WHERE")) {
            sql.append(" where 1=1 ");
        }
        if (this.getProvinceId() != null && this.getProvinceId().intValue() != 0) {
            sql.append("  and " + alais + " province_id= " + this.getProvinceId() + " ");
        }
        if (this.getCityId() != null && this.getCityId().intValue() != 0) {
            sql.append("  and " + alais + "city_id= " + this.getCityId() + " ");
        }
        if (this.getAreaId() != null && this.getAreaId().intValue() != 0) {
            sql.append("  and " + alais + "area_id = " + this.getAreaId() + " ");
        }
        if (this.getStreetId() != null && this.getStreetId().intValue() != 0) {
            sql.append("  and " + alais + "street_id= " + this.getStreetId() + " ");
        }


        return sql.toString();
    }




    /**
     * 如果传的是整个树，可以用这个
     *
     * @param idList
     */
    public void toFullCity(List<Long> idList) {
        if (idList.size() == 4) {
            provinceId = idList.get(0);
            cityId = idList.get(1);
            areaId = idList.get(2);
            streetId = idList.get(3);
        } else if (idList.size() == 3) {
            provinceId = idList.get(0);
            cityId = idList.get(1);
            areaId = idList.get(2);
        } else if (idList.size() == 2) {
            provinceId = idList.get(0);
            cityId = idList.get(1);
        } else if (idList.size() == 1) {
            provinceId = idList.get(0);
        }
    }







    public LabelNode toLabelNode() {
        LabelNode labelNode = new LabelNode();
        if (cityId == null || cityId.longValue() == 0) {
            labelNode.setValue(provinceId);
            labelNode.setLabel("provinceId");
            return labelNode;
        } else if (areaId == null || areaId.longValue() == 0) {
            labelNode.setValue(cityId);
            labelNode.setLabel("cityId");
            return labelNode;
        } else if (streetId == null || streetId.longValue() == 0) {
            labelNode.setValue(areaId);
            labelNode.setLabel("areaId");
            return labelNode;
        } else if (streetId != null && streetId.longValue() != 0) {
            labelNode.setValue(streetId);
            labelNode.setLabel("streetId");
            return labelNode;
        }
        return labelNode;
    }

    /**
     * 下一个字段，例如省的下一个字段是市
     *
     * @return
     */
    public String nextField() {
        LabelNode labelNode = toLabelNode();
        if (labelNode.getLabel().equals("provinceId")) {
            return labelNode.getValue().longValue() == 0 ? "provinceId" : "cityId";
        } else if (labelNode.getLabel().equals("cityId")) {
            return "areaId";
        } else if (labelNode.getLabel().equals("areaId")) {
            return "streetId";
        }
        return "streetId";
    }

    /**
     * 下一个字段，例如省的下一个字段是市
     *
     * @return
     */
    public static String nextField(LabelNode labelNode) {
        if (labelNode.getLabel().equals("provinceId")) {
            return "cityId";
        } else if (labelNode.getLabel().equals("cityId")) {
            return "areaId";
        } else if (labelNode.getLabel().equals("areaId")) {
            return "streetId";
        }
        return "streetId";
    }



    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getStreetId() {
        return streetId;
    }

    public void setStreetId(Long streetId) {
        this.streetId = streetId;
    }

    @Transient
    public String getRegionStr() {
        return regionStr;
    }

    public void setRegionStr(String regionStr) {
        this.regionStr = regionStr;
    }

}
