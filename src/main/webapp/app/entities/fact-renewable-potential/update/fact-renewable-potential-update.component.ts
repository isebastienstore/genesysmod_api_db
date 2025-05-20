import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IYear } from 'app/entities/year/year.model';
import { YearService } from 'app/entities/year/service/year.service';
import { ITechnology } from 'app/entities/technology/technology.model';
import { TechnologyService } from 'app/entities/technology/service/technology.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { FactRenewablePotentialService } from '../service/fact-renewable-potential.service';
import { IFactRenewablePotential } from '../fact-renewable-potential.model';
import { FactRenewablePotentialFormGroup, FactRenewablePotentialFormService } from './fact-renewable-potential-form.service';

@Component({
  selector: 'jhi-fact-renewable-potential-update',
  templateUrl: './fact-renewable-potential-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactRenewablePotentialUpdateComponent implements OnInit {
  isSaving = false;
  factRenewablePotential: IFactRenewablePotential | null = null;

  countriesSharedCollection: ICountry[] = [];
  yearsSharedCollection: IYear[] = [];
  technologiesSharedCollection: ITechnology[] = [];
  metadataSharedCollection: IMetadata[] = [];

  protected factRenewablePotentialService = inject(FactRenewablePotentialService);
  protected factRenewablePotentialFormService = inject(FactRenewablePotentialFormService);
  protected countryService = inject(CountryService);
  protected yearService = inject(YearService);
  protected technologyService = inject(TechnologyService);
  protected metadataService = inject(MetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactRenewablePotentialFormGroup = this.factRenewablePotentialFormService.createFactRenewablePotentialFormGroup();

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareYear = (o1: IYear | null, o2: IYear | null): boolean => this.yearService.compareYear(o1, o2);

  compareTechnology = (o1: ITechnology | null, o2: ITechnology | null): boolean => this.technologyService.compareTechnology(o1, o2);

  compareMetadata = (o1: IMetadata | null, o2: IMetadata | null): boolean => this.metadataService.compareMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factRenewablePotential }) => {
      this.factRenewablePotential = factRenewablePotential;
      if (factRenewablePotential) {
        this.updateForm(factRenewablePotential);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factRenewablePotential = this.factRenewablePotentialFormService.getFactRenewablePotential(this.editForm);
    if (factRenewablePotential.id !== null) {
      this.subscribeToSaveResponse(this.factRenewablePotentialService.update(factRenewablePotential));
    } else {
      this.subscribeToSaveResponse(this.factRenewablePotentialService.create(factRenewablePotential));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactRenewablePotential>>): void {
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

  protected updateForm(factRenewablePotential: IFactRenewablePotential): void {
    this.factRenewablePotential = factRenewablePotential;
    this.factRenewablePotentialFormService.resetForm(this.editForm, factRenewablePotential);

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      factRenewablePotential.country,
    );
    this.yearsSharedCollection = this.yearService.addYearToCollectionIfMissing<IYear>(
      this.yearsSharedCollection,
      factRenewablePotential.year,
    );
    this.technologiesSharedCollection = this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(
      this.technologiesSharedCollection,
      factRenewablePotential.technology,
    );
    this.metadataSharedCollection = this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(
      this.metadataSharedCollection,
      factRenewablePotential.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.factRenewablePotential?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.yearService
      .query()
      .pipe(map((res: HttpResponse<IYear[]>) => res.body ?? []))
      .pipe(map((years: IYear[]) => this.yearService.addYearToCollectionIfMissing<IYear>(years, this.factRenewablePotential?.year)))
      .subscribe((years: IYear[]) => (this.yearsSharedCollection = years));

    this.technologyService
      .query()
      .pipe(map((res: HttpResponse<ITechnology[]>) => res.body ?? []))
      .pipe(
        map((technologies: ITechnology[]) =>
          this.technologyService.addTechnologyToCollectionIfMissing<ITechnology>(technologies, this.factRenewablePotential?.technology),
        ),
      )
      .subscribe((technologies: ITechnology[]) => (this.technologiesSharedCollection = technologies));

    this.metadataService
      .query()
      .pipe(map((res: HttpResponse<IMetadata[]>) => res.body ?? []))
      .pipe(
        map((metadata: IMetadata[]) =>
          this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(metadata, this.factRenewablePotential?.metadata),
        ),
      )
      .subscribe((metadata: IMetadata[]) => (this.metadataSharedCollection = metadata));
  }
}
