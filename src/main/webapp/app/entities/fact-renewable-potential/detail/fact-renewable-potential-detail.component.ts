import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFactRenewablePotential } from '../fact-renewable-potential.model';

@Component({
  selector: 'jhi-fact-renewable-potential-detail',
  templateUrl: './fact-renewable-potential-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FactRenewablePotentialDetailComponent {
  factRenewablePotential = input<IFactRenewablePotential | null>(null);

  previousState(): void {
    window.history.back();
  }
}
