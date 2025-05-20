import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFuel } from '../fuel.model';
import { FuelService } from '../service/fuel.service';
import { FuelFormGroup, FuelFormService } from './fuel-form.service';

@Component({
  selector: 'jhi-fuel-update',
  templateUrl: './fuel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FuelUpdateComponent implements OnInit {
  isSaving = false;
  fuel: IFuel | null = null;

  protected fuelService = inject(FuelService);
  protected fuelFormService = inject(FuelFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FuelFormGroup = this.fuelFormService.createFuelFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fuel }) => {
      this.fuel = fuel;
      if (fuel) {
        this.updateForm(fuel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fuel = this.fuelFormService.getFuel(this.editForm);
    if (fuel.id !== null) {
      this.subscribeToSaveResponse(this.fuelService.update(fuel));
    } else {
      this.subscribeToSaveResponse(this.fuelService.create(fuel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuel>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fuel: IFuel): void {
    this.fuel = fuel;
    this.fuelFormService.resetForm(this.editForm, fuel);
  }
}
