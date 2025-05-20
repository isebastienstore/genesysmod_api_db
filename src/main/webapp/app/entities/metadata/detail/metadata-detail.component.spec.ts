import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MetadataDetailComponent } from './metadata-detail.component';

describe('Metadata Management Detail Component', () => {
  let comp: MetadataDetailComponent;
  let fixture: ComponentFixture<MetadataDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetadataDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./metadata-detail.component').then(m => m.MetadataDetailComponent),
              resolve: { metadata: () => of({ id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MetadataDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MetadataDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load metadata on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MetadataDetailComponent);

      // THEN
      expect(instance.metadata()).toEqual(expect.objectContaining({ id: '0eb823cb-eb27-4d44-a3a2-5ae4a3acb025' }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
