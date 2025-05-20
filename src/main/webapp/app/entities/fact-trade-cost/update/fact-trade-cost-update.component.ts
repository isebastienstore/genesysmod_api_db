import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFuel } from 'app/entities/fuel/fuel.model';
import { FuelService } from 'app/entities/fuel/service/fuel.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { FactTradeCostService } from '../service/fact-trade-cost.service';
import { IFactTradeCost } from '../fact-trade-cost.model';
import { FactTradeCostFormGroup, FactTradeCostFormService } from './fact-trade-cost-form.service';

@Component({
  selector: 'jhi-fact-trade-cost-update',
  templateUrl: './fact-trade-cost-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactTradeCostUpdateComponent implements OnInit {
  isSaving = false;
  factTradeCost: IFactTradeCost | null = null;

  fuelsSharedCollection: IFuel[] = [];
  metadataSharedCollection: IMetadata[] = [];

  protected factTradeCostService = inject(FactTradeCostService);
  protected factTradeCostFormService = inject(FactTradeCostFormService);
  protected fuelService = inject(FuelService);
  protected metadataService = inject(MetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactTradeCostFormGroup = this.factTradeCostFormService.createFactTradeCostFormGroup();

  compareFuel = (o1: IFuel | null, o2: IFuel | null): boolean => this.fuelService.compareFuel(o1, o2);

  compareMetadata = (o1: IMetadata | null, o2: IMetadata | null): boolean => this.metadataService.compareMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factTradeCost }) => {
      this.factTradeCost = factTradeCost;
      if (factTradeCost) {
        this.updateForm(factTradeCost);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factTradeCost = this.factTradeCostFormService.getFactTradeCost(this.editForm);
    if (factTradeCost.id !== null) {
      this.subscribeToSaveResponse(this.factTradeCostService.update(factTradeCost));
    } else {
      this.subscribeToSaveResponse(this.factTradeCostService.create(factTradeCost));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactTradeCost>>): void {
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

  protected updateForm(factTradeCost: IFactTradeCost): void {
    this.factTradeCost = factTradeCost;
    this.factTradeCostFormService.resetForm(this.editForm, factTradeCost);

    this.fuelsSharedCollection = this.fuelService.addFuelToCollectionIfMissing<IFuel>(this.fuelsSharedCollection, factTradeCost.fuel);
    this.metadataSharedCollection = this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(
      this.metadataSharedCollection,
      factTradeCost.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fuelService
      .query()
      .pipe(map((res: HttpResponse<IFuel[]>) => res.body ?? []))
      .pipe(map((fuels: IFuel[]) => this.fuelService.addFuelToCollectionIfMissing<IFuel>(fuels, this.factTradeCost?.fuel)))
      .subscribe((fuels: IFuel[]) => (this.fuelsSharedCollection = fuels));

    this.metadataService
      .query()
      .pipe(map((res: HttpResponse<IMetadata[]>) => res.body ?? []))
      .pipe(
        map((metadata: IMetadata[]) =>
          this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(metadata, this.factTradeCost?.metadata),
        ),
      )
      .subscribe((metadata: IMetadata[]) => (this.metadataSharedCollection = metadata));
  }
}
