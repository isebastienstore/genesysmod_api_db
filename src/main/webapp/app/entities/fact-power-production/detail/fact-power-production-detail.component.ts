import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFactPowerProduction } from '../fact-power-production.model';

@Component({
  selector: 'jhi-fact-power-production-detail',
  templateUrl: './fact-power-production-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FactPowerProductionDetailComponent {
  factPowerProduction = input<IFactPowerProduction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
