import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFactElectricityConsumption } from '../fact-electricity-consumption.model';

@Component({
  selector: 'jhi-fact-electricity-consumption-detail',
  templateUrl: './fact-electricity-consumption-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FactElectricityConsumptionDetailComponent {
  factElectricityConsumption = input<IFactElectricityConsumption | null>(null);

  previousState(): void {
    window.history.back();
  }
}
