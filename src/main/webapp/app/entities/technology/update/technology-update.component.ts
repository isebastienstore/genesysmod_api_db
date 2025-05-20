import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { CategoryType } from 'app/entities/enumerations/category-type.model';
import { ITechnology } from '../technology.model';
import { TechnologyService } from '../service/technology.service';
import { TechnologyFormGroup, TechnologyFormService } from './technology-form.service';

@Component({
  selector: 'jhi-technology-update',
  templateUrl: './technology-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechnologyUpdateComponent implements OnInit {
  isSaving = false;
  technology: ITechnology | null = null;
  categoryTypeValues = Object.keys(CategoryType);

  protected technologyService = inject(TechnologyService);
  protected technologyFormService = inject(TechnologyFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechnologyFormGroup = this.technologyFormService.createTechnologyFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ technology }) => {
      this.technology = technology;
      if (technology) {
        this.updateForm(technology);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const technology = this.technologyFormService.getTechnology(this.editForm);
    if (technology.id !== null) {
      this.subscribeToSaveResponse(this.technologyService.update(technology));
    } else {
      this.subscribeToSaveResponse(this.technologyService.create(technology));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechnology>>): void {
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

  protected updateForm(technology: ITechnology): void {
    this.technology = technology;
    this.technologyFormService.resetForm(this.editForm, technology);
  }
}
