import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMetadata } from '../metadata.model';

@Component({
  selector: 'jhi-metadata-detail',
  templateUrl: './metadata-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MetadataDetailComponent {
  metadata = input<IMetadata | null>(null);

  previousState(): void {
    window.history.back();
  }
}
