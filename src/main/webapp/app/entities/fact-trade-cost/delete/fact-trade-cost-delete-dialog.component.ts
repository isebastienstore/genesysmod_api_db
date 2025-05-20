import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactTradeCost } from '../fact-trade-cost.model';
import { FactTradeCostService } from '../service/fact-trade-cost.service';

@Component({
  templateUrl: './fact-trade-cost-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactTradeCostDeleteDialogComponent {
  factTradeCost?: IFactTradeCost;

  protected factTradeCostService = inject(FactTradeCostService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.factTradeCostService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
