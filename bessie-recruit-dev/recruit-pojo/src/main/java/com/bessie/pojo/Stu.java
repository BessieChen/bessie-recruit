package com.bessie.pojo;

import lombok.*;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-06-23 14:23
 **/
@Data
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Stu {
    private String name;
    private Integer age;
    private Integer id;
}
