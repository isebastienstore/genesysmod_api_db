import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IYear } from 'app/entities/year/year.model';
import { YearService } from 'app/entities/year/service/year.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { IFactElectricityConsumption } from '../fact-electricity-consumption.model';
import { FactElectricityConsumptionService } from '../service/fact-electricity-consumption.service';
import { FactElectricityConsumptionFormService } from './fact-electricity-consumption-form.service';

import { FactElectricityConsumptionUpdateComponent } from './fact-electricity-consumption-update.component';

describe('FactElectricityConsumption Management Update Component', () => {
  let comp: FactElectricityConsumptionUpdateComponent;
  let fixture: ComponentFixture<FactElectricityConsumptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factElectricityConsumptionFormService: FactElectricityConsumptionFormService;
  let factElectricityConsumptionService: FactElectricityConsumptionService;
  let yearService: YearService;
  let countryService: CountryService;
  let metadataService: MetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactElectricityConsumptionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FactElectricityConsumptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactElectricityConsumptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factElectricityConsumptionFormService = TestBed.inject(FactElectricityConsumptionFormService);
    factElectricityConsumptionService = TestBed.inject(FactElectricityConsumptionService);
    yearService = TestBed.inject(YearService);
    countryService = TestBed.inject(CountryService);
    metadataService = TestBed.inject(MetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Year query and add missing value', () => {
      const factElectricityConsumption: IFactElectricityConsumption = { id: '1f7a05ca-a240-4503-b4bc-d76a44933d59' };
      const year: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factElectricityConsumption.year = year;

      const yearCollection: IYear[] = [{ id: '7d487a17-3974-4015-af9f-a5353d384918' }];
      jest.spyOn(yearService, 'query').mockReturnValue(of(new HttpResponse({ body: yearCollection })));
      const additionalYears = [year];
      const expectedCollection: IYear[] = [...additionalYears, ...yearCollection];
      jest.spyOn(yearService, 'addYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factElectricityConsumption });
      comp.ngOnInit();

      expect(yearService.query).toHaveBeenCalled();
      expect(yearService.addYearToCollectionIfMissing).toHaveBeenCalledWith(
        yearCollection,
        ...additionalYears.map(expect.objectContaining),
      );
      expect(comp.yearsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Country query and add missing value', () => {
      const factElectricityConsumption: IFactElectricityConsumption = { id: '1f7a05ca-a240-4503-b4bc-d76a44933d59' };
      const country: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factElectricityConsumption.country = country;

      const countryCollection: ICountry[] = [{ id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factElectricityConsumption });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Metadata query and add missing value', () => {
      const factElectricityConsumption: IFactElectricityConsumption = { id: '1f7a05ca-a240-4503-b4bc-d76a44933d59' };
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factElectricityConsumption.metadata = metadata;

      const metadataCollection: IMetadata[] = [{ id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' }];
      jest.spyOn(metadataService, 'query').mockReturnValue(of(new HttpResponse({ body: metadataCollection })));
      const additionalMetadata = [metadata];
      const expectedCollection: IMetadata[] = [...additionalMetadata, ...metadataCollection];
      jest.spyOn(metadataService, 'addMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factElectricityConsumption });
      comp.ngOnInit();

      expect(metadataService.query).toHaveBeenCalled();
      expect(metadataService.addMetadataToCollectionIfMissing).toHaveBeenCalledWith(
        metadataCollection,
        ...additionalMetadata.map(expect.objectContaining),
      );
      expect(comp.metadataSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const factElectricityConsumption: IFactElectricityConsumption = { id: '1f7a05ca-a240-4503-b4bc-d76a44933d59' };
      const year: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factElectricityConsumption.year = year;
      const country: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factElectricityConsumption.country = country;
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factElectricityConsumption.metadata = metadata;

      activatedRoute.data = of({ factElectricityConsumption });
      comp.ngOnInit();

      expect(comp.yearsSharedCollection).toContainEqual(year);
      expect(comp.countriesSharedCollection).toContainEqual(country);
      expect(comp.metadataSharedCollection).toContainEqual(metadata);
      expect(comp.factElectricityConsumption).toEqual(factElectricityConsumption);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactElectricityConsumption>>();
      const factElectricityConsumption = { id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' };
      jest.spyOn(factElectricityConsumptionFormService, 'getFactElectricityConsumption').mockReturnValue(factElectricityConsumption);
      jest.spyOn(factElectricityConsumptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factElectricityConsumption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factElectricityConsumption }));
      saveSubject.complete();

      // THEN
      expect(factElectricityConsumptionFormService.getFactElectricityConsumption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factElectricityConsumptionService.update).toHaveBeenCalledWith(expect.objectContaining(factElectricityConsumption));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactElectricityConsumption>>();
      const factElectricityConsumption = { id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' };
      jest.spyOn(factElectricityConsumptionFormService, 'getFactElectricityConsumption').mockReturnValue({ id: null });
      jest.spyOn(factElectricityConsumptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factElectricityConsumption: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factElectricityConsumption }));
      saveSubject.complete();

      // THEN
      expect(factElectricityConsumptionFormService.getFactElectricityConsumption).toHaveBeenCalled();
      expect(factElectricityConsumptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactElectricityConsumption>>();
      const factElectricityConsumption = { id: '9bbbd317-5807-45f2-b8ef-4f4cbc54f9b5' };
      jest.spyOn(factElectricityConsumptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factElectricityConsumption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factElectricityConsumptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareYear', () => {
      it('should forward to yearService', () => {
        const entity = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
        const entity2 = { id: '70a58d86-c825-46c0-9c7b-bf2627e12900' };
        jest.spyOn(yearService, 'compareYear');
        comp.compareYear(entity, entity2);
        expect(yearService.compareYear).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCountry', () => {
      it('should forward to countryService', () => {
        const entity = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
        const entity2 = { id: 'd8127cae-0381-4e62-bed7-eae338eaa9ae' };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMetadata', () => {
      it('should forward to metadataService', () => {
        const entity = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
        const entity2 = { id: '7c8cb81a-eae2-4ec8-91d4-a5a5ec343fa8' };
        jest.spyOn(metadataService, 'compareMetadata');
        comp.compareMetadata(entity, entity2);
        expect(metadataService.compareMetadata).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
