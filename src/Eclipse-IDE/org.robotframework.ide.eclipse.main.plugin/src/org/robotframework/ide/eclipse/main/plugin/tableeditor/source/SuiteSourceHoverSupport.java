/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.tableeditor.source;

import static org.robotframework.ide.eclipse.main.plugin.assist.RedKeywordProposals.sortedByNames;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.widgets.Shell;
import org.robotframework.ide.eclipse.main.plugin.RedPlugin;
import org.robotframework.ide.eclipse.main.plugin.assist.RedKeywordProposal;
import org.robotframework.ide.eclipse.main.plugin.assist.RedKeywordProposals;
import org.robotframework.ide.eclipse.main.plugin.debug.model.RobotDebugTarget;
import org.robotframework.ide.eclipse.main.plugin.debug.model.RobotStackFrame;
import org.robotframework.ide.eclipse.main.plugin.model.RobotSuiteFile;
import org.robotframework.ide.eclipse.main.plugin.texteditor.contentAssist.ContentAssistKeywordContext;

import com.google.common.base.Optional;

public class SuiteSourceHoverSupport implements ITextHover, ITextHoverExtension, ITextHoverExtension2 {

    private final RobotSuiteFile suiteFile;

    public SuiteSourceHoverSupport(final RobotSuiteFile file) {
        this.suiteFile = file;
    }

    @Deprecated
    @Override
    public String getHoverInfo(final ITextViewer textViewer, final IRegion hoverRegion) {
        return null;
    }

    @Override
    public IRegion getHoverRegion(final ITextViewer textViewer, final int offset) {
        try {
            final IDocument document = textViewer.getDocument();
            return DocumentUtilities.findVariable(document, offset)
                    .or(DocumentUtilities.findCellRegion(document, offset))
                    .or(Optional.of(new Region(0, 0)))
                    .get();
        } catch (final BadLocationException e) {
            RedPlugin.logError(e.getMessage(), e);
            return new Region(0, 0);
        }
    }

    @Override
    public Object getHoverInfo2(final ITextViewer textViewer, final IRegion hoverRegion) {
        try {
            final String hoveredText = textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength());
            if (isVariable(hoveredText)) {
                return getVariableHoverInfo(hoveredText);
            } else {
                return getKeywordHoverInfo(hoveredText);
            }
        } catch (final BadLocationException | DebugException e) {
        }
        return null;
    }

    private String getVariableHoverInfo(final String variableName) throws DebugException {
        final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        for (final IDebugTarget target : launchManager.getDebugTargets()) {
            if (target instanceof RobotDebugTarget) {
                final RobotDebugTarget robotTarget = (RobotDebugTarget) target;
                if (!robotTarget.isSuspended()) {
                    continue;
                }
                for (final IStackFrame stackFrame : robotTarget.getRobotThread().getStackFrames()) {
                    final RobotStackFrame robotStackFrame = (RobotStackFrame) stackFrame;
                    if (robotStackFrame.getFileName().equals(suiteFile.getFile().getName())) {
                        for (final IVariable variable : robotStackFrame.getAllVariables()) {
                            if (variable.getName().equals(variableName)) {
                                return "Current value:\n\n" + variable.getValue().getValueString();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getKeywordHoverInfo(final String keywordName) {
        final RedKeywordProposals proposals = new RedKeywordProposals(suiteFile);
        final List<RedKeywordProposal> keywordProposals = proposals.getKeywordProposals(sortedByNames());

        for (final RedKeywordProposal proposal : keywordProposals) {
            if (proposal.getLabel().equals(keywordName)) {
                return new ContentAssistKeywordContext(proposal).getDescription();
            }
        }
        return null;
    }

    private static boolean isVariable(final String text) {
        return Pattern.matches("[@$&%]\\{.+\\}", text);
    }

    @Override
    public IInformationControlCreator getHoverControlCreator() {
        return new IInformationControlCreator() {
            @Override
            public IInformationControl createInformationControl(final Shell parent) {
                return new DefaultInformationControl(parent);
            }
        };
    }
}