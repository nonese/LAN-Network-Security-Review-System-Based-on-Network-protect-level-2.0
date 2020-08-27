package com.example.demo.entity;

    import com.baomidou.mybatisplus.annotation.TableName;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author Yaojiaqi
* @since 2020-08-27
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("Device")
    public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    private String ip;

    private String mac;

    private String portlist;

    private Integer uuid;


}
