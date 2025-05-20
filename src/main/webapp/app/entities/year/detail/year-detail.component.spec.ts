import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { YearDetailComponent } from './year-detail.component';

describe('Year Management Detail Component', () => {
  let comp: YearDetailComponent;
  let fixture: ComponentFixture<YearDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [YearDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./year-detail.component').then(m => m.YearDetailComponent),
              resolve: { year: () => of({ id: '7d487a17-3974-4015-af9f-a5353d384918' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(YearDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(YearDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load year on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', YearDetailComponent);

      // THEN
      expect(instance.year()).toEqual(expect.objectContaining({ id: '7d487a17-3974-4015-af9f-a5353d384918' }));
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
