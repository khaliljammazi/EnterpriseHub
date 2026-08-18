import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./layout/shell/shell').then((component) => component.Shell),
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard',
      },
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./features/dashboard/dashboard.routes').then((routes) => routes.DASHBOARD_ROUTES),
      },
      ...featureRoute('products'),
      ...featureRoute('inventory'),
      ...featureRoute('suppliers'),
      ...featureRoute('purchases'),
      ...featureRoute('pos'),
      ...featureRoute('sales'),
      ...featureRoute('customers'),
      ...featureRoute('employees'),
      ...featureRoute('reports'),
      ...featureRoute('settings'),
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];

function featureRoute(path: string): Routes {
  return [
    {
      path,
      loadComponent: () =>
        import('./shared/components/feature-placeholder/feature-placeholder').then(
          (component) => component.FeaturePlaceholder,
        ),
      data: { featureName: humanize(path) },
      title: `EnterpriseHub | ${humanize(path)}`,
    },
  ];
}

function humanize(value: string): string {
  return value.charAt(0).toUpperCase() + value.slice(1);
}
