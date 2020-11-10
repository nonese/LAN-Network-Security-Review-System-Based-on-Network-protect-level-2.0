package com.example.demo.entity;

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
* @since 2020-11-10
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Emergency implements Serializable {

    private static final long serialVersionUID = 1L;

    private String date;

    private String content;

    private String name;

    private String uuid;


}
