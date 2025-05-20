import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IYear } from 'app/entities/year/year.model';
import { YearService } from 'app/entities/year/service/year.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { FactElectricityConsumptionService } from '../service/fact-electricity-consumption.service';
import { IFactElectricityConsumption } from '../fact-electricity-consumption.model';
import { FactElectricityConsumptionFormGroup, FactElectricityConsumptionFormService } from './fact-electricity-consumption-form.service';

@Component({
  selector: 'jhi-fact-electricity-consumption-update',
  templateUrl: './fact-electricity-consumption-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactElectricityConsumptionUpdateComponent implements OnInit {
  isSaving = false;
  factElectricityConsumption: IFactElectricityConsumption | null = null;

  yearsSharedCollection: IYear[] = [];
  countriesSharedCollection: ICountry[] = [];
  metadataSharedCollection: IMetadata[] = [];

  protected factElectricityConsumptionService = inject(FactElectricityConsumptionService);
  protected factElectricityConsumptionFormService = inject(FactElectricityConsumptionFormService);
  protected yearService = inject(YearService);
  protected countryService = inject(CountryService);
  protected metadataService = inject(MetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactElectricityConsumptionFormGroup = this.factElectricityConsumptionFormService.createFactElectricityConsumptionFormGroup();

  compareYear = (o1: IYear | null, o2: IYear | null): boolean => this.yearService.compareYear(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareMetadata = (o1: IMetadata | null, o2: IMetadata | null): boolean => this.metadataService.compareMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factElectricityConsumption }) => {
      this.factElectricityConsumption = factElectricityConsumption;
      if (factElectricityConsumption) {
        this.updateForm(factElectricityConsumption);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factElectricityConsumption = this.factElectricityConsumptionFormService.getFactElectricityConsumption(this.editForm);
    if (factElectricityConsumption.id !== null) {
      this.subscribeToSaveResponse(this.factElectricityConsumptionService.update(factElectricityConsumption));
    } else {
      this.subscribeToSaveResponse(this.factElectricityConsumptionService.create(factElectricityConsumption));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactElectricityConsumption>>): void {
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

  protected updateForm(factElectricityConsumption: IFactElectricityConsumption): void {
    this.factElectricityConsumption = factElectricityConsumption;
    this.factElectricityConsumptionFormService.resetForm(this.editForm, factElectricityConsumption);

    this.yearsSharedCollection = this.yearService.addYearToCollectionIfMissing<IYear>(
      this.yearsSharedCollection,
      factElectricityConsumption.year,
    );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      factElectricityConsumption.country,
    );
    this.metadataSharedCollection = this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(
      this.metadataSharedCollection,
      factElectricityConsumption.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.yearService
      .query()
      .pipe(map((res: HttpResponse<IYear[]>) => res.body ?? []))
      .pipe(map((years: IYear[]) => this.yearService.addYearToCollectionIfMissing<IYear>(years, this.factElectricityConsumption?.year)))
      .subscribe((years: IYear[]) => (this.yearsSharedCollection = years));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.factElectricityConsumption?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.metadataService
      .query()
      .pipe(map((res: HttpResponse<IMetadata[]>) => res.body ?? []))
      .pipe(
        map((metadata: IMetadata[]) =>
          this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(metadata, this.factElectricityConsumption?.metadata),
        ),
      )
      .subscribe((metadata: IMetadata[]) => (this.metadataSharedCollection = metadata));
  }
}
