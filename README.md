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

Push a version tag such as `v0.1.1` to build the plugin and attach its ZIP to a GitHub Release. The tag supplies the plugin version. To build a ZIP without publishing, run **Build and release** from the Actions tab on a branch.
