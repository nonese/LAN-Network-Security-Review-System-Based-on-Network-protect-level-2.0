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
    @TableName("User_info")
    public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private String name;

    private String email;

    private String qqid;

    private String wechatsession;


}
