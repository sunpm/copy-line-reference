package io.github.sunpm.linereference;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.ActionPlaces;
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

public class CopyLineReferenceAction extends DumbAwareAction {
    private final CopyFormat format;

    public CopyLineReferenceAction() {
        this(CopyFormat.LINE_REFERENCE);
    }

    private CopyLineReferenceAction(CopyFormat format) {
        this.format = format;
    }

    public static final class ClaudeCode extends CopyLineReferenceAction {
        public ClaudeCode() {
            super(CopyFormat.CLAUDE_CODE);
        }
    }

    private CopyFormat enabledFormat(AnActionEvent event) {
        if (format.isEnabled()) return format;
        if (format == CopyFormat.LINE_REFERENCE && ActionPlaces.KEYBOARD_SHORTCUT.equals(event.getPlace())
                && CopyFormat.CLAUDE_CODE.isEnabled()) {
            return CopyFormat.CLAUDE_CODE;
        }
        return null;
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        VirtualFile file = editor == null ? null : FileDocumentManager.getInstance().getFile(editor.getDocument());
        event.getPresentation().setEnabledAndVisible(file != null && file.isInLocalFileSystem() && enabledFormat(event) != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        CopyFormat selectedFormat = enabledFormat(event);
        if (editor == null || selectedFormat == null) return;
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
        String path = pathFor(file, event.getProject(), selectedFormat.usesRelativePaths());
        String reference = selectedFormat.reference(path, lines);
        CopyPasteManager.getInstance().setContents(new StringSelection(reference));
    }

    private static String pathFor(VirtualFile file, Project project, boolean relative) {
        String path = file.getPath();
        String base = project == null ? null : project.getBasePath();
        if (relative && base != null) {
            path = Objects.requireNonNullElse(FileUtil.getRelativePath(base, path, '/'), path);
        }
        return path;
    }
}
