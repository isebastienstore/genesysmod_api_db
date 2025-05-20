import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IYear } from 'app/entities/year/year.model';
import { YearService } from 'app/entities/year/service/year.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { ITechnology } from 'app/entities/technology/technology.model';
import { TechnologyService } from 'app/entities/technology/service/technology.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { IFactPowerPlants } from '../fact-power-plants.model';
import { FactPowerPlantsService } from '../service/fact-power-plants.service';
import { FactPowerPlantsFormService } from './fact-power-plants-form.service';

import { FactPowerPlantsUpdateComponent } from './fact-power-plants-update.component';

describe('FactPowerPlants Management Update Component', () => {
  let comp: FactPowerPlantsUpdateComponent;
  let fixture: ComponentFixture<FactPowerPlantsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factPowerPlantsFormService: FactPowerPlantsFormService;
  let factPowerPlantsService: FactPowerPlantsService;
  let yearService: YearService;
  let countryService: CountryService;
  let technologyService: TechnologyService;
  let metadataService: MetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactPowerPlantsUpdateComponent],
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
      .overrideTemplate(FactPowerPlantsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactPowerPlantsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factPowerPlantsFormService = TestBed.inject(FactPowerPlantsFormService);
    factPowerPlantsService = TestBed.inject(FactPowerPlantsService);
    yearService = TestBed.inject(YearService);
    countryService = TestBed.inject(CountryService);
    technologyService = TestBed.inject(TechnologyService);
    metadataService = TestBed.inject(MetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Year query and add missing value', () => {
      const factPowerPlants: IFactPowerPlants = { id: 'd4810c1a-a1fd-4d59-912c-efae2e48d473' };
      const commissioningDate: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factPowerPlants.commissioningDate = commissioningDate;
      const decommissioningDate: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factPowerPlants.decommissioningDate = decommissioningDate;

      const yearCollection: IYear[] = [{ id: '7d487a17-3974-4015-af9f-a5353d384918' }];
      jest.spyOn(yearService, 'query').mockReturnValue(of(new HttpResponse({ body: yearCollection })));
      const additionalYears = [commissioningDate, decommissioningDate];
      const expectedCollection: IYear[] = [...additionalYears, ...yearCollection];
      jest.spyOn(yearService, 'addYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factPowerPlants });
      comp.ngOnInit();

      expect(yearService.query).toHaveBeenCalled();
      expect(yearService.addYearToCollectionIfMissing).toHaveBeenCalledWith(
        yearCollection,
        ...additionalYears.map(expect.objectContaining),
      );
      expect(comp.yearsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Country query and add missing value', () => {
      const factPowerPlants: IFactPowerPlants = { id: 'd4810c1a-a1fd-4d59-912c-efae2e48d473' };
      const country: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factPowerPlants.country = country;

      const countryCollection: ICountry[] = [{ id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factPowerPlants });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Technology query and add missing value', () => {
      const factPowerPlants: IFactPowerPlants = { id: 'd4810c1a-a1fd-4d59-912c-efae2e48d473' };
      const technology: ITechnology = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
      factPowerPlants.technology = technology;

      const technologyCollection: ITechnology[] = [{ id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' }];
      jest.spyOn(technologyService, 'query').mockReturnValue(of(new HttpResponse({ body: technologyCollection })));
      const additionalTechnologies = [technology];
      const expectedCollection: ITechnology[] = [...additionalTechnologies, ...technologyCollection];
      jest.spyOn(technologyService, 'addTechnologyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factPowerPlants });
      comp.ngOnInit();

      expect(technologyService.query).toHaveBeenCalled();
      expect(technologyService.addTechnologyToCollectionIfMissing).toHaveBeenCalledWith(
        technologyCollection,
        ...additionalTechnologies.map(expect.objectContaining),
      );
      expect(comp.technologiesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Metadata query and add missing value', () => {
      const factPowerPlants: IFactPowerPlants = { id: 'd4810c1a-a1fd-4d59-912c-efae2e48d473' };
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factPowerPlants.metadata = metadata;

      const metadataCollection: IMetadata[] = [{ id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' }];
      jest.spyOn(metadataService, 'query').mockReturnValue(of(new HttpResponse({ body: metadataCollection })));
      const additionalMetadata = [metadata];
      const expectedCollection: IMetadata[] = [...additionalMetadata, ...metadataCollection];
      jest.spyOn(metadataService, 'addMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factPowerPlants });
      comp.ngOnInit();

      expect(metadataService.query).toHaveBeenCalled();
      expect(metadataService.addMetadataToCollectionIfMissing).toHaveBeenCalledWith(
        metadataCollection,
        ...additionalMetadata.map(expect.objectContaining),
      );
      expect(comp.metadataSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const factPowerPlants: IFactPowerPlants = { id: 'd4810c1a-a1fd-4d59-912c-efae2e48d473' };
      const commissioningDate: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factPowerPlants.commissioningDate = commissioningDate;
      const decommissioningDate: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factPowerPlants.decommissioningDate = decommissioningDate;
      const country: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factPowerPlants.country = country;
      const technology: ITechnology = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
      factPowerPlants.technology = technology;
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factPowerPlants.metadata = metadata;

      activatedRoute.data = of({ factPowerPlants });
      comp.ngOnInit();

      expect(comp.yearsSharedCollection).toContainEqual(commissioningDate);
      expect(comp.yearsSharedCollection).toContainEqual(decommissioningDate);
      expect(comp.countriesSharedCollection).toContainEqual(country);
      expect(comp.technologiesSharedCollection).toContainEqual(technology);
      expect(comp.metadataSharedCollection).toContainEqual(metadata);
      expect(comp.factPowerPlants).toEqual(factPowerPlants);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactPowerPlants>>();
      const factPowerPlants = { id: '9bd7289f-8607-48ac-9984-c3804adbb150' };
      jest.spyOn(factPowerPlantsFormService, 'getFactPowerPlants').mockReturnValue(factPowerPlants);
      jest.spyOn(factPowerPlantsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factPowerPlants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factPowerPlants }));
      saveSubject.complete();

      // THEN
      expect(factPowerPlantsFormService.getFactPowerPlants).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factPowerPlantsService.update).toHaveBeenCalledWith(expect.objectContaining(factPowerPlants));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactPowerPlants>>();
      const factPowerPlants = { id: '9bd7289f-8607-48ac-9984-c3804adbb150' };
      jest.spyOn(factPowerPlantsFormService, 'getFactPowerPlants').mockReturnValue({ id: null });
      jest.spyOn(factPowerPlantsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factPowerPlants: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factPowerPlants }));
      saveSubject.complete();

      // THEN
      expect(factPowerPlantsFormService.getFactPowerPlants).toHaveBeenCalled();
      expect(factPowerPlantsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactPowerPlants>>();
      const factPowerPlants = { id: '9bd7289f-8607-48ac-9984-c3804adbb150' };
      jest.spyOn(factPowerPlantsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factPowerPlants });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factPowerPlantsService.update).toHaveBeenCalled();
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

    describe('compareTechnology', () => {
      it('should forward to technologyService', () => {
        const entity = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
        const entity2 = { id: '487ab60d-d042-4abc-9586-ecb3e3b9550d' };
        jest.spyOn(technologyService, 'compareTechnology');
        comp.compareTechnology(entity, entity2);
        expect(technologyService.compareTechnology).toHaveBeenCalledWith(entity, entity2);
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
