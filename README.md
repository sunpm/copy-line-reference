# Copy Line Reference

Copy a file path and the selected line numbers from WebStorm to your clipboard.

| Selection | Example |
| --- | --- |
| One line or just the caret | `/projects/demo/src/main.ts:12` |
| Several lines | `/projects/demo/src/main.ts:12-18` |
| Project-relative format | `src/main.ts:12-18` |
| Claude Code, project-relative | `@src/main.ts#L12-18` |

## Install

In WebStorm 2025.1 or later, install **Copy Line Reference** from **Settings → Plugins → Marketplace**. To download approved updates automatically, enable **Update plugins automatically** in **Settings → Appearance & Behavior → System Settings → Updates**. WebStorm applies them on the next restart. [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/36239-copy-line-reference)

For a manual installation, download the ZIP and select **Settings → Plugins → ⚙ → Install Plugin from Disk…**. [GitHub Releases](https://github.com/sunpm/copy-line-reference/releases/latest) · [Changelog](CHANGELOG.md)

## Use

Select code and press **Control + Command + C** on macOS or **Ctrl + Alt + Shift + P** on Windows and Linux. By default this copies a line reference with an absolute path.

In **Settings → Tools → Copy Line Reference**, enable **Line reference**, **Claude Code**, or both. Each enabled format has its own entry in the editor context menu. **Path format** applies to all enabled formats. Choose **Set per format** to select absolute or project-relative paths separately.

The existing shortcut uses the only enabled format, or **Copy Line Reference** when both are enabled. To assign a dedicated Claude Code shortcut, find **Copy for Claude Code** in **Settings → Keymap**.

## Build and release

With JDK 21 installed, run `./gradlew buildPlugin` (on Windows, `gradlew.bat buildPlugin`). The installable ZIP is written to `build/distributions/`.

Use a new `v` tag for each release. The tag determines the version in the ZIP. Update the [changelog](CHANGELOG.md) and write the user-facing notes in `releases/<tag>.md`.

- **From GitHub Releases**, publish a release for the tag. Actions attaches the ZIP and uses the description you entered. Leave it blank to use the notes file and GitHub's generated release notes.
- **From Git**, push the tag. Actions builds the ZIP and creates the GitHub Release with the notes. A tag with a hyphen, such as `v0.2.0-rc.1`, creates a prerelease.

For stable releases, Actions submits the same ZIP to JetBrains Marketplace with the changelog embedded. Prereleases stay on GitHub. Configure the repository Actions secret `JETBRAINS_MARKETPLACE_TOKEN` once with a token from the plugin owner's [Marketplace profile](https://plugins.jetbrains.com/author/me/tokens). If the submission fails, rerun the failed Marketplace job. Updates become available in the IDE after Marketplace approval.

To build a ZIP without publishing, run **Build and release** from the Actions tab on a branch.
