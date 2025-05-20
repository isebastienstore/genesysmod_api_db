import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MetadataService } from '../service/metadata.service';
import { IMetadata } from '../metadata.model';
import { MetadataFormService } from './metadata-form.service';

import { MetadataUpdateComponent } from './metadata-update.component';

describe('Metadata Management Update Component', () => {
  let comp: MetadataUpdateComponent;
  let fixture: ComponentFixture<MetadataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let metadataFormService: MetadataFormService;
  let metadataService: MetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MetadataUpdateComponent],
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
      .overrideTemplate(MetadataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MetadataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    metadataFormService = TestBed.inject(MetadataFormService);
    metadataService = TestBed.inject(MetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const metadata: IMetadata = { id: '7c8cb81a-eae2-4ec8-91d4-a5a5ec343fa8' };

      activatedRoute.data = of({ metadata });
      comp.ngOnInit();

      expect(comp.metadata).toEqual(metadata);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetadata>>();
      const metadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      jest.spyOn(metadataFormService, 'getMetadata').mockReturnValue(metadata);
      jest.spyOn(metadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metadata }));
      saveSubject.complete();

      // THEN
      expect(metadataFormService.getMetadata).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(metadataService.update).toHaveBeenCalledWith(expect.objectContaining(metadata));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetadata>>();
      const metadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      jest.spyOn(metadataFormService, 'getMetadata').mockReturnValue({ id: null });
      jest.spyOn(metadataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metadata: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: metadata }));
      saveSubject.complete();

      // THEN
      expect(metadataFormService.getMetadata).toHaveBeenCalled();
      expect(metadataService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMetadata>>();
      const metadata = { id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' };
      jest.spyOn(metadataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ metadata });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(metadataService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
