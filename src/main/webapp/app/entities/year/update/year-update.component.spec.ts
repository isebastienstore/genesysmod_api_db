import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { YearService } from '../service/year.service';
import { IYear } from '../year.model';
import { YearFormService } from './year-form.service';

import { YearUpdateComponent } from './year-update.component';

describe('Year Management Update Component', () => {
  let comp: YearUpdateComponent;
  let fixture: ComponentFixture<YearUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let yearFormService: YearFormService;
  let yearService: YearService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [YearUpdateComponent],
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
      .overrideTemplate(YearUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(YearUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    yearFormService = TestBed.inject(YearFormService);
    yearService = TestBed.inject(YearService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const year: IYear = { id: '70a58d86-c825-46c0-9c7b-bf2627e12900' };

      activatedRoute.data = of({ year });
      comp.ngOnInit();

      expect(comp.year).toEqual(year);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IYear>>();
      const year = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      jest.spyOn(yearFormService, 'getYear').mockReturnValue(year);
      jest.spyOn(yearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ year });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: year }));
      saveSubject.complete();

      // THEN
      expect(yearFormService.getYear).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(yearService.update).toHaveBeenCalledWith(expect.objectContaining(year));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IYear>>();
      const year = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      jest.spyOn(yearFormService, 'getYear').mockReturnValue({ id: null });
      jest.spyOn(yearService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ year: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: year }));
      saveSubject.complete();

      // THEN
      expect(yearFormService.getYear).toHaveBeenCalled();
      expect(yearService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IYear>>();
      const year = { id: '7d487a17-3974-4015-af9f-a5353d384918' };
      jest.spyOn(yearService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ year });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(yearService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
