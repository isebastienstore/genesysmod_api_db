import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFactTradeCapacity } from '../fact-trade-capacity.model';

@Component({
  selector: 'jhi-fact-trade-capacity-detail',
  templateUrl: './fact-trade-capacity-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FactTradeCapacityDetailComponent {
  factTradeCapacity = input<IFactTradeCapacity | null>(null);

  previousState(): void {
    window.history.back();
  }
}
