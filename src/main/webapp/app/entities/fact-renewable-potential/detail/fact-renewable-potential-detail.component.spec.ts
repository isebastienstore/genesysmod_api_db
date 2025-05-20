import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactRenewablePotentialDetailComponent } from './fact-renewable-potential-detail.component';

describe('FactRenewablePotential Management Detail Component', () => {
  let comp: FactRenewablePotentialDetailComponent;
  let fixture: ComponentFixture<FactRenewablePotentialDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactRenewablePotentialDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fact-renewable-potential-detail.component').then(m => m.FactRenewablePotentialDetailComponent),
              resolve: { factRenewablePotential: () => of({ id: '582d4161-271b-4a20-af9a-e881eb8f3345' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactRenewablePotentialDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactRenewablePotentialDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factRenewablePotential on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactRenewablePotentialDetailComponent);

      // THEN
      expect(instance.factRenewablePotential()).toEqual(expect.objectContaining({ id: '582d4161-271b-4a20-af9a-e881eb8f3345' }));
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
