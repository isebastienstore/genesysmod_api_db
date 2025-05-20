import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFactTransport } from '../fact-transport.model';

@Component({
  selector: 'jhi-fact-transport-detail',
  templateUrl: './fact-transport-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FactTransportDetailComponent {
  factTransport = input<IFactTransport | null>(null);

  previousState(): void {
    window.history.back();
  }
}
