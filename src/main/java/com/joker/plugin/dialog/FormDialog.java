package com.joker.plugin.dialog;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.joker.plugin.util.StringMessage;
import com.joker.plugin.util.StringUtils;
import com.joker.plugin.util.ValidationUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joker
 * @since 2021-08-15
 */
public class FormDialog extends DialogWrapper {

    private final JLabel start = new JLabel(StringMessage.LABEL_START);
    private final JTextField startContent = new JTextField();

    private final JLabel interval = new JLabel(StringMessage.LABEL_INTERVAL);
    private final JTextField intervalContent = new JTextField();

    private final Editor editor;

    public FormDialog(@NotNull Editor editor) {
        super(false);
        this.editor = editor;
        setTitle(StringMessage.TITLE);
        init();
    }

    @Override
    protected JComponent createNorthPanel() {
        return null;
    }

    @Override
    protected Action @NotNull [] createActions() {
        DialogWrapperExitAction exitAction = new DialogWrapperExitAction(StringMessage.BUTTON_CANCEL, CANCEL_EXIT_CODE);
        CustomOKAction okAction = new CustomOKAction();
        // 设置默认的焦点按钮
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        return new Action[]{okAction, exitAction};
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        startContent.setPreferredSize(new Dimension(140, 28));
        intervalContent.setPreferredSize(new Dimension(140, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(start, gbc);
        gbc.gridx = 1;
        startContent.setText("1");
        center.add(startContent, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        center.add(interval, gbc);
        gbc.gridx = 1;
        intervalContent.setText("1");
        center.add(intervalContent, gbc);

        return center;
    }

    protected class CustomOKAction extends DialogWrapperAction {

        protected CustomOKAction() {
            super(StringMessage.BUTTON_OK);
        }

        @Override
        protected void doAction(ActionEvent e) {
            String startText = StringUtils.trimAllWhitespace(startContent.getText());
            String intervalText = StringUtils.trimAllWhitespace(intervalContent.getText());

            if (!ValidationUtil.hasText(startText, intervalText)) {
                return;
            }

            if (!ValidationUtil.isLongType(startText, intervalText)) {
                return;
            }

            generate(Long.parseLong(startText), Long.parseLong(intervalText));
            close(OK_EXIT_CODE);
        }
    }

    private void generate(long startNum, long intervalNum) {
        List<Caret> allCarets = editor.getCaretModel().getAllCarets();

        // 预计算全部数字, 发生溢出时整体中止, 避免只生成一部分
        List<Long> numbers = new ArrayList<>(allCarets.size());
        try {
            for (int i = 0; i < allCarets.size(); i++) {
                numbers.add(Math.addExact(startNum, Math.multiplyExact(intervalNum, i)));
            }
        } catch (ArithmeticException e) {
            Messages.showMessageDialog(StringMessage.OVERFLOW, StringMessage.ERROR_TITLE, Messages.getWarningIcon());
            return;
        }

        Document document = editor.getDocument();
        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            for (int i = 0; i < allCarets.size(); i++) {
                long num = numbers.get(i);
                Caret caretTemp = allCarets.get(i);
                int offset = caretTemp.getOffset();
                if (caretTemp.hasSelection()) {
                    offset = caretTemp.getSelectionStart();
                    int selectionEnd = caretTemp.getSelectionEnd();
                    document.deleteString(offset, selectionEnd);
                }
                String value = String.valueOf(num);
                document.insertString(offset, value);
                caretTemp.moveToOffset(offset + value.length());
            }
        });
    }
}
