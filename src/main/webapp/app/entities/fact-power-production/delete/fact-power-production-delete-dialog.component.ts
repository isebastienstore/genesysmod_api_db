import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactPowerProduction } from '../fact-power-production.model';
import { FactPowerProductionService } from '../service/fact-power-production.service';

@Component({
  templateUrl: './fact-power-production-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactPowerProductionDeleteDialogComponent {
  factPowerProduction?: IFactPowerProduction;

  protected factPowerProductionService = inject(FactPowerProductionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.factPowerProductionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
