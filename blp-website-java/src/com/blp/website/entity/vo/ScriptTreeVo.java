package com.blp.website.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class ScriptTreeVo {
    private String name;
    private List<ScriptTreeVo> children;
}