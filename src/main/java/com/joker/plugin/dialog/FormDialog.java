package com.joker.plugin.dialog;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.joker.plugin.util.StringMessage;
import com.joker.plugin.util.StringUtils;
import com.joker.plugin.util.ValidationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author Joker
 * @since 2021/08/15
 */
public class FormDialog extends DialogWrapper {


    private final JLabel start = new JLabel(StringMessage.LABEL_START, SwingConstants.CENTER);
    private final JTextField startContent = new JTextField();

    private final JLabel interval = new JLabel(StringMessage.LABEL_INTERVAL, SwingConstants.CENTER);
    private final JTextField intervalContent = new JTextField();

    private final Editor editor;

    public FormDialog(@Nullable Editor editor) {
        super(true);
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
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(2, 2));
        center.add(start);
        startContent.setText("1");
        center.add(startContent);
        center.add(interval);
        intervalContent.setText("1");
        center.add(intervalContent);
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

            if(!ValidationUtil.hasText(startText, intervalText)){
                return;
            }

            if(!ValidationUtil.beLongType(startText, intervalText)){
                return;
            }

            generate(Long.parseLong(startText), Long.parseLong(intervalText));
            close(CANCEL_EXIT_CODE);

        }
    }

    private void generate(long startNum, long intervalNum) {
        List<Caret> allCarets = editor.getCaretModel().getAllCarets();
        Document document = editor.getDocument();
        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            for (int i = 0; i < allCarets.size(); i++) {
                long num = startNum + intervalNum * i;
                Caret caretTemp = allCarets.get(i);
                int offset = caretTemp.getOffset();
                if (caretTemp.hasSelection()) {
                    offset = caretTemp.getSelectionStart();
                    int selectionEnd = caretTemp.getSelectionEnd();
                    document.deleteString(offset, selectionEnd);
                }
                document.insertString(offset, num + "");
                caretTemp.moveToOffset(offset + StringUtils.getNumLength(num));
            }
        });
    }
}
