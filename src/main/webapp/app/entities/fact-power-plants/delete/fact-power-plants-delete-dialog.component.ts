import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactPowerPlants } from '../fact-power-plants.model';
import { FactPowerPlantsService } from '../service/fact-power-plants.service';

@Component({
  templateUrl: './fact-power-plants-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactPowerPlantsDeleteDialogComponent {
  factPowerPlants?: IFactPowerPlants;

  protected factPowerPlantsService = inject(FactPowerPlantsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.factPowerPlantsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
