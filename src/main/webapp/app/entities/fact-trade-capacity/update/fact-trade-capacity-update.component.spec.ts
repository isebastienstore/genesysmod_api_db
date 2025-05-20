import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IYear } from 'app/entities/year/year.model';
import { YearService } from 'app/entities/year/service/year.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IFuel } from 'app/entities/fuel/fuel.model';
import { FuelService } from 'app/entities/fuel/service/fuel.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { IFactTradeCapacity } from '../fact-trade-capacity.model';
import { FactTradeCapacityService } from '../service/fact-trade-capacity.service';
import { FactTradeCapacityFormService } from './fact-trade-capacity-form.service';

import { FactTradeCapacityUpdateComponent } from './fact-trade-capacity-update.component';

describe('FactTradeCapacity Management Update Component', () => {
  let comp: FactTradeCapacityUpdateComponent;
  let fixture: ComponentFixture<FactTradeCapacityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factTradeCapacityFormService: FactTradeCapacityFormService;
  let factTradeCapacityService: FactTradeCapacityService;
  let yearService: YearService;
  let countryService: CountryService;
  let fuelService: FuelService;
  let metadataService: MetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactTradeCapacityUpdateComponent],
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
      .overrideTemplate(FactTradeCapacityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactTradeCapacityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factTradeCapacityFormService = TestBed.inject(FactTradeCapacityFormService);
    factTradeCapacityService = TestBed.inject(FactTradeCapacityService);
    yearService = TestBed.inject(YearService);
    countryService = TestBed.inject(CountryService);
    fuelService = TestBed.inject(FuelService);
    metadataService = TestBed.inject(MetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Year query and add missing value', () => {
      const factTradeCapacity: IFactTradeCapacity = { id: '9ba6855f-5fa4-4323-b118-f92810f2a9ec' };
      const year: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factTradeCapacity.year = year;

      const yearCollection: IYear[] = [{ id: '7d487a17-3974-4015-af9f-a5353d384918' }];
      jest.spyOn(yearService, 'query').mockReturnValue(of(new HttpResponse({ body: yearCollection })));
      const additionalYears = [year];
      const expectedCollection: IYear[] = [...additionalYears, ...yearCollection];
      jest.spyOn(yearService, 'addYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factTradeCapacity });
      comp.ngOnInit();

      expect(yearService.query).toHaveBeenCalled();
      expect(yearService.addYearToCollectionIfMissing).toHaveBeenCalledWith(
        yearCollection,
        ...additionalYears.map(expect.objectContaining),
      );
      expect(comp.yearsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Country query and add missing value', () => {
      const factTradeCapacity: IFactTradeCapacity = { id: '9ba6855f-5fa4-4323-b118-f92810f2a9ec' };
      const country1: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factTradeCapacity.country1 = country1;
      const country2: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factTradeCapacity.country2 = country2;

      const countryCollection: ICountry[] = [{ id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country1, country2];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factTradeCapacity });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Fuel query and add missing value', () => {
      const factTradeCapacity: IFactTradeCapacity = { id: '9ba6855f-5fa4-4323-b118-f92810f2a9ec' };
      const fuel: IFuel = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
      factTradeCapacity.fuel = fuel;

      const fuelCollection: IFuel[] = [{ id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' }];
      jest.spyOn(fuelService, 'query').mockReturnValue(of(new HttpResponse({ body: fuelCollection })));
      const additionalFuels = [fuel];
      const expectedCollection: IFuel[] = [...additionalFuels, ...fuelCollection];
      jest.spyOn(fuelService, 'addFuelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factTradeCapacity });
      comp.ngOnInit();

      expect(fuelService.query).toHaveBeenCalled();
      expect(fuelService.addFuelToCollectionIfMissing).toHaveBeenCalledWith(
        fuelCollection,
        ...additionalFuels.map(expect.objectContaining),
      );
      expect(comp.fuelsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Metadata query and add missing value', () => {
      const factTradeCapacity: IFactTradeCapacity = { id: '9ba6855f-5fa4-4323-b118-f92810f2a9ec' };
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factTradeCapacity.metadata = metadata;

      const metadataCollection: IMetadata[] = [{ id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' }];
      jest.spyOn(metadataService, 'query').mockReturnValue(of(new HttpResponse({ body: metadataCollection })));
      const additionalMetadata = [metadata];
      const expectedCollection: IMetadata[] = [...additionalMetadata, ...metadataCollection];
      jest.spyOn(metadataService, 'addMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factTradeCapacity });
      comp.ngOnInit();

      expect(metadataService.query).toHaveBeenCalled();
      expect(metadataService.addMetadataToCollectionIfMissing).toHaveBeenCalledWith(
        metadataCollection,
        ...additionalMetadata.map(expect.objectContaining),
      );
      expect(comp.metadataSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const factTradeCapacity: IFactTradeCapacity = { id: '9ba6855f-5fa4-4323-b118-f92810f2a9ec' };
      const year: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factTradeCapacity.year = year;
      const country1: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factTradeCapacity.country1 = country1;
      const country2: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factTradeCapacity.country2 = country2;
      const fuel: IFuel = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
      factTradeCapacity.fuel = fuel;
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factTradeCapacity.metadata = metadata;

      activatedRoute.data = of({ factTradeCapacity });
      comp.ngOnInit();

      expect(comp.yearsSharedCollection).toContainEqual(year);
      expect(comp.countriesSharedCollection).toContainEqual(country1);
      expect(comp.countriesSharedCollection).toContainEqual(country2);
      expect(comp.fuelsSharedCollection).toContainEqual(fuel);
      expect(comp.metadataSharedCollection).toContainEqual(metadata);
      expect(comp.factTradeCapacity).toEqual(factTradeCapacity);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactTradeCapacity>>();
      const factTradeCapacity = { id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' };
      jest.spyOn(factTradeCapacityFormService, 'getFactTradeCapacity').mockReturnValue(factTradeCapacity);
      jest.spyOn(factTradeCapacityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factTradeCapacity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factTradeCapacity }));
      saveSubject.complete();

      // THEN
      expect(factTradeCapacityFormService.getFactTradeCapacity).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factTradeCapacityService.update).toHaveBeenCalledWith(expect.objectContaining(factTradeCapacity));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactTradeCapacity>>();
      const factTradeCapacity = { id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' };
      jest.spyOn(factTradeCapacityFormService, 'getFactTradeCapacity').mockReturnValue({ id: null });
      jest.spyOn(factTradeCapacityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factTradeCapacity: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factTradeCapacity }));
      saveSubject.complete();

      // THEN
      expect(factTradeCapacityFormService.getFactTradeCapacity).toHaveBeenCalled();
      expect(factTradeCapacityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactTradeCapacity>>();
      const factTradeCapacity = { id: 'ebd45b84-4255-4475-927b-8cf7f0a38e70' };
      jest.spyOn(factTradeCapacityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factTradeCapacity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factTradeCapacityService.update).toHaveBeenCalled();
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

    describe('compareFuel', () => {
      it('should forward to fuelService', () => {
        const entity = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
        const entity2 = { id: '5eb32af1-969a-475f-9f87-eb7f9dc4de39' };
        jest.spyOn(fuelService, 'compareFuel');
        comp.compareFuel(entity, entity2);
        expect(fuelService.compareFuel).toHaveBeenCalledWith(entity, entity2);
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
