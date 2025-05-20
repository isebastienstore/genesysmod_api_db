import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FuelDetailComponent } from './fuel-detail.component';

describe('Fuel Management Detail Component', () => {
  let comp: FuelDetailComponent;
  let fixture: ComponentFixture<FuelDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FuelDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fuel-detail.component').then(m => m.FuelDetailComponent),
              resolve: { fuel: () => of({ id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FuelDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FuelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load fuel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FuelDetailComponent);

      // THEN
      expect(instance.fuel()).toEqual(expect.objectContaining({ id: 'dcf040ff-6816-488d-abb8-4d7820bc9b0a' }));
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
