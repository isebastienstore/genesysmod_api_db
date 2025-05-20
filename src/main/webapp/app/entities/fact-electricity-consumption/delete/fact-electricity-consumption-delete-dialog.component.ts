import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactElectricityConsumption } from '../fact-electricity-consumption.model';
import { FactElectricityConsumptionService } from '../service/fact-electricity-consumption.service';

@Component({
  templateUrl: './fact-electricity-consumption-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactElectricityConsumptionDeleteDialogComponent {
  factElectricityConsumption?: IFactElectricityConsumption;

  protected factElectricityConsumptionService = inject(FactElectricityConsumptionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.factElectricityConsumptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
