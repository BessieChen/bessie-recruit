package com.bessie.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bessie
 * @since 2023-06-24
 */

public class Stu implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private Integer age;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "id=" + id +
                ", name=" + name +
                ", age=" + age +
                "}";
    }
}
