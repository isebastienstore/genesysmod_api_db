import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFactPowerPlants } from '../fact-power-plants.model';

@Component({
  selector: 'jhi-fact-power-plants-detail',
  templateUrl: './fact-power-plants-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FactPowerPlantsDetailComponent {
  factPowerPlants = input<IFactPowerPlants | null>(null);

  previousState(): void {
    window.history.back();
  }
}
