import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IYear, NewYear } from '../year.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IYear for edit and NewYearFormGroupInput for create.
 */
type YearFormGroupInput = IYear | PartialWithRequiredKeyOf<NewYear>;

type YearFormDefaults = Pick<NewYear, 'id'>;

type YearFormGroupContent = {
  id: FormControl<IYear['id'] | NewYear['id']>;
  year: FormControl<IYear['year']>;
};

export type YearFormGroup = FormGroup<YearFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class YearFormService {
  createYearFormGroup(year: YearFormGroupInput = { id: null }): YearFormGroup {
    const yearRawValue = {
      ...this.getFormDefaults(),
      ...year,
    };
    return new FormGroup<YearFormGroupContent>({
      id: new FormControl(
        { value: yearRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      year: new FormControl(yearRawValue.year, {
        validators: [Validators.required, Validators.min(1900)],
      }),
    });
  }

  getYear(form: YearFormGroup): IYear | NewYear {
    return form.getRawValue() as IYear | NewYear;
  }

  resetForm(form: YearFormGroup, year: YearFormGroupInput): void {
    const yearRawValue = { ...this.getFormDefaults(), ...year };
    form.reset(
      {
        ...yearRawValue,
        id: { value: yearRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): YearFormDefaults {
    return {
      id: null,
    };
  }
}
