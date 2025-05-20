import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { FuelService } from '../service/fuel.service';
import { IFuel } from '../fuel.model';
import { FuelFormService } from './fuel-form.service';

import { FuelUpdateComponent } from './fuel-update.component';

describe('Fuel Management Update Component', () => {
  let comp: FuelUpdateComponent;
  let fixture: ComponentFixture<FuelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fuelFormService: FuelFormService;
  let fuelService: FuelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FuelUpdateComponent],
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
      .overrideTemplate(FuelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fuelFormService = TestBed.inject(FuelFormService);
    fuelService = TestBed.inject(FuelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fuel: IFuel = { id: '5eb32af1-969a-475f-9f87-eb7f9dc4de39' };

      activatedRoute.data = of({ fuel });
      comp.ngOnInit();

      expect(comp.fuel).toEqual(fuel);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuel>>();
      const fuel = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
      jest.spyOn(fuelFormService, 'getFuel').mockReturnValue(fuel);
      jest.spyOn(fuelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fuel }));
      saveSubject.complete();

      // THEN
      expect(fuelFormService.getFuel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fuelService.update).toHaveBeenCalledWith(expect.objectContaining(fuel));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuel>>();
      const fuel = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
      jest.spyOn(fuelFormService, 'getFuel').mockReturnValue({ id: null });
      jest.spyOn(fuelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fuel }));
      saveSubject.complete();

      // THEN
      expect(fuelFormService.getFuel).toHaveBeenCalled();
      expect(fuelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFuel>>();
      const fuel = { id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' };
      jest.spyOn(fuelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fuelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
