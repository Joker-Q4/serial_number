package com.joker.plugin.action;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.joker.plugin.dialog.FormDialog;
import com.joker.plugin.util.StringMessage;
import com.joker.plugin.util.ValidationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenerateSerialNumber extends EditorAction {

    protected GenerateSerialNumber() {
        super(new GenerateSerialNumber.Handler());
    }

    public static class Handler extends EditorActionHandler {
        public Handler() {}
        @Override
        protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {

            ValidationInfo validationResult = ValidationUtil.isWritable(editor);
            if (validationResult != null) {
                Messages.showMessageDialog(validationResult.message, StringMessage.PLEASE_CHECK, Messages.getInformationIcon());
                return;
            }

            FormDialog formDialog = new FormDialog(editor);
            formDialog.setSize(300, 50);
            formDialog.setResizable(false);
            formDialog.show();
        }
    }
}
