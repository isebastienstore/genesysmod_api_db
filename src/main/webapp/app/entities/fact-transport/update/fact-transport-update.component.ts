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
import { ModalType } from 'app/entities/enumerations/modal-type.model';
import { FactTransportService } from '../service/fact-transport.service';
import { IFactTransport } from '../fact-transport.model';
import { FactTransportFormGroup, FactTransportFormService } from './fact-transport-form.service';

@Component({
  selector: 'jhi-fact-transport-update',
  templateUrl: './fact-transport-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactTransportUpdateComponent implements OnInit {
  isSaving = false;
  factTransport: IFactTransport | null = null;
  modalTypeValues = Object.keys(ModalType);

  yearsSharedCollection: IYear[] = [];
  countriesSharedCollection: ICountry[] = [];
  metadataSharedCollection: IMetadata[] = [];

  protected factTransportService = inject(FactTransportService);
  protected factTransportFormService = inject(FactTransportFormService);
  protected yearService = inject(YearService);
  protected countryService = inject(CountryService);
  protected metadataService = inject(MetadataService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactTransportFormGroup = this.factTransportFormService.createFactTransportFormGroup();

  compareYear = (o1: IYear | null, o2: IYear | null): boolean => this.yearService.compareYear(o1, o2);

  compareCountry = (o1: ICountry | null, o2: ICountry | null): boolean => this.countryService.compareCountry(o1, o2);

  compareMetadata = (o1: IMetadata | null, o2: IMetadata | null): boolean => this.metadataService.compareMetadata(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factTransport }) => {
      this.factTransport = factTransport;
      if (factTransport) {
        this.updateForm(factTransport);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factTransport = this.factTransportFormService.getFactTransport(this.editForm);
    if (factTransport.id !== null) {
      this.subscribeToSaveResponse(this.factTransportService.update(factTransport));
    } else {
      this.subscribeToSaveResponse(this.factTransportService.create(factTransport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactTransport>>): void {
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

  protected updateForm(factTransport: IFactTransport): void {
    this.factTransport = factTransport;
    this.factTransportFormService.resetForm(this.editForm, factTransport);

    this.yearsSharedCollection = this.yearService.addYearToCollectionIfMissing<IYear>(this.yearsSharedCollection, factTransport.year);
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing<ICountry>(
      this.countriesSharedCollection,
      factTransport.country,
    );
    this.metadataSharedCollection = this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(
      this.metadataSharedCollection,
      factTransport.metadata,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.yearService
      .query()
      .pipe(map((res: HttpResponse<IYear[]>) => res.body ?? []))
      .pipe(map((years: IYear[]) => this.yearService.addYearToCollectionIfMissing<IYear>(years, this.factTransport?.year)))
      .subscribe((years: IYear[]) => (this.yearsSharedCollection = years));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing<ICountry>(countries, this.factTransport?.country),
        ),
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.metadataService
      .query()
      .pipe(map((res: HttpResponse<IMetadata[]>) => res.body ?? []))
      .pipe(
        map((metadata: IMetadata[]) =>
          this.metadataService.addMetadataToCollectionIfMissing<IMetadata>(metadata, this.factTransport?.metadata),
        ),
      )
      .subscribe((metadata: IMetadata[]) => (this.metadataSharedCollection = metadata));
  }
}
