# Release notes

The publish workflow (`.github/workflows/gradle.yml`) writes the GitHub release notes when you push a
`vX.Y.Z` tag:

1. If `release-notes/X.Y.Z.md` exists in the tagged commit, its content comes first.
2. Then the workflow adds the list of commit titles since the previous tag ("What's Changed").

Write a notes file for each release that needs more than the commit titles: breaking changes, upgrade
steps, security cautions, or configuration changes.

**A notes file is necessary for a release with breaking changes.** If a commit since the previous tag has a
`type!:` title (for example `feat(crypto)!: ...`) or a `BREAKING CHANGE:` footer, and the notes file does
not exist, the workflow stops before it publishes to Maven Central. Add the file, then tag a commit that
contains it.

Use these sections when they apply:

```markdown
## ⚠️ Breaking changes

## 🔐 Security cautions

## Other changes
```
