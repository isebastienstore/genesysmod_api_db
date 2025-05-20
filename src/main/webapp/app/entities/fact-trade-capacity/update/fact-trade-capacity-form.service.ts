import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactTradeCapacity, NewFactTradeCapacity } from '../fact-trade-capacity.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactTradeCapacity for edit and NewFactTradeCapacityFormGroupInput for create.
 */
type FactTradeCapacityFormGroupInput = IFactTradeCapacity | PartialWithRequiredKeyOf<NewFactTradeCapacity>;

type FactTradeCapacityFormDefaults = Pick<NewFactTradeCapacity, 'id'>;

type FactTradeCapacityFormGroupContent = {
  id: FormControl<IFactTradeCapacity['id'] | NewFactTradeCapacity['id']>;
  value: FormControl<IFactTradeCapacity['value']>;
  year: FormControl<IFactTradeCapacity['year']>;
  country1: FormControl<IFactTradeCapacity['country1']>;
  country2: FormControl<IFactTradeCapacity['country2']>;
  fuel: FormControl<IFactTradeCapacity['fuel']>;
  metadata: FormControl<IFactTradeCapacity['metadata']>;
};

export type FactTradeCapacityFormGroup = FormGroup<FactTradeCapacityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactTradeCapacityFormService {
  createFactTradeCapacityFormGroup(factTradeCapacity: FactTradeCapacityFormGroupInput = { id: null }): FactTradeCapacityFormGroup {
    const factTradeCapacityRawValue = {
      ...this.getFormDefaults(),
      ...factTradeCapacity,
    };
    return new FormGroup<FactTradeCapacityFormGroupContent>({
      id: new FormControl(
        { value: factTradeCapacityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      value: new FormControl(factTradeCapacityRawValue.value, {
        validators: [Validators.required],
      }),
      year: new FormControl(factTradeCapacityRawValue.year),
      country1: new FormControl(factTradeCapacityRawValue.country1),
      country2: new FormControl(factTradeCapacityRawValue.country2),
      fuel: new FormControl(factTradeCapacityRawValue.fuel),
      metadata: new FormControl(factTradeCapacityRawValue.metadata),
    });
  }

  getFactTradeCapacity(form: FactTradeCapacityFormGroup): IFactTradeCapacity | NewFactTradeCapacity {
    return form.getRawValue() as IFactTradeCapacity | NewFactTradeCapacity;
  }

  resetForm(form: FactTradeCapacityFormGroup, factTradeCapacity: FactTradeCapacityFormGroupInput): void {
    const factTradeCapacityRawValue = { ...this.getFormDefaults(), ...factTradeCapacity };
    form.reset(
      {
        ...factTradeCapacityRawValue,
        id: { value: factTradeCapacityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactTradeCapacityFormDefaults {
    return {
      id: null,
    };
  }
}
