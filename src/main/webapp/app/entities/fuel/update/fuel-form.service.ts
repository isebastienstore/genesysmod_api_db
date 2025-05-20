import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFuel, NewFuel } from '../fuel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFuel for edit and NewFuelFormGroupInput for create.
 */
type FuelFormGroupInput = IFuel | PartialWithRequiredKeyOf<NewFuel>;

type FuelFormDefaults = Pick<NewFuel, 'id'>;

type FuelFormGroupContent = {
  id: FormControl<IFuel['id'] | NewFuel['id']>;
  name: FormControl<IFuel['name']>;
};

export type FuelFormGroup = FormGroup<FuelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FuelFormService {
  createFuelFormGroup(fuel: FuelFormGroupInput = { id: null }): FuelFormGroup {
    const fuelRawValue = {
      ...this.getFormDefaults(),
      ...fuel,
    };
    return new FormGroup<FuelFormGroupContent>({
      id: new FormControl(
        { value: fuelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(fuelRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
    });
  }

  getFuel(form: FuelFormGroup): IFuel | NewFuel {
    return form.getRawValue() as IFuel | NewFuel;
  }

  resetForm(form: FuelFormGroup, fuel: FuelFormGroupInput): void {
    const fuelRawValue = { ...this.getFormDefaults(), ...fuel };
    form.reset(
      {
        ...fuelRawValue,
        id: { value: fuelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FuelFormDefaults {
    return {
      id: null,
    };
  }
}
