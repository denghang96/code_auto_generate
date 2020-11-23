package com.dengooo.codegenerate.service;

import com.dengooo.codegenerate.common.vo.AllParams;

import java.io.IOException;

public interface IndexService {
    void generate(AllParams allParams) throws IOException;
}
