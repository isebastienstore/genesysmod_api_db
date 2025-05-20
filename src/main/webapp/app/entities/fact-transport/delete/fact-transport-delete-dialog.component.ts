import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactTransport } from '../fact-transport.model';
import { FactTransportService } from '../service/fact-transport.service';

@Component({
  templateUrl: './fact-transport-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactTransportDeleteDialogComponent {
  factTransport?: IFactTransport;

  protected factTransportService = inject(FactTransportService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.factTransportService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
