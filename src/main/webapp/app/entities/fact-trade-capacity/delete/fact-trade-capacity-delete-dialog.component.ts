import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactTradeCapacity } from '../fact-trade-capacity.model';
import { FactTradeCapacityService } from '../service/fact-trade-capacity.service';

@Component({
  templateUrl: './fact-trade-capacity-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactTradeCapacityDeleteDialogComponent {
  factTradeCapacity?: IFactTradeCapacity;

  protected factTradeCapacityService = inject(FactTradeCapacityService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.factTradeCapacityService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
