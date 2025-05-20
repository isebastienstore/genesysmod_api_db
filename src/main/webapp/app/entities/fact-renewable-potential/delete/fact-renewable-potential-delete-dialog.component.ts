import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFactRenewablePotential } from '../fact-renewable-potential.model';
import { FactRenewablePotentialService } from '../service/fact-renewable-potential.service';

@Component({
  templateUrl: './fact-renewable-potential-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactRenewablePotentialDeleteDialogComponent {
  factRenewablePotential?: IFactRenewablePotential;

  protected factRenewablePotentialService = inject(FactRenewablePotentialService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.factRenewablePotentialService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
