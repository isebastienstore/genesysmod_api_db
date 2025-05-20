import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechnologyDetailComponent } from './technology-detail.component';

describe('Technology Management Detail Component', () => {
  let comp: TechnologyDetailComponent;
  let fixture: ComponentFixture<TechnologyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechnologyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./technology-detail.component').then(m => m.TechnologyDetailComponent),
              resolve: { technology: () => of({ id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechnologyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechnologyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load technology on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechnologyDetailComponent);

      // THEN
      expect(instance.technology()).toEqual(expect.objectContaining({ id: '0563ae70-8be8-4bb1-8852-4d321adc6f90' }));
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
