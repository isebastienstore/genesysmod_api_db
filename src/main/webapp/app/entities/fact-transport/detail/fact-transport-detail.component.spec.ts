import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FactTransportDetailComponent } from './fact-transport-detail.component';

describe('FactTransport Management Detail Component', () => {
  let comp: FactTransportDetailComponent;
  let fixture: ComponentFixture<FactTransportDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FactTransportDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fact-transport-detail.component').then(m => m.FactTransportDetailComponent),
              resolve: { factTransport: () => of({ id: 'ab1926c6-7d20-40b5-9542-b6b3e981782d' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FactTransportDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactTransportDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load factTransport on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FactTransportDetailComponent);

      // THEN
      expect(instance.factTransport()).toEqual(expect.objectContaining({ id: 'ab1926c6-7d20-40b5-9542-b6b3e981782d' }));
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
