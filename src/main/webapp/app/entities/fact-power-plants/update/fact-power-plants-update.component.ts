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
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { StatusType } from 'app/entities/enumerations/status-type.model';
import { FactPowerPlantsService } from '../service/fact-power-plants.service';
import { IFactPowerPlants } from '../fact-power-plants.model';
import { FactPowerPlantsFormGroup, FactPowerPlantsFormService } from './fact-power-plants-form.service';

@Component({
  selector: 'jhi-fact-power-plants-update',
  templateUrl: './fact-power-plants-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactPowerPlantsUpdateComponent implements OnInit {
  isSaving = false;
  factPowerPlants: IFactPowerPlants | null = null;
  statusTypeValues = Object.keys(StatusType);

  yearsSharedCollection: IYear[] = [];
  countriesSharedCollection: ICountry[] = [];
  technologiesSharedCollection: ITechnology[] = [];
  metadataSharedCollection: IMetadata[] = [];

  protected factPowerPlantsService = inject(FactPowerPlantsService);
  protected factPowerPlantsFormService = inject(FactPowerPlantsFormService);
  protected yearService = inject(YearService);
  protected countryService = inject(CountryService);
  protected technologyService = inject(TechnologyService);
  protected metadataService = inject(MetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactPowerPlantsFormGroup = this.factPowerPlantsFormService.createFactPowerPlantsFormGroup();

  compareYear = (o1: IYear | null, o2: IYear | null): boolean => this.yearService.compareYear(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareTechnology = (o1: ITechnology | null, o2: ITechnology | null): boolean => this.technologyService.compareTechnology(o1, o2);

  compareMetadata = (o1: IMetadata | null, o2: IMetadata | null): boolean => this.metadataService.compareMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factPowerPlants }) => {
      this.factPowerPlants = factPowerPlants;
      if (factPowerPlants) {
        this.updateForm(factPowerPlants);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factPowerPlants = this.factPowerPlantsFormService.getFactPowerPlants(this.editForm);
    if (factPowerPlants.id !== null) {
      this.subscribeToSaveResponse(this.factPowerPlantsService.update(factPowerPlants));
    } else {
      this.subscribeToSaveResponse(this.factPowerPlantsService.create(factPowerPlants));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactPowerPlants>>): void {
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

  protected updateForm(factPowerPlants: IFactPowerPlants): void {
    this.factPowerPlants = factPowerPlants;
    this.factPowerPlantsFormService.resetForm(this.editForm, factPowerPlants);

    this.yearsSharedCollection = this.yearService.addYearToCollectionIfMissing<IYear>(
      this.yearsSharedCollection,
      factPowerPlants.commissioningDate,
      factPowerPlants.decommissioningDate,
    );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      factPowerPlants.country,
    );
    this.technologiesSharedCollection = this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(
      this.technologiesSharedCollection,
      factPowerPlants.technology,
    );
    this.metadataSharedCollection = this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(
      this.metadataSharedCollection,
      factPowerPlants.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.yearService
      .query()
      .pipe(map((res: HttpResponse<IYear[]>) => res.body ?? []))
      .pipe(
        map((years: IYear[]) =>
          this.yearService.addYearToCollectionIfMissing<IYear>(
            years,
            this.factPowerPlants?.commissioningDate,
            this.factPowerPlants?.decommissioningDate,
          ),
        ),
      )
      .subscribe((years: IYear[]) => (this.yearsSharedCollection = years));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.factPowerPlants?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.technologyService
      .query()
      .pipe(map((res: HttpResponse<ITechnology[]>) => res.body ?? []))
      .pipe(
        map((technologies: ITechnology[]) =>
          this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(technologies, this.factPowerPlants?.technology),
        ),
      )
      .subscribe((technologies: ITechnology[]) => (this.technologiesSharedCollection = technologies));

    this.metadataService
      .query()
      .pipe(map((res: HttpResponse<IMetadata[]>) => res.body ?? []))
      .pipe(
        map((metadata: IMetadata[]) =>
          this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(metadata, this.factPowerPlants?.metadata),
        ),
      )
      .subscribe((metadata: IMetadata[]) => (this.metadataSharedCollection = metadata));
  }
}
