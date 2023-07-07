package com.bessie.pojo.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: bessie-recruit-dev
 * @description:
 * @author: Bessie
 * @create: 2023-07-07 08:25
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SMSContentQO { //queue object
    private String mobile;
    private String content;
}
