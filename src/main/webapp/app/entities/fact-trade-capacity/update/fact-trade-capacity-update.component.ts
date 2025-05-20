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
import { IFuel } from 'app/entities/fuel/fuel.model';
import { FuelService } from 'app/entities/fuel/service/fuel.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { FactTradeCapacityService } from '../service/fact-trade-capacity.service';
import { IFactTradeCapacity } from '../fact-trade-capacity.model';
import { FactTradeCapacityFormGroup, FactTradeCapacityFormService } from './fact-trade-capacity-form.service';

@Component({
  selector: 'jhi-fact-trade-capacity-update',
  templateUrl: './fact-trade-capacity-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactTradeCapacityUpdateComponent implements OnInit {
  isSaving = false;
  factTradeCapacity: IFactTradeCapacity | null = null;

  yearsSharedCollection: IYear[] = [];
  countriesSharedCollection: ICountry[] = [];
  fuelsSharedCollection: IFuel[] = [];
  metadataSharedCollection: IMetadata[] = [];

  protected factTradeCapacityService = inject(FactTradeCapacityService);
  protected factTradeCapacityFormService = inject(FactTradeCapacityFormService);
  protected yearService = inject(YearService);
  protected countryService = inject(CountryService);
  protected fuelService = inject(FuelService);
  protected metadataService = inject(MetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactTradeCapacityFormGroup = this.factTradeCapacityFormService.createFactTradeCapacityFormGroup();

  compareYear = (o1: IYear | null, o2: IYear | null): boolean => this.yearService.compareYear(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareFuel = (o1: IFuel | null, o2: IFuel | null): boolean => this.fuelService.compareFuel(o1, o2);

  compareMetadata = (o1: IMetadata | null, o2: IMetadata | null): boolean => this.metadataService.compareMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factTradeCapacity }) => {
      this.factTradeCapacity = factTradeCapacity;
      if (factTradeCapacity) {
        this.updateForm(factTradeCapacity);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factTradeCapacity = this.factTradeCapacityFormService.getFactTradeCapacity(this.editForm);
    if (factTradeCapacity.id !== null) {
      this.subscribeToSaveResponse(this.factTradeCapacityService.update(factTradeCapacity));
    } else {
      this.subscribeToSaveResponse(this.factTradeCapacityService.create(factTradeCapacity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactTradeCapacity>>): void {
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

  protected updateForm(factTradeCapacity: IFactTradeCapacity): void {
    this.factTradeCapacity = factTradeCapacity;
    this.factTradeCapacityFormService.resetForm(this.editForm, factTradeCapacity);

    this.yearsSharedCollection = this.yearService.addYearToCollectionIfMissing<IYear>(this.yearsSharedCollection, factTradeCapacity.year);
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      factTradeCapacity.country1,
      factTradeCapacity.country2,
    );
    this.fuelsSharedCollection = this.fuelService.addFuelToCollectionIfMissing<IFuel>(this.fuelsSharedCollection, factTradeCapacity.fuel);
    this.metadataSharedCollection = this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(
      this.metadataSharedCollection,
      factTradeCapacity.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.yearService
      .query()
      .pipe(map((res: HttpResponse<IYear[]>) => res.body ?? []))
      .pipe(map((years: IYear[]) => this.yearService.addYearToCollectionIfMissing<IYear>(years, this.factTradeCapacity?.year)))
      .subscribe((years: IYear[]) => (this.yearsSharedCollection = years));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(
            countries,
            this.factTradeCapacity?.country1,
            this.factTradeCapacity?.country2,
          ),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.fuelService
      .query()
      .pipe(map((res: HttpResponse<IFuel[]>) => res.body ?? []))
      .pipe(map((fuels: IFuel[]) => this.fuelService.addFuelToCollectionIfMissing<IFuel>(fuels, this.factTradeCapacity?.fuel)))
      .subscribe((fuels: IFuel[]) => (this.fuelsSharedCollection = fuels));

    this.metadataService
      .query()
      .pipe(map((res: HttpResponse<IMetadata[]>) => res.body ?? []))
      .pipe(
        map((metadata: IMetadata[]) =>
          this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(metadata, this.factTradeCapacity?.metadata),
        ),
      )
      .subscribe((metadata: IMetadata[]) => (this.metadataSharedCollection = metadata));
  }
}
