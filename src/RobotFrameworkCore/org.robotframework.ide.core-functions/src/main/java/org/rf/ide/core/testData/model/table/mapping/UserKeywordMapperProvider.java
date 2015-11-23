/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testData.model.table.mapping;

import java.util.ArrayList;
import java.util.List;

import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordArgumentsMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordArgumentsValueMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordDocumentationMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordDocumentationTextMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordReturnMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordReturnValueMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTagsMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTagsTagNameMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTeardownArgumentMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTeardownMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTeardownNameMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTimeoutMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTimeoutMessageMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.KeywordTimeoutValueMapper;
import org.rf.ide.core.testData.model.table.userKeywords.mapping.UserKeywordNameMapper;


public class UserKeywordMapperProvider {

    private static final List<IParsingMapper> mappers = new ArrayList<>();

    static {
        mappers.add(new UserKeywordNameMapper());
        mappers.add(new KeywordDocumentationMapper());
        mappers.add(new KeywordDocumentationTextMapper());
        mappers.add(new KeywordTagsMapper());
        mappers.add(new KeywordTagsTagNameMapper());
        mappers.add(new KeywordArgumentsMapper());
        mappers.add(new KeywordArgumentsValueMapper());
        mappers.add(new KeywordReturnMapper());
        mappers.add(new KeywordReturnValueMapper());
        mappers.add(new KeywordTeardownMapper());
        mappers.add(new KeywordTeardownNameMapper());
        mappers.add(new KeywordTeardownArgumentMapper());
        mappers.add(new KeywordTimeoutMapper());
        mappers.add(new KeywordTimeoutValueMapper());
        mappers.add(new KeywordTimeoutMessageMapper());
    }


    public List<IParsingMapper> getMappers() {
        return mappers;
    }
}