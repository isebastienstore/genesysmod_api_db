import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITechnology, NewTechnology } from '../technology.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITechnology for edit and NewTechnologyFormGroupInput for create.
 */
type TechnologyFormGroupInput = ITechnology | PartialWithRequiredKeyOf<NewTechnology>;

type TechnologyFormDefaults = Pick<NewTechnology, 'id'>;

type TechnologyFormGroupContent = {
  id: FormControl<ITechnology['id'] | NewTechnology['id']>;
  name: FormControl<ITechnology['name']>;
  category: FormControl<ITechnology['category']>;
};

export type TechnologyFormGroup = FormGroup<TechnologyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TechnologyFormService {
  createTechnologyFormGroup(technology: TechnologyFormGroupInput = { id: null }): TechnologyFormGroup {
    const technologyRawValue = {
      ...this.getFormDefaults(),
      ...technology,
    };
    return new FormGroup<TechnologyFormGroupContent>({
      id: new FormControl(
        { value: technologyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(technologyRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      category: new FormControl(technologyRawValue.category, {
        validators: [Validators.required],
      }),
    });
  }

  getTechnology(form: TechnologyFormGroup): ITechnology | NewTechnology {
    return form.getRawValue() as ITechnology | NewTechnology;
  }

  resetForm(form: TechnologyFormGroup, technology: TechnologyFormGroupInput): void {
    const technologyRawValue = { ...this.getFormDefaults(), ...technology };
    form.reset(
      {
        ...technologyRawValue,
        id: { value: technologyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TechnologyFormDefaults {
    return {
      id: null,
    };
  }
}
