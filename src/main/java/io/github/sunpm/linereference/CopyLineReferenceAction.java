package io.github.sunpm.linereference;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;
import java.util.Objects;

public final class CopyLineReferenceAction extends DumbAwareAction {
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        VirtualFile file = editor == null ? null : FileDocumentManager.getInstance().getFile(editor.getDocument());
        event.getPresentation().setEnabledAndVisible(file != null && file.isInLocalFileSystem());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) return;
        Document document = editor.getDocument();
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        if (file == null || !file.isInLocalFileSystem()) return;

        Caret caret = editor.getCaretModel().getPrimaryCaret();
        int start = caret.getSelectionStart();
        int end = caret.getSelectionEnd();
        int firstLine = document.getLineNumber(start) + 1;
        // The selection end is exclusive, including when it falls at the next line's start.
        int lastLine = document.getLineNumber(end > start ? end - 1 : start) + 1;
        String lines = firstLine == lastLine ? Integer.toString(firstLine) : firstLine + "-" + lastLine;
        String reference = pathFor(file, event.getProject()) + ":" + lines;
        CopyPasteManager.getInstance().setContents(new StringSelection(reference));
    }

    private static String pathFor(VirtualFile file, Project project) {
        String path = file.getPath();
        String base = project == null ? null : project.getBasePath();
        if (PathFormatSettings.useRelativePaths() && base != null) {
            path = Objects.requireNonNullElse(FileUtil.getRelativePath(base, path, '/'), path);
        }
        return FileUtil.toSystemDependentName(path);
    }
}
