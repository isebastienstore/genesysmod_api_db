import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFactElectricityConsumption, NewFactElectricityConsumption } from '../fact-electricity-consumption.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFactElectricityConsumption for edit and NewFactElectricityConsumptionFormGroupInput for create.
 */
type FactElectricityConsumptionFormGroupInput = IFactElectricityConsumption | PartialWithRequiredKeyOf<NewFactElectricityConsumption>;

type FactElectricityConsumptionFormDefaults = Pick<NewFactElectricityConsumption, 'id'>;

type FactElectricityConsumptionFormGroupContent = {
  id: FormControl<IFactElectricityConsumption['id'] | NewFactElectricityConsumption['id']>;
  value: FormControl<IFactElectricityConsumption['value']>;
  year: FormControl<IFactElectricityConsumption['year']>;
  country: FormControl<IFactElectricityConsumption['country']>;
  metadata: FormControl<IFactElectricityConsumption['metadata']>;
};

export type FactElectricityConsumptionFormGroup = FormGroup<FactElectricityConsumptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactElectricityConsumptionFormService {
  createFactElectricityConsumptionFormGroup(
    factElectricityConsumption: FactElectricityConsumptionFormGroupInput = { id: null },
  ): FactElectricityConsumptionFormGroup {
    const factElectricityConsumptionRawValue = {
      ...this.getFormDefaults(),
      ...factElectricityConsumption,
    };
    return new FormGroup<FactElectricityConsumptionFormGroupContent>({
      id: new FormControl(
        { value: factElectricityConsumptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      value: new FormControl(factElectricityConsumptionRawValue.value, {
        validators: [Validators.required],
      }),
      year: new FormControl(factElectricityConsumptionRawValue.year),
      country: new FormControl(factElectricityConsumptionRawValue.country),
      metadata: new FormControl(factElectricityConsumptionRawValue.metadata),
    });
  }

  getFactElectricityConsumption(form: FactElectricityConsumptionFormGroup): IFactElectricityConsumption | NewFactElectricityConsumption {
    return form.getRawValue() as IFactElectricityConsumption | NewFactElectricityConsumption;
  }

  resetForm(form: FactElectricityConsumptionFormGroup, factElectricityConsumption: FactElectricityConsumptionFormGroupInput): void {
    const factElectricityConsumptionRawValue = { ...this.getFormDefaults(), ...factElectricityConsumption };
    form.reset(
      {
        ...factElectricityConsumptionRawValue,
        id: { value: factElectricityConsumptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactElectricityConsumptionFormDefaults {
    return {
      id: null,
    };
  }
}
