# Copy Line Reference

Copy a file path and the selected line numbers from WebStorm to your clipboard.

| Selection | Example |
| --- | --- |
| One line or just the caret | `/projects/demo/src/main.ts:12` |
| Several lines | `/projects/demo/src/main.ts:12-18` |
| Project-relative format | `src/main.ts:12-18` |

## Install

Download the plugin ZIP from [GitHub Releases](https://github.com/sunpm/copy-line-reference/releases/latest). In WebStorm 2025.1 or later, open **Settings → Plugins → ⚙ → Install Plugin from Disk…** and select the ZIP.

## Use

Select code and press **Control + Command + C** on macOS or **Ctrl + Alt + Shift + P** on Windows and Linux. You can also right-click the selection and choose **Copy Line Reference**.

Absolute paths are the default. To use relative paths, open **Settings → Tools → Copy Line Reference** and set **Path format** to **Relative to project root**.

To change the shortcut, open **Settings → Keymap** and search for **Copy Line Reference**.

## Build and release

With JDK 21 installed, run `./gradlew buildPlugin` (on Windows, `gradlew.bat buildPlugin`). The installable ZIP is written to `build/distributions/`.

Write the user-facing changes in `releases/<tag>.md` (for example, `releases/v0.1.3.md`). Commit the file, then push the matching tag. GitHub Actions uses the tag as the plugin version and publishes the notes alongside the installable ZIP on [GitHub Releases](https://github.com/sunpm/copy-line-reference/releases).

To build a ZIP without publishing, run **Build and release** from the Actions tab on a branch.
