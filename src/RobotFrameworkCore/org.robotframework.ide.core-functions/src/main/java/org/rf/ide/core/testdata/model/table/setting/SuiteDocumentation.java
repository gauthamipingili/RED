/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rf.ide.core.testdata.model.AModelElement;
import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.ModelType;
import org.rf.ide.core.testdata.model.table.SettingTable;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;


public class SuiteDocumentation extends AModelElement<SettingTable> {

    private final RobotToken declaration;
    private final List<RobotToken> text = new ArrayList<>();
    private final List<RobotToken> comment = new ArrayList<>();


    public SuiteDocumentation(final RobotToken declaration) {
        this.declaration = declaration;
    }


    public void addDocumentationText(final RobotToken token) {
        text.add(token);
    }


    public List<RobotToken> getDocumentationText() {
        return Collections.unmodifiableList(text);
    }


    public List<RobotToken> getComment() {
        return Collections.unmodifiableList(comment);
    }


    public void addCommentPart(final RobotToken rt) {
        this.comment.add(rt);
    }


    public RobotToken getDeclaration() {
        return declaration;
    }


    @Override
    public boolean isPresent() {
        return (getDeclaration() != null);
    }


    @Override
    public ModelType getModelType() {
        return ModelType.SUITE_DOCUMENTATION;
    }


    @Override
    public FilePosition getBeginPosition() {
        return getDeclaration().getFilePosition();
    }


    @Override
    public List<RobotToken> getElementTokens() {
        final List<RobotToken> tokens = new ArrayList<>();
        if (isPresent()) {
            tokens.add(getDeclaration());
            tokens.addAll(getDocumentationText());
            tokens.addAll(getComment());
        }

        return tokens;
    }
}