package com.wx.mini.pojo.apiprocessor;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author alan.chen
 * @date 2019/10/31 6:14 PM
 */
@Data
@Table(name = "api_coll_data")
public class ApiCollData {

    @Id
    private Long id;

    private String content;

    @Column(name = "create_date")
    private Date createDate;

    private String source;

}
