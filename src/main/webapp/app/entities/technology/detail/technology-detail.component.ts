import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITechnology } from '../technology.model';

@Component({
  selector: 'jhi-technology-detail',
  templateUrl: './technology-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TechnologyDetailComponent {
  technology = input<ITechnology | null>(null);

  previousState(): void {
    window.history.back();
  }
}
