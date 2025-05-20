import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IFuel } from 'app/entities/fuel/fuel.model';
import { FuelService } from 'app/entities/fuel/service/fuel.service';
import { IMetadata } from 'app/entities/metadata/metadata.model';
import { MetadataService } from 'app/entities/metadata/service/metadata.service';
import { IFactTradeCost } from '../fact-trade-cost.model';
import { FactTradeCostService } from '../service/fact-trade-cost.service';
import { FactTradeCostFormService } from './fact-trade-cost-form.service';

import { FactTradeCostUpdateComponent } from './fact-trade-cost-update.component';

describe('FactTradeCost Management Update Component', () => {
  let comp: FactTradeCostUpdateComponent;
  let fixture: ComponentFixture<FactTradeCostUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factTradeCostFormService: FactTradeCostFormService;
  let factTradeCostService: FactTradeCostService;
  let fuelService: FuelService;
  let metadataService: MetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactTradeCostUpdateComponent],
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
      .overrideTemplate(FactTradeCostUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactTradeCostUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factTradeCostFormService = TestBed.inject(FactTradeCostFormService);
    factTradeCostService = TestBed.inject(FactTradeCostService);
    fuelService = TestBed.inject(FuelService);
    metadataService = TestBed.inject(MetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Fuel query and add missing value', () => {
      const factTradeCost: IFactTradeCost = { id: '5a917248-626f-45b4-aca9-ee51466a0b6a' };
      const fuel: IFuel = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
      factTradeCost.fuel = fuel;

      const fuelCollection: IFuel[] = [{ id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' }];
      jest.spyOn(fuelService, 'query').mockReturnValue(of(new HttpResponse({ body: fuelCollection })));
      const additionalFuels = [fuel];
      const expectedCollection: IFuel[] = [...additionalFuels, ...fuelCollection];
      jest.spyOn(fuelService, 'addFuelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factTradeCost });
      comp.ngOnInit();

      expect(fuelService.query).toHaveBeenCalled();
      expect(fuelService.addFuelToCollectionIfMissing).toHaveBeenCalledWith(
        fuelCollection,
        ...additionalFuels.map(expect.objectContaining),
      );
      expect(comp.fuelsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Metadata query and add missing value', () => {
      const factTradeCost: IFactTradeCost = { id: '5a917248-626f-45b4-aca9-ee51466a0b6a' };
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factTradeCost.metadata = metadata;

      const metadataCollection: IMetadata[] = [{ id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' }];
      jest.spyOn(metadataService, 'query').mockReturnValue(of(new HttpResponse({ body: metadataCollection })));
      const additionalMetadata = [metadata];
      const expectedCollection: IMetadata[] = [...additionalMetadata, ...metadataCollection];
      jest.spyOn(metadataService, 'addMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ factTradeCost });
      comp.ngOnInit();

      expect(metadataService.query).toHaveBeenCalled();
      expect(metadataService.addMetadataToCollectionIfMissing).toHaveBeenCalledWith(
        metadataCollection,
        ...additionalMetadata.map(expect.objectContaining),
      );
      expect(comp.metadataSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const factTradeCost: IFactTradeCost = { id: '5a917248-626f-45b4-aca9-ee51466a0b6a' };
      const fuel: IFuel = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
      factTradeCost.fuel = fuel;
      const metadata: IMetadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      factTradeCost.metadata = metadata;

      activatedRoute.data = of({ factTradeCost });
      comp.ngOnInit();

      expect(comp.fuelsSharedCollection).toContainEqual(fuel);
      expect(comp.metadataSharedCollection).toContainEqual(metadata);
      expect(comp.factTradeCost).toEqual(factTradeCost);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactTradeCost>>();
      const factTradeCost = { id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' };
      jest.spyOn(factTradeCostFormService, 'getFactTradeCost').mockReturnValue(factTradeCost);
      jest.spyOn(factTradeCostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factTradeCost });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factTradeCost }));
      saveSubject.complete();

      // THEN
      expect(factTradeCostFormService.getFactTradeCost).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factTradeCostService.update).toHaveBeenCalledWith(expect.objectContaining(factTradeCost));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactTradeCost>>();
      const factTradeCost = { id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' };
      jest.spyOn(factTradeCostFormService, 'getFactTradeCost').mockReturnValue({ id: null });
      jest.spyOn(factTradeCostService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factTradeCost: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: factTradeCost }));
      saveSubject.complete();

      // THEN
      expect(factTradeCostFormService.getFactTradeCost).toHaveBeenCalled();
      expect(factTradeCostService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFactTradeCost>>();
      const factTradeCost = { id: 'cf07c929-0eb5-4e6c-8753-8a751d3f8996' };
      jest.spyOn(factTradeCostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ factTradeCost });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factTradeCostService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
