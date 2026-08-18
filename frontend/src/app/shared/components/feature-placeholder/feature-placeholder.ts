import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-feature-placeholder',
  template: `
    <section>
      <p>FEATURE MODULE</p>
      <h1>{{ featureName }}</h1>
      <div>This module has its route and architecture boundary. Build it as an independent vertical slice.</div>
    </section>
  `,
  styles: `
    section { background: var(--surface); border: 1px solid var(--border); border-radius: 1rem; padding: 2rem; }
    p { color: var(--brand-dark); font-size: .75rem; font-weight: 800; letter-spacing: .12em; }
    h1 { font-size: 2.5rem; margin: .35rem 0 1rem; }
    div { color: var(--text-muted); }
  `,
})
export class FeaturePlaceholder {
  protected readonly featureName = inject(ActivatedRoute).snapshot.data['featureName'] as string;
}
