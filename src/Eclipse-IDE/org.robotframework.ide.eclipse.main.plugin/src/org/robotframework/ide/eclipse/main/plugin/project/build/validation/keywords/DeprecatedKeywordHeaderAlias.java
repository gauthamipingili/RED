/*
 * Copyright 2016 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.project.build.validation.keywords;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.rf.ide.core.testdata.model.table.ARobotSectionTable;
import org.rf.ide.core.testdata.model.table.KeywordTable;
import org.rf.ide.core.testdata.model.table.TableHeader;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.robotframework.ide.eclipse.main.plugin.model.RobotKeywordsSection;
import org.robotframework.ide.eclipse.main.plugin.project.build.ProblemsReportingStrategy;
import org.robotframework.ide.eclipse.main.plugin.project.build.RobotArtifactsValidator.ModelUnitValidator;
import org.robotframework.ide.eclipse.main.plugin.project.build.RobotProblem;
import org.robotframework.ide.eclipse.main.plugin.project.build.causes.KeywordsProblem;

public class DeprecatedKeywordHeaderAlias implements ModelUnitValidator {

    private final IFile file;

    private final ProblemsReportingStrategy reporter;

    private final RobotKeywordsSection section;

    public DeprecatedKeywordHeaderAlias(final IFile file, final ProblemsReportingStrategy reporter,
            final RobotKeywordsSection section) {
        this.file = file;
        this.reporter = reporter;
        this.section = section;
    }

    @Override
    public void validate(final IProgressMonitor monitor) throws CoreException {
        KeywordTable keywords = (KeywordTable) section.getLinkedElement();
        if (keywords.isPresent()) {
            for (final TableHeader<? extends ARobotSectionTable> th : keywords.getHeaders()) {
                RobotToken declaration = th.getDeclaration();
                String raw = declaration.getRaw();
                final String rawWihtoutWithspaces = raw.toLowerCase().replaceAll("\\s", "");
                if (rawWihtoutWithspaces.toLowerCase().contains("userkeyword")) {
                    reporter.handleProblem(RobotProblem.causedBy(KeywordsProblem.USER_KEYWORD_TABLE_HEADER_SYNONIM)
                            .formatMessageWith(raw), file, declaration);
                }
            }
        }
    }
}
