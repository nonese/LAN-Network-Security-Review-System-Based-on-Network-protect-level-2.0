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
* @since 2020-11-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Scan implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String date;

    private String ip;

    private String status;

    private String uuid;

    private String pid;


}
