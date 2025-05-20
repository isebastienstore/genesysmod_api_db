import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IYear } from '../year.model';

@Component({
  selector: 'jhi-year-detail',
  templateUrl: './year-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class YearDetailComponent {
  year = input<IYear | null>(null);

  previousState(): void {
    window.history.back();
  }
}
