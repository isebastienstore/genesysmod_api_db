import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFactTradeCost } from '../fact-trade-cost.model';

@Component({
  selector: 'jhi-fact-trade-cost-detail',
  templateUrl: './fact-trade-cost-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FactTradeCostDetailComponent {
  factTradeCost = input<IFactTradeCost | null>(null);

  previousState(): void {
    window.history.back();
  }
}
