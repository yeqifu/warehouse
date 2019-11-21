package com.yeqifu.sys.common;

import com.yeqifu.sys.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 落亦-
 * @Date: 2019/11/21 20:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiverUser {

    private User user;

    private List<String> roles;

    private List<String> permission;

}
