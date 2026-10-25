# V2DSprite

## JavaDoc

- Every class, interface, enum, constructor, method, and field must have a JavaDoc comment.
- Never start a description with redundant articles. Begin directly with an active verb or noun phrase (e.g., `Loads a sprite sheet` not `The sprite sheet loader`).
- Use all relevant tags: `@param`, `@return`, `@throws`, `@see`, `@since`, `@deprecated`, `@link`, `@code`, etc.
- Use HTML where it improves clarity: `<p>` for multi-paragraph descriptions, `<ul>`/`<li>` for lists, `<pre>` for inline code examples, `<b>`/`<em>` for emphasis.
- Cross-reference related types and methods with `{@link}`.

## Variables

- Declare all variables as `final` unless they are explicitly mutated after initial assignment.

## Versioning

- Use date-based versioning: `YYYY.MM.DD` (e.g. `2026.10.25`).
- Append `-break` when the release contains breaking changes (e.g. `2026.10.25-break`).
- Update the version in both `pom.xml` and all snippet blocks in `README.md` on every release.
