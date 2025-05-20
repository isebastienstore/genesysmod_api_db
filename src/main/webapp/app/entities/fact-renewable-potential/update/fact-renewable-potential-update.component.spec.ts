import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IYear } from 'app/entities/year/year.model';
import { YearService } from 'app/entities/year/service/year.service';
import { ITechnology } from 'app/entities/technology/technology.model';
import { TechnologyService } from 'app/entities/technology/service/technology.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { IFactRenewablePotential } from '../fact-renewable-potential.model';
import { FactRenewablePotentialService } from '../service/fact-renewable-potential.service';
import { FactRenewablePotentialFormService } from './fact-renewable-potential-form.service';

import { FactRenewablePotentialUpdateComponent } from './fact-renewable-potential-update.component';

describe('FactRenewablePotential Management Update Component', () => {
  let comp: FactRenewablePotentialUpdateComponent;
  let fixture: ComponentFixture<FactRenewablePotentialUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factRenewablePotentialFormService: FactRenewablePotentialFormService;
  let factRenewablePotentialService: FactRenewablePotentialService;
  let countryService: CountryService;
  let yearService: YearService;
  let technologyService: TechnologyService;
  let metadataService: MetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactRenewablePotentialUpdateComponent],
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
      .overrideTemplate(FactRenewablePotentialUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactRenewablePotentialUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factRenewablePotentialFormService = TestBed.inject(FactRenewablePotentialFormService);
    factRenewablePotentialService = TestBed.inject(FactRenewablePotentialService);
    countryService = TestBed.inject(CountryService);
    yearService = TestBed.inject(YearService);
    technologyService = TestBed.inject(TechnologyService);
    metadataService = TestBed.inject(MetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Country query and add missing value', () => {
      const factRenewablePotential: IFactRenewablePotential = { id: '26a44af4-c24b-4497-907c-1d2ef7f335e5' };
      const country: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factRenewablePotential.country = country;

      const countryCollection: ICountry[] = [{ id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' }];
      jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
      const additionalCountries = [country];
      const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
      jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factRenewablePotential });
      comp.ngOnInit();

      expect(countryService.query).toHaveBeenCalled();
      expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(
        countryCollection,
        ...additionalCountries.map(expect.objectContaining),
      );
      expect(comp.countriesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Year query and add missing value', () => {
      const factRenewablePotential: IFactRenewablePotential = { id: '26a44af4-c24b-4497-907c-1d2ef7f335e5' };
      const year: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factRenewablePotential.year = year;

      const yearCollection: IYear[] = [{ id: '7d487a17-3974-4015-af9f-a5353d384918' }];
      jest.spyOn(yearService, 'query').mockReturnValue(of(new HttpResponse({ body: yearCollection })));
      const additionalYears = [year];
      const expectedCollection: IYear[] = [...additionalYears, ...yearCollection];
      jest.spyOn(yearService, 'addYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factRenewablePotential });
      comp.ngOnInit();

      expect(yearService.query).toHaveBeenCalled();
      expect(yearService.addYearToCollectionIfMissing).toHaveBeenCalledWith(
        yearCollection,
        ...additionalYears.map(expect.objectContaining),
      );
      expect(comp.yearsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Technology query and add missing value', () => {
      const factRenewablePotential: IFactRenewablePotential = { id: '26a44af4-c24b-4497-907c-1d2ef7f335e5' };
      const technology: ITechnology = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
      factRenewablePotential.technology = technology;

      const technologyCollection: ITechnology[] = [{ id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' }];
      jest.spyOn(technologyService, 'query').mockReturnValue(of(new HttpResponse({ body: technologyCollection })));
      const additionalTechnologies = [technology];
      const expectedCollection: ITechnology[] = [...additionalTechnologies, ...technologyCollection];
      jest.spyOn(technologyService, 'addTechnologyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factRenewablePotential });
      comp.ngOnInit();

      expect(technologyService.query).toHaveBeenCalled();
      expect(technologyService.addTechnologyToCollectionIfMissing).toHaveBeenCalledWith(
        technologyCollection,
        ...additionalTechnologies.map(expect.objectContaining),
      );
      expect(comp.technologiesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Metadata query and add missing value', () => {
      const factRenewablePotential: IFactRenewablePotential = { id: '26a44af4-c24b-4497-907c-1d2ef7f335e5' };
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factRenewablePotential.metadata = metadata;

      const metadataCollection: IMetadata[] = [{ id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' }];
      jest.spyOn(metadataService, 'query').mockReturnValue(of(new HttpResponse({ body: metadataCollection })));
      const additionalMetadata = [metadata];
      const expectedCollection: IMetadata[] = [...additionalMetadata, ...metadataCollection];
      jest.spyOn(metadataService, 'addMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factRenewablePotential });
      comp.ngOnInit();

      expect(metadataService.query).toHaveBeenCalled();
      expect(metadataService.addMetadataToCollectionIfMissing).toHaveBeenCalledWith(
        metadataCollection,
        ...additionalMetadata.map(expect.objectContaining),
      );
      expect(comp.metadataSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const factRenewablePotential: IFactRenewablePotential = { id: '26a44af4-c24b-4497-907c-1d2ef7f335e5' };
      const country: ICountry = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
      factRenewablePotential.country = country;
      const year: IYear = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      factRenewablePotential.year = year;
      const technology: ITechnology = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
      factRenewablePotential.technology = technology;
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factRenewablePotential.metadata = metadata;

      activatedRoute.data = of({ factRenewablePotential });
      comp.ngOnInit();

      expect(comp.countriesSharedCollection).toContainEqual(country);
      expect(comp.yearsSharedCollection).toContainEqual(year);
      expect(comp.technologiesSharedCollection).toContainEqual(technology);
      expect(comp.metadataSharedCollection).toContainEqual(metadata);
      expect(comp.factRenewablePotential).toEqual(factRenewablePotential);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactRenewablePotential>>();
      const factRenewablePotential = { id: '582d4161-271b-4a20-af9a-e881eb8f3345' };
      jest.spyOn(factRenewablePotentialFormService, 'getFactRenewablePotential').mockReturnValue(factRenewablePotential);
      jest.spyOn(factRenewablePotentialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factRenewablePotential });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factRenewablePotential }));
      saveSubject.complete();

      // THEN
      expect(factRenewablePotentialFormService.getFactRenewablePotential).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factRenewablePotentialService.update).toHaveBeenCalledWith(expect.objectContaining(factRenewablePotential));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactRenewablePotential>>();
      const factRenewablePotential = { id: '582d4161-271b-4a20-af9a-e881eb8f3345' };
      jest.spyOn(factRenewablePotentialFormService, 'getFactRenewablePotential').mockReturnValue({ id: null });
      jest.spyOn(factRenewablePotentialService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factRenewablePotential: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factRenewablePotential }));
      saveSubject.complete();

      // THEN
      expect(factRenewablePotentialFormService.getFactRenewablePotential).toHaveBeenCalled();
      expect(factRenewablePotentialService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactRenewablePotential>>();
      const factRenewablePotential = { id: '582d4161-271b-4a20-af9a-e881eb8f3345' };
      jest.spyOn(factRenewablePotentialService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factRenewablePotential });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factRenewablePotentialService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCountry', () => {
      it('should forward to countryService', () => {
        const entity = { id: 'a1ca43c7-d3dc-4ed5-b59f-305e45dea973' };
        const entity2 = { id: 'd8127cae-0381-4e62-bed7-eae338eaa9ae' };
        jest.spyOn(countryService, 'compareCountry');
        comp.compareCountry(entity, entity2);
        expect(countryService.compareCountry).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareYear', () => {
      it('should forward to yearService', () => {
        const entity = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
        const entity2 = { id: '70a58d86-c825-46c0-9c7b-bf2627e12900' };
        jest.spyOn(yearService, 'compareYear');
        comp.compareYear(entity, entity2);
        expect(yearService.compareYear).toHaveBeenCalledWith(entity, entity2);
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
