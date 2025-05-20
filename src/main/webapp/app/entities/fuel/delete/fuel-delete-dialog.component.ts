import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFuel } from '../fuel.model';
import { FuelService } from '../service/fuel.service';

@Component({
  templateUrl: './fuel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FuelDeleteDialogComponent {
  fuel?: IFuel;

  protected fuelService = inject(FuelService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.fuelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
