import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFuel } from '../fuel.model';

@Component({
  selector: 'jhi-fuel-detail',
  templateUrl: './fuel-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FuelDetailComponent {
  fuel = input<IFuel | null>(null);

  previousState(): void {
    window.history.back();
  }
}
