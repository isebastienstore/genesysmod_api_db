import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'year',
    data: { pageTitle: 'Years' },
    loadChildren: () => import('./year/year.routes'),
  },
  {
    path: 'country',
    data: { pageTitle: 'Countries' },
    loadChildren: () => import('./country/country.routes'),
  },
  {
    path: 'technology',
    data: { pageTitle: 'Technologies' },
    loadChildren: () => import('./technology/technology.routes'),
  },
  {
    path: 'fuel',
    data: { pageTitle: 'Fuels' },
    loadChildren: () => import('./fuel/fuel.routes'),
  },
  {
    path: 'fact-power-production',
    data: { pageTitle: 'FactPowerProductions' },
    loadChildren: () => import('./fact-power-production/fact-power-production.routes'),
  },
  {
    path: 'fact-electricity-consumption',
    data: { pageTitle: 'FactElectricityConsumptions' },
    loadChildren: () => import('./fact-electricity-consumption/fact-electricity-consumption.routes'),
  },
  {
    path: 'fact-power-plants',
    data: { pageTitle: 'FactPowerPlants' },
    loadChildren: () => import('./fact-power-plants/fact-power-plants.routes'),
  },
  {
    path: 'fact-renewable-potential',
    data: { pageTitle: 'FactRenewablePotentials' },
    loadChildren: () => import('./fact-renewable-potential/fact-renewable-potential.routes'),
  },
  {
    path: 'fact-trade-cost',
    data: { pageTitle: 'FactTradeCosts' },
    loadChildren: () => import('./fact-trade-cost/fact-trade-cost.routes'),
  },
  {
    path: 'fact-trade-capacity',
    data: { pageTitle: 'FactTradeCapacities' },
    loadChildren: () => import('./fact-trade-capacity/fact-trade-capacity.routes'),
  },
  {
    path: 'fact-transport',
    data: { pageTitle: 'FactTransports' },
    loadChildren: () => import('./fact-transport/fact-transport.routes'),
  },
  {
    path: 'metadata',
    data: { pageTitle: 'Metadata' },
    loadChildren: () => import('./metadata/metadata.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
