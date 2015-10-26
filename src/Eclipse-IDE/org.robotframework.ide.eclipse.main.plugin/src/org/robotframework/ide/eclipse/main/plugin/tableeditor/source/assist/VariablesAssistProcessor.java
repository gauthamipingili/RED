/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.tableeditor.source.assist;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.robotframework.ide.eclipse.main.plugin.RedImages;
import org.robotframework.ide.eclipse.main.plugin.assist.RedVariableProposal;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.source.DocumentUtilities;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.source.SuiteSourceAssistantContext;
import org.robotframework.ide.eclipse.main.plugin.texteditor.contentAssist.RedCompletionBuilder;
import org.robotframework.ide.eclipse.main.plugin.texteditor.contentAssist.RedCompletionProposal;
import org.robotframework.red.graphics.ImagesManager;

import com.google.common.base.Optional;


/**
 * @author Michal Anglart
 *
 */
public class VariablesAssistProcessor extends RedContentAssistProcessor {

    private final SuiteSourceAssistantContext assist;

    public VariablesAssistProcessor(final SuiteSourceAssistantContext assist) {
        this.assist = assist;
    }

    @Override
    protected String getProposalsTitle() {
        return "Variables";
    }

    @Override
    protected List<ICompletionProposal> computeProposals(final ITextViewer viewer, final int offset) {
        final IDocument document = viewer.getDocument();
        try {
            if (offset < document.getLineInformationOfOffset(offset).getOffset() + 2) {
                return null;
            }

            final Optional<IRegion> variable = DocumentUtilities.findLiveVariable(document, offset);
            final String prefix = variable.isPresent() ? getPrefix(document, variable.get(), offset) : "";
            final Optional<IRegion> cellRegion = DocumentUtilities.findCellRegion(document, offset);
            final String content = cellRegion.isPresent() && variable.isPresent()
                    ? document.get(variable.get().getOffset(), cellRegion.get().getLength()) : "";

            final List<ICompletionProposal> proposals = newArrayList();
            for (final RedVariableProposal varProposal : assist.getVariables(offset)) {
                if (varProposal.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
                    final String additionalInfo = createSecondaryInfo(varProposal);
                    final Image image = getImage(varProposal.getName());

                    final RedCompletionProposal proposal = RedCompletionBuilder.newProposal()
                            .will(assist.getAcceptanceMode())
                            .theText(varProposal.getName())
                            .atOffset(offset - prefix.length())
                            .givenThatCurrentPrefixIs(prefix)
                            .andWholeContentIs(content)
                            .secondaryPopupShouldBeDisplayed(additionalInfo)
                            .thenCursorWillStopAtTheEndOfInsertion()
                            .currentPrefixShouldBeDecorated()
                            .proposalsShouldHaveIcon(image)
                            .create();
                    proposals.add(proposal);
                }
            }
            return proposals;
        } catch (final BadLocationException e) {
            return newArrayList();
        }
    }

    private Image getImage(final String name) {
        if (name.startsWith("&")) {
            return ImagesManager.getImage(RedImages.getRobotDictionaryVariableImage());
        } else if (name.startsWith("@")) {
            return ImagesManager.getImage(RedImages.getRobotListVariableImage());
        } else {
            return ImagesManager.getImage(RedImages.getRobotScalarVariableImage());
        }
    }

    private static String createSecondaryInfo(final RedVariableProposal varProposal) {
        String info = "Source: " + varProposal.getSource() + "\n";
        final String variableValue = varProposal.getValue();
        if (variableValue != null && !variableValue.isEmpty()) {
            info += "Value: " + variableValue + "\n";
        }
        final String variableComment = varProposal.getComment();
        if (variableComment != null && !variableComment.isEmpty()) {
            info += "Comment: " + variableComment;
        }
        return info;
    }

    private String getPrefix(final IDocument document, final IRegion wholeRegion, final int offset) {
        try {
            return document.get(wholeRegion.getOffset(), offset - wholeRegion.getOffset());
        } catch (final BadLocationException e) {
            return "";
        }
    }
}