package com.joker.plugin.action;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.joker.plugin.dialog.FormDialog;
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
            FormDialog formDialog = new FormDialog(editor);
            formDialog.setSize(300, 50);
            formDialog.setResizable(true);
            formDialog.show();
        }
    }
}
