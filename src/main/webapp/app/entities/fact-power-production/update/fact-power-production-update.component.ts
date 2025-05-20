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
import { ITechnology } from 'app/entities/technology/technology.model';
import { TechnologyService } from 'app/entities/technology/service/technology.service';
import { IFuel } from 'app/entities/fuel/fuel.model';
import { FuelService } from 'app/entities/fuel/service/fuel.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { FactPowerProductionService } from '../service/fact-power-production.service';
import { IFactPowerProduction } from '../fact-power-production.model';
import { FactPowerProductionFormGroup, FactPowerProductionFormService } from './fact-power-production-form.service';

@Component({
  selector: 'jhi-fact-power-production-update',
  templateUrl: './fact-power-production-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactPowerProductionUpdateComponent implements OnInit {
  isSaving = false;
  factPowerProduction: IFactPowerProduction | null = null;

  yearsSharedCollection: IYear[] = [];
  countriesSharedCollection: ICountry[] = [];
  technologiesSharedCollection: ITechnology[] = [];
  fuelsSharedCollection: IFuel[] = [];
  metadataSharedCollection: IMetadata[] = [];

  protected factPowerProductionService = inject(FactPowerProductionService);
  protected factPowerProductionFormService = inject(FactPowerProductionFormService);
  protected yearService = inject(YearService);
  protected countryService = inject(CountryService);
  protected technologyService = inject(TechnologyService);
  protected fuelService = inject(FuelService);
  protected metadataService = inject(MetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactPowerProductionFormGroup = this.factPowerProductionFormService.createFactPowerProductionFormGroup();

  compareYear = (o1: IYear | null, o2: IYear | null): boolean => this.yearService.compareYear(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareTechnology = (o1: ITechnology | null, o2: ITechnology | null): boolean => this.technologyService.compareTechnology(o1, o2);

  compareFuel = (o1: IFuel | null, o2: IFuel | null): boolean => this.fuelService.compareFuel(o1, o2);

  compareMetadata = (o1: IMetadata | null, o2: IMetadata | null): boolean => this.metadataService.compareMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factPowerProduction }) => {
      this.factPowerProduction = factPowerProduction;
      if (factPowerProduction) {
        this.updateForm(factPowerProduction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factPowerProduction = this.factPowerProductionFormService.getFactPowerProduction(this.editForm);
    if (factPowerProduction.id !== null) {
      this.subscribeToSaveResponse(this.factPowerProductionService.update(factPowerProduction));
    } else {
      this.subscribeToSaveResponse(this.factPowerProductionService.create(factPowerProduction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactPowerProduction>>): void {
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

  protected updateForm(factPowerProduction: IFactPowerProduction): void {
    this.factPowerProduction = factPowerProduction;
    this.factPowerProductionFormService.resetForm(this.editForm, factPowerProduction);

    this.yearsSharedCollection = this.yearService.addYearToCollectionIfMissing<IYear>(this.yearsSharedCollection, factPowerProduction.year);
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      factPowerProduction.country,
    );
    this.technologiesSharedCollection = this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(
      this.technologiesSharedCollection,
      factPowerProduction.technology,
    );
    this.fuelsSharedCollection = this.fuelService.addFuelToCollectionIfMissing<IFuel>(this.fuelsSharedCollection, factPowerProduction.fuel);
    this.metadataSharedCollection = this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(
      this.metadataSharedCollection,
      factPowerProduction.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.yearService
      .query()
      .pipe(map((res: HttpResponse<IYear[]>) => res.body ?? []))
      .pipe(map((years: IYear[]) => this.yearService.addYearToCollectionIfMissing<IYear>(years, this.factPowerProduction?.year)))
      .subscribe((years: IYear[]) => (this.yearsSharedCollection = years));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.factPowerProduction?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.technologyService
      .query()
      .pipe(map((res: HttpResponse<ITechnology[]>) => res.body ?? []))
      .pipe(
        map((technologies: ITechnology[]) =>
          this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(technologies, this.factPowerProduction?.technology),
        ),
      )
      .subscribe((technologies: ITechnology[]) => (this.technologiesSharedCollection = technologies));

    this.fuelService
      .query()
      .pipe(map((res: HttpResponse<IFuel[]>) => res.body ?? []))
      .pipe(map((fuels: IFuel[]) => this.fuelService.addFuelToCollectionIfMissing<IFuel>(fuels, this.factPowerProduction?.fuel)))
      .subscribe((fuels: IFuel[]) => (this.fuelsSharedCollection = fuels));

    this.metadataService
      .query()
      .pipe(map((res: HttpResponse<IMetadata[]>) => res.body ?? []))
      .pipe(
        map((metadata: IMetadata[]) =>
          this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(metadata, this.factPowerProduction?.metadata),
        ),
      )
      .subscribe((metadata: IMetadata[]) => (this.metadataSharedCollection = metadata));
  }
}
