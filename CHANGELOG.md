# Changelog

## Unreleased

- feat(changelog): 根据提交和合并的 PR 自动生成更新日志

## v0.2.0

- Enable **Claude Code** in **Settings → Tools → Copy Line Reference** to copy references such as `@src/main.ts#L12-18`. Enable both formats to see both actions in the editor context menu.
- Choose one path format for all actions, or select **Set per format** to configure absolute and project-relative paths independently. The path selectors are wider and keep their individual settings when you switch back to a shared format.
- The existing shortcut uses the only enabled format, or the regular line reference when both are enabled. **Copy for Claude Code** can also have its own shortcut in **Settings → Keymap**.

## v0.1.3

- Added a plugin icon and bundled the MIT license with the plugin. [JetBrains Marketplace release](https://plugins.jetbrains.com/plugin/36239-copy-line-reference/versions/stable/1223358)

## v0.1.2

- Moved **Copy Line Reference** to the top of the editor context menu so it is easier to reach after selecting code.

## v0.1.1

- The **Path format** dropdown now measures the full option text, giving **Relative to project root** enough room when the dropdown is closed.

## v0.1.0

- Copy a file path with the selected line or line range, such as `src/main.ts:12-18`. With no selection, the shortcut copies the current line's location.
- Use **Control + Command + C** on macOS or **Ctrl + Alt + Shift + P** on Windows and Linux. The action is also available in the editor context menu.
- Paths are absolute by default. Choose **Relative to project root** in **Settings → Tools → Copy Line Reference** for shorter references.
