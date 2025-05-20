import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IYear } from '../year.model';
import { YearService } from '../service/year.service';
import { YearFormGroup, YearFormService } from './year-form.service';

@Component({
  selector: 'jhi-year-update',
  templateUrl: './year-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class YearUpdateComponent implements OnInit {
  isSaving = false;
  year: IYear | null = null;

  protected yearService = inject(YearService);
  protected yearFormService = inject(YearFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: YearFormGroup = this.yearFormService.createYearFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ year }) => {
      this.year = year;
      if (year) {
        this.updateForm(year);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const year = this.yearFormService.getYear(this.editForm);
    if (year.id !== null) {
      this.subscribeToSaveResponse(this.yearService.update(year));
    } else {
      this.subscribeToSaveResponse(this.yearService.create(year));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IYear>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(year: IYear): void {
    this.year = year;
    this.yearFormService.resetForm(this.editForm, year);
  }
}
