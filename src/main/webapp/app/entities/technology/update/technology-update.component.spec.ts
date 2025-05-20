import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TechnologyService } from '../service/technology.service';
import { ITechnology } from '../technology.model';
import { TechnologyFormService } from './technology-form.service';

import { TechnologyUpdateComponent } from './technology-update.component';

describe('Technology Management Update Component', () => {
  let comp: TechnologyUpdateComponent;
  let fixture: ComponentFixture<TechnologyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let technologyFormService: TechnologyFormService;
  let technologyService: TechnologyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TechnologyUpdateComponent],
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
      .overrideTemplate(TechnologyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechnologyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    technologyFormService = TestBed.inject(TechnologyFormService);
    technologyService = TestBed.inject(TechnologyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const technology: ITechnology = { id: '487ab60d-d042-4abc-9586-ecb3e3b9550d' };

      activatedRoute.data = of({ technology });
      comp.ngOnInit();

      expect(comp.technology).toEqual(technology);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnology>>();
      const technology = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
      jest.spyOn(technologyFormService, 'getTechnology').mockReturnValue(technology);
      jest.spyOn(technologyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technology });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: technology }));
      saveSubject.complete();

      // THEN
      expect(technologyFormService.getTechnology).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(technologyService.update).toHaveBeenCalledWith(expect.objectContaining(technology));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnology>>();
      const technology = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
      jest.spyOn(technologyFormService, 'getTechnology').mockReturnValue({ id: null });
      jest.spyOn(technologyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technology: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: technology }));
      saveSubject.complete();

      // THEN
      expect(technologyFormService.getTechnology).toHaveBeenCalled();
      expect(technologyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITechnology>>();
      const technology = { id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' };
      jest.spyOn(technologyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ technology });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(technologyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
