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
* @since 2020-08-28
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("Session_list")
    public class SessionList implements Serializable {

    private static final long serialVersionUID = 1L;

    private String session;

    private String uuid;

    private String time;

    private String vaild;


}
