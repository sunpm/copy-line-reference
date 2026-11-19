#!/usr/bin/env bash
set -euo pipefail

commit_notes() {
  local revision=$1 base=$2 hash subject number title
  git log --first-parent --format='%h %s' "${base:+$base..}$revision" -- . ':(exclude)CHANGELOG.md' |
    while read -r hash subject; do
      if [[ $subject =~ ^Merge\ pull\ request\ #([0-9]+)\ from\  ]]; then
        number=${BASH_REMATCH[1]}
        title=$(git show -s --format=%b "$hash" | sed -n '1p')
        if [[ -n $title ]]; then subject="$title (#$number)"; fi
      fi
      printf -- '- %s\n' "$subject"
    done
}

previous_stable() {
  git describe --first-parent --tags --abbrev=0 --match 'v[0-9]*' --exclude '*-*' "$1" 2>/dev/null || true
}

release_notes() {
  local tag=$1
  if [[ -f "releases/$tag.md" ]]; then
    cat "releases/$tag.md"
  else
    local entries
    entries=$(commit_notes "$tag" "$(previous_stable "$tag^")")
    printf '%s\n' "${entries:-- No changes outside the changelog.}"
  fi
}

if [[ $# -eq 1 ]]; then
  release_notes "$1"
  exit
fi

printf '# Changelog\n'
unreleased=$(commit_notes HEAD "$(previous_stable HEAD)")
if [[ -n $unreleased ]]; then
  printf '\n## Unreleased\n\n%s\n' "$unreleased"
fi

{
  git tag --list 'v[0-9]*'
  # Some historical versions were published only on Marketplace.
  shopt -s nullglob
  for note in releases/v*.md; do
    basename "$note" .md
  done
# A tilde sorts before the matching stable version in version sort.
} | sed 's/-/~/' | LC_ALL=C sort -Vru | sed 's/~/-/' | while read -r tag; do
  printf '\n## %s\n\n' "$tag"
  release_notes "$tag"
done
