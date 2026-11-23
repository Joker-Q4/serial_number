package com.joker.plugin.action;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.ui.Messages;
import com.joker.plugin.dialog.FormDialog;
import com.joker.plugin.util.StringMessage;
import com.joker.plugin.util.ValidationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Joker
 * @since 2021-08-15
 */
public class GenerateSerialNumber extends EditorAction {

    protected GenerateSerialNumber() {
        super(new GenerateSerialNumber.Handler());
    }

    public static class Handler extends EditorActionHandler {
        public Handler() {}
        @Override
        protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {

            String notWritableMessage = ValidationUtil.isWritable(editor);
            if (notWritableMessage != null) {
                Messages.showMessageDialog(notWritableMessage, StringMessage.PLEASE_CHECK, Messages.getWarningIcon());
                return;
            }

            FormDialog formDialog = new FormDialog(editor);
            formDialog.show();
        }
    }
}
