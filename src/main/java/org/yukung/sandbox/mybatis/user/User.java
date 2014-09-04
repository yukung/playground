package org.yukung.sandbox.mybatis.user;

import lombok.Data;

/**
 * @author yukung
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private Gender gender;
}
